package com.llj.living.logic.vm

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.llj.living.data.bean.LoginBean
import com.llj.living.data.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DatabaseVM(application: Application, savedStateHandle: SavedStateHandle) :
    BaseAdapterVM(application, savedStateHandle) {

    private var suppleDoingDao: SuppleDoingDao
    private var checkDoingDao: CheckDoingDao
    private var suppleFinishedDao: SuppleFinishedDao
    private var oldManInfoDao: OldManInfoDao
    private var checkHadDao: CheckHadDao

    private val _entBeanLiveDate = MutableLiveData<LoginBean>()
    val entBeanLiveDate: LiveData<LoginBean> = _entBeanLiveDate

    fun setEntBean(lb: LoginBean) {
        _entBeanLiveDate.postValue(lb)
    }

    init {
        val oldDatabase: OldManDatabase = OldManDatabase.newInstance(application.applicationContext)
        suppleDoingDao = oldDatabase.getSuppleDoingDao()
        suppleFinishedDao = oldDatabase.getSuppleFinishedDao()
        oldManInfoDao = oldDatabase.getOldmanInfoDao()
        checkDoingDao = oldDatabase.getCheckDoingDao()
        checkHadDao = oldDatabase.getCheckHadDao()
    }

    fun insertSuppleDoing(beanList: List<SuppleDoing>) =
        viewModelScope.launch(Dispatchers.IO) { suppleDoingDao.inserts(beanList) }

    fun insertSuppleFinished(beanList: List<SuppleFinished>) =
        viewModelScope.launch(Dispatchers.IO) { suppleFinishedDao.inserts(beanList) }

    fun insertOldmanInfo(beanList: List<OldManInfoWait>) =
        viewModelScope.launch(Dispatchers.IO) { oldManInfoDao.insertsWait(beanList) }

    fun getSuppleDoingListLD() = suppleDoingDao.getAll()

    fun getSuppleInfoItemListLD(id: Int) = suppleDoingDao.getOneById(id)

    fun getSuppleFinishedListLD() = suppleFinishedDao.getAll()
    fun getSuppleFinishedLD() = suppleFinishedDao.getAllLiveData()

    fun getOldManInfoLD() = oldManInfoDao.getWaitAll()
    fun getOldManInfoHadLD() = oldManInfoDao.getHadAll()

    fun getOldManInfoById(id: Int) = oldManInfoDao.queryOneWaitByIdLD(id)

    fun finishedOneInfo(bean: OldManInfoWait, suppleDoingId: Int) {
        oldManInfoDao.deleteWaitById(bean.id)
        oldManInfoDao.insertsHad(
            listOf(
                OldManInfoHad(
                    name = bean.name,
                    idCard = bean.idCard, sex = bean.sex
                )
            )
        )
        suppleDoingDao.updateCount(suppleDoingId)
    }

    fun getCheckDoingLD() = checkDoingDao.getAll()
    fun getCheckDoingItemById(id: Int) = checkDoingDao.queryOneWaitById(id)
    fun insertCheckDoing(list: List<CheckDoing>) = viewModelScope.launch(Dispatchers.IO) {
        checkDoingDao.inserts(list)
    }

    fun finishedOneHad(bean: OldManInfoWait, checkDoingId: Int) {
        oldManInfoDao.deleteWaitById(bean.id)
        checkHadDao.insertOne(
            OldManInfoCheckHad(
                name = bean.name,
                idCard = bean.idCard, sex = bean.sex
            )
        )
        checkDoingDao.updateCount(checkDoingId)
    }

    fun getCheckFinishedLD() = checkHadDao.getAll()




}