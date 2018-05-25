package c.domanin;

import java.util.List;

import Shared.TrainingDB;
import a.host.UpdateTraining;

public interface ITrainingService {
	
	public List<TrainingDB> GetDatabaseInfo();
	public void TrainService(UpdateTraining userResponse);

}
