package com.srivats.odpm.DB

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PwdDatabaseDao {
    @Query("SELECT * from pwd_list")
    fun getAll(): LiveData<List<PwdItem>>

    @Query("SELECT * from pwd_list where itemId = :id")
    fun getById(id: Int): PwdItem?

    @Insert
    suspend fun insert(item: PwdItem)

    @Update
    suspend fun update(item: PwdItem)

    @Delete
    suspend fun delete(item: PwdItem)

    @Query("DELETE FROM pwd_list")
    suspend fun deleteAllSitePwd()
}