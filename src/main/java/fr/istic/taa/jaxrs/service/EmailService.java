package fr.istic.taa.jaxrs.service;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class EmailService {

    private static final Logger LOGGER = Logger.getLogger(EmailService.class.getName());
    private static final Properties FILE_CONFIG = loadFileConfig();

    public boolean sendPlainText(String to, String subject, String body) {
        String host = config("SMTP_HOST", null);
        String from = config("SMTP_FROM", null);
        String username = config("SMTP_USERNAME", null);
        String password = config("SMTP_PASSWORD", null);
        String port = config("SMTP_PORT", "587");
        boolean auth = Boolean.parseBoolean(config("SMTP_AUTH", "true"));
        boolean startTls = Boolean.parseBoolean(config("SMTP_STARTTLS", "true"));

        if (isBlank(host) || isBlank(from) || isBlank(to)) {
            LOGGER.info("Email disabled: missing SMTP_HOST/SMTP_FROM/to");
            return false;
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", String.valueOf(auth));
        props.put("mail.smtp.starttls.enable", String.valueOf(startTls));

        Session session;
        if (auth) {
            if (isBlank(username) || isBlank(password)) {
                LOGGER.warning("Email disabled: SMTP_AUTH=true but SMTP_USERNAME/SMTP_PASSWORD are missing");
                return false;
            }
            session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
        } else {
            session = Session.getInstance(props);
        }

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            return true;
        } catch (Exception e) {
            LOGGER.warning("Failed to send email to " + to + ": " + e.getMessage());
            return false;
        }
    }

    private String env(String name) {
        return System.getenv(name);
    }

    private String config(String key, String defaultValue) {
        String fromEnv = env(key);
        if (!isBlank(fromEnv)) {
            return fromEnv;
        }
        String fromFile = FILE_CONFIG.getProperty(key);
        if (!isBlank(fromFile)) {
            return fromFile;
        }
        return defaultValue;
    }

    private static Properties loadFileConfig() {
        Properties properties = new Properties();
        try (InputStream input = EmailService.class.getClassLoader().getResourceAsStream("smtp.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (Exception ignored) {
        }
        return properties;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
