package tech.androidplay.sonali.todo.data.model


import com.google.gson.annotations.SerializedName

data class PixabayImageResults(
    @SerializedName("comments")
    val comments: Int?, // 206
    @SerializedName("downloads")
    val downloads: Int?, // 823344
    @SerializedName("favorites")
    val favorites: Int?, // 1903
    @SerializedName("id")
    val id: Int?, // 3140492
    @SerializedName("imageHeight")
    val imageHeight: Int?, // 3803
    @SerializedName("imageSize")
    val imageSize: Int?, // 3511611
    @SerializedName("imageWidth")
    val imageWidth: Int?, // 5689
    @SerializedName("largeImageURL")
    val largeImageURL: String?, // https://pixabay.com/get/55e1d1434e5bae14f6da8c7dda793677173bdee75a526c48732f7dd59f4bcc5abd_1280.jpg
    @SerializedName("likes")
    val likes: Int?, // 1890
    @SerializedName("pageURL")
    val pageURL: String?, // https://pixabay.com/photos/flower-nature-flora-petal-summer-3140492/
    @SerializedName("previewHeight")
    val previewHeight: Int?, // 100
    @SerializedName("previewURL")
    val previewURL: String?, // https://cdn.pixabay.com/photo/2018/02/08/22/27/flower-3140492_150.jpg
    @SerializedName("previewWidth")
    val previewWidth: Int?, // 150
    @SerializedName("tags")
    val tags: String?, // flower, nature, flora
    @SerializedName("type")
    val type: String?, // photo
    @SerializedName("user")
    val user: String?, // JillWellington
    @SerializedName("user_id")
    val userId: Int?, // 334088
    @SerializedName("userImageURL")
    val userImageURL: String?, // https://cdn.pixabay.com/user/2018/06/27/01-23-02-27_250x250.jpg
    @SerializedName("views")
    val views: Int?, // 1355187
    @SerializedName("webformatHeight")
    val webformatHeight: Int?, // 427
    @SerializedName("webformatURL")
    val webformatURL: String?, // https://pixabay.com/get/55e1d1434e5bae14f1dc846096293e7c113fdcec504c704f75297adc9144c75d_640.jpg
    @SerializedName("webformatWidth")
    val webformatWidth: Int? // 640
)