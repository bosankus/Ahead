package tech.androidplay.sonali.todo.data.model


import com.google.gson.annotations.SerializedName

data class ImageResponse(
    @SerializedName("imageResults")
    val imageResults: List<ImageResults>?,
    @SerializedName("total")
    val total: Int?, // 206836
    @SerializedName("totalHits")
    val totalHits: Int? // 500
)