package com.pawatr.coinmarketcap.ui.home.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pawatr.coinmarketcap.R
import com.pawatr.coinmarketcap.databinding.ItemHorizontalWrapperBinding
import com.pawatr.coinmarketcap.utils.setSpanColor

class HorizontalWrapperAdapter(
    private val adapter: CoinTopRankAdapter
) : RecyclerView.Adapter<HorizontalWrapperAdapter.HorizontalWrapperViewHolder>() {
    private var lastScrollX = 0

    companion object {
        private const val KEY_SCROLL_X = "horizontal.wrapper.adapter.key_scroll_x"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalWrapperViewHolder {
        return HorizontalWrapperViewHolder(
            ItemHorizontalWrapperBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: HorizontalWrapperViewHolder, position: Int) {
        holder.bind(adapter, lastScrollX) { x ->
            lastScrollX = x
        }
    }

    override fun getItemCount(): Int = 1

    fun onSaveState(outState: Bundle) {
        outState.putInt(KEY_SCROLL_X, lastScrollX)
    }

    fun onRestoreState(savedState: Bundle) {
        lastScrollX = savedState.getInt(KEY_SCROLL_X)
    }

    inner class HorizontalWrapperViewHolder(
        private val binding: ItemHorizontalWrapperBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(adapter: CoinTopRankAdapter, lastScrollX: Int, onScrolled: (Int) -> Unit) {
            val context = binding.root.context
            binding.tvTitle.setSpanColor(
                context.getString(R.string.coin_top_rank_title),
                context.getString(R.string.coin_top_rank_title_bold),
                context.getColor(R.color.red), true
            )
            binding.recyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerView.adapter = adapter
            binding.recyclerView.doOnPreDraw {
                binding.recyclerView.scrollBy(lastScrollX, 0)
            }
            binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    onScrolled(recyclerView.computeHorizontalScrollOffset())
                }
            })
        }
    }
}