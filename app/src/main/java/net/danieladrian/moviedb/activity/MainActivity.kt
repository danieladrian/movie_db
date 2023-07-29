package net.danieladrian.moviedb.activity

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Contacts.Intents.UI
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import net.danieladrian.moviedb.R
import net.danieladrian.moviedb.adapter.GenreAdapter
import net.danieladrian.moviedb.rest.params.result.ResultGenre
import net.danieladrian.moviedb.viewmodel.GenreViewModel

class MainActivity : AppCompatActivity() {

    val ON_PROGRESS:Int = 1
    val SUCCESS_LOAD:Int = 2
    val FAILED_LOAD:Int = 3

    var llErrorLoadGenre:LinearLayout? = null
    var rvGenre:RecyclerView? = null
    var skeletonGenre: Skeleton? = null
    var linearLayoutManager: LinearLayoutManager? = null
    var genreAdapter:GenreAdapter? = null
    var btnRetry: TextView? = null

    var genreViewModel: GenreViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUI()
        initViewModel()

    }

    fun initUI(){
        rvGenre = findViewById(R.id.rvGenre)
        llErrorLoadGenre = findViewById(R.id.llErrorLoadGenre)
        btnRetry = findViewById(R.id.btnRetry)

        linearLayoutManager = LinearLayoutManager(applicationContext,RecyclerView.VERTICAL,false)
        genreAdapter = GenreAdapter(object : GenreAdapter.Callback{
            override fun onItemClick(genresObject: ResultGenre.GenresObject?) {
                var intent = Intent(applicationContext,GenreActivity::class.java)
                var bundle:Bundle = Bundle()
                bundle.putInt("id", genresObject?.id!!)
                bundle.putString("name",genresObject.name)
                intent.putExtras(bundle)
                startActivity(intent)
            }

        })
        rvGenre?.adapter = genreAdapter
        rvGenre?.layoutManager = linearLayoutManager

        skeletonGenre = rvGenre?.applySkeleton(R.layout.item_genre,4)
        skeletonGenre?.shimmerColor = Color.parseColor("#67686D")
        skeletonGenre?.showShimmer = true
        skeletonGenre?.shimmerDurationInMillis = 500

        btnRetry?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                genreViewModel?.doGet()
            }

        })
    }

    fun initViewModel(){
        genreViewModel = ViewModelProviders.of(this).get(GenreViewModel::class.java)
        genreViewModel?.status?.observe(this, Observer {
            when(it){
                genreViewModel?.ON_PROGRESS->{
                    UIControl(ON_PROGRESS)
                }
                genreViewModel?.SUCCESS_LOAD->{
                    UIControl(SUCCESS_LOAD)
                    genreAdapter?.setData(genreViewModel?.genreData?.value?.genres)
                }
                genreViewModel?.CANCEL_LOAD->{
                    UIControl(FAILED_LOAD)
                }
                genreViewModel?.FAILED_LOAD->{
                   UIControl(FAILED_LOAD)
                }
            }
        })

        if(genreViewModel?.genreData?.value == null) {
            genreViewModel?.doGet()
        }else{
            genreAdapter?.setData(genreViewModel?.genreData?.value?.genres)
        }
    }

    fun UIControl(progress: Int){
        when(progress){
            ON_PROGRESS->{
                skeletonGenre?.showSkeleton()
                llErrorLoadGenre?.visibility = View.GONE
                rvGenre?.visibility = View.VISIBLE
            }
            SUCCESS_LOAD->{
                skeletonGenre?.showOriginal()
                llErrorLoadGenre?.visibility = View.GONE
                rvGenre?.visibility = View.VISIBLE
            }
            FAILED_LOAD->{
                skeletonGenre?.showOriginal()
                llErrorLoadGenre?.visibility = View.VISIBLE
                rvGenre?.visibility = View.GONE
            }
        }
    }

}