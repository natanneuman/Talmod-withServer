package com.example.myapp.network.responses.studyResponses;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class StudyPlanItem {

	@SerializedName("id")
	private int studyPlanID;

	@SerializedName("typeOfStudy")
	private String typeOfStudy;

	@SerializedName("active")
	private boolean active;

	@SerializedName("user")
	private int userID;

	@SerializedName("wantChazara")
	private int wantChazara;

	@SerializedName("dafLearned")
	private List<DafLearnedItem> dafLearned;

	public StudyPlanItem(int userID, String typeOfStudy, int wantChazara, boolean active) {
		this.userID = userID;
		this.typeOfStudy = typeOfStudy;
		this.wantChazara = wantChazara;
		this.active = active;
	}

	public String getTypeOfStudy(){
		return typeOfStudy;
	}

	public boolean isActive(){
		return active;
	}

	public int getUserID(){
		return userID;
	}

	public int getWantChazara(){
		return wantChazara;
	}

	public List<DafLearnedItem> getDafLearned(){
		return dafLearned;
	}

	public int getStudyPlanID() {
		return studyPlanID;
	}
}