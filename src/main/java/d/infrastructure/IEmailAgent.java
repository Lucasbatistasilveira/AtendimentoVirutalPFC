package d.infrastructure;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

public interface IEmailAgent {

	public void generateAndSendEmail(String subject, String body, String email) throws AddressException, MessagingException;
}
