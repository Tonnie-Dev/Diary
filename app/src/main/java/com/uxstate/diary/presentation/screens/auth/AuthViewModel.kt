package com.uxstate.diary.presentation.screens.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uxstate.diary.util.Constants.APP_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.GoogleAuthType
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {


    var loadingState = mutableStateOf(false)
        private set

    fun setLoading(isLoading: Boolean) {
        Timber.i("setLoading() invoked()")
        loadingState.value = isLoading
    }

    fun signInWithMongoAtlas(
        tokenId: String,
        onSuccess: (Boolean) -> Unit,
        onError: (Exception) -> Unit
    ) {
        Timber.i("signInWithMongoAtlas invoked()")
        //launch a coroutine
        viewModelScope.launch {

            try {

                /* Authenticate with MongoDb and store the outcome on the result variable
  this can be true or false depending on whether the user has been logged in*/
                //get result in IO Dispatchers as this is a network operation
                val result = withContext(IO) {

                    //App Class is from the Mongo's Realm SDK

                    //Create() creates a new user/account if none exists

                    //login() takes a Credentials argument
                    App.create(appId = APP_ID)
                            .login(
                                    credentials = Credentials.google(
                                            token = tokenId,
                                            type = GoogleAuthType.ID_TOKEN
                                    )
                                    //loggedIn() returns a boolean if there is an user logged in
                            ).loggedIn

                }
                //After we get the result from App.create().login.loggedIn we pass it to onSuccess

                //we now change the Dispatchers to Main as onSuccess is called by composables
                withContext(Main) {

                    //onSuccess is triggered from a composable function
                    onSuccess(result)
                    Timber.i("signUp Success")
                }
            } catch (e: Exception) {

                //if an error is received we also use Dispatchers main
                withContext(Main) {

                    //onError is also triggered from Composables who don't have
                    //to wait
                    onError(e)

                    Timber.i("Error")
                }

            }
        }


    }
}