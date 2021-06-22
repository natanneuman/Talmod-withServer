package com.example.myapp.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.model.DafLearned;
import com.example.model.Learning;
import com.example.model.StudyPlan;
import com.example.model.enums.FILTERS;
import com.example.myapp.R;
import com.example.myapp.dataBase.AppDataBase;
import com.example.myapp.network.RequestsManager;
import com.example.myapp.network.responses.studyResponses.DafLearnedItem;
import com.example.myapp.network.responses.userResponses.UserResponse;
import com.example.myapp.utils.ConvertIntToPage;
import com.example.myapp.utils.ManageSharedPreferences;
import com.example.myapp.utils.UtilsCalender;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

import static com.example.myapp.utils.StaticVariables.index1;
import static com.example.myapp.utils.StaticVariables.index2;
import static com.example.myapp.utils.StaticVariables.index3;
import static com.example.myapp.utils.StaticVariables.stringDafHayomi;

public class DafAdapter extends RecyclerView.Adapter<DafAdapter.ViewHolder> {

    private final Activity activity;
    private RecyclerView.LayoutManager mLayoutManager;
    private listenerOneDafAdapter mListener;
    private Learning mMaster1;
    private Learning mMaster2;
    private Learning mMaster3;
    private ArrayList<DafLearned> myListALLDaf = new ArrayList<>();
    private ArrayList<DafLearned> myListDafToDisplay = new ArrayList<>();
    private ArrayList<DafLearned> myListFilterDaf = new ArrayList<>();
    private int currentTabString;
    private UserResponse userToken;



    public DafAdapter(Activity activity, RecyclerView.LayoutManager layoutManager, Learning mMaster1 , Learning mMaster2, Learning mMaster3, listenerOneDafAdapter mListener) {
        this.activity = activity;
        this.mLayoutManager = layoutManager;
        this.mMaster1 = mMaster1;
        this.mMaster2 = mMaster2;
        this.mMaster3 = mMaster3;
        this.mListener = mListener;
        userToken = ManageSharedPreferences.getToken(activity);
        initLearning(mMaster1);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_daf, parent, false);
        return new DafAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setHolder(myListDafToDisplay.get(position));
    }

    @Override
    public int getItemCount() {
        return myListDafToDisplay.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private void initLearning(Learning mMaster1) {
        this.myListALLDaf.addAll(mMaster1.getAllDafsToShow());
        this.myListDafToDisplay.addAll(myListALLDaf);
        scrollRecyclerView(myListALLDaf);
        setSummaryLearning(R.string.all,myListALLDaf);
    }

    private void scrollRecyclerView(ArrayList<DafLearned> myListALLDaf) {
        if (myListALLDaf.size() > 2000) {
            int todayDaf = findTodayDafHayomi(myListALLDaf);
            if (todayDaf != -1) {
                mLayoutManager.scrollToPosition(todayDaf);
            }
        }else if (myListALLDaf.size()>0 && myListALLDaf.size()<300){
            int correctDaf =  findLastPageLearned(myListALLDaf);
            if (correctDaf != -1) {
                mLayoutManager.scrollToPosition(correctDaf);
            }else {
                mLayoutManager.scrollToPosition(0);
            }
        }
    }

    private int findLastPageLearned(ArrayList<DafLearned> myListALLDaf) {
        int lastLearned =-1;
        for (int i = myListALLDaf.size()-1; i >= 0 ; i--) {
            if (myListALLDaf.get(i).isLearned()) {
                lastLearned = i;
                break;
            }
        }
        return lastLearned;
    }

    private int findTodayDafHayomi(ArrayList<DafLearned> myListALLDaf) {
        int todayDafInList = -1;
        String today = UtilsCalender.dateStringFormat(Calendar.getInstance());
        for (int i = 0; i < myListALLDaf.size(); i++) {
            if (myListALLDaf.get(i).getPageDate().equals(today)) {
                todayDafInList = i;
                break;
            }
        }
        return todayDafInList;
    }

    private void setSummaryLearning(int tabString, ArrayList<DafLearned> mList) {
        currentTabString = tabString;
        switch(tabString) {
            case R.string.all:
            case R.string.learned:
                mListener.initSummaryLearning(getNameLearning(mList),
                        String.format(Locale.getDefault(),"%s %d %s", activity.getString(R.string.you_learned), calculateHowMuchLearned(mList), activity.getString(R.string.pages)),
                        String.format(Locale.getDefault(),"%s %d", activity.getString(R.string.from), mList.size()));
                break;
            case R.string.skipped:
                mListener.initSummaryLearning(getNameLearning(mList),
                        String.format(Locale.getDefault(),"%d %s", + calculateHowMuchSkipped(mList), activity.getString(R.string.pages_to_complete)),
                        "");
                break;
        }
    }

    private int calculateHowMuchLearned(ArrayList<DafLearned> mList) {
        int learned = 0;
        for (DafLearned daf: mList) {
            if (daf.isLearned()){
                learned++;
            }
        }
        return learned;
    }

    private int calculateHowMuchSkipped(ArrayList<DafLearned> mList) {
        int learned = mList.size();
        for (DafLearned daf: mList) {
            if (daf.isLearned()){
                learned--;
            }
        }
        return learned;
    }


    private String getNameLearning(ArrayList<DafLearned> mList) {
        if (mList.size() > 0) {
            StudyPlan mStudyPlan = findStudyPlan(mList.get(0));
            if (mStudyPlan != null)
            return mStudyPlan.getTypeOfStudy().equals(stringDafHayomi) ? stringDafHayomi : String.format(Locale.getDefault(),"%s %s", activity.getString(R.string.masechet), mStudyPlan.getTypeOfStudy());
        }
        return "";
    }

    private StudyPlan findStudyPlan(DafLearned dafLearned) {
        if (dafLearned.getStudyPlanID() == mMaster1.getStudyPlan().getId()){
            return mMaster1.getStudyPlan();
        }
        if (dafLearned.getStudyPlanID() == mMaster2.getStudyPlan().getId()){
            return mMaster2.getStudyPlan();
        }

        if (dafLearned.getStudyPlanID() == mMaster3.getStudyPlan().getId()){
            return mMaster3.getStudyPlan();
        }
        return null;
    }

    public void filter(FILTERS filter){
        myListFilterDaf.clear();

        if (filter ==FILTERS.allLearning){
            changeDisplayList(myListALLDaf);
            visibleRVMasechtot(myListALLDaf.size());
            scrollRecyclerView(myListALLDaf);
            setSummaryLearning(R.string.all,myListALLDaf);
        }

        if (filter == FILTERS.learned){
            for (int i = 0; i <myListALLDaf.size() ; i++) {
                if (myListALLDaf.get(i).isLearned()) {
                    myListFilterDaf.add(myListALLDaf.get(i));
                }
            }
            Collections.reverse(myListFilterDaf);
            changeDisplayList(myListFilterDaf);
            setSummaryLearning(R.string.learned,myListALLDaf);
        }

        if (filter == FILTERS.skipped){
            if(myListALLDaf.size()<2000){
                int myLastPageHasBeenLearned = -1;
               myLastPageHasBeenLearned = findLastPageLearned(myListALLDaf);
                if (myLastPageHasBeenLearned != -1) {
                    for (int i = 0; i <= myLastPageHasBeenLearned; i++) {
                        if (!myListALLDaf.get(i).isLearned()) {
                            myListFilterDaf.add(myListALLDaf.get(i));
                        }
                    }
                }
            }
            if(myListALLDaf.size()>2000){
                int todayDaf = -1;
                todayDaf = findTodayDafHayomi(myListALLDaf);
                if (todayDaf != -1) {
                    for (int i = 0; i <= todayDaf-1; i++) {
                        if (!myListALLDaf.get(i).isLearned()) {
                            myListFilterDaf.add(myListALLDaf.get(i));
                        }
                    }
                }
            }
            Collections.reverse(myListFilterDaf);
            changeDisplayList(myListFilterDaf);
            setSummaryLearning(R.string.skipped,myListFilterDaf);
        }
    }

    private void visibleRVMasechtot(int size) {
        RecyclerView masechtotRecyclerView = activity.findViewById(R.id.show_study_RV_masechtot);
        if (size>2000){
            masechtotRecyclerView.setVisibility(View.VISIBLE);
        }else {
            masechtotRecyclerView.setVisibility(View.GONE);
        }
    }

    public void changeTypeStudy(int typeStudy){
        myListALLDaf.clear();
        switch(typeStudy) {
            case index1:
                myListALLDaf.addAll(mMaster1.getAllDafsToShow());
                break;
            case index2:
                myListALLDaf.addAll(mMaster2.getAllDafsToShow());
                break;
            case index3:
                myListALLDaf.addAll(mMaster3.getAllDafsToShow());
                break;
        }
        changeDisplayList(myListALLDaf);
        setSummaryLearning(R.string.all,myListALLDaf);
    }

    public void filterOneMasechet(String nameMasechet){
        myListFilterDaf.clear();
        for (int i = 0; i <myListALLDaf.size() ; i++) {
            if (myListALLDaf.get(i).getMasechetName().equals(nameMasechet)) {
                myListFilterDaf.add(myListALLDaf.get(i));
            }
        }
        changeDisplayList(myListFilterDaf);
    }

    private void changeDisplayList(ArrayList<DafLearned> mNewList) {
        myListDafToDisplay.clear();
        myListDafToDisplay.addAll(mNewList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
       private CheckBox ifLearn;
       private TextView masechet;
       private TextView numDaf;
       private TextView date;
       private LinearLayout linearLayoutChazara;
       private CheckBox chazara1;
       private CheckBox chazara2;
       private CheckBox chazara3;
       private TextView showDafTV;
       private DafLearned mDaf;
       private int chazaraBeforeUpdate;


        ViewHolder(View itemView) {
            super(itemView);
            ifLearn = itemView.findViewById(R.id.one_daf_checkbox);
            masechet = itemView.findViewById(R.id.one_daf_Tv_masechet);
            numDaf = itemView.findViewById(R.id.one_daf_Tv_numPage);
            date = itemView.findViewById((R.id.one_daf_Tv_date));
            linearLayoutChazara = itemView.findViewById((R.id.one_daf_chazara_LL));
            chazara1 = itemView.findViewById((R.id.chazara_1_CB));
            chazara2 = itemView.findViewById((R.id.chazara_2_CB));
            chazara3 = itemView.findViewById((R.id.chazara_3_CB));
            showDafTV = itemView.findViewById((R.id.IRD_show_daf_TV));

            


            ifLearn.setOnClickListener(v -> {
                mDaf.setLearned(ifLearn.isChecked());
                if(ifLearn.isChecked()){
                    requestsLeanedDaf(false);

                }else {
                    RequestsManager.getInstance().unLearnDaf(parsDafToDafLearnedItem(mDaf), userToken, new RequestsManager.OnRequestManagerResponseListener<DafLearnedItem>() {
                        @Override
                        public void onRequestSucceed(DafLearnedItem result) {
                            mDaf.setLearned(false);
                            mDaf.setDafLearnedId(0);
                            mDaf.setChazara(0);
                            AppDataBase.getInstance(activity).daoLearning().deleteDaf(mDaf.getStudyPlanID(),mDaf.getMasechetName(),mDaf.getPageNumber());
                            setStatusChazara(0);
                        }

                        @Override
                        public void onRequestFailed(String error) {
                            ifLearn.setChecked(true);
                            mDaf.setLearned(true);
                            //                                    log server eror
                        }
                    });



                }


//                AppDataBase.getInstance(activity).daoLearning().updateIsLearning(ifLearn.isChecked(),mDaf.getIndexTypeOfStudy() ,mDaf.getMasechet(),mDaf.getPageNumber());
//                updateListIfLearning(ifLearn.isChecked());
                updateSummaryLearning();
            });

            setChazaraListener(true);
            showDafTV.setOnClickListener(v -> EventBus.getDefault().post(mDaf));


        }

        private void requestsLeanedDaf(boolean updateChazara) {
            RequestsManager.getInstance().leanedDaf(parsDafToDafLearnedItem(mDaf), userToken, new RequestsManager.OnRequestManagerResponseListener<DafLearnedItem>() {
                @Override
                public void onRequestSucceed(DafLearnedItem result) {
                    mDaf.setDafLearnedId(result.getDafID());
                    AppDataBase.getInstance(activity).daoLearning().insertDaf(mDaf);
                }

                @Override
                public void onRequestFailed(String error) {
                    mDaf.setLearned(false);
                    ifLearn.setChecked(false);
                    if (updateChazara) {
                        mDaf.setChazara(chazaraBeforeUpdate);
                        setStatusChazara(chazaraBeforeUpdate);
                    }
//                                    log server eror
                    error.toString();

                }
            });
        }


        private void setStatusChazara(int chazara) {
            setChazaraListener(false);
            switch(chazara) {
                case 0:
                    chazara1.setChecked(false);
                    chazara2.setChecked(false);
                    chazara3.setChecked(false);
                    break;
                case 1:
                    chazara1.setChecked(true);
                    chazara2.setChecked(false);
                    chazara3.setChecked(false);
                    break;
                case 2:
                    chazara1.setChecked(true);
                    chazara2.setChecked(true);
                    chazara3.setChecked(false);
                    break;
                case 3:
                    chazara1.setChecked(true);
                    chazara2.setChecked(true);
                    chazara3.setChecked(true);
                    break;
            }
            setChazaraListener(true);
        }



        private void setChazaraListener(boolean setListener) {
            if (setListener) {
                chazara1.setOnClickListener(v -> chazaraClickListener(chazara1, 1));
                chazara2.setOnClickListener(v -> chazaraClickListener(chazara2, 2));
                chazara3.setOnClickListener(v -> chazaraClickListener(chazara3, 3));
            }else {
                chazara1.setOnClickListener(null);
                chazara2.setOnClickListener(null);
                chazara3.setOnClickListener(null);
            }
        }

        private void updateSummaryLearning() {
            switch(currentTabString) {
                case R.string.all:
                case R.string.learned:
                    setSummaryLearning(currentTabString,myListALLDaf);
                    break;
                case R.string.skipped:
                    setSummaryLearning(currentTabString,myListDafToDisplay);
                    break;
            }
        }

        private void chazaraClickListener(CheckBox chazara, int indexChazara) {
            if (chazara.isChecked()) {
                updateServerAndDBChazara(indexChazara);
            } else {
                updateServerAndDBChazara(indexChazara-1);
            }
        }

        private void updateServerAndDBChazara(int chazara) {
            chazaraBeforeUpdate = mDaf.getChazara();
            mDaf.setChazara(chazara);
            setStatusChazara(chazara);
            if (!ifLearn.isChecked()){
                mDaf.setLearned(true);
                ifLearn.setChecked(true);
                requestsLeanedDaf(true);
            }else {
                updateChazaraListAndDB(chazara);
            }
        }

//        private void updateListIfLearning(boolean checked) {
//            for (int i = 0; i <myListALLDaf.size() ; i++) {
//                if (myListALLDaf.get(i).getMasechetName().equals(mDaf.getMasechetName()) && myListALLDaf.get(i).getPageNumber() == mDaf.getPageNumber()){
//                    myListALLDaf.get(i).setLearned(checked);
//                    return;
//                }
//            }
//        }

        private void updateChazaraListAndDB(int chazara) {
            RequestsManager.getInstance().updateChazara(parsDafToDafLearnedItem(mDaf), userToken ,new RequestsManager.OnRequestManagerResponseListener<DafLearnedItem>() {
                        @Override
                        public void onRequestSucceed(DafLearnedItem result) {
                            AppDataBase.getInstance(activity).daoLearning().updateChazara(chazara ,mDaf.getStudyPlanID(),mDaf.getMasechetName(),mDaf.getPageNumber());
                        }

                        @Override
                        public void onRequestFailed(String error) {
//                            log
                            setStatusChazara(chazaraBeforeUpdate);
                            mDaf.setChazara(chazaraBeforeUpdate);
                        }
                    });
        }

//        private void updateListInChazara(int chazara) {
//            for (int i = 0; i <myListALLDaf.size() ; i++) {
//                if (myListALLDaf.get(i).getMasechetName().equals(mDaf.getMasechetName()) && myListALLDaf.get(i).getPageNumber() == mDaf.getPageNumber()){
//                    myListALLDaf.get(i).setChazara(chazara);
//                    return;
//                }
//            }
//        }

        public void setHolder(DafLearned mDaf) {
            this.mDaf = mDaf;
            initWantChazara(findStudyPlan(mDaf));
            ifLearn.setChecked(mDaf.isLearned());
            masechet.setText(mDaf.getMasechetName());
            numDaf.setText(ConvertIntToPage.intToPage(mDaf.getPageNumber()));
            initChazara(mDaf.getChazara());
            if (mDaf.getPageDate() != null && !mDaf.getPageDate().isEmpty()) {
                date.setText(UtilsCalender.convertDateToHebrewDate(mDaf.getPageDate()));
            }else {
                date.setText("");
            }
        }


        public void initWantChazara(StudyPlan studyPlan) {
            if (studyPlan != null) {
                switch (studyPlan.getWantChazara()) {
                    case 0:
                        linearLayoutChazara.setVisibility(View.GONE);
                        break;
                    case 1:
                        linearLayoutChazara.setVisibility(View.VISIBLE);
                        chazara1.setVisibility(View.VISIBLE);
                        chazara2.setVisibility(View.GONE);
                        chazara3.setVisibility(View.GONE);
                        break;
                    case 2:
                        linearLayoutChazara.setVisibility(View.VISIBLE);
                        chazara1.setVisibility(View.VISIBLE);
                        chazara2.setVisibility(View.VISIBLE);
                        chazara3.setVisibility(View.GONE);
                        break;
                    case 3:
                        linearLayoutChazara.setVisibility(View.VISIBLE);
                        chazara1.setVisibility(View.VISIBLE);
                        chazara2.setVisibility(View.VISIBLE);
                        chazara3.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }

        private void initChazara(int chazara) {
            switch(chazara) {
                case 0:
                    chazara1.setChecked(false);
                    chazara2.setChecked(false);
                    chazara3.setChecked(false);
                    break;
                case 1:
                    chazara1.setChecked(true);
                    chazara2.setChecked(false);
                    chazara3.setChecked(false);
                    break;
                case 2:
                    chazara1.setChecked(true);
                    chazara2.setChecked(true);
                    chazara3.setChecked(false);
                    break;
                case 3:
                    chazara1.setChecked(true);
                    chazara2.setChecked(true);
                    chazara3.setChecked(true);
                    break;
            }
        }
    }

    private DafLearnedItem parsDafToDafLearnedItem(DafLearned mDaf) {
        return new DafLearnedItem(mDaf.getStudyPlanID(),mDaf.getDafLearnedId(),mDaf.getMasechetName(), mDaf.getPageNumber(), mDaf.getChazara(), mDaf.getIndexInListDafs());
    }

    public interface listenerOneDafAdapter {
        void initSummaryLearning(String nameLearning , String pageLearned  , String totalLearning);

    }
}

