package com.pepsa.pepsadispatch.shared.utils.networkUtils

import com.pepsa.pepsadispatch.shared.presentation.viewStates.ViewState
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkUtils @Inject constructor() {
    fun <T> getServerResponse(serverResponse: Response<T>): ViewState<T> {
        return when {
            serverResponse.code() in 200..299 -> {
                ViewState.success(serverResponse.body()!!)
            }
            serverResponse.code() in 400..499 -> {
                ViewState.error(null)
            }
            serverResponse.code() >= 500 -> {
                ViewState.serverError(null)
            }
            else -> {
                ViewState.error(null)
            }
        }
    }
}
