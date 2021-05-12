package com.llj.living.data.pagesource

import com.llj.living.custom.ext.quickRequest
import com.llj.living.data.bean.EntInfoBean
import com.llj.living.net.repository.SystemRepository

class EntInfoDataSource : BaseDataSource<EntInfoBean>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, EntInfoBean> {
        return baseFunction(params.key,
            errTips
        ) { token, currentPage ->
            quickRequest<List<EntInfoBean>> {
                SystemRepository.getEntInfoRequest(token, currentPage)
            }
        }
    }

    companion object {
        private const val errTips = "新闻列表"
    }
}