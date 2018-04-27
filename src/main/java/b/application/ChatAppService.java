package b.application;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import Shared.RetornoNLP;
import c.domanin.ChatService;

public class ChatAppService implements IChatAppService {

	private ChatService _chatService = new ChatService();
	@Override
	public RetornoNLP sendMessage(String msg,String id) throws AddressException, MessagingException {
		
		return _chatService.sendMessage(msg,id);
	}

}
