package builds.utilities;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.io.File;
import java.util.Properties;

public class SendingEmail {
    public static void main(String[] args) {
        String host = "smtp.office365.com"; // SMTP server (e.g., Gmail, Outlook)
        String port = "587"; // Port for TLS
        String username = "your-email@gmail.com"; // Sender email
        String password = "your-email-password"; // Use App Password if using Gmail
        String toEmail = "amirulhakim.ab@maybank.com"; // Recipient email
        String subject = "Automation Run Report";
        String body = "Hi All, \n Attached is Automation Report";
        String filePath = "C:/path/to/yourfile.txt"; // File to attach

        sendEmailWithAttachment(host, port, username, password, toEmail, subject, body, filePath);
    }

    public static void sendEmailWithAttachment(String host, String port, String username, String password,
                                               String toEmail, String subject, String body, String filePath) {
        // SMTP Server Properties
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // Use TLS

        // Authenticate and create session
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Create email message
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject(subject);

            // Create email body
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);

            // Create attachment part
            MimeBodyPart attachmentPart = new MimeBodyPart();
            File file = new File(filePath);
            DataSource source = new FileDataSource(file);
            attachmentPart.setDataHandler(new DataHandler(source));
            attachmentPart.setFileName(file.getName());

            // Combine body and attachment
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);

            // Send email
            Transport.send(message);
            System.out.println("✅ Email sent successfully with attachment: " + filePath);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}