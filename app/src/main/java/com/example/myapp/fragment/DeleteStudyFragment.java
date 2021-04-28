package com.example.myapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.model.Learning;
import com.example.myapp.R;
import com.example.myapp.activity.SplashActivity;
import com.example.myapp.dataBase.AppDataBase;
import com.example.myapp.databinding.FragmentDeleteStudyBinding;
import com.example.myapp.network.RequestsManager;
import com.example.myapp.network.responses.studyResponses.DafLearnedItem;
import com.example.myapp.utils.ManageSharedPreferences;
import com.example.myapp.viewModel.MainViewModel;

import java.util.ArrayList;
import java.util.Objects;

//import static com.example.myapp.activity.MainActivity.KEY_STUDY_1;
//import static com.example.myapp.activity.MainActivity.KEY_STUDY_2;
//import static com.example.myapp.activity.MainActivity.KEY_STUDY_3;


public class DeleteStudyFragment extends Fragment {
    public static final String TAG = DeleteStudyFragment.class.getSimpleName();
    public final static String IS_AFTER_DELETE_STUDY = "IS_AFTER_DELETE_STUDY";
    private FragmentDeleteStudyBinding binding;
    private MainViewModel mainViewModel;
    private LinearLayout linearLayoutButtons;
    private static Bundle args;
//    private ArrayList<Daf> myListLearning1 = new ArrayList<>();
//    private ArrayList<Daf> myListLearning2 = new ArrayList<>();
//    private ArrayList<Daf> myListLearning3 = new ArrayList<>();
   private ArrayList<Learning> allLearning;


    public DeleteStudyFragment() {
        // Required empty public constructor
    }


    public static DeleteStudyFragment newInstance() {
        DeleteStudyFragment fragment = new DeleteStudyFragment();
        args = new Bundle();
//        args.putParcelableArrayList(KEY_STUDY_1,myList1);
//        args.putParcelableArrayList(KEY_STUDY_2,myList2);
//        args.putParcelableArrayList(KEY_STUDY_3,myList3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            myListLearning1 = getArguments().getParcelableArrayList(KEY_STUDY_1);
//            myListLearning2 = getArguments().getParcelableArrayList(KEY_STUDY_2);
//            myListLearning3 = getArguments().getParcelableArrayList(KEY_STUDY_3);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDeleteStudyBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        args.clear();
        mainViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity())).get(MainViewModel.class);
        mainViewModel.getAllLearning().observe(getViewLifecycleOwner(), new Observer<ArrayList<Learning>>() {
            @Override
            public void onChanged(ArrayList<Learning> learnings) {
                allLearning = learnings;
                initViews();
                initListeners();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        linearLayoutButtons.setVisibility(View.VISIBLE);
    }

    private void initListeners() {
        binding.deleteStudyBU.setOnClickListener(v -> {
            int selectedId = binding.deleteTypeOfStudyRG.getCheckedRadioButtonId();
            switch (selectedId) {
                case R.id.typeOfStudy1_RB:
                    deleteStudy(allLearning.get(0).getStudyPlan().getId());
                    break;
                case R.id.typeOfStudy2_RB:
                    deleteStudy(allLearning.get(1).getStudyPlan().getId());
                    break;
                case R.id.typeOfStudy3_RB:
                    deleteStudy(allLearning.get(2).getStudyPlan().getId());
                    break;
            }

        });
    }

    private void goToSplashActivity() {
        Intent intent = new Intent(getActivity(), SplashActivity.class);
        intent.putExtra(IS_AFTER_DELETE_STUDY , true);
        startActivity(intent);
        Objects.requireNonNull(getActivity()).finish();
    }

    private void deleteStudy(int studyPlanId) {
        binding.FDSProgressBarPB.setVisibility(View.VISIBLE);
        RequestsManager.getInstance().deleteStudyPlan(studyPlanId, "Token "+ ManageSharedPreferences.getToken(getContext()).getToken(), new RequestsManager.OnRequestManagerResponseListener<DafLearnedItem>() {
            @Override
            public void onRequestSucceed(DafLearnedItem result) {
                AppDataBase.getInstance(getContext()).daoLearning().deleteStudyPlan(studyPlanId);
                updateLiveData(studyPlanId);
                binding.FDSProgressBarPB.setVisibility(View.GONE);
                goToSplashActivity();
            }

            @Override
            public void onRequestFailed(String error) {
                binding.FDSProgressBarPB.setVisibility(View.GONE);
                //                                    log server eror
            }
        });
    }

    private void updateLiveData(int studyPlanId) {
        int index;
        for (index = 0; index < allLearning.size(); index++) {
            if (allLearning.get(index).getStudyPlan().getId() == studyPlanId) {
                break;
            }
        }
        allLearning.remove(index);
    }

    private void initViews() {
        linearLayoutButtons = Objects.requireNonNull(getActivity()).findViewById(R.id.typeOfStudy_buttons_LL);
        linearLayoutButtons.setVisibility(View.GONE);
        if (allLearning != null && allLearning.size()>0){
            binding.typeOfStudy1RB.setText(allLearning.get(0).getStudyPlan().getTypeOfStudy());
        }else {
            binding.typeOfStudy1RB.setVisibility(View.GONE);
        }
        if (allLearning != null && allLearning.size()>1){
            binding.typeOfStudy2RB.setText(allLearning.get(1).getStudyPlan().getTypeOfStudy());
        }else {
            binding.typeOfStudy2RB.setVisibility(View.GONE);
        }
        if (allLearning != null && allLearning.size()>2){
            binding.typeOfStudy3RB.setText(allLearning.get(2).getStudyPlan().getTypeOfStudy());
        }else {
            binding.typeOfStudy3RB.setVisibility(View.GONE);
        }
    }
}