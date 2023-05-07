package com.uxstate.diary.connectivity

interface ConnectivityObserver {

    fun onbserve():Flow<Status>

    enum class Staus(){

        AVAILABLE,
        UNAVAILABLE,
        LOSING,
        LOST

    }
}