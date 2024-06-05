package com.tekskills.er_tekskills.presentation.di

import com.tekskills.er_tekskills.data.db.TaskCategoryDao
import com.tekskills.er_tekskills.data.remote.ApiHelper
import com.tekskills.er_tekskills.data.repository.LocationRepository
import com.tekskills.er_tekskills.domain.TaskCategoryRepository
import com.tekskills.er_tekskills.data.repository.TaskCategoryRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideTaskCategoryRepository(taskCategoryDao: TaskCategoryDao) : TaskCategoryRepository {
        return TaskCategoryRepositoryImpl(taskCategoryDao)
    }
}