package com.devtides.coroutinesroom.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    @Query("SELECT * FROM users WHERE user_name = :userName")
    suspend fun getUser(userName: String): User

    @Query("DELETE FROM users WHERE user_id = :id")
    suspend fun deleteUser(id: Long)
}