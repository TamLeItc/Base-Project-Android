package fxc.dev.core.data.source.remote.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import fxc.dev.core.domain.model.User
import kotlinx.parcelize.Parcelize

/**
 *
 * Created by tamle on 07/06/2023
 *
 */

@Parcelize
data class UserDTO(
    @SerializedName("page")
    val page: Int,

    @SerializedName("per_page")
    val per_page: Int,

    @SerializedName("total")
    val total: Int,

    @SerializedName("total_pages")
    val total_pages: Int,

    @SerializedName("data")
    val data: List<User>
) : Parcelable