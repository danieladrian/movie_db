package net.danieladrian.moviedb.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import net.danieladrian.moviedb.rest.params.result.ResultGenre
import net.danieladrian.moviedb.rest.params.result.ResultUserReview
import net.danieladrian.moviedb.rest.params.result.ResultVideos
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VideoTrailerViewModel(application: Application): AndroidViewModel(application) {

    val CANCEL_LOAD:Int = -1
    val FAILED_LOAD:Int = 0
    val ON_PROGRESS:Int = 1
    val SUCCESS_LOAD:Int = 2

    var status = MutableLiveData<Int>()
    var videosData = MutableLiveData<ResultVideos>()
    var request: Call<ResultVideos>? = null

    fun doGet(movieID:Int){
        if(status.value != ON_PROGRESS) {
            status.value = ON_PROGRESS
            request?.enqueue(object : Callback<ResultVideos> {
                override fun onResponse(call: Call<ResultVideos>?, response: Response<ResultVideos>?) {
                    if(call?.isExecuted!! && !call.isCanceled){
                        if(response?.code()==200){
                            videosData.value = response.body()
                            status.value = SUCCESS_LOAD
                        }else{
                            status.value = FAILED_LOAD
                        }
                    }else{
                        status.value = CANCEL_LOAD
                    }
                }

                override fun onFailure(call: Call<ResultVideos>?, t: Throwable?) {
                    Log.d("Error Failure Genre",t.toString())
                    if(call?.isExecuted!! && !call.isCanceled){
                        status.value = FAILED_LOAD
                    }else{
                        status.value = CANCEL_LOAD
                    }
                }
            })
        }
    }

    fun doCancel() {
        if(request?.isExecuted!!) {
            request?.cancel()
        }
    }
}
