package d.infrastructure;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

public interface IEmailAgent {

	public void generateAndSendEmail(String email, String nome, String assunto) throws AddressException, MessagingException;
}
