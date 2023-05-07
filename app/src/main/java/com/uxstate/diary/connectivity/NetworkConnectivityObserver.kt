package com.uxstate.diary.connectivity

import android.content.Context
import kotlinx.coroutines.flow.Flow

class NetworkConnectivityObserver(context:Context): ConnectivityObserver {
    override fun observe(): Flow<ConnectivityObserver.Status> {
        TODO("Not yet implemented")
    }
}