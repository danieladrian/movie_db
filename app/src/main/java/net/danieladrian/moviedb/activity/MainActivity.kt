package net.danieladrian.moviedb.activity

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
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

    var llErrorLoadGenre:LinearLayout? = null
    var rvGenre:RecyclerView? = null
    var skeletonGenre: Skeleton? = null
    var linearLayoutManager: LinearLayoutManager? = null
    var genreAdapter:GenreAdapter? = null

    var genreViewModel: GenreViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvGenre = findViewById(R.id.rvGenre)
        llErrorLoadGenre = findViewById(R.id.llErrorLoadGenre)
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

        genreViewModel = ViewModelProviders.of(this).get(GenreViewModel::class.java)
        genreViewModel?.status?.observe(this, Observer {
            when(it){
                genreViewModel?.ON_PROGRESS->{
                    skeletonGenre?.showSkeleton()
                    llErrorLoadGenre?.visibility = View.GONE
                    rvGenre?.visibility = View.VISIBLE
                }
                genreViewModel?.SUCCESS_LOAD->{
                    skeletonGenre?.showOriginal()
                    llErrorLoadGenre?.visibility = View.GONE
                    rvGenre?.visibility = View.VISIBLE
                    genreAdapter?.setData(genreViewModel?.genreData?.value?.genres)
                }
                genreViewModel?.CANCEL_LOAD->{
                    skeletonGenre?.showOriginal()
                    llErrorLoadGenre?.visibility = View.VISIBLE
                    rvGenre?.visibility = View.GONE
                }
                genreViewModel?.FAILED_LOAD->{
                    skeletonGenre?.showOriginal()
                    llErrorLoadGenre?.visibility = View.VISIBLE
                    rvGenre?.visibility = View.GONE
                }
            }
        })

        genreViewModel?.doGet()

    }



}