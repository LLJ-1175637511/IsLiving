package com.llj.living.data.database

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface SuppleDoingDao {

    @Insert
    fun inserts(data: List<SuppleDoing>)

    @Query("DELETE FROM SUPPLE_DOING")
    fun deleteAll()

    @Query("SELECT * FROM SUPPLE_DOING ORDER BY id")
    fun getAll(): DataSource.Factory<Int, SuppleDoing>

    @Update
    fun updates(vararg bean: SuppleDoing) //可以设置返回值：Int 可以返回影响的条数

    @Query("UPDATE SUPPLE_DOING SET SUPPLE_WAITDEALWITH = SUPPLE_WAITDEALWITH-1,supple_hadDealWith = supple_hadDealWith+1 WHERE id = :id")
    fun updateCount(id: Int):Int

    @Query("SELECT * FROM SUPPLE_DOING WHERE ID = :queryId")
    fun getOneById(queryId: Int): LiveData<SuppleDoing>

}

@Dao
interface OldManInfoDao {

    @Insert
    fun insertsWait(data: List<OldManInfoWait>)

    @Query("SELECT * FROM OLDMAN_INFO_WAIT WHERE ID = :id")
    fun queryOneWaitByIdLD(id: Int): LiveData<OldManInfoWait>

    @Query("SELECT * FROM OLDMAN_INFO_WAIT ORDER BY id")
    fun getWaitAll(): DataSource.Factory<Int, OldManInfoWait>

    @Query("DELETE FROM OLDMAN_INFO_WAIT WHERE id = :id")
    fun deleteWaitById(id: Int)

    @Insert
    fun insertsHad(data: List<OldManInfoHad>)

    @Query("SELECT * FROM OLDMAN_INFO_HAD ORDER BY id")
    fun getHadAll(): DataSource.Factory<Int, OldManInfoHad>

    @Query("DELETE FROM OLDMAN_INFO_HAD WHERE id = :id")
    fun deleteHadById(id: Int)
}

@Dao
interface SuppleFinishedDao {

    @Insert
    fun inserts(student: List<SuppleFinished>)

    @Insert
    fun insertOne(student: SuppleFinished)

    @Query("DELETE FROM SUPPLE_FINISHED")
    fun deleteAll()

    @Query("SELECT * FROM SUPPLE_FINISHED ORDER BY id")
    fun getAll(): DataSource.Factory<Int, SuppleFinished>

    @Query("SELECT * FROM SUPPLE_FINISHED ORDER BY id")
    fun getAllLiveData(): LiveData<List<SuppleFinished>>

    @Query("DELETE FROM SUPPLE_FINISHED WHERE ID = :id")
    fun deleteOneById(id: Int): Int

    @Update
    fun updates(vararg bean: SuppleFinished) //可以设置返回值：Int 可以返回影响的条数

}

@Dao
interface CheckDoingDao {

    @Insert
    fun inserts(student: List<CheckDoing>)

    @Insert
    fun insertOne(student: CheckDoing)

    @Query("DELETE FROM CHECK_DOING")
    fun deleteAll()

    @Query("SELECT * FROM CHECK_DOING ORDER BY id")
    fun getAll(): DataSource.Factory<Int, CheckDoing>

    @Query("SELECT * FROM CHECK_DOING ORDER BY id")
    fun getAllLiveData(): LiveData<List<CheckDoing>>

    @Query("DELETE FROM CHECK_DOING WHERE ID = :id")
    fun deleteOneById(id: Int): Int

    @Query("SELECT * FROM CHECK_DOING WHERE ID = :id")
    fun queryOneWaitById(id: Int): LiveData<CheckDoing>

    @Query("UPDATE CHECK_DOING SET CHECK_WAITDEALWITH = check_waitDealWith-1,check_hadDealWith = check_hadDealWith+1 WHERE id = :id")
    fun updateCount(id: Int):Int

    @Update
    fun updates(vararg bean: SuppleFinished) //可以设置返回值：Int 可以返回影响的条数

}


@Dao
interface CheckHadDao {

    @Insert
    fun insertOne(student: OldManInfoCheckHad)

    @Query("SELECT * FROM OLDMAN_INFO_CHECK_HAD ORDER BY id")
    fun getAll(): DataSource.Factory<Int, OldManInfoCheckHad>

    @Query("SELECT * FROM OLDMAN_INFO_CHECK_HAD ORDER BY id")
    fun getAllLiveData(): LiveData<List<OldManInfoCheckHad>>

}