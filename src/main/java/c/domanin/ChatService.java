package c.domanin;

import Shared.RetornoNLP;
import Shared.User;
import d.infrastructure.NLPAgent;

public class ChatService implements IChatService {
	
	private NLPAgent _nlpAgent =  new NLPAgent();
	@Override
	public RetornoNLP sendMessage(String msg) {
		// TODO Auto-generated method stub
		if(User.getContext() == null) {
			User.setContext("init");
		}
		return _nlpAgent.enviaWit(msg);
	}

}
