//package com.example.bettominator.web.rest;
//
//import com.example.bettominator.dto.ChargeRequest;
//import com.example.bettominator.model.ShoppingCart;
//import com.example.bettominator.model.User;
//import com.example.bettominator.model.exceptions.GameAlreadyInShoppingCartException;
//import com.example.bettominator.model.exceptions.GameNotFoundException;
//import com.example.bettominator.service.AuthService;
//import com.example.bettominator.service.GameService;
//import com.example.bettominator.service.ShoppingCartService;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.Authentication;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//
//@RestController
//@RequestMapping("/api/Cart")
//public class PaymentRestController {
//
//    @Value("${STRIPE_P_KEY}")
//    private String publicKey;
//
//    private final ShoppingCartService shoppingCartService;
//    private final AuthService authService;
//    private final GameService gameService;
//
//    public PaymentRestController(ShoppingCartService shoppingCartService,
//                                 AuthService authService, GameService gameService) {
//        this.shoppingCartService = shoppingCartService;
//        this.authService = authService;
//
//        this.gameService = gameService;
//    }
//
//
//    @GetMapping
//    public String getCheckoutPage(HttpServletRequest req, Model model) {
//        try {
//            String username = req.getRemoteUser();
//            ShoppingCart shoppingCart = this.shoppingCartService.getActiveShoppingCart(username);
//            model.addAttribute("gamess", gameService.findAll());
//            model.addAttribute("games", this.shoppingCartService.listAllGamesInShoppingCart(shoppingCart.getId()));
//            model.addAttribute("shoppingCart", shoppingCart);
//            model.addAttribute("currency", "eur");
//            model.addAttribute("amount", 50);
//            model.addAttribute("stripePublicKey", this.publicKey);
//            return "Bet-Page";
//        } catch (RuntimeException ex) {
//            return "redirect:/Bet-Page?error=" + ex.getLocalizedMessage();
//        }
//    }
//
//
//    @PostMapping
//    public String checkout(ChargeRequest chargeRequest, Model model) {
//        try {
//            ShoppingCart shoppingCart = this.shoppingCartService.checkoutShoppingCart(this.authService.getCurrentUserId(), chargeRequest);
//            return "redirect:/Bet-Page?message=Successful Payment";
//        } catch (RuntimeException ex) {
//            return "redirect:/Bet-Page?error=" + ex.getLocalizedMessage();
//        }
//    }
//
//    @PostMapping("/add-game/{id}")
//    public String addGameToShoppingCart(@PathVariable Long id, HttpServletRequest req, Authentication
//            authentication, @RequestParam(required = false) String betType) {
//        try {
//            User user = (User) authentication.getPrincipal();
//            this.shoppingCartService.addGameToShoppingCart(user.getUsername(), id, betType);
//            return "redirect:/Bet-Page";
//        } catch (GameNotFoundException | GameAlreadyInShoppingCartException exception) {
//            return "redirect:/Bet-Page?error=" + exception.getMessage();
//        }
//    }
//
//    @PostMapping("/{gameId}/remove-game")
//    public String removeGameFromShoppingCart(@PathVariable Long productId) {
//        ShoppingCart shoppingCart = this.shoppingCartService.removeGameFromShoppingCart(this.authService.getCurrentUserId(), productId);
//        return "redirect:/cart";
//    }
//}