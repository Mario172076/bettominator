package com.example.bettominator.service.implementation;

import com.example.bettominator.dto.ChargeRequest;
import com.example.bettominator.model.*;
import com.example.bettominator.model.enumerations.ShoppingCartStatus;
import com.example.bettominator.model.exceptions.*;
import com.example.bettominator.repostiroy.ShoppingCartRepository;
import com.example.bettominator.repostiroy.UserRepository;
import com.example.bettominator.service.*;
import com.stripe.exception.*;
import com.stripe.model.Charge;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final PaymentService paymentService;
    private final BetInfoService betInfoService;
    private final BiltenService biltenService;
    private final BetLogsService betLogsService;

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository, UserService userService, UserRepository userRepository, PaymentService paymentService, BetInfoService betInfoService, BiltenService biltenService, BetLogsService betLogsService) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.paymentService = paymentService;
        this.betInfoService = betInfoService;
        this.biltenService = biltenService;
        this.betLogsService = betLogsService;
    }

    @Override
    public List<BetInfo> listAllGamesInShoppingCart(Long cartId) {
        if (!this.shoppingCartRepository.findById(cartId).isPresent())
            throw new ShoppingCartNotFoundException(cartId);
        return this.shoppingCartRepository.findById(cartId).get().getBetInfoList();
    }

    @Override
    public ShoppingCart getActiveShoppingCart(String username) {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return this.shoppingCartRepository
                .findByUserAndStatus(user, ShoppingCartStatus.CREATED)
                .orElseGet(() -> {
                    ShoppingCart cart = new ShoppingCart(user);
                    return this.shoppingCartRepository.save(cart);
                });
    }

    @Override
    public void deleteAll(List<ShoppingCart> shoppingCarts) {
        this.shoppingCartRepository.deleteAllInBatch(this.shoppingCartRepository.findAll());
    }



    @Override
    public String checkIfUserHasWon(Long id) {
        if(checkIfPayoutCompleted(id)){
            return "PayoutAlreadyMadeForThisTicket";
        }
        Optional<ShoppingCart> shoppingCart = this.shoppingCartRepository.findById(id);
        List<BetLogs> betLogs = this.betLogsService.findByShoppingCart(shoppingCart.get());
        if(betLogs.size()==0) return "NoSuchTicket";
        for(BetLogs bt : betLogs){
            SportsGame sg = bt.getSportsGame();
            if(sg.playingToday() || sg.isLive() || sg.getGameResults()==null)
                return "TicketHasUnfinishedGames";
            else if(!sg.getGameResults().get(bt.getBetType()).equals(bt.getSelection()))
                return "UserHasLost";
        }
        return "You won";
    }

    @Override
    public Boolean checkIfPayoutCompleted(Long id) {
        Optional<ShoppingCart> shoppingCart = this.shoppingCartRepository.findById(id);
        return shoppingCart.get().getPayoutCompleted();
    }

    @Override
    public ShoppingCart addGameToShoppingCart(String username, Long gameId, String betType) {
        Character last = betType.charAt(betType.length() - 1);

        String betTypee = betType.substring(0, betType.length() - 1);

        ShoppingCart shoppingCart = this.getActiveShoppingCart(username);

        Bilten bilten = this.biltenService.findByGameIdAndBetType(gameId, betTypee)
                .orElseThrow(() -> new GameNotFoundException(gameId));

        BetInfo betInfo = betInfoService.setBetInfo(bilten, betTypee, bilten.getSportsGame(), last);

        if (shoppingCart.getBetInfoList()
                .stream().filter(i -> i.getSportsGame().getId().equals(betInfo.getSportsGame().getId()) && i.getBetType().equals(betTypee))
                .collect(Collectors.toList()).size() > 0)
            throw new GameAlreadyInShoppingCartException(gameId, username);


        BetInfo res = this.betInfoService.save(betInfo);
        if (res != null) shoppingCart.getBetInfoList().add(betInfo);
        else throw new GameAlreadyInShoppingCartException(gameId, username);

        return this.shoppingCartRepository.save(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCart removeGameFromShoppingCart(String userId, Long gameId) {
        ShoppingCart shoppingCart = this.getActiveShoppingCart(userId);
        shoppingCart.setBetInfoList(
                shoppingCart.getBetInfoList()
                        .stream()
                        .filter(game -> !game.getId().equals(gameId))
                        .collect(Collectors.toList())
        );
        this.betInfoService.deleteById(gameId);
        return this.shoppingCartRepository.save(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCart checkoutShoppingCart(String userId, ChargeRequest chargeRequest) {
        ShoppingCart shoppingCart = this.shoppingCartRepository
                .findByUserUsernameAndStatus(userId, ShoppingCartStatus.CREATED)
                .orElseThrow(() -> new ShoppingCartIsNotActiveException(userId));

        List<BetInfo> betInfoList = shoppingCart.getBetInfoList();

        Charge charge = null;
        try {
            charge = this.paymentService.pay(chargeRequest);
        } catch (CardException | APIException | AuthenticationException | APIConnectionException | InvalidRequestException e) {
            throw new TransactionFailedException(userId, e.getMessage());
        }

        shoppingCart.setAmountOfBet((double) chargeRequest.getAmount()/100);
        String result = String.format("%.2f",(chargeRequest.getAmount()/100) * this.getCoeffitients(shoppingCart));
        shoppingCart.setAmountToWin(Double.parseDouble(result));
        shoppingCart.setStatus(ShoppingCartStatus.FINISHED);
        return this.shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart cancelActiveShoppingCart(String userId) {
        ShoppingCart shoppingCart = this.shoppingCartRepository
                .findByUserUsernameAndStatus(userId, ShoppingCartStatus.CREATED)
                .orElseThrow(() -> new ShoppingCartIsNotActiveException(userId));
        shoppingCart.setStatus(ShoppingCartStatus.CANCELED);
        return this.shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart createNewShoppingCart(String userId) {
        User user = this.userService.findById(userId);
        if (this.shoppingCartRepository.existsByUserUsernameAndStatus(
                user.getUsername(),
                ShoppingCartStatus.CREATED
        )) {
            throw new ShoppingCartIsAlreadyCreatedException(userId);
        }
        ShoppingCart shoppingCart = new ShoppingCart(user);
//        shoppingCart.setUser(user);
        return this.shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public List<ShoppingCart> findAllByUsername(String userId) {
        return this.shoppingCartRepository.findAllByUserUsername(userId);
    }

    @Override
    public double getCoeffitients(ShoppingCart shoppingCart) {
        double coeffitients = 0;
        for (BetInfo bi : shoppingCart.getBetInfoList()) {
            coeffitients += bi.getCoefficient();
        }
        return coeffitients;
    }

    @Override
    public List<Double> getDoubleCoefficients(ShoppingCart shoppingCart) {
       List<Double> coefs = new ArrayList<>();
       for(BetInfo bi: shoppingCart.getBetInfoList()){
           coefs.add(bi.getCoefficient());
       }
       return coefs;
    }

    @Override
    public List<ShoppingCart> findAll() {
        return this.shoppingCartRepository.findAll();
    }

    @Override
    public List<ShoppingCart> findByTicketNumber(String ticketNumber) {
        String ticketLike = ticketNumber != null ? "%" + ticketNumber + "%" : null;
        if (ticketLike != null) {
            return this.shoppingCartRepository.findShoppingCartByTicketCodeLike(ticketLike);
        } else {
            return this.shoppingCartRepository.findAll();
        }
    }

    @Override
    public boolean payout(Long id) {
        Optional<ShoppingCart> shoppingCart = this.shoppingCartRepository.findById(id);
        if(checkIfUserHasWon(shoppingCart.get().getId()).equals("You won")){
            shoppingCart.get().setPayoutCompleted(true);
            this.shoppingCartRepository.save(shoppingCart.get());
            return true;
        }
        return false;
    }


}
