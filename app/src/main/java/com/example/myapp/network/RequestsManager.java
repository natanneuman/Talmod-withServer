package com.example.myapp.network;

import com.example.myapp.network.requests.SignUpRequest;
import com.example.myapp.network.responses.studyResponses.DafLearnedItem;
import com.example.myapp.network.responses.studyResponses.StudyPlanItem;
import com.example.myapp.network.responses.studyResponses.StudyResponse;
import com.example.myapp.network.responses.userResponses.UserResponse;
import com.example.myapp.utils.ManageSharedPreferences;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestsManager {
    private static RequestsManager mInstance;
    private GetDataService service;

    public static RequestsManager getInstance() {
        if (mInstance == null) {
            mInstance = new RequestsManager();
        }
        return mInstance;
    }


    private RequestsManager() {
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
    }

    public void signUp (SignUpRequest signUpRequest, OnRequestManagerResponseListener callback) {
        Call<UserResponse> call = service.signUp(signUpRequest);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.body() != null) {
                    callback.onRequestSucceed(response.body());
                } else {
                    callback.onRequestFailed("sign up Failure, response is null");
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                callback.onRequestFailed(t.getMessage());
            }
        });
    }

    public void login (SignUpRequest signUpRequest, OnRequestManagerResponseListener callback) {
        Call<UserResponse> call = service.login(signUpRequest);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.body() != null) {
                    callback.onRequestSucceed(response.body());
                } else {
                    callback.onRequestFailed("login Failure, response is null");
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                callback.onRequestFailed(t.getMessage());
            }
        });
    }

    public void createStudyPlan(StudyPlanItem studyPlanItem, String token, OnRequestManagerResponseListener callback) {
        Call<StudyPlanItem> call = service.createStudyPlan(token, studyPlanItem);
        call.enqueue(new Callback<StudyPlanItem>() {
            @Override
            public void onResponse(Call<StudyPlanItem> call, Response<StudyPlanItem> response) {
                if (response.body() != null) {
                    callback.onRequestSucceed(response.body());
                } else {
                    callback.onRequestFailed("getAllLearning Failure, response is null");
                }
            }

            @Override
            public void onFailure(Call<StudyPlanItem> call, Throwable t) {
                callback.onRequestFailed(t.getMessage());
            }
        });
    }

    public void getStudyPlanWithLearning(UserResponse userResponse, OnRequestManagerResponseListener callback) {
        Call<StudyResponse> call = service.getAllLearning("Token " + userResponse.getToken() ,userResponse.getId());
        call.enqueue(new Callback<StudyResponse>() {
            @Override
            public void onResponse(Call<StudyResponse> call, Response<StudyResponse> response) {
                if (response.body() != null) {
                    callback.onRequestSucceed(response.body());
                } else {
                    callback.onRequestFailed("getAllLearning Failure, response is null");
                }
            }

            @Override
            public void onFailure(Call<StudyResponse> call, Throwable t) {
                callback.onRequestFailed(t.getMessage());
            }
        });
    }

    public void leanedDaf(DafLearnedItem dafLearned, UserResponse userResponse,OnRequestManagerResponseListener callback) {
        Call<DafLearnedItem> call = service.leanedDaf("Token " + userResponse.getToken() ,dafLearned);
        call.enqueue(new Callback<DafLearnedItem>() {
            @Override
            public void onResponse(Call<DafLearnedItem> call, Response<DafLearnedItem> response) {
                if (response.body() != null) {
                    callback.onRequestSucceed(response.body());
                } else {
                    callback.onRequestFailed("leanedDaf Failure, response is null");
                }
            }

            @Override
            public void onFailure(Call<DafLearnedItem> call, Throwable t) {
                callback.onRequestFailed(t.getMessage());
            }
        });
    }

    public void updateChazara (DafLearnedItem dafLearned, UserResponse userResponse ,OnRequestManagerResponseListener callback) {
        Call<DafLearnedItem> call = service.updateChazara("Token " + userResponse.getToken() ,dafLearned.getDafID(),dafLearned);
        call.enqueue(new Callback<DafLearnedItem>() {
            @Override
            public void onResponse(Call<DafLearnedItem> call, Response<DafLearnedItem> response) {
                if (response.body() != null) {
                    callback.onRequestSucceed(response.body());
                } else {
                    callback.onRequestFailed("updateChazara Failure, response is null");
                }
            }

            @Override
            public void onFailure(Call<DafLearnedItem> call, Throwable t) {
                callback.onRequestFailed(t.getMessage());
            }
        });
    }

    public void unLearnDaf (DafLearnedItem dafLearned, UserResponse userResponse ,OnRequestManagerResponseListener callback) {
        Call<DafLearnedItem> call = service.unLeanedDaf("Token " + userResponse.getToken() ,dafLearned.getDafID());
        call.enqueue(new Callback<DafLearnedItem>() {
            @Override
            public void onResponse(Call<DafLearnedItem> call, Response<DafLearnedItem> response) {
      //          if (response.body() != null) {
                    callback.onRequestSucceed(response.body());
        //        }
//                else {
//                    callback.onRequestFailed("unLearnDaf Failure, response is null");
//                }
            }

            @Override
            public void onFailure(Call<DafLearnedItem> call, Throwable t) {
                callback.onRequestFailed(t.getMessage());
            }
        });
    }

    public void deleteStudyPlan(int studyPlanID, String token, OnRequestManagerResponseListener callback) {
        Call<StudyPlanItem> call = service.deleteStudyPlan(token, studyPlanID);
        call.enqueue(new Callback<StudyPlanItem>() {
            @Override
            public void onResponse(Call<StudyPlanItem> call, Response<StudyPlanItem> response) {
                    callback.onRequestSucceed(response.body());
            }

            @Override
            public void onFailure(Call<StudyPlanItem> call, Throwable t) {
                callback.onRequestFailed(t.getMessage());
            }
        });
    }

    public interface OnRequestManagerResponseListener<T> {

        void onRequestSucceed(T result);

        void onRequestFailed(String error);

    }
}

