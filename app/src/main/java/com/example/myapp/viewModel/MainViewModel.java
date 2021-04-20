package com.example.myapp.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.example.model.Learning;
import com.example.model.StudyPlan;
import com.example.myapp.dataBase.AppDataBase;
import com.example.myapp.network.responses.studyResponses.StudyPlanItem;
import com.example.myapp.repository.Repository;
import java.util.ArrayList;

public class MainViewModel extends AndroidViewModel {


    private Application mApplication;
    private Repository mRepository;

    public MainViewModel(Application application) {
        super(application);
        mApplication = application;
        mRepository = Repository.getInstance(mApplication,null);
    }

    public MutableLiveData<ArrayList<Learning>> getAllLearning() {
      return mRepository.getAllLearning();
    }



//    public void createNewStudyPlan(int newStudyPlanID) {
//        StudyPlan mStudyPlan = AppDataBase.getInstance(mApplication).daoLearning().getStudyPlan(newStudyPlanID);
//        Learning newLearning = new Learning();
//        newLearning.setStudyPlan(mStudyPlan);
//        mRepository.initListAllDafsToShow(newLearning);
//        ArrayList <Learning> learnings = getAllLearning().getValue();
//        learnings.add(newLearning);
//        getAllLearning().setValue(learnings);
//    }

    public void createNewStudyPlan(StudyPlanItem studyPlanItem) {
        Learning newLearning = new Learning();
        newLearning.setStudyPlan(new StudyPlan(studyPlanItem.getStudyPlanID(),studyPlanItem.getTypeOfStudy(),studyPlanItem.getWantChazara()));
        mRepository.initListAllDafsToShow(newLearning);
        ArrayList <Learning> learnings = getAllLearning().getValue();
        if (learnings == null){
            learnings = new ArrayList<>();
        }
        learnings.add(newLearning);
        getAllLearning().setValue(learnings);
    }
}
