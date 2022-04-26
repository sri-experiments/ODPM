package com.srivats.odpm.DB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pwd_list")
data class PwdItem(

    @PrimaryKey(autoGenerate = true)
    var itemId: Long = 0L,

    @ColumnInfo(name = "site_name")
    val siteName: String,

    @ColumnInfo(name = "site_pwd")
    val sitePwd: String,
)