package com.llj.living.data.pagesource

import com.llj.living.custom.ext.quickRequest
import com.llj.living.data.bean.EntCheckBean
import com.llj.living.data.enums.TypeEnums
import com.llj.living.net.repository.SystemRepository
import com.llj.living.utils.LogUtils

class EntCheckDataSource(private val typeEnums: TypeEnums) : BaseDataSource<EntCheckBean>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, EntCheckBean> {
        return baseFunction(params.key, errTips) { token, currentPage ->
            quickRequest<List<EntCheckBean>> {
                val requestType = when (typeEnums) {
                    TypeEnums.FINISHED -> 1
                    TypeEnums.DOING -> 2
                    TypeEnums.ALL -> 0
                }
                LogUtils.d("EntCheckDataSource_TTT","currentPage:${currentPage}")
                SystemRepository.getEntCheckRequest(token, currentPage, requestType)
            }
        }
    }

    companion object {
        private const val errTips = "核查"
    }
}