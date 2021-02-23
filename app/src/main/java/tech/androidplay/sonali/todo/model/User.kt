package tech.androidplay.sonali.todo.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 24/Dec/2020
 * Email: ankush@androidplay.in
 */
data class User(
    @SerializedName("uid")
    var uid: String = "",
    @SerializedName("email")
    var email: String = "",
    @SerializedName("displayName")
    var displayName: String = "",
    @SerializedName("token")
    var token: String = "",
    @SerializedName("createdOn")
    var createdOn: String = "",
) : Serializable