package net.danieladrian.moviedb.activity

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.faltenreich.skeletonlayout.createSkeleton
import net.danieladrian.moviedb.R
import net.danieladrian.moviedb.adapter.MoviesAdapter
import net.danieladrian.moviedb.rest.params.result.ResultMovieByGenre
import net.danieladrian.moviedb.viewmodel.GenreViewModel
import net.danieladrian.moviedb.viewmodel.MovieByGenreViewModel
import org.w3c.dom.Text

class GenreActivity : AppCompatActivity() {

    val ON_PROGRESS:Int = 1
    val SUCCESS_LOAD:Int = 2
    val FAILED_LOAD:Int = 3

    var ivBack: ImageView? = null
    var tvGenre: TextView? = null
    var rvMovie:RecyclerView? = null
    var llErrorLoad:LinearLayout? = null
    var btnRetry:TextView? = null

    var genreID:Int? = null
    var genreName:String? = null

    var linearLayoutManager:LinearLayoutManager? =null
    var moviesAdapter:MoviesAdapter? = null
    var skeletonLoading: Skeleton? = null

    var movieByGenreViewModel: MovieByGenreViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genre)

        genreID = intent.getIntExtra("id",-1)
        genreName = intent.getStringExtra("name")

        initUI()
        initViewModel()

    }

    fun initUI(){
        ivBack = findViewById(R.id.ivBack)
        tvGenre = findViewById(R.id.tvGenre)
        rvMovie = findViewById(R.id.rvMovie)
        llErrorLoad = findViewById(R.id.llErrorLoad)
        btnRetry = findViewById(R.id.btnRetry)

        ivBack?.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        linearLayoutManager = LinearLayoutManager(applicationContext,RecyclerView.VERTICAL,false)
        moviesAdapter = MoviesAdapter(object : MoviesAdapter.Callback{
            override fun onItemClick(movieObject: ResultMovieByGenre.MoviesObject?) {
                var intent = Intent(applicationContext,DetailActivity::class.java)
                var bundle:Bundle = Bundle()
                bundle.putInt("id", movieObject?.id!!)
                bundle.putString("name", movieObject.original_title)
                intent.putExtras(bundle)
                startActivity(intent)
            }

        })

        rvMovie?.adapter = moviesAdapter
        rvMovie?.layoutManager = linearLayoutManager

        rvMovie?.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                var layoutManager:LinearLayoutManager = rvMovie?.layoutManager as LinearLayoutManager
                if(layoutManager.findLastVisibleItemPosition() == moviesAdapter?.itemCount!! - 1 && movieByGenreViewModel?.lastPage?.value != null){
                    if(movieByGenreViewModel?.lastPage?.value != movieByGenreViewModel?.maxPage?.value){
                        movieByGenreViewModel?.getNextPage()
                    }
                }
            }
        })

        tvGenre?.text = genreName

        btnRetry?.setOnClickListener(object : OnClickListener{
            override fun onClick(p0: View?) {
                movieByGenreViewModel?.getNextPage()
            }

        })

        skeletonLoading = rvMovie?.applySkeleton(R.layout.item_movies)
        skeletonLoading?.shimmerColor = Color.parseColor("#67686D")
        skeletonLoading?.showShimmer = true
        skeletonLoading?.shimmerDurationInMillis = 500
    }

    fun initViewModel(){
        movieByGenreViewModel = ViewModelProviders.of(this).get(MovieByGenreViewModel::class.java)
        movieByGenreViewModel?.status?.observe(this, Observer {
            when(it){
                movieByGenreViewModel?.ON_PROGRESS->{
                    if(movieByGenreViewModel?.lastPage?.value == null) {
                        UIControl(ON_PROGRESS)
                    }
                }
                movieByGenreViewModel?.SUCCESS_LOAD->{
                    if(movieByGenreViewModel?.lastPage?.value == null) {
                        UIControl(SUCCESS_LOAD)
                        moviesAdapter?.setData(movieByGenreViewModel?.moviesData?.value)
                    }else{
                        moviesAdapter?.appendData(movieByGenreViewModel?.moviesData?.value)
                    }
                }
                movieByGenreViewModel?.CANCEL_LOAD->{

                  UIControl(FAILED_LOAD)

                }
                movieByGenreViewModel?.FAILED_LOAD->{
                    UIControl(FAILED_LOAD)

                }
            }
        })

        if(movieByGenreViewModel?.lastPage?.value == null){
            movieByGenreViewModel?.doGet(genreID!!)
        }else{
            moviesAdapter?.setData(movieByGenreViewModel?.moviesData?.value)
        }


    }

    fun UIControl(progress: Int){
        when(progress){
            ON_PROGRESS->{
                skeletonLoading?.showSkeleton()
                rvMovie?.visibility = View.VISIBLE
                llErrorLoad?.visibility = View.GONE
            }
            SUCCESS_LOAD->{
                skeletonLoading?.showOriginal()
                llErrorLoad?.visibility = View.GONE
                rvMovie?.visibility = View.VISIBLE
            }
            FAILED_LOAD->{
                llErrorLoad?.visibility = View.VISIBLE
                rvMovie?.visibility = View.GONE
            }
        }
    }
}