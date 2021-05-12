package com.llj.living.data.pagesource

import com.llj.living.custom.ext.quickRequest
import com.llj.living.data.bean.EntAddonsBean
import com.llj.living.data.bean.EntInfoBean
import com.llj.living.data.enums.TypeEnums
import com.llj.living.net.repository.SystemRepository

class EntAddonsDataSource(private val typeEnums: TypeEnums) : BaseDataSource<EntAddonsBean>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, EntAddonsBean> {
        return baseFunction(params.key, errTips) { token, currentPage ->
            quickRequest<List<EntAddonsBean>> {
                val requestType = when (typeEnums) {
                    TypeEnums.FINISHED -> 1
                    TypeEnums.DOING -> 2
                    TypeEnums.ALL -> 0
                }
                SystemRepository.getEntAddonsRequest(token, currentPage, requestType)
            }
        }
    }

    companion object {
        private const val errTips = "正在补录"
    }
}