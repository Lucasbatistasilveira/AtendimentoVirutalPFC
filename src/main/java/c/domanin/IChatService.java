package c.domanin;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import Shared.RetornoNLP;

public interface IChatService {
	RetornoNLP sendMessage(String msg,String id) throws AddressException, MessagingException;
}
