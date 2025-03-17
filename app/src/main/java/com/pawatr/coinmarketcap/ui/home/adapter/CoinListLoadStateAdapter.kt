package com.pawatr.coinmarketcap.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pawatr.coinmarketcap.databinding.ItemLoadingBinding

class CoinListLoadStateAdapter(
    private val onRetry: () -> Unit
) : LoadStateAdapter<CoinListLoadStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadStateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState, onRetry)
    }

    class LoadStateViewHolder(private val binding: ItemLoadingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(loadState: LoadState, onRetry: () -> Unit) = with(binding) {
            when (loadState) {
                is LoadState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                    layoutTryAgain.visibility = View.GONE
                }
                is LoadState.Error -> {
                    progressBar.visibility = View.GONE
                    layoutTryAgain.visibility = View.VISIBLE
                    layoutTryAgain.setOnClickListener { onRetry() }
                }
                is LoadState.NotLoading -> {
                    progressBar.visibility = View.GONE
                    layoutTryAgain.visibility = View.GONE
                }
            }
        }
    }
}