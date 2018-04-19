package d.infrastructure;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import org.json.*;

import Shared.RetornoNLP;

public class NLPAgent implements INLPAgent {

	@Override
	public RetornoNLP sendMessage(String Message) {
		
		RetornoNLP retornoNlp =  new RetornoNLP();
		retornoNlp.setMessage("Mensagem nova de boa tarde para a sua tarde ser perfeita!");
		retornoNlp.setIntent("Saudacao");
		retornoNlp.setAction("nothing");
		return  retornoNlp;
	}
	
	private static String readAll(Reader rd) throws IOException {
	    StringBuilder sb = new StringBuilder();
	    int cp;
	    while ((cp = rd.read()) != -1) {
	      sb.append((char) cp);
	    }
	    return sb.toString();
	  }

	@Override
	public JSONObject enviaWit(String Message) {
		// TODO Auto-generated method stub
		try {
			
			String msg = URLEncoder.encode(Message,"UTF-8");
			String url = "https://api.wit.ai/message?v=20180418&q=" + msg;
			URL obj = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
			
			conn.setRequestProperty("Authorization", "Bearer L4BRASJEB2TBVB4EKROGZI7GYHEEBDWV");
		    conn.setDoOutput(true);
		    conn.setRequestMethod("POST");
		 
		    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(),Charset.forName("UTF-8"))); 
		    String text = readAll(rd);
		    
		    JSONObject json = new JSONObject(text);
		 
		    return json;
		    
		    
		    
		    
		}catch(Exception e) {
			// printStackTrace method
            // prints line numbers + call stack
            e.printStackTrace();
             
            // Prints what exception has been thrown
            System.out.println(e);
		}
		
		return null;
	}

}


