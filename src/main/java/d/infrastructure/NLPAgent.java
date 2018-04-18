package d.infrastructure;

import Shared.RetornoNLP;

public class NLPAgent implements INLPAgent {

	@Override
	public RetornoNLP sendMessage(String Message) {
		RetornoNLP retornoNlp =  new RetornoNLP();
		retornoNlp.setMensagem("Mensagem nova de boa tarde para a sua tarde ser perfeita!");
		retornoNlp.setIntent("Saudacao");
		retornoNlp.setAction("nothing");
		return  retornoNlp;
	}

}
