package com.zgame.zgame.model

import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class PostModelRequestImagesLikes {
    var comment = ""
    var imageId = ""
    var images: String = ""
    var like: Boolean = false

}

class ImageObj {
    var image = ArrayList<PostModelRequestImagesLikes>()
}

class UserGetImages{
    var abc = ArrayList<HashMap<String, Objects>>()
}