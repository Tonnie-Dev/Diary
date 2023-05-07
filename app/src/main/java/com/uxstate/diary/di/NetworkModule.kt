package com.uxstate.diary.di

import com.uxstate.diary.connectivity.ConnectivityObserver
import com.uxstate.diary.connectivity.NetworkConnectivityObserver
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    @Binds
    @Singleton
    fun providesNetworkConnectivityObserver(connectivityObserver: ConnectivityObserver) = NetworkConnectivityObserver
}