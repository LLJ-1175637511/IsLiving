package com.llj.living.data.pagesource

import com.llj.living.custom.ext.quickRequest
import com.llj.living.data.bean.InfoByEntIdBean
import com.llj.living.data.enums.TypeEnums
import com.llj.living.net.repository.SystemRepository
import com.llj.living.utils.LogUtils

class AddonsByIdSource(private val typeEnums: TypeEnums, private val id: Int) :
    BaseDataSource<InfoByEntIdBean>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, InfoByEntIdBean> {
        return baseFunction(params.key, errTips) { token, currentPage ->
            quickRequest<List<InfoByEntIdBean>> {
                val requestType = when (typeEnums) {
                    TypeEnums.FINISHED -> 1
                    TypeEnums.DOING -> 2
                    TypeEnums.ALL -> 0
                }
                LogUtils.d("EntAddonsDataSource_AddonsByIdSource_TTT","currentPage:${currentPage}")
                SystemRepository.getEntAddonsByIdRequest(token, currentPage , requestType, id)
            }
        }
    }

    companion object {
        private const val errTips = "人员列表"
    }
}