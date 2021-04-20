package com.example.myapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.model.DafLearned;
import com.example.model.Learning;
import com.example.model.enums.FILTERS;
import com.example.myapp.R;
import com.example.myapp.adapters.AllMasechtotAdapter;
import com.example.myapp.adapters.DafAdapter;
import com.example.myapp.databinding.FragmentShewStudyRvBinding;
import com.example.myapp.viewModel.MainViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Objects;

//import static com.example.myapp.activity.MainActivity.KEY_STUDY_1;
//import static com.example.myapp.activity.MainActivity.KEY_STUDY_2;
//import static com.example.myapp.activity.MainActivity.KEY_STUDY_3;
import static com.example.myapp.utils.StaticVariables.index0;
import static com.example.myapp.utils.StaticVariables.index1;
import static com.example.myapp.utils.StaticVariables.index2;

public class ShewStudyRvFragment extends Fragment implements AllMasechtotAdapter.ListenerNameMasechet, DafAdapter.listenerOneDafAdapter {

    public static final String TAG = ShewStudyRvFragment.class.getSimpleName();
    private static Bundle args;
    private FragmentShewStudyRvBinding binding;
    private TabLayout tabLayout;
//    private ArrayList<DafLearned> myListLearning1 = new ArrayList<>();
//    private ArrayList<DafLearned> myListLearning2 = new ArrayList<>();
//    private ArrayList<DafLearned> myListLearning3 = new ArrayList<>();

    private Learning mLearning1;
    private Learning mLearning2;
    private Learning mLearning3;
    private RecyclerView recyclerViewPage;
    private DafAdapter myAdapter;
    private MainViewModel mainViewModel;





    public static ShewStudyRvFragment newInstance() {
        ShewStudyRvFragment fragment = new ShewStudyRvFragment();
        args = new Bundle();
//        args.putParcelableArrayList(KEY_STUDY_1, myList1);
//        args.putParcelableArrayList(KEY_STUDY_2, myList2);
//        args.putParcelableArrayList(KEY_STUDY_3, myList3);
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
        binding = FragmentShewStudyRvBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTabLayout();
        mainViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity())).get(MainViewModel.class);
        mainViewModel.getAllLearning().observe(getViewLifecycleOwner(), new Observer<ArrayList<Learning>>() {
            @Override
            public void onChanged(ArrayList<Learning> learnings) {
                if (learnings != null && learnings.size() >0){
//                    myListLearning1 = learnings.get(0).getAllDafsToShow();
                    mLearning1 = learnings.get(0);
                }
                if (learnings != null && learnings.size() >1){
//                    myListLearning2 = learnings.get(1).getAllDafsToShow();
                    mLearning2 = learnings.get(1);
                }
                if (learnings != null && learnings.size() >2){
//                    myListLearning3 = learnings.get(2).getAllDafsToShow();
                    mLearning3 = learnings.get(2);
                }
                initRVMasechtot();
                checkIfVisibleRVMasechtot(index1);
                initRVPage();
            }
        });
    }


    private void initRVMasechtot() {
        binding.showStudyRVMasechtot.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        ArrayList<DafLearned> listDafHayomi = null;
        if (mLearning1 != null && mLearning1.getAllDafsToShow()!= null && mLearning1.getAllDafsToShow().size() > 2000) {
            listDafHayomi = mLearning1.getAllDafsToShow();
        } else if (mLearning2 != null && mLearning2.getAllDafsToShow()!= null && mLearning2.getAllDafsToShow().size() > 2000) {
            listDafHayomi = mLearning2.getAllDafsToShow();
        } else if (mLearning3 != null && mLearning3.getAllDafsToShow()!= null && mLearning3.getAllDafsToShow().size() > 2000) {
            listDafHayomi = mLearning3.getAllDafsToShow();
        }
        if (listDafHayomi != null) {
            ArrayList<String> allMasechtotName = getNameAllMasechtot(listDafHayomi);
            AllMasechtotAdapter allMasechtotAdapter = new AllMasechtotAdapter(allMasechtotName, this);
            binding.showStudyRVMasechtot.setAdapter(allMasechtotAdapter);
        }
    }


    private ArrayList<String> getNameAllMasechtot(ArrayList<DafLearned> listDafHayomi) {
        ArrayList<String> allMasechtot = new ArrayList<>();
        for (int i = 1; i < listDafHayomi.size(); i++) {
            if (allMasechtot.size() == 0) {
                allMasechtot.add(listDafHayomi.get(i).getMasechetName());
            } else if (!listDafHayomi.get(i).getMasechetName().equals(allMasechtot.get(allMasechtot.size() - 1))) {
                allMasechtot.add(listDafHayomi.get(i).getMasechetName());
            }
        }
        return allMasechtot;
    }


    private void checkIfVisibleRVMasechtot(int typeOfStudy) {
        switch (typeOfStudy) {
            case 0:
                if (mLearning1 != null && mLearning1.getAllDafsToShow().size() > 0)
                    visibleRVMasechtot(mLearning1.getAllDafsToShow().size());
                break;
            case 1:
                if (mLearning2 != null && mLearning2.getAllDafsToShow().size() > 0)
                    visibleRVMasechtot(mLearning2.getAllDafsToShow().size());
                break;
            case 2:
                if (mLearning3 != null && mLearning3.getAllDafsToShow().size() > 0)
                    visibleRVMasechtot(mLearning3.getAllDafsToShow().size());
                break;
        }
    }

    private void visibleRVMasechtot(int listSize) {
        if (listSize > 2000) {
            binding.showStudyRVMasechtot.setVisibility(View.VISIBLE);
        } else {
            binding.showStudyRVMasechtot.setVisibility(View.GONE);
        }
    }

    private void initRVPage() {
        recyclerViewPage = binding.showStudyRVDapim;
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewPage.setLayoutManager(mLayoutManager);
        myAdapter = new DafAdapter(getActivity(), mLayoutManager, mLearning1, mLearning2, mLearning3, this);
        recyclerViewPage.setAdapter(myAdapter);
    }

    private void initTabLayout() {
        tabLayout = binding.tabLayoutTypeList;
        createTab(R.string.all);
        createTab(R.string.learned);
        createTab(R.string.skipped);
        setListenerOfTabLayout(tabLayout);
    }

    private void createTab(int tabString) {
        TabLayout.Tab tab = tabLayout.newTab();
        tab.setText(tabString);
        tabLayout.addTab(tab);
    }

    private void setListenerOfTabLayout(TabLayout tabLayout) {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case index0:
                        myAdapter.filter(FILTERS.allLearning);
                        break;
                    case index1:
                        binding.showStudyRVMasechtot.setVisibility(View.GONE);
                        myAdapter.filter(FILTERS.learned);
                        recyclerViewPage.scrollToPosition(0);
                        break;
                    case index2:
                        binding.showStudyRVMasechtot.setVisibility(View.GONE);
                        myAdapter.filter(FILTERS.skipped);
                        recyclerViewPage.scrollToPosition(0);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                        myAdapter.filter(FILTERS.allLearning);
                }
            }
        });
    }

    public void changeLearning(int typeOfStudy) {
        checkIfVisibleRVMasechtot(typeOfStudy);
        myAdapter.changeTypeStudy(typeOfStudy);
        TabLayout.Tab newTab = tabLayout.getTabAt(0);
        Objects.requireNonNull(newTab).select();
    }

    public void initSummaryLearning(String nameLearning, String pageLearned, String totalLearning) {
        binding.showStudySummaryTypeOfLearningTV.setText(nameLearning);
        binding.showStudySummaryLearnedTV.setText(pageLearned);
        binding.showStudySummaryTotalTV.setText(totalLearning);
    }

    @Override
    public void nameMasechet(String nameMasechet) {
        myAdapter.filterOneMasechet(nameMasechet);
        recyclerViewPage.scrollToPosition(0);
    }
}