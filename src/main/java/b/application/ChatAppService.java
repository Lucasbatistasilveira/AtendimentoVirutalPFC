package b.application;

import Shared.RetornoNLP;
import c.domanin.ChatService;

public class ChatAppService implements IChatAppService {

	private ChatService _chatService = new ChatService();
	@Override
	public RetornoNLP sendMessage(String msg,String id) {

		return _chatService.sendMessage(msg,id);
	}

}
