package com.llj.living.data.datasource

import androidx.collection.ArraySet
import androidx.paging.PageKeyedDataSource
import com.llj.living.application.MyApplication
import com.llj.living.data.bean.SecondFragmentBean
import com.llj.living.data.enums.NetStatus
import com.llj.living.logic.vm.ActSuppleViewModel
import com.llj.living.utils.LogUtils

class HadSuppleDataSource(
    private val viewModel: ActSuppleViewModel,
    private val arraySet: ArraySet<Int>
) :
    PageKeyedDataSource<Int, SecondFragmentBean>() {
    private var retry: (() -> Any)? = null
    private var count = 0

    fun retryLoadData() {
        retry?.invoke()
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, SecondFragmentBean>
    ) {
        retry = null
        viewModel.updateHadSuppleNetStatus(NetStatus.LOADING)
        LogUtils.d("CheckFinishedDataSource", "loadInitial key:${params.requestedLoadSize}")
//        val firstList = mutableListOf<SecondFragmentBean>()

        /*   for (i in 0..6) {
               if (arraySet.contains(i))
                   firstList.add(
                       SecondFragmentBean(
                           uName = name[i],
                           sex = sex[i],
                           id = i,
                           idCard = idCard[i]
                       )
                   )
           }*/
        MyApplication.suppleHadList.value?.let {
            callback.onResult(it, 0, 1)
        }
        viewModel.updateHadSuppleNetStatus(NetStatus.SUCCESS)
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, SecondFragmentBean>
    ) {
        return
        retry = null
        viewModel.updateHadSuppleNetStatus(NetStatus.LOADING)
        LogUtils.d(
            "CheckFinishedDataSource",
            "loadAfter key:${params.key} request:${params.requestedLoadSize}"
        )
        val firstList = mutableListOf<SecondFragmentBean>()

        for (i in 7..11) {
            if (arraySet.contains(i)) firstList.add(
                SecondFragmentBean(
                    uName = name[i],
                    sex = sex[i],
                    id = i,
                    idCard = idCard[i]
                )
            )
        }

        if (count < 3) {
            count++
            viewModel.updateHadSuppleNetStatus(NetStatus.SUCCESS)
            callback.onResult(firstList, params.key + 1)
        } else {
            viewModel.updateHadSuppleNetStatus(NetStatus.FAILED)
            retry = { loadAfter(params, callback) }
            count = 0
        }
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, SecondFragmentBean>
    ) {
    }

    val name = arrayOf<String>(
        "寿百易",
        "申丹洁",
        "李惜天",
        "熊问兰",
        "蒋今瑶",
        "车怀玉",
        "衡玉寒",
        "红丰雅",
        "扶雅博",
        "步松雨",
        "冉洁",
        "俞卿腾",
        "潘贝",
        "崔达",
        "补咏仪",
        "马若",
        "向义",
        "吉宝",
        "候贞",
        "孟芳",
        "赵枫",
        "耿文"
    )
    val sex = arrayOf<String>(
        "男",
        "女",
        "女",
        "女",
        "女",
        "女",
        "男",
        "女",
        "男",
        "男",
        "女",
        "男",
        "男",
        "男",
        "女",
        "女",
        "男",
        "女",
        "女",
        "女",
        "男",
        "男"
    )
    val idCard = arrayOf<String>(
        "522324196506251622",
        "522622195407155028",
        "522129196211071028",
        "522129196811251065",
        "522129196911111018",
        "220102195403072466",
        "220102195403070120",
        "11010119600307600X",
        "110101195903075226",
        "130102196003079032",
        "372324196702013754",
        "422802195906186030",
        "610124195805231422",
        "610526194704214033",
        "320981195602094007",
        "362202195307172328",
        "421127195111191571",
        "420881195504282111",
        "130532196207070027",
        "130921196107010817",
        "130921196010170181",
        "511028195809295317"
    )
}