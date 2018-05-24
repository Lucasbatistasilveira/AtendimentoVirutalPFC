package com.ufmgatendimento.antendimentovirtual;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import a.host.RequestLogin;
import a.host.RequestTraining;
import a.host.ResponseLogin;
import b.application.TrainingAppService;

@Path("trainingmonitor")
public class TrainingMonitor {
	
	TrainingAppService _trainingAppService = new TrainingAppService();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public RequestTraining getRequestParameters() {
		RequestTraining response = _trainingAppService.RequestTrainingData();
		return response;
		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseLogin SendMessage(RequestLogin userResponse){
	    return null;
	}
}
