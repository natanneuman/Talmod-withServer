package com.example.myapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.model.DafLearned;
import com.example.model.Learning;
import com.example.myapp.R;
import com.example.myapp.databinding.ActivityMainBinding;
import com.example.myapp.fragment.DeleteStudyFragment;
import com.example.myapp.fragment.ShewStudyRvFragment;
import com.example.myapp.fragment.ShowDafFragment;
import com.example.myapp.utils.ToastAndDialog;
import com.example.myapp.viewModel.MainViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import static com.example.myapp.utils.StaticVariables.index1;
import static com.example.myapp.utils.StaticVariables.index2;
import static com.example.myapp.utils.StaticVariables.index3;


public class MainActivity extends AppCompatActivity {


    private MainViewModel mainViewModel;
    private ActivityMainBinding binding;
    private ArrayList<Learning> mLearning;

////    private Shas mAllShas;
//    public final static String KEY_STUDY_1 = "KEY_STUDY_1";
//    public final static String KEY_STUDY_2 = "KEY_STUDY_2";
//    public final static String KEY_STUDY_3 = "KEY_STUDY_3";
//
//    private ArrayList<DafLearned> myStudy1;
//    private ArrayList<DafLearned> myStudy2;
//    private ArrayList<DafLearned> myStudy3;
    private ShewStudyRvFragment mShewStudyRvFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
//        mAllShas = InitAllShasFromGson.getInstance(this);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
//        mainViewModel = new MainViewModel(this.getApplication()).get(MainViewModel.class);
        initData();
//        openFragment(mShewStudyRvFragment = ShewStudyRvFragment.newInstance(myStudy1, myStudy2, myStudy3), ShewStudyRvFragment.TAG);
        openFragment(mShewStudyRvFragment = ShewStudyRvFragment.newInstance(), ShewStudyRvFragment.TAG);
        initViews();
        initListeners();
    }

    private void initData() {
        mainViewModel.getAllLearning().observe(this, new Observer<ArrayList<Learning>>() {
            @Override
            public void onChanged(ArrayList<Learning> learnings) {
                mLearning = learnings;
                initButtonsTypesStudy();
            }
        });
//        mLearning = (ArrayList<Learning>) AppDataBase.getInstance(this).daoLearning().getStudyPlanWithLearning();
//        initAllDafs(mLearning);

//       if (mLearning.size() == 1){
//           myStudy1 = mLearning.get(0).getAllDafsToShow();
//       }
//        if (mLearning.size() == 2){
//            myStudy1 = mLearning.get(0).getAllDafsToShow();
//            myStudy2 = mLearning.get(1).getAllDafsToShow();
//        }
//        if (mLearning.size() == 3){
//            myStudy1 = mLearning.get(0).getAllDafsToShow();
//            myStudy2 = mLearning.get(1).getAllDafsToShow();
//            myStudy3 = mLearning.get(2).getAllDafsToShow();
//        }
    }

//    private void initAllDafs(ArrayList<Learning> mLearning) {
//        for (Learning learning : mLearning) {
//            if (learning.getStudyPlan().getTypeOfStudy().equals(stringDafHayomi)) {
//                learning.setAllDafsToShow(createListDafHayomi(learning.getStudyPlan()));
//            } else {
//                learning.setAllDafsToShow(createListMasechet(learning.getStudyPlan()));
//            }
//            updateDafsLearned(learning);
//        }
//    }
//
//
//    private void updateDafsLearned(Learning learning) {
//        for (DafLearned dafLearned:learning.getDafLearned()) {
//           DafLearned mCorrectDafLearned = learning.getAllDafsToShow().get(dafLearned.getIndexInListDafs());
//           if (!mCorrectDafLearned.getMasechetName().equals(dafLearned.getMasechetName()) || mCorrectDafLearned.getPageNumber() != (dafLearned.getPageNumber())){
//               mCorrectDafLearned = findDafIfIndexInListNotCorrect(dafLearned, learning.getAllDafsToShow());
//           }
//
//           if (mCorrectDafLearned != null) {
//               mCorrectDafLearned.setDafLearnedId(dafLearned.getDafLearnedId());
//               mCorrectDafLearned.setLearned(true);
//               mCorrectDafLearned.setChazara(dafLearned.getChazara());
//           }
//
//
////            if (dafLearned.getMasechetName().equals() == learning.getStudyPlan().getId()){
////                learning.getAllDafs().get(dafLearned.getIndexInListDafs()).setLearning(true);
////                learning.getAllDafs().get(dafLearned.getIndexInListDafs()).setChazara(dafLearned.getChazara());
////            }
//        }
//    }
//
//    private DafLearned findDafIfIndexInListNotCorrect(DafLearned dafLearned, ArrayList<DafLearned> allDafsToShow) {
//        for (DafLearned dafFromDafsToShow :allDafsToShow) {
//            if (dafFromDafsToShow.getMasechetName().equals(dafLearned.getMasechetName()) || dafFromDafsToShow.getPageNumber() == dafLearned.getPageNumber()){
//                return dafFromDafsToShow;
//            }
//        }
//        return null;
//    }
//
//
//    private ArrayList<DafLearned> createListDafHayomi(StudyPlan mStudyPlan) {
//        ArrayList <DafLearned> allDafs = new ArrayList<>();
//        Calendar dateDafHayomi = UtilsCalender.findDateOfStartDafHayomiEnglishDate();
//        for (int i = 0; i < mAllShas.getSeder().size(); i++) {
//            for (int j = 0; j < mAllShas.getSeder().get(i).getMasechtot().size(); j++) {
//                for (int k = 2; k < (mAllShas.getSeder().get(i).getMasechtot().get(j).getPages() + 2); k++) {
//                    int masechetPage = ConvertIntToPage.fixKinimTamidMidot(k, mAllShas.getSeder().get(i).getMasechtot().get(j).getName());
////                    Daf mPage = new Daf(mAllShas.getSeder().get(i).getMasechtot().get(j).getName(), masechetPage, stringDafHayomi, mStudyPlan.getId());
//                    DafLearned mDafLearned = new DafLearned(mStudyPlan.getId(),mAllShas.getSeder().get(i).getMasechtot().get(j).getName(),
//                            masechetPage,false, 0,k-2);
//                    mDafLearned.setPageDate(UtilsCalender.dateStringFormat(dateDafHayomi));
//                    allDafs.add(mDafLearned);
//                    dateDafHayomi.add(Calendar.DATE, 1);
//                }
//            }
//        }
//        return allDafs;
//    }
//
//    private ArrayList<DafLearned> createListMasechet(StudyPlan mStudyPlan) {
//        int pages = findPageSum(mStudyPlan.getTypeOfStudy());
//        ArrayList <DafLearned> allDafs = new ArrayList<>();
//        for (int i = 2; i < (pages + 2); i++) {
////            allDafs.add(new Daf(mStudyPlan.getTypeOfStudy(), ConvertIntToPage.fixKinimTamidMidot(i, mStudyPlan.getTypeOfStudy()),
////                    mStudyPlan.getTypeOfStudy(), mStudyPlan.getId()));
//            allDafs.add(new DafLearned(mStudyPlan.getId(), mStudyPlan.getTypeOfStudy(),ConvertIntToPage.fixKinimTamidMidot(i, mStudyPlan.getTypeOfStudy()),
//                    false,0,i-2));
//        }
//        return allDafs;
//    }
//
//
//    private int findPageSum(String typeOfStudy) {
//        for (Seder mSeder :mAllShas.getSeder()) {
//            for (Masechet masechet:mSeder.getMasechtot()) {
//                if (masechet.getName().equals(typeOfStudy)){
//                    return masechet.getPages();
//                }
//            }
//        }
//        return -1;
//    }

    public void openFragment(Fragment myFragment, String tag) {
        getSupportFragmentManager().beginTransaction().replace(R.id.MA_frameLayout, myFragment)
                .addToBackStack(tag)
                .commit();
    }

    private void initViews() {
        initToolbar();

    }


    private void initToolbar() {
        Toolbar toolbar = binding.toolbarMainActivityTB;
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_type_of_study_MENU_I:
                if (mLearning.size() < 3) {
                    Intent intent = new Intent(this, ProfileActivity.class);
                    intent.putExtra("IS_ADD_STUDY", true);
                    startActivityForResult(intent,999);
//                    startActivity(new Intent(this, ProfileActivity.class));
//                    finish();
                } else {
                    ToastAndDialog.toast(this, getString(R.string.more_than_thre_types_of_study));
                }
                return true;
            case R.id.delete_type_of_study_MENU_I:
                openFragment(DeleteStudyFragment.newInstance(), DeleteStudyFragment.TAG);
                return true;
            case R.id.edit_profile_MENU_I:
                // TODO: edit profile
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 999) {
            if(resultCode == Activity.RESULT_OK){
//                int newStudyPlanID = data.getIntExtra("result", -1);
//                mainViewModel.createNewStudyPlan(newStudyPlanID);

//
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Toast i cont add study
            }
        }
    }





    private void initButtonsTypesStudy() {
        binding.typeOfStudy1BU.setVisibility(View.GONE);
        binding.typeOfStudy2BU.setVisibility(View.GONE);
        binding.typeOfStudy3BU.setVisibility(View.GONE);
        if (mLearning != null) {
            switch (mLearning.size()) {
                case 3:
                    binding.typeOfStudy3BU.setVisibility(View.VISIBLE);
                    binding.typeOfStudy3BU.setText(mLearning.get(2).getStudyPlan().getTypeOfStudy());
                case 2:
                    binding.typeOfStudy2BU.setVisibility(View.VISIBLE);
                    binding.typeOfStudy2BU.setText(mLearning.get(1).getStudyPlan().getTypeOfStudy());
                case 1:
                    binding.typeOfStudy1BU.setVisibility(View.VISIBLE);
                    binding.typeOfStudy1BU.setText(mLearning.get(0).getStudyPlan().getTypeOfStudy());
            }
        }
    }


//        if (myStudy1 != null && myStudy1.size() > 0) {
////            binding.typeOfStudy1BU.setText(myStudy1.get(0).getTypeOfStudy());
//        }
//        if (myStudy2 != null && myStudy2.size() > 0) {
//            binding.typeOfStudy2BU.setVisibility(View.VISIBLE);
////            binding.typeOfStudy2BU.setText(myStudy2.get(0).getTypeOfStudy());
//        } else {
//            binding.typeOfStudy2BU.setVisibility(View.GONE);
//        }
//        if (myStudy3 != null && myStudy3.size() > 0) {
//            binding.typeOfStudy3BU.setVisibility(View.VISIBLE);
////            binding.typeOfStudy3BU.setText(myStudy3.get(0).getTypeOfStudy());
//        } else {
//            binding.typeOfStudy3BU.setVisibility(View.GONE);
//        }
//    }

    private void initListeners() {
        binding.typeOfStudy1BU.setOnClickListener(v -> mShewStudyRvFragment.changeLearning(index1));
        binding.typeOfStudy2BU.setOnClickListener(v -> mShewStudyRvFragment.changeLearning(index2));
        binding.typeOfStudy3BU.setOnClickListener(v -> mShewStudyRvFragment.changeLearning(index3));
    }


//    private boolean checkIfCanOpenTypeOfStudy() {
////        ArrayList<Integer> mLeaning = (ArrayList<Integer>) AppDataBase.getInstance(this).daoLearning().getAllIndexTypeOfLeaning();
////        return !mLeaning.contains(index1) || !mLeaning.contains(index2) || !mLeaning.contains(index3);
//        return ;
//    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.MA_frameLayout);
        if (currentFragment instanceof ShewStudyRvFragment) {
            finish();
        }
        if (currentFragment instanceof DeleteStudyFragment) {
            fragmentManager.popBackStack();
        }
        if (currentFragment instanceof ShowDafFragment) {
            fragmentManager.popBackStack();
        }
    }

    @Subscribe()
    public void showDaf(DafLearned dafToShow) {
        openFragment(ShowDafFragment.newInstance(dafToShow), ShowDafFragment.TAG);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

}