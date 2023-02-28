package com.pepsa.pepsadispatch.shared.presentation.viewStates

import android.app.AlertDialog
import android.app.Dialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.pepsa.pepsadispatch.R
import com.pepsa.pepsadispatch.shared.utils.AppUtils.showSnackBar

data class ViewState<out T>(
    val status: Status,
    val content: T?,
    val message: String,
) {
    companion object {
        fun <T> success(content: T?): ViewState<T> = ViewState(
            Status.SUCCESS,
            content,
            "Success",
        )

        fun <T> error(content: T?): ViewState<T> = ViewState(
            Status.ERROR,
            content,
            "Failed",
        )

        fun <T> loading(content: T?): ViewState<T> = ViewState(
            Status.LOADING,
            content,
            "Loading...",
        )

        fun <T> timeOut(content: T?): ViewState<T> = ViewState(
            Status.TIMEOUT,
            content,
            "Time out! Please try again later",
        )

        fun <T> serverError(content: T?): ViewState<T> = ViewState(
            Status.INITIAL_DEFAULT,
            content,
            "Server error, kindly try again later",
        )

        fun <T> initialDefault(content: T?): ViewState<T> = ViewState(
            Status.INITIAL_DEFAULT,
            content,
            "App just stated up, no action required",
        )

        fun <T> Fragment.observeServerResponse(
            serverResponse: LiveData<ViewState<T>>,
            loader: Dialog,
            successAction: (response: T) -> Unit,
        ) {
            serverResponse.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        loader.dismiss()
                        loader.cancel()
                        successAction(it.content!!)
                    }
                    Status.SERVER_ERROR -> {
                        loader.dismiss()
                        loader.cancel()
                        showSnackBar(R.string.server_error)
                    }
                    Status.INITIAL_DEFAULT -> {
                        loader.dismiss()
                        loader.cancel()
                    }
                    Status.TIMEOUT -> {
                        loader.dismiss()
                        loader.cancel()
                        showSnackBar(R.string.time_out)
                    }
                    Status.LOADING -> {
                        loader.show()
                    }
                    Status.ERROR -> {
                        loader.dismiss()
                        loader.cancel()
                        showSnackBar(R.string.failed_tryagain)
                    }
                }
            }
        }
    }
}
