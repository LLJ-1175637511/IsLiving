import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagedList
import androidx.paging.toLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.llj.living.R
import com.llj.living.data.bean.CheckFinishedBean
import com.llj.living.data.factory.CheckFinishedDSFactory
import com.llj.living.databinding.FragmentCheckFinishedBinding
import com.llj.living.ui.adapter.CheckFinishedAdapter
import com.llj.living.ui.fragment.NavBaseFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CheckFinishedFragment : NavBaseFragment<FragmentCheckFinishedBinding>() {

    override fun getLayoutId() = R.layout.fragment_check_finished

    lateinit var recyclerView: RecyclerView
    private val adapter by lazy { CheckFinishedAdapter() }
    private val factory = CheckFinishedDSFactory()
    private lateinit var refreshLayout:SwipeRefreshLayout
    private val liveData:LiveData<PagedList<CheckFinishedBean>> = factory.toLiveData(
        20,null
    )

    override fun init() {
        recyclerView = getBinding().recyclerviewCheckFinished
        recyclerView.adapter = adapter
        liveData.observe(this, Observer {
            adapter.submitList(it)
            refreshLayout.isRefreshing = false
        })
        refreshLayout = getBinding().refreshCheckFinished
        refreshLayout.apply {
            setColorSchemeResources(R.color.qq_blue) //设置显示颜色
            setOnRefreshListener {
                isRefreshing = true
                lifecycleScope.launch(Dispatchers.Default) {
                    delay(500)
                    //重置数据
                    refreshData()
                }
            }
        }
    }

    private fun refreshData(){
        liveData.value?.dataSource?.invalidate()
    }

}