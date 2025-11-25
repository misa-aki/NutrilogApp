package br.edu.ufam.nutrilogapp.data.api

import br.edu.ufam.nutrilogapp.data.model.Alimento
import br.edu.ufam.nutrilogapp.data.model.AppConfig
import br.edu.ufam.nutrilogapp.data.model.FullUserProfile
import br.edu.ufam.nutrilogapp.data.model.LoginResponse
import br.edu.ufam.nutrilogapp.data.model.PaginatedResponse
import br.edu.ufam.nutrilogapp.data.model.SessionResponse
import retrofit2.Response
import retrofit2.http.*

interface NutrilogApi {
    @FormUrlEncoded
    @POST("api/session/login/")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @GET("api/session/")
    suspend fun getSession(): Response<SessionResponse>

    @GET("api/configuracoes/")
    suspend fun getProfile(): Response<FullUserProfile>

    @FormUrlEncoded
    @PUT("api/configuracoes/")
    suspend fun updateProfile(
        @Field("username") username: String? = null,
        @Field("email") email: String? = null,
        @Field("current_password") currentPassword: String? = null,
        @Field("new_password") newPassword: String? = null
    ): Response<FullUserProfile>

    @GET("api/configuracoes/")
    suspend fun getConfiguracoes(): Response<PaginatedResponse<AppConfig>>

    @POST("api/configuracoes/")
    suspend fun createConfiguracoes(@Body config: AppConfig): Response<AppConfig>

    @PUT("api/configuracoes/{id}/")
    suspend fun updateConfiguracoes(
        @Path("id") id: Int,
        @Body config: AppConfig
    ): Response<AppConfig>

    @FormUrlEncoded
    @POST("api/usuarios/")
    suspend fun register(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @GET("api/alimentos/import/")
    suspend fun getAlimentoByBarcode(
        @Query("barcode") barcode: String
    ): Response<Alimento>
}

