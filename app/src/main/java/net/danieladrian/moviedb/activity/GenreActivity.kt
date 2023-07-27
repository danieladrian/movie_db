package net.danieladrian.moviedb.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import net.danieladrian.moviedb.R
import org.w3c.dom.Text

class GenreActivity : AppCompatActivity() {

    var ivBack: ImageView? = null
    var tvGenre: TextView? = null
    var genreID:Int? = null
    var genreName:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genre)
        ivBack = findViewById(R.id.ivBack)
        tvGenre = findViewById(R.id.tvGenre)

        ivBack?.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        genreID = intent.getIntExtra("id",-1)
        genreName = intent.getStringExtra("name")

        tvGenre?.text = genreName

    }
}