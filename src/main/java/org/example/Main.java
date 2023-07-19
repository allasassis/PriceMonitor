package org.example;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) {
        ZoneId brasiliaZone = ZoneId.of("America/Sao_Paulo");
        String formattedDateTime = LocalDateTime.now().atZone(brasiliaZone).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        KabumPriceMonitor kabumPriceMonitor = new KabumPriceMonitor();
        String emailPassword = System.getenv("EMAIL_PASSWORD");
        String emailSender1 = System.getenv("EMAIL_SENDER");
        String recipient = System.getenv("RECIPIENT");
        EmailSender emailSender = new EmailSender(emailSender1, emailPassword, recipient);

        double currentPrice = 0.0;

        while (true) {
            try {
                double price = kabumPriceMonitor.getPrice();
                if (price != currentPrice) {
                    emailSender.sendEmail(price);
                    System.out.println("The price changed! The new price is: R$ " + price + "! and the local time is: " + formattedDateTime);
                    currentPrice = price;
                }

                // Defina o intervalo de tempo para verificar o preço novamente (em milissegundos)
                Thread.sleep(60000); // Verifica a cada 1 minuto
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}