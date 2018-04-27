package b.application;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import Shared.RetornoNLP;

public interface IChatAppService {
	RetornoNLP sendMessage(String msg,String id) throws AddressException, MessagingException;
}
