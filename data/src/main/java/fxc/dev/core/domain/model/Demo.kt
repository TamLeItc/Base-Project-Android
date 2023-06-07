package fxc.dev.core.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by Tam Le on 30/10/2021.
 */

@Parcelize
@Entity(tableName = "Post")
data class Demo(
    @SerializedName("id")
    @PrimaryKey
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("body")
    val body: String
) : Parcelable