package org.example;

import java.io.IOException;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        double currentPrice = 0.0;
        KabumPriceMonitor kabumPriceMonitor = new KabumPriceMonitor();

        while (true) {
            try {
                double price = kabumPriceMonitor.getPrice();
                if (price != currentPrice) {
                    System.out.println("The price changed! The new price is: R$ " + price + "! And the local time is: " + LocalDateTime.now());
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