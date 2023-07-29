package net.danieladrian.moviedb.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import net.danieladrian.moviedb.helper.RetrofitHelper
import net.danieladrian.moviedb.rest.params.result.ResultGenre
import net.danieladrian.moviedb.rest.params.result.ResultMovieByGenre
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieByGenreViewModel(application: Application): AndroidViewModel(application) {

    val CANCEL_LOAD:Int = -1
    val FAILED_LOAD:Int = 0
    val ON_PROGRESS:Int = 1
    val SUCCESS_LOAD:Int = 2

    var status = MutableLiveData<Int>()
    var moviesData = MutableLiveData<ArrayList<ResultMovieByGenre.MoviesObject>>()
    var request: Call<ResultMovieByGenre>? = null

    var genreID = MutableLiveData<Int>()
    var lastPage = MutableLiveData<Int>()
    var maxPage = MutableLiveData<Int>()

    fun getNextPage(){
        doGet(genreID.value!!)
    }
    fun doGet(id:Int){
        if(status.value != ON_PROGRESS) {
            status.value = ON_PROGRESS
            var page = 1
            if(lastPage.value != null){
                page = lastPage.value!! + 1
            }
            genreID.value = id
            request = RetrofitHelper().getDefaultInterface(getApplication())?.getMovieByGenre(id,page)
            request?.enqueue(object : Callback<ResultMovieByGenre> {
                override fun onResponse(call: Call<ResultMovieByGenre>?, response: Response<ResultMovieByGenre>?) {
                    if(call?.isExecuted!! && !call.isCanceled){
                        if(response?.code()==200){
                            if( moviesData.value == null){
                                moviesData.value = ArrayList()
                            }
                            moviesData.value?.addAll(response.body()?.results!!)
                            status.value = SUCCESS_LOAD
                            maxPage.value = response.body()?.total_pages
                            lastPage.value = response.body()?.page
                        }else{
                            status.value = FAILED_LOAD
                        }
                    }else{
                        status.value = CANCEL_LOAD
                    }
                }

                override fun onFailure(call: Call<ResultMovieByGenre>?, t: Throwable?) {
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
