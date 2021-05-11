package com.llj.living.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "supple_doing")
data class SuppleDoing(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "supple_title")
    val title: String,
    @ColumnInfo(name = "supple_endTime")
    val endTime: String,
    @ColumnInfo(name = "supple_startTime")
    val startTime: String,
    @ColumnInfo(name = "supple_waitDealWith")
    var waitDealWith: Int,
    @ColumnInfo(name = "supple_hadDealWith")
    var hadDealWith: Int
)

@Entity(tableName = "supple_finished")
data class SuppleFinished(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "supple_title")
    val title: String,
    @ColumnInfo(name = "supple_endTime")
    val endTime: String,
    @ColumnInfo(name = "supple_startTime")
    val startTime: String,
    @ColumnInfo(name = "supple_waitDealWith")
    var waitDealWith: Int,
    @ColumnInfo(name = "supple_hadDealWith")
    var hadDealWith: Int
)

@Entity(tableName = "check_doing")
data class CheckDoing(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "check_title")
    val title: String,
    @ColumnInfo(name = "check_endTime")
    val endTime: String,
    @ColumnInfo(name = "check_startTime")
    val startTime: String,
    @ColumnInfo(name = "check_waitDealWith")
    var waitDealWith: Int,
    @ColumnInfo(name = "check_hadDealWith")
    var hadDealWith: Int
)

@Entity(tableName = "oldman_info_wait")
data class OldManInfoWait(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "oldman_name")
    val name: String,
    @ColumnInfo(name = "oldman_sex")
    val sex: String,
    @ColumnInfo(name = "oldman_idCard")
    val idCard: String,
    @ColumnInfo(name = "oldman_uri")
    val uris: String = ""
)

@Entity(tableName = "oldman_info_had")
data class OldManInfoHad(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "oldman_name")
    val name: String,
    @ColumnInfo(name = "oldman_sex")
    val sex: String,
    @ColumnInfo(name = "oldman_idCard")
    val idCard: String,
    @ColumnInfo(name = "oldman_uri")
    val uris: String = ""
)

@Entity(tableName = "oldman_info_check_had")
data class OldManInfoCheckHad(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "oldman_name")
    val name: String,
    @ColumnInfo(name = "oldman_sex")
    val sex: String,
    @ColumnInfo(name = "oldman_idCard")
    val idCard: String,
    @ColumnInfo(name = "oldman_uri")
    val uris: String = ""
)