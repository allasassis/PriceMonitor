package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KabumPriceMonitor {

    private static final String PRODUCT_URL = "https://www.kabum.com.br/produto/150657/placa-de-video-rtx-3060-1-click-oc-galax-geforce-12gb-gddr6-lhr-dlss-ray-tracing-36nol7md1voc";

    public static double getPrice() throws IOException {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');

        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", symbols);

        Document doc = Jsoup.connect(PRODUCT_URL).get();
        Elements priceElements = doc.select("h4.finalPrice");

        if (priceElements.isEmpty()) {
            return 0.0;
        }

        String priceText = priceElements.get(0).text();
        Pattern pattern = Pattern.compile("[0-9.,]+");
        Matcher matcher = pattern.matcher(priceText);

        if (matcher.find()) {
            priceText = matcher.group();
        } else {
            System.out.println("Invalid price format: " + priceText);
            return 0.0;
        }

        priceText = priceText.replace(".", "");

        Double numeroDouble = 0.0;
        try {
            Number parsedNumber = decimalFormat.parse(priceText);
            numeroDouble = parsedNumber.doubleValue();
        } catch (ParseException e) {
        }

        return numeroDouble;
    }
}