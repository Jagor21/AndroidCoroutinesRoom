package com.devtides.coroutinesroom.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    var id: Long = 0L,
    @ColumnInfo(name = "user_name")
    val userName: String,
    @ColumnInfo(name = "password_hash")
    val passwordHash: Int,
    @ColumnInfo(name = "info")
    val info: String
)