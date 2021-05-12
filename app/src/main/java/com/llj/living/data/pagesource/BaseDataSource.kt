package com.llj.living.data.pagesource

import androidx.paging.PagingSource
import com.llj.living.custom.exception.TokenErrException
import com.llj.living.custom.ext.getSP
import com.llj.living.data.const.Const
import com.llj.living.utils.ContextUtils
import com.llj.living.utils.ToastUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseDataSource<T : Any> : PagingSource<Int, T>() {

    suspend inline fun baseFunction(
        paramsKey: Int?,
        errTips: String = "",
        crossinline block: suspend (String, Int) -> List<T>?
    ): LoadResult<Int, T> = try {
        val token =
            ContextUtils.getInstance().getSP(Const.SPUser).getString(Const.SPUserTokenLogin, "")
        if (token.isNullOrEmpty()) throw TokenErrException()
        //页码未定义置为1
        val currentPage = paramsKey ?: 1
        val beanList = block(token, currentPage)
        val nextPage = if (!beanList.isNullOrEmpty()) { //当前页码 小于 总页码 页面加1
            currentPage + 1
        } else {//没有更多数据
            null
        }
        beanList ?: throw Exception()
        LoadResult.Page(data = beanList, prevKey = null, nextKey = nextPage)
    } catch (e: Exception) {
        withContext(Dispatchers.Main) {
            ToastUtils.toastShort("${errTips}请求错误 请重试")
        }
        LoadResult.Error(e)
    }


    suspend inline fun <reified T : Any> pageTryException(
        errTips: String = "",
        crossinline block: suspend () -> PagingSource.LoadResult<Int, T>
    ): LoadResult<Int, T> {
        return try {
            block()
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                ToastUtils.toastShort("${errTips}请求错误 请重试")
            }
            LoadResult.Error(e)
        }
    }
}