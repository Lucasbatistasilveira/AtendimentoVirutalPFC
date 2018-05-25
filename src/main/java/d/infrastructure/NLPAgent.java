package d.infrastructure;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import org.json.*;

import Shared.RetornoNLP;

public class NLPAgent implements INLPAgent {
	
	private final static String UpdateIntentJson = "[{\"text\":\" %s \","
    		+ "\"entities\": ["
    		+ "{"
    		+ "	\"entity\": \"intent\","
    		+ "	\"value\":\"%s\""
    		+ "}"
    		+ "]"
    		+ "}]";

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
	
	public void CreateNewIntent(String message,String intentName) {
		
		try {
			String url = "https://api.wit.ai/samples?v=20180522";
			URL obj;
			obj = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
			
			conn.setRequestProperty("Authorization", "Bearer O23ZT2QR6VSEQEDEN4VQOER3QZOXEBVH");
			conn.setRequestProperty("Content-Type", "application/json");
		    conn.setDoOutput(true);
		    conn.setRequestMethod("POST");
		    
		    OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
		    String data = String.format(UpdateIntentJson, message, intentName);
		    
		    System.out.println("JSON to wit : " + data);
		    
		    out.write(data);
		    out.close();
		    
		    new InputStreamReader(conn.getInputStream());
		    
		    
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	public JSONObject SendWitSecondary(String message) {
		
		try {
			
			String msg = URLEncoder.encode(message,"UTF-8");
			String url = "https://api.wit.ai/message?v=20180522&q=" + msg;
			URL obj = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
			
			conn.setRequestProperty("Authorization", "Bearer O23ZT2QR6VSEQEDEN4VQOER3QZOXEBVH");
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

	public void UpdatePrimaryIntent(String message, String intent) {
		try {
			String url = "https://api.wit.ai/samples?v=20180524";
			URL obj;
			obj = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
			
			conn.setRequestProperty("Authorization", "Bearer SRJWMIJ7ZZBZYHQBGAZ6V4ABOMP56QD6");
			conn.setRequestProperty("Content-Type", "application/json");
		    conn.setDoOutput(true);
		    conn.setRequestMethod("POST");
		    
		    OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
		    String data = String.format(UpdateIntentJson, message, intent);
		    
		    out.write(data);
		    out.close();
		    
		    new InputStreamReader(conn.getInputStream());
		    
		    
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}


