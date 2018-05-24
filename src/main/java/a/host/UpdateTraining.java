package a.host;

import java.util.List;

import Shared.TrainingInput;

public class UpdateTraining {
	List<TrainingInput> listTrainingInput;
	String action;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public List<TrainingInput> getListTrainingInput() {
		return listTrainingInput;
	}

	public void setListTrainingInput(List<TrainingInput> listTrainingInput) {
		this.listTrainingInput = listTrainingInput;
	}
}
