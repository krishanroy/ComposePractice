package com.krishan.composePractice.lowesPrep.data.di

import com.krishan.composePractice.lowesPrep.data.remote.contracts.PhotosApiService
import com.krishan.composePractice.lowesPrep.data.remote.contracts.TodosApiService
import com.krishan.composePractice.lowesPrep.data.remote.contracts.UserApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://jsonplaceholder.typicode.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideUserApiService(retrofit: Retrofit): UserApiService = retrofit.create(UserApiService::class.java)

    @Provides
    @Singleton
    fun providePhotosApiService(retrofit: Retrofit): PhotosApiService = retrofit.create(PhotosApiService::class.java)

    @Provides
    @Singleton
    fun provideTodosApiService(retrofit: Retrofit): TodosApiService = retrofit.create(TodosApiService::class.java)

}
