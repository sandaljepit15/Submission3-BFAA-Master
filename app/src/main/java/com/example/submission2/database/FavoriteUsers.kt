package com.example.submission2.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class FavoriteUsers(
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @PrimaryKey
    @ColumnInfo(name = "login")
    var login: String = "",

    @ColumnInfo(name = "avatar_url")
    var avatar_url: String? = null,

) : Parcelable