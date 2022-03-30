package com.tnote.tnoteapp.api

import com.tnote.tnoteapp.models.*
import retrofit2.Response
import retrofit2.http.*

interface TNoteApi {

    @POST(Constants.LOGIN_URL)
    suspend fun login(@Body request: LoginRequest) : Response<UserResponse>

    @POST(Constants.LOGOUT_URL)
    suspend fun logout(@Header("Authorization") token: String) : Response<LogoutResponse>

    @POST(Constants.REGISTER_URL)
    suspend fun register(@Body request: RegistrationRequest) : Response<UserResponse>

    @GET("${Constants.USERS_URL}/{id}")
    suspend fun getUser(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ) : Response<User>

    @GET(Constants.USERNOTES_URL)
    suspend fun getNotes(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ) : Response<List<Note>>

    @GET("${Constants.NOTES_URL}/{id}")
    suspend fun getNote(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ) : Response<Note>

    @POST(Constants.NOTES_URL)
    suspend fun newNote(
        @Header("Authorization") token: String,
        @Body note: Note
    ) : Response<Note>

    @PATCH("${Constants.NOTES_URL}/{id}")
    suspend fun updateNote(
        @Path("id") id: Int,
        @Header("Authorization") token: String,
        @Body note: Note
    ) : Response<Note>

    @DELETE("${Constants.NOTES_URL}/{id}")
    suspend fun deleteNote(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    )

    @GET(Constants.USERTIMETABLES)
    suspend fun getTimetables(
        @Path("id") id: Int,
        @Header("Authorization") token: String,
    ) : Response<List<Timetable>>

    @POST(Constants.TIMETABLES_URL)
    suspend fun newTimetable(
        @Header("Authorization") token: String,
        @Body timetable: Timetable
    ) : Response<Timetable>

    @GET(Constants.SELECTEDTIMETABLE)
    suspend fun getSelectedTimetable(
        @Path("id") id: Int,
        @Header("Authorization") token: String,
    ) : Response<List<TTElement>>

    @DELETE(Constants.SELECTEDTIMETABLE)
    suspend fun deleteTimetable(
        @Path("id") id: Int,
        @Header("Authorization") token: String,
    )

    @GET("${Constants.TTElements_URL}/{id}")
    suspend fun getSelectedTTElement(
        @Path("id") id: Int,
        @Header("Authorization") token: String,
    ) : Response<TTElement>

    @POST("${Constants.TTElements_URL}/{id}")
    suspend fun createTTElement(
        @Path("id") id: Int,
        @Header("Authorization") token: String,
        @Body ttElement: TTElement
    ) : Response<TTElement>

    @PATCH("${Constants.TTElements_URL}/{id}")
    suspend fun updateTTElement(
        @Path("id") id: Int,
        @Header("Authorization") token: String,
        @Body ttElement: TTElement
    ) : Response<TTElement>

    @DELETE("${Constants.TTElements_URL}/{id}")
    suspend fun deleteTTElement(
        @Path("id") id: Int,
        @Header("Authorization") token: String,
    )
}