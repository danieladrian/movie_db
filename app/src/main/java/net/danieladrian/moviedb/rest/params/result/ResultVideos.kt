package net.danieladrian.moviedb.rest.params.result

class ResultVideos {
    var id:Int? = null
    var results:ArrayList<VideosObject>? = null

    inner class VideosObject{
        var iso_639_1:String? = null
        var iso_3166_1:String? = null
        var name:String? = null
        var key:String? = null
        var site:String? = null
        var size:String? = null
        var type:String? = null
        var official:Boolean? = null
        var published_at:String? = null
        var id:String? = null
    }
}