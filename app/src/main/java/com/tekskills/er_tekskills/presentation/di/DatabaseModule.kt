package com.tekskills.er_tekskills.presentation.di

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.google.gson.GsonBuilder
//import com.tekskills.er_tekskills.data.db.TaskCategoryDao
//import com.tekskills.er_tekskills.data.db.TaskDatabase
import com.tekskills.er_tekskills.data.remote.ApiHelper
import com.tekskills.er_tekskills.data.remote.ApiHelperImpl
import com.tekskills.er_tekskills.data.remote.ApiService
import com.tekskills.er_tekskills.data.repository.LocationRepository
import com.tekskills.er_tekskills.utils.Common
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
//    @Provides
//    @Singleton
//    fun provideTaskDatabase(app: Application) : TaskDatabase {
//        return Room.databaseBuilder(app, TaskDatabase::class.java, "task_db")
//            .fallbackToDestructiveMigration()
//            .build()
//    }
//
//    @Singleton
//    @Provides
//    fun provideTaskCategoryDao(taskDatabase: TaskDatabase): TaskCategoryDao {
//        return taskDatabase.getTaskCategoryDao()
//    }

    @Provides
    fun provideBaseUrl() = Common.BASE_URL

    @Singleton
    @Provides
    fun provideOkHttpClient() =
//        if (BuildConfig.DEBUG)
        run {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
        }
//    else{
//        OkHttpClient
//            .Builder()
//            .build()
//    }

    private fun getRequestResponseInterceptor(): OkHttpClient {
        val httpClient: OkHttpClient.Builder = OkHttpClient().newBuilder()
        val logging = HttpLoggingInterceptor()

        logging.level = HttpLoggingInterceptor.Level.BODY
        httpClient.interceptors().add(logging)
        httpClient.interceptors().add(interceptor)
        httpClient.connectTimeout(60, TimeUnit.SECONDS)
        httpClient.writeTimeout(0, TimeUnit.SECONDS)
        httpClient.readTimeout(1, TimeUnit.MINUTES)
        return httpClient.build()
    }

    val interceptor = Interceptor { chain ->
        val request = chain.request()
        val response = chain.proceed(request)

        if (response.code == 401) {
            Log.d("okhttp.OkHttpClient","401 error occurred")
        }
        Log.d("okhttp.OkHttpClient",request.body.toString())

        Log.d("okhttp.OkHttpClient",response.message)

//        return@Interceptor if (response.code == 204) {
//            response.newBuilder().code(200).build()
//        } else {
//            response
//        }

        return@Interceptor response
    }


    var gson = GsonBuilder()
        .setLenient()
        .create()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL:String): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(getRequestResponseInterceptor())
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit) = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideApiHelper(apiHelper: ApiHelperImpl): ApiHelper = apiHelper

    @Provides
    @Singleton
    fun provideLocationRepository(apiHelper : ApiHelper) : LocationRepository {
        return LocationRepository(apiHelper)
    }

}