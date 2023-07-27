package net.danieladrian.moviedb.rest.params.result

class ResultGenre {
    var genres:GenresObject? = null

    inner class GenresObject{
        var id:Int? = null
        var name:String? = null
    }
}