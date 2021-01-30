package tech.androidplay.sonali.todo.model


import com.google.gson.annotations.SerializedName

data class Quote(
    @SerializedName("author")
    var author: String?, // Wayne Dyer
    @SerializedName("text")
    var text: String? // You'll see it when you believe it.
)