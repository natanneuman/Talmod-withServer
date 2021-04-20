package com.example.myapp.repository;

import android.app.Application;
import androidx.lifecycle.MutableLiveData;
import com.example.model.DafLearned;
import com.example.model.Learning;
import com.example.model.StudyPlan;
import com.example.model.shas_masechtot.Masechet;
import com.example.model.shas_masechtot.Seder;
import com.example.model.shas_masechtot.Shas;
import com.example.myapp.dataBase.AppDataBase;
import com.example.myapp.network.RequestsManager;
import com.example.myapp.network.responses.studyResponses.DafLearnedItem;
import com.example.myapp.network.responses.studyResponses.StudyPlanItem;
import com.example.myapp.network.responses.studyResponses.StudyResponse;
import com.example.myapp.network.responses.userResponses.UserResponse;
import com.example.myapp.utils.ConvertIntToPage;
import com.example.myapp.utils.InitAllShasFromGson;
import com.example.myapp.utils.ManageSharedPreferences;
import com.example.myapp.utils.UtilsCalender;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import static com.example.myapp.utils.StaticVariables.stringDafHayomi;

public class Repository {
    private Application mApplication;
    private static Repository INSTANCE;
    private MutableLiveData<ArrayList<Learning>> allLearning = new MutableLiveData<>();
    private Shas mAllShas;
    private boolean splashActivityUpdated;

    public static Repository getInstance(Application mContext, RepositoryListener mListener){
        if (INSTANCE == null){
            INSTANCE = new Repository(mContext, mListener);
        }
      return INSTANCE;
    }

    private void afterDeleteStudyCheckIfHave(RepositoryListener mListener) {
    }


    private Repository(Application context, RepositoryListener mListener) {
        this.mApplication = context;
        initDataFromDBAndServer(mListener);
    }

    public void initDataFromDBAndServer(RepositoryListener mListener){
//        AppDataBase.getInstance(mApplication).daoLearning().deleteAll();


        mAllShas = InitAllShasFromGson.getInstance(mApplication);
        ArrayList<Learning> mLearning = (ArrayList<Learning>) AppDataBase.getInstance(mApplication).daoLearning().getStudyPlanWithLearning();

        if (mLearning != null && mLearning.size()>0) {
            allLearning.setValue(initListsAllDafs(mLearning));
            mListener.haveStudyPlan(false);
            splashActivityUpdated = true;
        }
       UserResponse userResponse = ManageSharedPreferences.getToken( mApplication);
            RequestsManager.getInstance().getStudyPlanWithLearning(userResponse, new RequestsManager.OnRequestManagerResponseListener<StudyResponse>() {
                @Override
                public void onRequestSucceed(StudyResponse result) {
                    if (result != null && result.getStudyPlans() != null && result.getStudyPlans().size()>0){
                        allLearning.setValue( initListsAllDafs(parsAndSaveData(result.getStudyPlans())));
                        if (!splashActivityUpdated) {
                            mListener.haveStudyPlan(false);
                        }
                    }else {
                        if (!splashActivityUpdated) {
                            mListener.haveStudyPlan(true);
                        }
                    }
                }

                @Override
                public void onRequestFailed(String error) {
                    if (!splashActivityUpdated) {
//                         log thet we cant
//                        finish app
                    }
                }
            });
        }

    private ArrayList<Learning> initListsAllDafs(ArrayList<Learning> mAllLearning) {
        for (Learning learning:mAllLearning) {
            initListAllDafsToShow(learning);
            updateDafsLearned(learning);
        }
        return mAllLearning;
    }


    private ArrayList<Learning> parsAndSaveData(List<StudyPlanItem> result) {
        AppDataBase.getInstance(mApplication).daoLearning().deleteAll();
        ArrayList<Learning> allLearning = new ArrayList<>();
        for (StudyPlanItem studyPlanServer:result) {
            Learning learning = new Learning();
            StudyPlan studyPlan = parsStudyPlan(studyPlanServer);
            learning.setStudyPlan(studyPlan);
            AppDataBase.getInstance(mApplication).daoLearning().insertStudyPlan(studyPlan);

            ArrayList <DafLearned> mDafLearned = parsDafLearned(studyPlanServer.getDafLearned());
            learning.setDafLearned(mDafLearned);
            AppDataBase.getInstance(mApplication).daoLearning().insertAllDaf(mDafLearned);

            allLearning.add(learning);
        }
        return allLearning;
    }

    private ArrayList<DafLearned> parsDafLearned(List<DafLearnedItem> dafLearnedItem) {
        ArrayList<DafLearned> mDafLearned = new ArrayList<>();
        for (DafLearnedItem item:dafLearnedItem) {
            mDafLearned.add(new DafLearned(item.getStudyPlan(),item.getDafID(),item.getMasechetName(),item.getPageNumber(),true,item.getChazara(), item.getIndexInListDafs()));
        }
        return mDafLearned;
    }


    public void initListAllDafsToShow(Learning learning) {
        if (learning.getStudyPlan().getTypeOfStudy().equals(stringDafHayomi)) {
            learning.setAllDafsToShow(createListDafHayomi(learning.getStudyPlan()));
        } else {
            learning.setAllDafsToShow(createListMasechet(learning.getStudyPlan()));
        }
    }


    private void updateDafsLearned(Learning learning) {
        for (DafLearned dafLearned:learning.getDafLearned()) {
            DafLearned mCorrectDafLearned = learning.getAllDafsToShow().get(dafLearned.getIndexInListDafs());
            if (!mCorrectDafLearned.getMasechetName().equals(dafLearned.getMasechetName()) || mCorrectDafLearned.getPageNumber() != (dafLearned.getPageNumber())){
                mCorrectDafLearned = findDafIfIndexInListNotCorrect(dafLearned, learning.getAllDafsToShow());
            }

            if (mCorrectDafLearned != null) {
                mCorrectDafLearned.setDafLearnedId(dafLearned.getDafLearnedId());
                mCorrectDafLearned.setLearned(true);
                mCorrectDafLearned.setChazara(dafLearned.getChazara());
            }


//            if (dafLearned.getMasechetName().equals() == learning.getStudyPlan().getId()){
//                learning.getAllDafs().get(dafLearned.getIndexInListDafs()).setLearning(true);
//                learning.getAllDafs().get(dafLearned.getIndexInListDafs()).setChazara(dafLearned.getChazara());
//            }
        }
    }

    private DafLearned findDafIfIndexInListNotCorrect(DafLearned dafLearned, ArrayList<DafLearned> allDafsToShow) {
        for (DafLearned dafFromDafsToShow :allDafsToShow) {
            if (dafFromDafsToShow.getMasechetName().equals(dafLearned.getMasechetName()) && dafFromDafsToShow.getPageNumber() == dafLearned.getPageNumber()){
                return dafFromDafsToShow;
            }
        }
        return null;
    }


    private ArrayList<DafLearned> createListDafHayomi(StudyPlan mStudyPlan) {
        ArrayList <DafLearned> allDafs = new ArrayList<>();
        Calendar dateDafHayomi = UtilsCalender.findDateOfStartDafHayomiEnglishDate();
        for (int i = 0; i < mAllShas.getSeder().size(); i++) {
            for (int j = 0; j < mAllShas.getSeder().get(i).getMasechtot().size(); j++) {
                for (int k = 2; k < (mAllShas.getSeder().get(i).getMasechtot().get(j).getPages() + 2); k++) {
                    int masechetPage = ConvertIntToPage.fixKinimTamidMidot(k, mAllShas.getSeder().get(i).getMasechtot().get(j).getName());
//                    Daf mPage = new Daf(mAllShas.getSeder().get(i).getMasechtot().get(j).getName(), masechetPage, stringDafHayomi, mStudyPlan.getId());
                    DafLearned mDafLearned = new DafLearned(mStudyPlan.getId(), mAllShas.getSeder().get(i).getMasechtot().get(j).getName(),
                            masechetPage,false, 0,k-2);
                    mDafLearned.setPageDate(UtilsCalender.dateStringFormat(dateDafHayomi));
                    allDafs.add(mDafLearned);
                    dateDafHayomi.add(Calendar.DATE, 1);
                }
            }
        }
        return allDafs;
    }

    private ArrayList<DafLearned> createListMasechet(StudyPlan mStudyPlan) {
        int pages = findPageSum(mStudyPlan.getTypeOfStudy());
        ArrayList <DafLearned> allDafs = new ArrayList<>();
        for (int i = 2; i < (pages + 2); i++) {
//            allDafs.add(new Daf(mStudyPlan.getTypeOfStudy(), ConvertIntToPage.fixKinimTamidMidot(i, mStudyPlan.getTypeOfStudy()),
//                    mStudyPlan.getTypeOfStudy(), mStudyPlan.getId()));
            allDafs.add(new DafLearned(mStudyPlan.getId(), mStudyPlan.getTypeOfStudy(),ConvertIntToPage.fixKinimTamidMidot(i, mStudyPlan.getTypeOfStudy()),
                    false,0,i-2));
        }
        return allDafs;
    }


    private int findPageSum(String typeOfStudy) {
        for (Seder mSeder :mAllShas.getSeder()) {
            for (Masechet masechet:mSeder.getMasechtot()) {
                if (masechet.getName().equals(typeOfStudy)){
                    return masechet.getPages();
                }
            }
        }
        return -1;
    }


    private StudyPlan parsStudyPlan(StudyPlanItem studyPlanServer) {
       return new StudyPlan(studyPlanServer.getStudyPlanID(),studyPlanServer.getTypeOfStudy(),studyPlanServer.getWantChazara());
    }

    public MutableLiveData<ArrayList<Learning>> getAllLearning() {
        return allLearning;
    }

    public interface RepositoryListener {

        void haveStudyPlan(boolean firstTime);
    }
}
