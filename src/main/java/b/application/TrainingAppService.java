package b.application;

import a.host.RequestTraining;
import c.domanin.TrainingService;

public class TrainingAppService implements ITrainingAppService{
	
	TrainingService _trainAgent = new TrainingService();
	
	public RequestTraining RequestTrainingData() {
		
		RequestTraining response = new RequestTraining();
		
		response.setTrainingList(_trainAgent.GetDatabaseInfo());
		return response;
		
	}
}
