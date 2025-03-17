package com.pawatr.coinmarketcap.ui.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.pawatr.coinmarketcap.R
import com.pawatr.coinmarketcap.databinding.ItemCoinTopRankBinding
import com.pawatr.coinmarketcap.utils.setImageUrl
import com.pawatr.coinmarketcap.data.model.Coin

class CoinTopRankAdapter(
    private val onItemClicked: (Coin) -> Unit
) : RecyclerView.Adapter<CoinTopRankAdapter.TopRankViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    var topRankCoins: List<Coin> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int {
        return topRankCoins.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopRankViewHolder {
        return TopRankViewHolder(
            ItemCoinTopRankBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        ) {
            onItemClicked.invoke(it)
        }
    }

    override fun onBindViewHolder(holder: TopRankViewHolder, position: Int) {
        holder.bind(topRankCoins[position])
    }

    inner class TopRankViewHolder(private var binding: ItemCoinTopRankBinding,private val onItemClicked: (Coin) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(coin: Coin) {
            binding.apply {
                val context = itemView.context
                coinItemImg.setImageUrl(coin.url)
                coinNameItemTv.text = coin.name
                coinSymbolItemTv.text = coin.symbol
                if (coin.change.contains("-")){
                    coinChangeItemTv.setTextColor(context.getColor(R.color.red))
                    arrowChange.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_arrow_down))
                } else {
                    coinChangeItemTv.setTextColor(context.getColor(R.color.green))
                    arrowChange.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_arrow_up))
                }
                coinChangeItemTv.text = coin.change
                itemView.setOnClickListener {
                    onItemClicked.invoke(coin)
                }
            }
        }
    }
}