package c.domanin;

import Shared.RetornoNLP;

public interface IChatService {
	RetornoNLP sendMessage(String msg,String id);
}
