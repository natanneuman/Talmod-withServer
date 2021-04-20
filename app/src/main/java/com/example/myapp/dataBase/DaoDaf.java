package com.example.myapp.dataBase;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.model.DafLearned;
import com.example.model.Learning;
import com.example.model.StudyPlan;

import java.util.List;

@Dao
public interface DaoDaf {

    @Query("DELETE FROM StudyPlan")
    public void deleteAll();

    @Query("SELECT * FROM StudyPlan")
    List<StudyPlan> getAllStudyPlans();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStudyPlan(StudyPlan studyPlan);

    @Query("SELECT * FROM StudyPlan WHERE id = :StudyPlanID")
    StudyPlan getStudyPlan(int StudyPlanID);

    @Query("DELETE FROM StudyPlan WHERE id = :StudyPlanID")
    void deleteStudyPlan(int StudyPlanID);

    @Transaction
    @Query("SELECT * FROM StudyPlan")
    List<Learning> getStudyPlanWithLearning();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllDaf(List<DafLearned> dafLearned);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDaf(DafLearned dafLearned);

    @Query("DELETE FROM DafLearned WHERE studyPlanID = :studyPlanId and masechetName = :masechetName and pageNumber = :pageNumber")
    void deleteDaf (int studyPlanId, String masechetName, int pageNumber);

    @Query("UPDATE DafLearned SET chazara = :chazara WHERE studyPlanID = :studyPlanId and masechetName = :masechetName and pageNumber = :pageNumber")
    void updateChazara(int chazara , int studyPlanId, String masechetName, int pageNumber);

// delete
//    @Query("SELECT MAX(id) FROM StudyPlan")
//    int getHighestStudyPlanId();

}
