package email.service.emailservice;

import com.sendgrid.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static com.sendgrid.Method.POST;
import static email.service.emailservice.MailProvider.MailGun;
import static email.service.emailservice.MailProvider.SendGrid;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@RestController
public class MailRestController {

    Logger logger = LoggerFactory.getLogger(MailRestController.class);

    @PostMapping(path = "/mail/send", consumes = "application/json", produces = "application/json")
    public ResponseEntity<SendMailResponse> sendMail(@RequestBody SendMailRequest sendMailRequest) {

        if (sendMailRequest.hasAPIKeys()) {

            if (sendMailRequest.provider == MailGun) {
                ResponseEntity<SendMailResponse> sendMailResponseResponseEntity = sendMail_MailGun(sendMailRequest);
                if (sendMailResponseResponseEntity.hasBody()) {
                    return sendMailResponseResponseEntity;
                } else {
                    logger.info("Switching to sendgrid api");
                    return sendMail_SendGrid(sendMailRequest);
                }
            } else if (sendMailRequest.provider == SendGrid) {
                ResponseEntity<SendMailResponse> sendMailResponseResponseEntity = sendMail_SendGrid(sendMailRequest);
                if (sendMailResponseResponseEntity.hasBody()) {
                    return sendMailResponseResponseEntity;
                } else {
                    logger.info("Switching to mailgun api");
                    return sendMail_MailGun(sendMailRequest);
                }
            }
        }

        logger.error("The SendMailRequest passed to sendMail(sendMailRequest) has no API Keys!");
        return ResponseEntity.badRequest().build();
    }

    private ResponseEntity<SendMailResponse> sendMail_MailGun(SendMailRequest sendMailRequest) {
        try {
            logger.info("Trying to forward the post request to the mailgun api.");

            com.mashape.unirest.http.HttpResponse<com.mashape.unirest.http.JsonNode> response = Unirest.post("https://api.mailgun.net/v3/" + sendMailRequest.mailgun_domain + "/messages")
                    .basicAuth("api", sendMailRequest.mailgun_api_key)
                    .field("from", sendMailRequest.from)
                    .field("to", sendMailRequest.to)
                    .field("subject", sendMailRequest.subject)
                    .field("text", sendMailRequest.text)
                    .asJson();

            logger.info("The post request has been forwarded to the mailgun api.");
            HttpStatus httpStatus = HttpStatus.resolve(response.getStatus());
            if (httpStatus.is2xxSuccessful()) {
                logger.info("The mailgun api responded with success: {}", httpStatus.toString());
                SendMailResponse sendMailResponse = new SendMailResponse(MailGun, response.getBody().toString());
                return new ResponseEntity<>(sendMailResponse, httpStatus);
            } else {
                logger.warn("The mailgun api responded with an error: {}", httpStatus.toString());
                return new ResponseEntity<>(httpStatus);
            }

        } catch (UnirestException unirestException) {
            logger.error("While trying to forward to mailgun api some internal server error occurred: {}", unirestException.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<SendMailResponse> sendMail_SendGrid(SendMailRequest sendMailRequest) {

        SendGrid sg = new SendGrid(sendMailRequest.sendgrid_api_key);

        try {
            logger.info("Trying to forward the post request to the sendgrid api.");

            Request request = new Request();
            request.setMethod(POST);
            request.setEndpoint("mail/send");
            request.setBody(sendMailRequest.toSendGridBody());

            Response response = sg.api(request);

            logger.info("The post request has been forwarded to the sendgrid api");
            HttpStatus httpStatus = HttpStatus.resolve(response.getStatusCode());
            if (httpStatus.is2xxSuccessful()) {
                logger.info("The sendgrid api responded with success: {}", httpStatus.toString());
                SendMailResponse sendMailResponse = new SendMailResponse(SendGrid, response.getBody());
                return new ResponseEntity<>(sendMailResponse, httpStatus);
            } else {
                logger.warn("The sendgrid api responded with an error: {}", httpStatus.toString());
                return new ResponseEntity<>(httpStatus);
            }

        } catch (IOException ex) {
            logger.error("While trying to forward to sendgrid api some internal server error occurred: {}", ex.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}