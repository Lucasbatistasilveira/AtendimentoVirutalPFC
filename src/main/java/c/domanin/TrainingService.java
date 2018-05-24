package c.domanin;

import d.infrastructure.SQLRepository;

import java.util.List;

import Shared.TrainingDB;

public class TrainingService implements ITrainingService {
	
	private SQLRepository _sqlAgent = new SQLRepository();
	
	public List<TrainingDB> GetDatabaseInfo() {
		
		List<TrainingDB> result = _sqlAgent.GetTrainingDB();
		
		return result;
		
	}
}
