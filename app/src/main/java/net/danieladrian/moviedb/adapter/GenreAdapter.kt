package net.danieladrian.moviedb.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import net.danieladrian.moviedb.R
import net.danieladrian.moviedb.rest.params.result.ResultGenre

class GenreAdapter(callback: Callback): RecyclerView.Adapter<GenreAdapter.ViewHolder>()  {
    interface Callback{
        fun onItemClick(genresObject: ResultGenre.GenresObject?)
    }

    var genresData:ArrayList<ResultGenre.GenresObject>? = null
    var mCallback:Callback = callback

    fun setData(genresObjects :ArrayList<ResultGenre.GenresObject>?){
        genresData = genresObjects
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreAdapter.ViewHolder {
        val context = parent.context
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = inflater.inflate(R.layout.item_genre, parent, false)
        val viewHolder:ViewHolder = ViewHolder(v)

        return viewHolder
    }

    override fun onBindViewHolder(holder: GenreAdapter.ViewHolder, position: Int) {
        holder.bind(genresData?.get(position))
    }

    override fun getItemCount(): Int {
        if(genresData == null){
            return 0
        }
        return genresData!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var tvItemGenre: TextView? = null

        fun bind(genresObject: ResultGenre.GenresObject?) {
            tvItemGenre = itemView.findViewById(R.id.tvItemGenre)
            tvItemGenre?.text = genresObject?.name
            itemView.setOnClickListener(object :View.OnClickListener {
                override fun onClick(p0: View?) {
                    mCallback.onItemClick(genresObject)
                }
            })
        }
    }
}