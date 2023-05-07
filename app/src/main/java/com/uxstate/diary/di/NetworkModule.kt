package com.uxstate.diary.di

import com.uxstate.diary.connectivity.ConnectivityObserver
import com.uxstate.diary.connectivity.ConnectivityObserverImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {
    //ConnectivityObserverImpl
    @Binds
    @Singleton
    abstract fun provideConnectivityObserver(
        connectivityObserverImpl: ConnectivityObserverImpl
    ): ConnectivityObserver
}