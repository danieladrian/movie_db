package net.danieladrian.moviedb.adapter

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import net.danieladrian.moviedb.R
import net.danieladrian.moviedb.rest.params.result.ResultMovieByGenre

class MoviesAdapter(callback: Callback): RecyclerView.Adapter<MoviesAdapter.ViewHolder>()  {
    interface Callback{
        fun onItemClick(movieObject: ResultMovieByGenre.MoviesObject?)
    }
    var moviesData:ArrayList<ResultMovieByGenre.MoviesObject>? = null

    var mCallback: Callback = callback

    fun setData(movies :ArrayList<ResultMovieByGenre.MoviesObject>?){
        moviesData = movies
        notifyDataSetChanged()
    }

    fun appendData(movies :ArrayList<ResultMovieByGenre.MoviesObject>?){
        val lastPosition = moviesData?.size!!-1
        moviesData = movies
        notifyItemInserted(lastPosition)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = inflater.inflate(R.layout.item_movies, parent, false)
        val viewHolder:ViewHolder = ViewHolder(v)

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(moviesData?.get(position))
    }

    override fun getItemCount(): Int {
        if(moviesData == null){
            return 0
        }
        return moviesData!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var tvTitle: TextView? = null
        var ivPoster: ImageView? = null
        var tvRating: TextView? = null
        var tvDate: TextView? = null

        fun bind(moviesObject: ResultMovieByGenre.MoviesObject?) {
            tvTitle = itemView.findViewById(R.id.tvTitle)
            ivPoster = itemView.findViewById(R.id.ivPoster)
            tvRating = itemView.findViewById(R.id.tvRating)
            tvDate = itemView.findViewById(R.id.tvDate)

            tvTitle?.text = moviesObject?.original_title
            tvRating?.text = moviesObject?.vote_average.toString()
            tvDate?.text = moviesObject?.release_date

            Glide.with(itemView.context).load("https://image.tmdb.org/t/p/w154" + moviesObject?.poster_path) .centerCrop() .into(ivPoster!!)

            itemView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    mCallback.onItemClick(moviesObject)
                }
            })
        }
    }
}