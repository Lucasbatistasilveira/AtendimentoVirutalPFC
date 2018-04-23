package a.host;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import Shared.RetornoNLP;
import b.application.ChatAppService;

@Path("sends")
public class ChatController {	
	private ChatAppService _chatAppService = new ChatAppService();
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	public Response SendMessage(Request userMessage) {
	    RetornoNLP NplFallBack = _chatAppService.sendMessage(userMessage.getMessage(),userMessage.getId());
	    Response response = new Response();
	    response.setMessage(NplFallBack.getMessage());
	    return response;
	}
}