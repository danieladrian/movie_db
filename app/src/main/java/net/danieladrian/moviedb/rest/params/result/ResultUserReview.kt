package net.danieladrian.moviedb.rest.params.result

class ResultUserReview {
    var id:Int? = null
    var page:Int? = null
    var total_pages:Int? = null
    var results:ArrayList<ReviewObject>? = null

    inner class ReviewObject{
        var author:String? = null
        var content:String? = null
        var created_at:String? = null
        var id:String? = null
        var author_details:AuthorDetails? = null

        inner class AuthorDetails{
            var name:String? = null
            var username:String? = null
            var avatar_path:String? = null
        }
    }
}