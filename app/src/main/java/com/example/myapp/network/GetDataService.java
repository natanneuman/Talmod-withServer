package com.example.myapp.network;

import com.example.myapp.network.requests.SignUpRequest;
import com.example.myapp.network.responses.studyResponses.DafLearnedItem;
import com.example.myapp.network.responses.studyResponses.StudyPlanItem;
import com.example.myapp.network.responses.studyResponses.StudyResponse;
import com.example.myapp.network.responses.userResponses.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetDataService {

    @GET("api/user/{userID}")
    Call<StudyResponse> getAllLearning(@Header ("Authorization") String token, @Path("userID") int userID);

    @POST("signup/")
    Call<UserResponse> signUp(@Body SignUpRequest signUpRequest);

    @POST("login/")
    Call<UserResponse> login(@Body SignUpRequest signUpRequest);

    @POST("api/daf/")
    Call <DafLearnedItem> leanedDaf(@Header ("Authorization") String token,@Body DafLearnedItem dafLearnedItem);

    @PUT("api/daf/{dafId}/")
    Call <DafLearnedItem> updateChazara (@Header ("Authorization") String token,@Path("dafId") int dafId, @Body DafLearnedItem dafLearnedItem);

    @DELETE("api/daf/{dafId}/")
    Call <DafLearnedItem> unLeanedDaf (@Header ("Authorization") String token,@Path("dafId") int dafId);

    @POST("api/studyplan/")
    Call<StudyPlanItem> createStudyPlan(@Header ("Authorization") String token, @Body StudyPlanItem studyPlanItem);

    @DELETE("api/studyplan/{studyPlanID}/")
    Call<StudyPlanItem> deleteStudyPlan(@Header ("Authorization") String token, @Path("studyPlanID") int studyPlanID);
}
