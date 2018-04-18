package c.domanin;

import Shared.RetornoNLP;
import d.infrastructure.NLPAgent;

public class ChatService implements IChatService {
	
	private NLPAgent _nlpAgent =  new NLPAgent();
	@Override
	public RetornoNLP sendMessage(String msg) {
		return _nlpAgent.sendMessage(msg);
	}

}
