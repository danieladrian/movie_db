package net.danieladrian.moviedb.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import net.danieladrian.moviedb.helper.RetrofitHelper
import net.danieladrian.moviedb.rest.params.result.ResultGenre
import net.danieladrian.moviedb.rest.params.result.ResultMovieByGenre
import net.danieladrian.moviedb.rest.params.result.ResultMovieDetail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailViewModel(application: Application): AndroidViewModel(application) {

    val CANCEL_LOAD:Int = -1
    val FAILED_LOAD:Int = 0
    val ON_PROGRESS:Int = 1
    val SUCCESS_LOAD:Int = 2

    var status = MutableLiveData<Int>()
    var moviesData = MutableLiveData<ResultMovieDetail>()
    var request: Call<ResultMovieDetail>? = null

    fun doGet(movieID:Int){
        if(status.value != ON_PROGRESS) {
            status.value = ON_PROGRESS
            request = RetrofitHelper().getDefaultInterface(getApplication())?.getMovieDetail(movieID)
            request?.enqueue(object : Callback<ResultMovieDetail> {
                override fun onResponse(call: Call<ResultMovieDetail>?, response: Response<ResultMovieDetail>?) {
                    if(call?.isExecuted!! && !call.isCanceled){
                        if(response?.code()==200){
                            moviesData.value = response.body()
                            status.value = SUCCESS_LOAD
                        }else{
                            status.value = FAILED_LOAD
                        }
                    }else{
                        status.value = CANCEL_LOAD
                    }
                }

                override fun onFailure(call: Call<ResultMovieDetail>?, t: Throwable?) {
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
