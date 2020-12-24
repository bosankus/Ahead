package tech.androidplay.sonali.todo.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 24/Dec/2020
 * Email: ankush@androidplay.in
 */
data class User(
    @SerializedName("uid")
    var uid: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("fName")
    var fName: String? = "",
    @SerializedName("lName")
    var lName: String? = "",
    @SerializedName("profileImg")
    var profileImg: String = "",
    @SerializedName("createdOn")
    var createdOn: String
)