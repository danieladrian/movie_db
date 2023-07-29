package net.danieladrian.moviedb.adapter

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import net.danieladrian.moviedb.R
import net.danieladrian.moviedb.rest.params.result.ResultMovieByGenre
import net.danieladrian.moviedb.rest.params.result.ResultUserReview

class ReviewAdapter: RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    var reviewsData:ArrayList<ResultUserReview.ReviewObject>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewAdapter.ViewHolder {
        val context = parent.context
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = inflater.inflate(R.layout.item_reviews, parent, false)
        val viewHolder: ReviewAdapter.ViewHolder = ViewHolder(v)

        return viewHolder
    }

    override fun onBindViewHolder(holder: ReviewAdapter.ViewHolder, position: Int) {
        holder.bind(reviewsData?.get(position)!!)
    }

    override fun getItemCount(): Int {
        if(reviewsData == null){
            return 0
        }
        return reviewsData!!.size
    }

    fun setData(reviews :ArrayList<ResultUserReview.ReviewObject>?){
        reviewsData = reviews
        notifyDataSetChanged()
    }

    fun appendData(reviews :ArrayList<ResultUserReview.ReviewObject>?){
        val lastPosition = reviews?.size!!-1
        reviewsData = reviews
        notifyItemInserted(lastPosition)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var ivAvatar:ImageView? = null
        var tvAuthor:TextView? = null
        var tvComment:TextView? = null
        var tvDate:TextView? = null
        var tvRating:TextView? = null
        var llRating:LinearLayout? = null

        fun bind(reviewObject: ResultUserReview.ReviewObject) {
            ivAvatar = itemView.findViewById(R.id.ivAvatar)
            tvAuthor = itemView.findViewById(R.id.tvAuthor)
            tvComment = itemView.findViewById(R.id.tvComment)
            tvDate = itemView.findViewById(R.id.tvDate)
            tvRating = itemView.findViewById(R.id.tvRating)
            llRating = itemView.findViewById(R.id.llRating)

            tvAuthor?.text = reviewObject.author
            tvComment?.text = Html.fromHtml(reviewObject.content)
            tvDate?.text = reviewObject.created_at
            tvRating?.text = reviewObject.author_details?.rating.toString()

            if(reviewObject.author_details?.rating == null){
                llRating?.visibility = View.GONE
            }
            if(reviewObject.author_details?.avatar_path !=null) {
                if (reviewObject.author_details?.avatar_path?.contains("gravatar")!!) {
                    Glide.with(itemView.context)
                        .load("https://secure.gravatar.com/avatar" + reviewObject.author_details?.avatar_path)
                        .into(ivAvatar!!)
                } else {
                    Glide.with(itemView.context)
                        .load("https://image.tmdb.org/t/p/w154" + reviewObject.author_details?.avatar_path)
                        .into(ivAvatar!!)
                }
            }else{
                Glide.with(itemView.context)
                    .load("https://secure.gravatar.com/avatar/4965583abc47850975522bdfcad0c325.jpg")
                    .into(ivAvatar!!)
            }

        }
    }
}