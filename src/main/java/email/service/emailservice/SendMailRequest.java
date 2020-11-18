package email.service.emailservice;

public class SendMailRequest {
    public String mailgun_api_key;
    public String mailgun_domain;
    public String sendgrid_api_key;
    public MailProvider provider;
    public String recipient;
    public String from;
    public String to;
    public String subject;
    public String text;

    SendMailRequest(String mailgun_api_key, String mailgun_domain, String sendgrid_api_key, MailProvider provider, String recipient, String from, String to, String subject, String text) {

        this.mailgun_api_key = mailgun_api_key;
        this.mailgun_domain = mailgun_domain;
        this.sendgrid_api_key = sendgrid_api_key;
        this.provider = provider;
        this.recipient = recipient;
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.text = text;
    }

    boolean hasAPIKeys() {
        if (this.mailgun_api_key == null || this.mailgun_api_key.isEmpty()) return false;
        if (this.mailgun_domain == null || this.mailgun_domain.isEmpty()) return false;
        if (this.sendgrid_api_key == null || this.sendgrid_api_key.isEmpty()) return false;
        return true;
    }

    public String toSendGridBody() {
        return
        "{\"personalizations\":[{\"to\":" +
                "[{\"email\":\"" + this.to + "\"}]," +
                "\"subject\":\"" + this.subject + "\"}]," +
                "\"from\":{\"email\":\"" + this.from + "\"}," +
                "\"content\":[{\"type\":\"text/plain\"," +
                "\"value\": \"" + this.text + "\"}]}";
    }
}

