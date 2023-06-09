package fxc.dev.core.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 *
 * Created by tamle on 07/06/2023
 *
 */

@Parcelize
@Entity(tableName = "User")
data class User(
    @SerializedName("id")
    @PrimaryKey
    val id: Int,

    @SerializedName("email")
    val email: String,

    @SerializedName("first_name")
    val first_name: String,

    @SerializedName("last_name")
    val last_name: String,

    @SerializedName("avatar")
    val avatar: String
) : Parcelable {

    fun getFullName() = "$first_name $last_name"

}