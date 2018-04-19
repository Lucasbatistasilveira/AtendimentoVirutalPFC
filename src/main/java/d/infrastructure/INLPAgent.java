package d.infrastructure;

import org.json.JSONObject;

import Shared.RetornoNLP;

public interface INLPAgent {
	public RetornoNLP sendMessage(String Message);
	public JSONObject enviaWit(String Message);

}

