package b.application;

import Shared.RetornoNLP;

public interface IChatAppService {
	RetornoNLP sendMessage(String msg);
}
