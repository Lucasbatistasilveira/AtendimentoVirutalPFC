package com.ufmgatendimento.antendimentovirtual;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import Shared.RetornoNLP;

import a.host.RequestLogin;

import a.host.ResponseLogin;
import b.application.ChatAppService;

import java.util.HashMap;
import java.util.Map;

import b.application.*;

@Path("unifylogin")
public class UnifyLoginController {
	private LoginAppService _loginAppService= new LoginAppService();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Map<String, String> getRequestParameters(@QueryParam("token") String token) {
		Map<String, String> response = _loginAppService.GetUserByToken(token);
		return response;
		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseLogin SendMessage(RequestLogin userResponse){
		
		ResponseLogin responseLogin = _loginAppService.SetUserLoginAsUnique(userResponse);;
	    return responseLogin;
	}

}


