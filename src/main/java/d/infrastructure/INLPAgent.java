package d.infrastructure;

import Shared.RetornoNLP;

public interface INLPAgent {
	public RetornoNLP sendMessage(String Message);
	public RetornoNLP enviaWit(String Message);

}

