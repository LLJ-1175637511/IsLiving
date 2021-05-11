package com.llj.living.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [SuppleDoing::class, SuppleFinished::class, OldManInfoWait::class, OldManInfoHad::class, CheckDoing::class, OldManInfoCheckHad::class],
    version = 1,
    exportSchema = false
)
abstract class OldManDatabase : RoomDatabase() {

    abstract fun getSuppleDoingDao(): SuppleDoingDao
    abstract fun getCheckDoingDao(): CheckDoingDao
    abstract fun getSuppleFinishedDao(): SuppleFinishedDao
    abstract fun getOldmanInfoDao(): OldManInfoDao
    abstract fun getCheckHadDao(): CheckHadDao

    companion object {
        private var instance: OldManDatabase? = null

        @Synchronized
        fun newInstance(context: Context) =
            instance ?: Room.databaseBuilder(context, OldManDatabase::class.java, "oldman_db")
//                .allowMainThreadQueries()
                .build().apply { instance = this }
    }
}