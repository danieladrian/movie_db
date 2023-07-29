package net.danieladrian.moviedb.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.faltenreich.skeletonlayout.createSkeleton
import net.danieladrian.moviedb.R
import net.danieladrian.moviedb.adapter.ReviewAdapter
import net.danieladrian.moviedb.rest.params.result.ResultMovieDetail
import net.danieladrian.moviedb.rest.params.result.ResultVideos
import net.danieladrian.moviedb.viewmodel.MovieDetailViewModel
import net.danieladrian.moviedb.viewmodel.UserReviewViewModel
import net.danieladrian.moviedb.viewmodel.VideoTrailerViewModel
import java.math.BigDecimal


class DetailActivity : AppCompatActivity() {

    val SHOW_DETAIL_PROGRESS:Int = 1
    val SUCCESS_LOAD_DETAIL:Int = 2
    val FAILED_LOAD_DETAIL:Int = 3
    val SHOW_TRAILER:Int = 4
    val HIDE_TRAILER:Int = 5
    val SHOW_REVIEWS_PROGRESS:Int = 6
    val SHOW_REVIEWS:Int = 7
    val HIDE_REVIEWS:Int = 8
    val NO_REVIEWS:Int = 9

    var movieID:Int? = null

    var ivBack: ImageView? = null
    var ivBackdrop:ImageView? = null
    var ivPoster:ImageView? = null
    var tvTitle:TextView? = null
    var tvOverview:TextView? = null
    var tvRating:TextView? = null
    var btnTrailer: Button? = null
    var rvReview: RecyclerView? = null
    var llErrorLoad: LinearLayout? = null
    var llMovieDetail: LinearLayout? = null
    var llReview: LinearLayout? = null
    var tvReviewMsg: TextView? = null
    var btnRetry: TextView? = null
    var nsScroll: NestedScrollView? =null

    var linearLayoutManager:LinearLayoutManager? = null
    var reviewAdapter:ReviewAdapter? = null

    var skeletonTitle:Skeleton? = null
    var skeletonOverview:Skeleton? = null
    var skeletonRating:Skeleton? = null
    var skeletonReview:Skeleton? = null

    var movieDetailViewModel: MovieDetailViewModel? = null
    var userReviewViewModel:UserReviewViewModel? = null
    var videoTrailerViewModel:VideoTrailerViewModel? = null

    var videoID:String? = null
    var movieName:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        movieID = intent.getIntExtra("id",-1)
        movieName = intent.getStringExtra("name")

        initUI()
        initViewModel()
    }

    fun showDetail(resultMovieDetail: ResultMovieDetail){
        tvTitle?.text = resultMovieDetail.original_title
        tvOverview?.text = resultMovieDetail.overview
        tvRating?.text = resultMovieDetail.vote_average?.toBigDecimal()?.setScale(1, BigDecimal.ROUND_HALF_UP).toString()
        Glide.with(applicationContext).load("https://image.tmdb.org/t/p/w154" + resultMovieDetail?.poster_path).into(ivPoster!!)
        Glide.with(applicationContext).load("https://image.tmdb.org/t/p/w500" + resultMovieDetail?.backdrop_path).into(ivBackdrop!!)
    }

    fun initUI(){
        ivBackdrop = findViewById(R.id.ivBackdrop)
        ivPoster = findViewById(R.id.ivPosterDetail)
        tvTitle = findViewById(R.id.tvTitle)
        tvOverview = findViewById(R.id.tvOverview)
        tvRating = findViewById(R.id.tvRating)
        btnTrailer = findViewById(R.id.btnTrailer)
        rvReview = findViewById(R.id.rvReview)
        ivBack = findViewById(R.id.ivBack)
        llErrorLoad = findViewById(R.id.llErrorLoad)
        llMovieDetail = findViewById(R.id.llMovieDetail)
        llReview = findViewById(R.id.llReview)
        tvReviewMsg = findViewById(R.id.tvReviewMsg)
        btnRetry = findViewById(R.id.btnRetry)
        nsScroll = findViewById(R.id.nsScroll)

        ivBack?.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        rvReview?.isNestedScrollingEnabled = false

        linearLayoutManager = LinearLayoutManager(applicationContext,RecyclerView.VERTICAL,false)
        reviewAdapter = ReviewAdapter()

        rvReview?.layoutManager = linearLayoutManager
        rvReview?.adapter = reviewAdapter

        skeletonTitle = tvTitle?.createSkeleton()
        skeletonTitle?.shimmerColor = Color.parseColor("#67686D")
        skeletonTitle?.showShimmer = true
        skeletonTitle?.shimmerDurationInMillis = 500

        skeletonOverview = tvOverview?.createSkeleton()
        skeletonOverview?.shimmerColor = Color.parseColor("#67686D")
        skeletonOverview?.showShimmer = true
        skeletonOverview?.shimmerDurationInMillis = 500

        skeletonRating = tvRating?.createSkeleton()
        skeletonRating?.shimmerColor = Color.parseColor("#67686D")
        skeletonRating?.showShimmer = true
        skeletonRating?.shimmerDurationInMillis = 500

        skeletonReview = rvReview?.applySkeleton(R.layout.item_reviews,2)
        skeletonReview?.shimmerColor = Color.parseColor("#67686D")
        skeletonReview?.showShimmer = true
        skeletonReview?.shimmerDurationInMillis = 500

        btnTrailer?.setOnClickListener(object:View.OnClickListener{
            override fun onClick(p0: View?) {
                var intent = Intent(applicationContext,YoutubePlayerActvity::class.java)
                var bundle:Bundle = Bundle()
                bundle.putString("name",movieName)
                bundle.putString("videoID",videoID)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })

        btnRetry?.setOnClickListener(object :View.OnClickListener{
            override fun onClick(p0: View?) {
                if(movieDetailViewModel?.moviesData?.value == null){
                    movieDetailViewModel?.doGet(movieID!!)
                }else{

                }

                if(userReviewViewModel?.userReviewData?.value == null){
                    userReviewViewModel?.doGet(movieID!!)
                }else{

                }

                if(videoTrailerViewModel?.videosData?.value == null){
                    videoTrailerViewModel?.doGet(movieID!!)
                }else{

                }
            }
        })

        nsScroll?.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener{
            override fun onScrollChange(
                v: NestedScrollView,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {

                val diff: Int =
                    nsScroll?.getChildAt(nsScroll?.getChildCount()!! - 1)?.bottom!! - (nsScroll?.getHeight()!! + nsScroll?.getScrollY()!!)

                if (diff == 0) {
                   userReviewViewModel?.getNextPage()
                }
            }

        })
    }

    fun initViewModel(){
        movieDetailViewModel = ViewModelProviders.of(this).get(MovieDetailViewModel::class.java)
        userReviewViewModel = ViewModelProviders.of(this).get(UserReviewViewModel::class.java)
        videoTrailerViewModel = ViewModelProviders.of(this).get(VideoTrailerViewModel::class.java)

        movieDetailViewModel?.status?.observe(this, Observer {
            when(it){
                movieDetailViewModel?.ON_PROGRESS->{
                    UIControl(SHOW_DETAIL_PROGRESS)
                }
                movieDetailViewModel?.SUCCESS_LOAD->{
                    UIControl(SUCCESS_LOAD_DETAIL)
                    showDetail(movieDetailViewModel?.moviesData?.value!!)
                }
                movieDetailViewModel?.CANCEL_LOAD->{
                    UIControl(FAILED_LOAD_DETAIL)

                }
                movieDetailViewModel?.FAILED_LOAD->{
                    UIControl(FAILED_LOAD_DETAIL)
                }
            }
        })

        videoTrailerViewModel?.status?.observe(this, Observer {
            when(it){
                videoTrailerViewModel?.ON_PROGRESS->{
                   UIControl(HIDE_TRAILER)
                }
                videoTrailerViewModel?.SUCCESS_LOAD->{
                    var foundTrailer = false
                    for(videoObject:ResultVideos.VideosObject in videoTrailerViewModel?.videosData?.value!!){
                        if(videoObject.official == true && videoObject.type== "Trailer" && videoObject.site=="YouTube"){
                            foundTrailer = true
                            videoID = videoObject.key
                            break
                        }
                    }

                    if(foundTrailer){
                        UIControl(SHOW_TRAILER)
                    }else {
                        UIControl(HIDE_TRAILER)
                    }
                }
                videoTrailerViewModel?.CANCEL_LOAD->{
                    UIControl(HIDE_TRAILER)
                }
                videoTrailerViewModel?.FAILED_LOAD->{
                    UIControl(HIDE_TRAILER)
                }
            }
        })

        userReviewViewModel?.status?.observe(this, Observer {
            when(it){
                userReviewViewModel?.ON_PROGRESS->{
                    if(userReviewViewModel?.lastPage?.value == null) {
                        UIControl(SHOW_REVIEWS_PROGRESS)
                    }
                }
                userReviewViewModel?.SUCCESS_LOAD->{
                    if(userReviewViewModel?.lastPage?.value == null) {
                        UIControl(SHOW_REVIEWS)
                        reviewAdapter?.setData(userReviewViewModel?.userReviewData?.value)
                    }else{
                        reviewAdapter?.appendData(userReviewViewModel?.userReviewData?.value)
                    }

                    if(reviewAdapter?.reviewsData?.size == 0){
                        UIControl(NO_REVIEWS)
                    }
                }
                userReviewViewModel?.CANCEL_LOAD->{
                   UIControl(HIDE_REVIEWS)
                }
                userReviewViewModel?.FAILED_LOAD->{
                    UIControl(HIDE_REVIEWS)
                }
            }
        })

        if(movieDetailViewModel?.moviesData?.value == null){
            movieDetailViewModel?.doGet(movieID!!)
        }else{

        }

        if(userReviewViewModel?.userReviewData?.value == null){
            userReviewViewModel?.doGet(movieID!!)
        }else{

        }

        if(videoTrailerViewModel?.videosData?.value == null){
            videoTrailerViewModel?.doGet(movieID!!)
        }else{

        }
    }

    fun UIControl(state:Int){
        when(state){
            SHOW_DETAIL_PROGRESS->{
                skeletonOverview?.showSkeleton()
                skeletonTitle?.showSkeleton()
                skeletonRating?.showSkeleton()
                llMovieDetail?.visibility = View.VISIBLE
                llErrorLoad?.visibility = View.GONE
                llReview?.visibility = View.GONE
            }
            SUCCESS_LOAD_DETAIL->{
                skeletonOverview?.showOriginal()
                skeletonTitle?.showOriginal()
                skeletonRating?.showOriginal()
                llMovieDetail?.visibility = View.VISIBLE
                llErrorLoad?.visibility = View.GONE
                llReview?.visibility = View.VISIBLE
            }
            FAILED_LOAD_DETAIL->{
                skeletonOverview?.showOriginal()
                skeletonTitle?.showOriginal()
                skeletonRating?.showOriginal()
                llMovieDetail?.visibility = View.GONE
                llErrorLoad?.visibility = View.VISIBLE
                llReview?.visibility = View.GONE
            }
            SHOW_TRAILER->{
                btnTrailer?.visibility = View.VISIBLE
            }
            HIDE_TRAILER->{
                btnTrailer?.visibility = View.GONE
            }
            SHOW_REVIEWS_PROGRESS->{
                llReview?.visibility = View.VISIBLE
                rvReview?.visibility = View.VISIBLE
                tvReviewMsg?.visibility = View.GONE
                skeletonReview?.showSkeleton()
            }
            SHOW_REVIEWS->{
                skeletonReview?.showOriginal()
                llReview?.visibility = View.VISIBLE
                rvReview?.visibility = View.VISIBLE
                tvReviewMsg?.visibility = View.GONE
            }
            HIDE_REVIEWS->{
                skeletonReview?.showOriginal()
                llReview?.visibility = View.GONE
            }
            NO_REVIEWS->{
                skeletonReview?.showOriginal()
                llReview?.visibility = View.VISIBLE
                rvReview?.visibility = View.GONE
                tvReviewMsg?.visibility = View.VISIBLE
                tvReviewMsg?.text = "No Reviews"
            }
        }
    }
}