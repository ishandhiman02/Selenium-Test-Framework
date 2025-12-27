//package com.orangehrm.listeners;
//
//import jakarta.mail.Authenticator;
//import jakarta.mail.BodyPart;
//import jakarta.mail.Message;
//import jakarta.mail.Multipart;
//import jakarta.mail.PasswordAuthentication;
//import jakarta.mail.Session;
//import jakarta.mail.Transport;
//import jakarta.mail.internet.InternetAddress;
//import jakarta.mail.internet.MimeBodyPart;
//import jakarta.mail.internet.MimeMessage;
//import jakarta.mail.internet.MimeMultipart;
//
//import java.io.File;
//import java.util.Properties;
//
//public class EmailUtil {
//
//    public static void sendEmailAlert(String testName, String screenshotPath) {
//
//        String from = "ishandhiman1973@gmail.com";
//        String  to= "ishan.dhiman@synlabs.io";   // SENDER
//        String host = "smtp.gmail.com";
//
//        // ✅ FIX: Properties initialize karo
//        Properties prop = new Properties();
//
//        prop.put("mail.smtp.host", host);
//        prop.put("mail.smtp.port", "587");
//        prop.put("mail.smtp.auth", "true");
//        prop.put("mail.smtp.starttls.enable", "true");
//
//        Session session = Session.getInstance(prop,
//            new Authenticator() {
//                protected PasswordAuthentication getPasswordAuthentication() {
//                    // ⚠️ Gmail App Password yaha daalo
//                    return new PasswordAuthentication(
//                            "ishandhiman1973@gmail.com",
//                            "$Darshan@3177"
//                    );
//                }
//            });
//
//        try {
//            MimeMessage message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(from));
//            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
//            message.setSubject("❌ Test Failed: " + testName);
//
//            BodyPart textPart = new MimeBodyPart();
//            textPart.setText(
//                "Test Failed: " + testName +
//                "\nPlease check attached screenshot."
//            );
//
//            Multipart multipart = new MimeMultipart();
//            multipart.addBodyPart(textPart);
//
//            // 📸 Screenshot attach
//            MimeBodyPart attachPart = new MimeBodyPart();
//            attachPart.attachFile(new File(screenshotPath));
//            multipart.addBodyPart(attachPart);
//
//            message.setContent(multipart);
//            Transport.send(message);
//
//            System.out.println("✅ Email sent successfully for test: " + testName);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
