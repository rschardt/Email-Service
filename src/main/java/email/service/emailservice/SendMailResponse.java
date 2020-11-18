package email.service.emailservice;

public class SendMailResponse {
    public MailProvider provider;

    SendMailResponse(MailProvider mailProvider, String responseBody) {
        this.provider = mailProvider;
    }
}

