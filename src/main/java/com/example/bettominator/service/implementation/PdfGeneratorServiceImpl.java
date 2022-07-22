package com.example.bettominator.service.implementation;

import com.example.bettominator.model.BetInfo;
import com.example.bettominator.model.BetLogs;
import com.example.bettominator.model.ShoppingCart;
import com.example.bettominator.service.BetInfoService;
import com.example.bettominator.service.BetLogsService;
import com.example.bettominator.service.PdfGeneratorService;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Random;

@Service
public class PdfGeneratorServiceImpl implements PdfGeneratorService {

    private static Random RANDOM = new Random();
    private final BetInfoService betInfoService;
    private final BetLogsService betLogsService;

    public PdfGeneratorServiceImpl(BetInfoService betInfoService, BetLogsService betLogsService) {
        this.betInfoService = betInfoService;
        this.betLogsService = betLogsService;
    }


    @Override
    public void export(HttpServletResponse response, ShoppingCart shoppingCart, List<BetInfo> betInfoList) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();


        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontTitle.setSize(18);
        Paragraph paragraph = new Paragraph("BETTOMINATOR BETTING TICKET\n\n\n", fontTitle);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);

        PdfPTable table = new PdfPTable(4);
        table.addCell(String.format("%s'S TICKET", shoppingCart.getUser().getName().toUpperCase()));
        table.addCell("TICKET ID");
        table.addCell(shoppingCart.getId().toString());
        table.addCell("");
        table.addCell("\n");
        table.addCell("\n");
        table.addCell("\n");
        table.addCell("\n");
        table.addCell("GAME INFO");
        table.addCell("BET TYPE");
        table.addCell("COEFFICIENT");
        table.addCell("BET SELECTION");

        Font fontParagraph = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontParagraph.setSize(14);
        long num = Math.abs(RANDOM.nextLong(100000, 999999));
        Paragraph paragraph1 = new Paragraph(String.format("\n\n\nYOUR CODE: %d", num), fontParagraph);

        for(BetInfo bt: betInfoList){
            BetLogs bl = new BetLogs();
            table.addCell(String.format("%s",bt.getSportsGame().getGameInfo()));
            table.addCell(String.format("%s",bt.getBetType()));
            table.addCell(String.format("%.2f",bt.getCoefficient()));
            table.addCell(String.format("%c",bt.getSelection()));
            bl.setCode(String.valueOf(num));
            bl.setSportsGame(bt.getSportsGame());
            bl.setBetType(bt.getBetType());
            bl.setSelection(bt.getSelection());
            bl.setShoppingCart(shoppingCart);
            betLogsService.save(bl);

        }

        Paragraph paragraph2 = new Paragraph(String.format("\nYOUR WIN: %.2f€", shoppingCart.getAmountToWin()), fontParagraph);
        Paragraph paragraph3 = new Paragraph(String.format("\nGOOD LUCK!"), fontParagraph);
        paragraph1.setAlignment(Paragraph.ALIGN_CENTER);
        paragraph2.setAlignment(Paragraph.ALIGN_CENTER);
        paragraph3.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(paragraph);
        document.add(table);
        document.add(paragraph1);
        document.add(paragraph2);
        document.add(paragraph3);
        document.close();

        shoppingCart.setTicketCode(String.valueOf(num));
        shoppingCart.setBetInfoList(betInfoList);
        this.betInfoService.deleteAll(betInfoList);
        betInfoList.clear();
    }

}
