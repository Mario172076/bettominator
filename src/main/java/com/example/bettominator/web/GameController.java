package com.example.bettominator.web;

import com.example.bettominator.dto.ChargeRequest;
import com.example.bettominator.model.*;
import com.example.bettominator.model.enumerations.Role;
import com.example.bettominator.model.exceptions.*;
import com.example.bettominator.service.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/Bet-Page")
public class GameController {

    @Value("${STRIPE_P_KEY}")
    private String publicKey;

    private final GameService gameService;
    private final CategoryService categoryService;
    private final CountryService countryService;
    private final ShoppingCartService shoppingCartService;
    private final AuthService authService;
    private final BiltenService biltenService;
    private final BetInfoService betInfoService;
    private final PdfGeneratorService pdfGeneratorService;
    private final UserService userService;


    public GameController(GameService gameService, CategoryService categoryService, CountryService countryService, ShoppingCartService shoppingCartService, AuthService authService, BiltenService biltenService, BetInfoService betInfoService, PdfGeneratorService pdfGeneratorService, UserService userService) {
        this.gameService = gameService;
        this.categoryService = categoryService;
        this.countryService = countryService;
        this.shoppingCartService = shoppingCartService;
        this.authService = authService;
        this.biltenService = biltenService;
        this.betInfoService = betInfoService;
        this.pdfGeneratorService = pdfGeneratorService;
        this.userService = userService;
    }


    @GetMapping
    public String getGamePage(
            HttpServletRequest req,
            Model model,
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) Country country,
            @RequestParam(required = false) String countries,
            @RequestParam(required = false) String competitorName,
            @RequestParam(required = false) String playingLive,
            @RequestParam(required = false) String today,
            @RequestParam(required = false) String betnow
    ) {

        this.gameService.deleteAllThatFinished();
        List<Bilten> biltenInfo = biltenService.findAll();
        List<SportsGame> listGames = this.gameService.fullSearch(playingLive, today, betnow, category, country, competitorName);
        List<Category> categories = categoryService.findAll();
        List<Country> countriess = new ArrayList<>();
        if (countries != null) {
            countriess = countryService.findAll();
        }

        try {
            String username = req.getRemoteUser();
            ShoppingCart shoppingCart = this.shoppingCartService.getActiveShoppingCart(username);
            double coefficients = this.shoppingCartService.getCoeffitients(shoppingCart);

            model.addAttribute("shoppingCart", shoppingCart);
            model.addAttribute("currency", "eur");
            model.addAttribute("coefficients", coefficients);
            model.addAttribute("stripePublicKey", this.publicKey);
        } catch (ShoppingCartNotFoundException | UsernameNotFoundException ex) {
            return "redirect:/Bet-Page?error=" + ex.getLocalizedMessage();
        }
        model.addAttribute("listGames", listGames);
        model.addAttribute("Categories", categories);
        model.addAttribute("Countries", countriess);
        model.addAttribute("biltenInfo", biltenInfo);
        return "Bet-Page";
    }

    @GetMapping("/cart/shoppingCarts")
    public String getCartsPage(@RequestParam(required = false) String cartCode,
                               @RequestParam(required = false) String error,
                               HttpServletRequest req,
                               Model model) {
        List<ShoppingCart> shoppingCarts = new ArrayList<>();

        User user  = this.userService.findById(req.getRemoteUser());

        if(user.getRole().equals(Role.ROLE_USER)){
            shoppingCarts.addAll(this.shoppingCartService.findAllByUsername(user.getUsername())
                    .stream()
                    .filter(i-> i.getTicketCode() != null)
                    .collect(Collectors.toList()));
        }
        else{
            if (cartCode == null) {
                shoppingCarts.addAll(this.shoppingCartService.findAll()
                        .stream()
                        .filter(i -> i.getTicketCode() != null)
                        .collect(Collectors.toList()));
            } else {
                if (this.shoppingCartService.findByTicketNumber(cartCode) != null)
                    shoppingCarts.addAll(this.shoppingCartService.findByTicketNumber(cartCode));
            }

        }


        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        model.addAttribute("shoppingCarts", shoppingCarts);
        return "ShoppingCarts";
    }

    @PostMapping("/cart/add-game/{id}")
    public String addGameToShoppingCart(@PathVariable Long id,
                                        HttpServletRequest req,
                                        Authentication authentication,
                                        @RequestParam(required = false) String betType) {
        try {
            User user = (User) authentication.getPrincipal();
            this.shoppingCartService.addGameToShoppingCart(user.getUsername(), id, betType);
            return "redirect:/Bet-Page";
        } catch (GameNotFoundException | GameAlreadyInShoppingCartException exception) {
            return "redirect:/Bet-Page?error=" + exception.getLocalizedMessage();
        }
    }

    @GetMapping("/cart/{id}/remove-game")
    public String removeGameFromShoppingCart(@PathVariable Long id) {
        ShoppingCart shoppingCart = this.shoppingCartService.removeGameFromShoppingCart(this.authService.getCurrentUserId(), id);
        return "redirect:/Bet-Page";
    }


    @PostMapping("/cart")
    public String checkout(ChargeRequest chargeRequest, Model model, @RequestParam String stake, HttpServletResponse response) throws IOException {
        try {
            int value = Integer.parseInt(stake) * 100;
            chargeRequest.setAmount(value);

            ShoppingCart shoppingCartInfo = this.shoppingCartService.getActiveShoppingCart(this.authService.getCurrentUserId());
            ShoppingCart shoppingCart = this.shoppingCartService.checkoutShoppingCart(this.authService.getCurrentUserId(), chargeRequest);

            response.setContentType("application/pdf");
            DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
            String currentDateTime = dateFormatter.format(new Date());
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
            response.setHeader(headerKey, headerValue);
            this.pdfGeneratorService.export(response, shoppingCartInfo, shoppingCartInfo.getBetInfoList());
            return "/Bet-Page";
        } catch (ShoppingCartIsNotActiveException | TransactionFailedException ex) {
            return "redirect:/Bet-Page?error=" + ex.getLocalizedMessage();
        }
    }

    @GetMapping("/cart/payout/{id}")
    public String payout(@PathVariable Long id, Model model) {
        boolean res = this.shoppingCartService.payout(id);
        model.addAttribute("shoppingCarts", this.shoppingCartService.findAll());
        if(res) return "redirect:/Bet-Page/cart/shoppingCarts?message=PayoutSuccessful";
        else return "redirect:/Bet-Page/cart/shoppingCarts?message=Payout-Unsuccessful-Please-check-the-ticket";
    }

    @GetMapping("/cart/win/{id}")
    public String checkIfWon(@PathVariable Long id, Model model) {
        String res = this.shoppingCartService.checkIfUserHasWon(id);
        model.addAttribute("shoppingCarts", this.shoppingCartService.findAll());
        return switch (res) {
            case "PayoutAlreadyMadeForThisTicket" -> "redirect:/Bet-Page/cart/shoppingCarts?message=PayoutAlreadyMadeForThisTicket";
            case "NoSuchTicket" -> "redirect:/Bet-Page/cart/shoppingCarts?message=NoSuchTicket";
            case "TicketHasUnfinishedGames" -> "redirect:/Bet-Page/cart/shoppingCarts?message=TicketHasUnfinishedGames";
            case "UserHasLost" -> "redirect:/Bet-Page/cart/shoppingCarts?message=UserHasLost";
            default -> "redirect:/Bet-Page/cart/shoppingCarts?message=UserHasWon";
        };
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String handleCartException(RedirectAttributes ra){
        System.out.println("Access only allowed for admins");
        ra.addAttribute("error", "Access only allowed for admins");
        return "redirect:/Home";
    }

    //    @GetMapping("/details/{id}")
//    public String getGameDetailPage(@PathVariable Long id, Model model) {
//        SportsGame sportsGame = this.gameService.findById(id).orElseThrow(() -> new GameNotFoundException(id));
//        List<SportsGame> games = this.gameService.findAll();
//        List<Category> categories = this.categoryService.findAll();
//        List<Country> countries = this.countryService.findAll();
//        model.addAttribute("sportsGame", sportsGame);
//        model.addAttribute("games", games);
//        model.addAttribute("Categories", categories);
//        model.addAttribute("Countries", countries);
//        return "Bet-Page";
//    }


//    @DeleteMapping("/delete/{id}")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    public String deleteGame(@PathVariable Long id) {
//        try {
//            this.gameService.deleteById(id);
//        } catch (GameIsNotInShoppingCartException ex) {
//            return String.format("redirect:/Bet-Page?error=%s", ex.getMessage());
//        }
//        return "redirect:/Bet-Page";
//    }

//    @GetMapping("/edit-form/{id}")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    public String editGamePage(@PathVariable Long id, Model model) {
//        if (this.gameService.findById(id).isPresent()) {
//            SportsGame sportsGame = this.gameService.findById(id).get();
//            List<Country> countries = this.countryService.findAll();
//            List<Category> categories = this.categoryService.findAll();
//            model.addAttribute("Countries", countries);
//            model.addAttribute("Categories", categories);
//            model.addAttribute("sportsGame", sportsGame);
//            return "Bet-Page";
//
//        }
//        return "redirect:/Bet-Page?error=ProductNotFound";
//    }

//    @GetMapping("/add-form")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    public String addGamePage(Model model) {
//        List<Country> countries = this.countryService.findAll();
//        List<Category> categories = this.categoryService.findAll();
//        model.addAttribute("Countries", countries);
//        model.addAttribute("Categories", categories);
//        return "Bet-Page";
//    }

    //    @PostMapping("/add")
//    @Secured("ROLE_ADMIN")
//    public String saveGame(
//            @Valid SportsGame sportsGame,
//            BindingResult bindingResult,
//            Model model) {
//
//        if (bindingResult.hasErrors()) {
//            List<Country> countries = this.countryService.findAll();
//            List<Category> categories = this.categoryService.findAll();
//            model.addAttribute("Countries", countries);
//            model.addAttribute("Categories", categories);
//            return "Bet-Page";
//        }
//
//        if (sportsGame.getId() != null) {
//            try {
//                this.gameService.edit(sportsGame.getId(), sportsGame);
//            } catch (CategoryNotFoundException | CountryNotFoundException | IOException exception) {
//                return "redirect:/Bet-Page?error=" + exception.getLocalizedMessage();
//            }
//        } else {
//            try {
//                this.gameService.save(sportsGame);
//            } catch (CategoryNotFoundException | CountryNotFoundException | IOException exception) {
//                return "redirect:/Bet-Page?error=" + exception.getLocalizedMessage();
//            }
//
//        }
//        return "redirect:/Bet-Page";
//    }


}
