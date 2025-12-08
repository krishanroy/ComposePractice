package com.krishan.composePractice.lowesPrep.data.di

import com.krishan.composePractice.lowesPrep.data.repository.PhotosRepositoryImpl
import com.krishan.composePractice.lowesPrep.data.repository.TodosRepositoryImpl
import com.krishan.composePractice.lowesPrep.data.repository.UserRepositoryImpl
import com.krishan.composePractice.lowesPrep.domain.repo.PhotosRepository
import com.krishan.composePractice.lowesPrep.domain.repo.TodosRepository
import com.krishan.composePractice.lowesPrep.domain.repo.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

// Use @Binds when:
// You’re mapping an interface to a concrete implementation.
// No extra logic is needed — just “this interface = that class.”
// @Binds → abstract function → must live in an abstract class.

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindsUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun bindsPhotosRepository(impl: PhotosRepositoryImpl): PhotosRepository

    @Binds
    abstract fun bindsTodosRepository(impl: TodosRepositoryImpl): TodosRepository
}