package net.danieladrian.moviedb.rest.params.result

class ResultMovieByGenre {
    var page:Int? = null
    var total_pages:Int? = null
    var results:ArrayList<MoviesObject>? = null

    inner class MoviesObject{
        var id:Int? = null
        var adult:Boolean? = null
        var backdrop_path:String? = null
        var original_language:String? = null
        var original_title:String? = null
        var overview:String? = null
        var popularity:String? = null
        var poster_path:String? = null
        var release_date:String? = null
        var title:String? = null
        var vote_average:Float? = null
        var vote_count:Int? = null
    }
}