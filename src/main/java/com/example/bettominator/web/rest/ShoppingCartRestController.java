//package com.example.bettominator.web.rest;
//
//import com.example.bettominator.dto.ChargeRequest;
//import com.example.bettominator.model.ShoppingCart;
//import com.example.bettominator.service.AuthService;
//import com.example.bettominator.service.ShoppingCartService;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/carts")
//public class ShoppingCartRestController {
//
//    private final ShoppingCartService shoppingCartService;
//    private final AuthService authService;
//
//    public ShoppingCartRestController(ShoppingCartService shoppingCartService,
//                                      AuthService authService) {
//        this.shoppingCartService = shoppingCartService;
//        this.authService = authService;
//    }
//
//    @GetMapping
//    public List<ShoppingCart> findAllByUsername() {
//        return this.shoppingCartService.findAllByUsername(this.authService.getCurrentUserId());
//    }
//
//    @PostMapping
//    public ShoppingCart createNewShoppingCart() {
//        return this.shoppingCartService.createNewShoppingCart(this.authService.getCurrentUserId());
//    }
//
//    @PatchMapping("/{gameId}/games")
//    public ShoppingCart addProductToCart(@PathVariable Long gameId, String betType) {
//        return this.shoppingCartService.addGameToShoppingCart(this.authService.getCurrentUserId(), gameId, betType);
//    }
//
//    @DeleteMapping("/{gameId}/games")
//    public ShoppingCart removeProductFromCart(@PathVariable Long gameId) {
//        return this.shoppingCartService.removeGameFromShoppingCart(
//                this.authService.getCurrentUserId(),
//                gameId
//        );
//    }
//
//    @PatchMapping("/cancel")
//    public ShoppingCart cancelActiveShoppingCart() {
//        return this.shoppingCartService.cancelActiveShoppingCart(this.authService.getCurrentUserId());
//    }
//
//    @PatchMapping("/checkout")
//    public ShoppingCart checkoutActiveShoppingCart(ChargeRequest chargeRequest) {
//       return this.shoppingCartService.checkoutShoppingCart(this.authService.getCurrentUserId(), chargeRequest);
//        //return null;
//    }
//
//
//
//}