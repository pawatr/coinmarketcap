package com.pawatr.coinmarketcap.ui.home.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pawatr.coinmarketcap.R
import com.pawatr.coinmarketcap.data.model.Coin
import com.pawatr.coinmarketcap.data.model.CoinItem
import com.pawatr.coinmarketcap.databinding.ItemCoinHeaderBinding
import com.pawatr.coinmarketcap.databinding.ItemCoinListBinding
import com.pawatr.coinmarketcap.databinding.ItemInviteFriendBinding
import com.pawatr.coinmarketcap.utils.setImageUrl
import com.pawatr.coinmarketcap.utils.setSpanColor
import com.pawatr.coinmarketcap.utils.toCurrencyDecimalLongFormat

class CoinListAdapter(
    private val onItemClicked: (Coin) -> Unit
) : PagingDataAdapter<CoinItem, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_NORMAL = 1
        private const val VIEW_TYPE_INVITE_FRIEND = 2

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CoinItem>() {
            override fun areItemsTheSame(oldItem: CoinItem, newItem: CoinItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: CoinItem, newItem: CoinItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    var isSearch: Boolean = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is CoinItem.HeaderItem -> VIEW_TYPE_HEADER
            is CoinItem.CoinData -> VIEW_TYPE_NORMAL
            is CoinItem.InviteItem -> VIEW_TYPE_INVITE_FRIEND
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> CoinHeaderViewHolder(
                ItemCoinHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            VIEW_TYPE_NORMAL -> CoinViewHolder(
                ItemCoinListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ) {
                onItemClicked.invoke(it)
            }
            VIEW_TYPE_INVITE_FRIEND -> InviteFriendViewHolder(
                ItemInviteFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is CoinItem.HeaderItem -> {
                (holder as CoinHeaderViewHolder).bind(item.text)
            }
            is CoinItem.CoinData -> {
                (holder as CoinViewHolder).bind(item.coin)
            }
            is CoinItem.InviteItem -> {
                (holder as InviteFriendViewHolder).bind(item.text, item.description)
            }
            else -> {
                throw IllegalArgumentException("Invalid view type")
            }
        }
    }

    inner class CoinHeaderViewHolder(private var binding: ItemCoinHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(text: String) {
                binding.tvTitle.text = text
            }
        }

    inner class CoinViewHolder(private var binding: ItemCoinListBinding,private val onItemClicked: (Coin) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(coin: Coin) {
            binding.apply {
                val context = itemView.context
                coinItemImg.setImageUrl(coin.url)
                coinNameItemTv.text = coin.name
                coinSymbolItemTv.text = coin.symbol
                coinPriceItemTv.text = coin.price.toCurrencyDecimalLongFormat()
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

    inner class InviteFriendViewHolder(private var binding: ItemInviteFriendBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(text: String, description: String) {
            binding.apply {
                val context = itemView.context
                inviteTv.setSpanColor(text, description, context.getColor(R.color.dark_blue), true)
                itemView.setOnClickListener {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "text/plain"
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL")
                    intent.putExtra(Intent.EXTRA_TEXT, "https://careers.lmwn.com")
                    itemView.context.startActivity(Intent.createChooser(intent, "Share URL"))
                }
            }
        }
    }
}