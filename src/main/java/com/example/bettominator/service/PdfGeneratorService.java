package com.example.bettominator.service;

import com.example.bettominator.model.BetInfo;
import com.example.bettominator.model.ShoppingCart;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface PdfGeneratorService {
    public void export(HttpServletResponse response, ShoppingCart shoppingCart, List<BetInfo> betInfoList) throws IOException;

}
