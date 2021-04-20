package com.example.myapp.network.responses.studyResponses;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class StudyResponse {

	@SerializedName("id")
	private int id;

	@SerializedName("username")
	private String username;

	@SerializedName("study_plans")
	private List<StudyPlanItem> studyPlans;

	public int getId(){
		return id;
	}

	public String getUsername(){
		return username;
	}

	public List<StudyPlanItem> getStudyPlans(){
		return studyPlans;
	}
}