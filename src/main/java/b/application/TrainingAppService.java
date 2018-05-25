package b.application;

import a.host.RequestTraining;
import a.host.UpdateTraining;
import c.domanin.TrainingService;

public class TrainingAppService implements ITrainingAppService{
	
	TrainingService _trainAgent = new TrainingService();
	
	public RequestTraining RequestTrainingData() {
		
		RequestTraining response = new RequestTraining();
		
		response.setTrainingList(_trainAgent.GetDatabaseInfo());
		return response;
		
	}

	public void UpdateNlpTraining(UpdateTraining userResponse) {
		_trainAgent.TrainService(userResponse);
		
	}
}
