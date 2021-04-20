package com.example.myapp.network.responses.studyResponses;

import com.google.gson.annotations.SerializedName;

public class DafLearnedItem{

	@SerializedName("study_plan")
	private int studyPlan;

	@SerializedName("id")
	private int dafID;

	@SerializedName("masechet_name")
	private String masechetName;

	@SerializedName("page_number")
	private int pageNumber;

	@SerializedName("chazara")
	private int chazara;

	@SerializedName("index_in_list_dafs")
	private int indexInListDafs;

	@SerializedName("created_on")
	private String createdOn;

	public DafLearnedItem(int studyPlan, int dafID, String masechetName, int pageNumber, int chazara, int indexInListDafs) {
		this.studyPlan = studyPlan;
		this.dafID = dafID;
		this.masechetName = masechetName;
		this.pageNumber = pageNumber;
		this.chazara = chazara;
		this.indexInListDafs = indexInListDafs;
	}

	public int getChazara(){
		return chazara;
	}

	public int getPageNumber(){
		return pageNumber;
	}

	public String getCreatedOn(){
		return createdOn;
	}

	public String getMasechetName(){
		return masechetName;
	}

	public int getStudyPlan(){ return studyPlan; }

	public int getIndexInListDafs() { return indexInListDafs; }

	public int getDafID() { return dafID; }
}