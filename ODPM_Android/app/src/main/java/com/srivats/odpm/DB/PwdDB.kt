package com.srivats.odpm.DB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PwdItem::class], version = 1, exportSchema = true)
abstract class PwdDB : RoomDatabase() {
    abstract fun pwdDao(): PwdDatabaseDao

    companion object {

        private var INSTANCE: PwdDB? = null

        fun getInstance(context: Context): PwdDB {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PwdDB::class.java,
                        "pwd_list_database"
                    ).fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}