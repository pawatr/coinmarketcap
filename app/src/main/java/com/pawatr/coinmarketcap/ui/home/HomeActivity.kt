package com.pawatr.coinmarketcap.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.insertSeparators
import androidx.paging.map
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.pawatr.coinmarketcap.R
import com.pawatr.coinmarketcap.databinding.ActivityHomeBinding
import com.pawatr.coinmarketcap.data.model.Coin
import com.pawatr.coinmarketcap.data.model.CoinDetail
import com.pawatr.coinmarketcap.data.model.CoinItem
import com.pawatr.coinmarketcap.domain.model.Result
import com.pawatr.coinmarketcap.ui.detail.CoinDetailBottomSheetDialogFragment
import com.pawatr.coinmarketcap.ui.detail.CoinDetailViewModel
import com.pawatr.coinmarketcap.ui.home.adapter.CoinListAdapter
import com.pawatr.coinmarketcap.ui.home.adapter.CoinListLoadStateAdapter
import com.pawatr.coinmarketcap.ui.home.adapter.CoinTopRankAdapter
import com.pawatr.coinmarketcap.ui.home.adapter.HorizontalWrapperAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.UUID

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModel<HomeViewModel>()
    private val coinDetailViewModel: CoinDetailViewModel by viewModel<CoinDetailViewModel>()
    private lateinit var horizontalWrapperAdapter: HorizontalWrapperAdapter
    private lateinit var coinTopRankAdapter: CoinTopRankAdapter
    private lateinit var coinAdapter: CoinListAdapter
    private lateinit var concatAdapter: ConcatAdapter
    private lateinit var mergeAdapter: ConcatAdapter
    private var isSearch = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()
        setupEditText()
        setupSwipeRefresh()
        collectCoinList()
        coinDetailObserver()

        viewModel.refreshTopRankCoins() // default first time to get top 3 rank coins
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        horizontalWrapperAdapter.onSaveState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        horizontalWrapperAdapter.onRestoreState(savedInstanceState)
    }

    private fun setupRecyclerView() = with(binding) {
        coinTopRankAdapter = CoinTopRankAdapter { coin ->
            getCoinDetail(coin.uuid)
        }
        coinAdapter = CoinListAdapter { coin ->
            getCoinDetail(coin.uuid)
        }
        horizontalWrapperAdapter = HorizontalWrapperAdapter(coinTopRankAdapter)
        mergeAdapter = coinAdapter.withLoadStateFooter(
            footer = CoinListLoadStateAdapter { coinAdapter.retry() }
        )
        concatAdapter = ConcatAdapter(horizontalWrapperAdapter, mergeAdapter)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = concatAdapter
        }

        coinAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.NotLoading && coinAdapter.itemCount == 0) {
                binding.layoutEmpty.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            } else {
                binding.layoutEmpty.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            }
        }
    }

    private fun setupEditText() {
        binding.searchEditText.doOnTextChanged { text, _, _, _ ->
            onTextQuery(text.toString())
        }
    }

    private fun onTextQuery(text: String?) {
        if (text != null) {
            if (text.trim().isNotEmpty()) {
                isSearch = true
                viewModel.setSearchQuery(text.toString())
            } else {
                isSearch = false
                viewModel.setSearchQuery("")
            }
            coinAdapter.isSearch = isSearch
        }
    }

    private fun setupSwipeRefresh() = with(binding) {
        swipeRefresh.setOnRefreshListener {
            coinAdapter.refresh()
            viewModel.refreshTopRankCoins()
            swipeRefresh.isRefreshing = false
        }
    }

    @SuppressLint("CheckResult")
    private fun collectCoinList() {
        lifecycleScope.launch {
            viewModel.topRankCoins.collectLatest { pagingData ->
                coinTopRankAdapter.topRankCoins = pagingData
            }
        }
        lifecycleScope.launch {
            viewModel.coinList.collectLatest { pagingData ->
                concatAdapter = if (isSearch) {
                    ConcatAdapter(mergeAdapter)
                } else {
                    ConcatAdapter(horizontalWrapperAdapter, mergeAdapter)
                }
                binding.recyclerView.adapter = concatAdapter
                coinAdapter.submitData(mapCoinListData(pagingData))
            }
        }
    }

    private fun mapCoinListData(pagingData: PagingData<Coin>): PagingData<CoinItem> {
        var currentPosition = 0
        return pagingData
            .map { coin ->
                currentPosition++
                if (isInvitePosition(currentPosition)) {
                    CoinItem.InviteItem(getString(R.string.text_invite_friend), getString(R.string.text_invite_friend_bold))
                } else {
                    CoinItem.CoinData(coin)
                }
            }
            .insertSeparators { before, after ->
                if (before == null && after != null) {
                    CoinItem.HeaderItem(getString(R.string.coin_list_title))
                } else {
                    null
                }
            }
    }

    private fun isInvitePosition(position: Int): Boolean {
        if (position < 5) return false
        var currentPosition = 5
        while (currentPosition < position) {
            currentPosition *= 2
        }
        return currentPosition == position
    }

    private fun getCoinDetail(uuid: String) {
        coinDetailViewModel.getCoinDetail(uuid)
    }

    private fun coinDetailObserver() {
        coinDetailViewModel.coinDetail.observe(this) { result ->
            when(result) {
                is Result.Success -> {
                    showBottomSheet(result.data)
                }
                is Result.Error -> {
                    Snackbar.make(this, binding.root, result.exception.message.toString(), Snackbar.LENGTH_SHORT).show()
                }
                is Result.Loading -> {

                }
            }
        }
    }

    private fun showBottomSheet(coinDetail: CoinDetail) {
        val bottomSheetFragment = CoinDetailBottomSheetDialogFragment(coinDetail)
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
    }
}