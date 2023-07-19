package org.example;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class EmailSender {

    ZoneId brasiliaZone = ZoneId.of("America/Sao_Paulo");
    String formattedDateTime = LocalDateTime.now().atZone(brasiliaZone).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

    private final String fromEmail;
    private final String password;
    private final String toEmail;

    public EmailSender(String fromEmail, String password, String toEmail) {
        this.fromEmail = fromEmail;
        this.password = password;
        this.toEmail = toEmail;
    }

    public void sendEmail(double newPrice) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject("Video card's price changed!");
            message.setText("The video card's price changed to R$ " + formatPrice(newPrice) + ". And the local date is: " + formattedDateTime);

            Transport.send(message);
            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    private static String formatPrice(double price) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", symbols);
        return decimalFormat.format(price);
    }
}
