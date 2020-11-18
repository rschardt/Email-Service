package email.service.emailservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmailserviceApplicationTests {

	@Autowired
	private MailRestController mailRestController;

	@Test
	void contextLoads() {
		assertNotNull(mailRestController);
	}

	@Test
	void test_sendSimpleMail_MailGun() {
		String mailgun_api_key = System.getProperty("MAIL_GUN_API_KEY");
		String mailgun_domain = System.getProperty("MAIL_GUN_DOMAIN");
		SendMailRequest sendMailRequest = new SendMailRequest(
				mailgun_api_key,
				mailgun_domain,
				"test",
				MailProvider.MailGun,
				"serakannzie@trash-mail.com",
				"The PostMaster <postmaster@" + mailgun_domain,
				"serakannzie@trash-mail.com",
				"test",
				"Lore ipsum"
		);

		ResponseEntity<SendMailResponse> sendMailResponseResponseEntity = mailRestController.sendMail(sendMailRequest);
		assertEquals(sendMailResponseResponseEntity.getStatusCode(), HttpStatus.OK);
		assertNotEquals(sendMailResponseResponseEntity.getBody(), null);
		assertEquals(sendMailResponseResponseEntity.getBody().provider, MailProvider.MailGun);
	}

	@Test
	void test_switchProvider_MailGun_SendGrid() {
		String sendgrid_api_key = System.getProperty("SENDGRID_API_KEY");
		SendMailRequest sendMailRequest = new SendMailRequest(
				"test",
				"test",
				sendgrid_api_key,
				MailProvider.MailGun,
				"serakannzie@trash-mail.com",
				"The PostMaster <postmaster@test.loc",
				"serakannzie@trash-mail.com",
				"test",
				"Lore ipsum"
		);

		ResponseEntity<SendMailResponse> sendMailResponseResponseEntity = mailRestController.sendMail(sendMailRequest);
		assertEquals(sendMailResponseResponseEntity.getStatusCode(), HttpStatus.OK);
		assertNotEquals(sendMailResponseResponseEntity.getBody(), null);
		assertEquals(sendMailResponseResponseEntity.getBody().provider, MailProvider.SendGrid);
	}

	@Test
	void test_sendSimpleMail_SendGrid() {
		String sendgrid_api_key = System.getProperty("SENDGRID_API_KEY");
		SendMailRequest sendMailRequest = new SendMailRequest(
				"test",
				"test",
				sendgrid_api_key,
				MailProvider.SendGrid,
				"serakannzie@trash-mail.com",
				"The PostMaster <postmaster@test.loc>",
				"serakannzie@trash-mail.com",
				"test",
				"Lore ipsum"
		);

		ResponseEntity<SendMailResponse> sendMailResponseResponseEntity = mailRestController.sendMail(sendMailRequest);
		assertEquals(sendMailResponseResponseEntity.getStatusCode(), HttpStatus.OK);
		assertNotEquals(sendMailResponseResponseEntity.getBody(), null);
		assertEquals(sendMailResponseResponseEntity.getBody().provider, MailProvider.SendGrid);
	}

	@Test
	void test_switchProvider_SendGrid_MailGun() {
		String mailgun_api_key = System.getProperty("MAIL_GUN_API_KEY");
		String mailgun_domain = System.getProperty("MAIL_GUN_DOMAIN");
		SendMailRequest sendMailRequest = new SendMailRequest(
				mailgun_api_key,
				mailgun_domain,
				"test",
				MailProvider.SendGrid,
				"serakannzie@trash-mail.com",
				"The PostMaster <postmaster@" + mailgun_domain,
				"serakannzie@trash-mail.com",
				"test",
				"Lore ipsum"
		);

		ResponseEntity<SendMailResponse> sendMailResponseResponseEntity = mailRestController.sendMail(sendMailRequest);
		assertEquals(sendMailResponseResponseEntity.getStatusCode(), HttpStatus.OK);
		assertNotEquals(sendMailResponseResponseEntity.getBody(), null);
		assertEquals(sendMailResponseResponseEntity.getBody().provider, MailProvider.MailGun);
	}

	@Test
	void test_incompleteRequest_1() {
		SendMailRequest sendMailRequest = new SendMailRequest(
				"test",
				"",
				"",
				MailProvider.MailGun,
				"",
				"",
				"",
				"",
				""
		);

		ResponseEntity<SendMailResponse> sendMailResponseResponseEntity = mailRestController.sendMail(sendMailRequest);
		assertEquals(sendMailResponseResponseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
	}

	@Test
	void test_incompleteRequest_2() {
		SendMailRequest sendMailRequest = new SendMailRequest(
				"",
				"test",
				"",
				MailProvider.MailGun,
				"",
				"",
				"",
				"",
				""
		);

		ResponseEntity<SendMailResponse> sendMailResponseResponseEntity = mailRestController.sendMail(sendMailRequest);
		assertEquals(sendMailResponseResponseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
	}

	@Test
	void test_incompleteRequest_3() {
		SendMailRequest sendMailRequest = new SendMailRequest(
				"",
				"",
				"test",
				MailProvider.SendGrid,
				"",
				"",
				"",
				"",
				""
		);

		ResponseEntity<SendMailResponse> sendMailResponseResponseEntity = mailRestController.sendMail(sendMailRequest);
		assertEquals(sendMailResponseResponseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
}
