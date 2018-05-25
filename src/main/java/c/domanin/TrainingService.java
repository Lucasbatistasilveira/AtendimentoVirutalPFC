package c.domanin;

import d.infrastructure.NLPAgent;
import d.infrastructure.SQLRepository;

import java.util.List;

import Shared.TrainingDB;
import Shared.TrainingInput;
import a.host.UpdateTraining;

public class TrainingService implements ITrainingService {
	
	private SQLRepository _sqlAgent = new SQLRepository();
	private NLPAgent _nlpAgent = new NLPAgent();
	
	public List<TrainingDB> GetDatabaseInfo() {
		
		List<TrainingDB> result = _sqlAgent.GetTrainingDB();
		
		return result;
		
	}

	public void TrainService(UpdateTraining userResponse) {
		
		
		switch(userResponse.getAction()) {
		case "validate" :
			System.out.println("Validando frases.");
			for(TrainingInput i : userResponse.getListTrainingInput()) {
				//System.out.println(i.getIntent() + " " + i.getMessage());
				_nlpAgent.UpdatePrimaryIntent(i.getMessage(), i.getIntent());
				_sqlAgent.CheckLogTrainingAsViewed(i.getMessage());
			}
			break;
		case "delete" :
			for(TrainingInput i : userResponse.getListTrainingInput()) {
				_sqlAgent.CheckLogTrainingAsViewed(i.getMessage());
			}
			break;
		default :
				System.out.println("Ação não identificada.");
		}
		
		
		// TODO : 	Treinar mensagens
		//			Marcar mensagens como visualizadas no BD
		//			
		
	}
}