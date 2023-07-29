package net.danieladrian.moviedb.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import net.danieladrian.moviedb.R

class YoutubePlayerActvity : AppCompatActivity() {

    var ytPlayer: YouTubePlayerView? = null
    var ivBack: ImageView? = null
    var tvMovieName: TextView? = null

    var videoID:String? = null
    var movieName:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_player)

        videoID = intent.getStringExtra("videoID")
        movieName = intent.getStringExtra("name")
        initUI()

    }

    fun initUI(){
        ytPlayer = findViewById(R.id.ytPlayer)
        tvMovieName = findViewById(R.id.tvMovieName)
        ivBack = findViewById(R.id.ivBack)

        lifecycle.addObserver(ytPlayer!!)


        ytPlayer?.addYouTubePlayerListener(object: AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)
                youTubePlayer.loadVideo(videoID!!, 0F)
            }
        })

        ivBack?.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        tvMovieName?.text = movieName
    }
}