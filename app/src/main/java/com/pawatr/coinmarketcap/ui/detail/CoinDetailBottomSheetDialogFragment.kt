package com.pawatr.coinmarketcap.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pawatr.coinmarketcap.data.model.CoinDetail
import com.pawatr.coinmarketcap.databinding.BottomSheetCoinDetailBinding
import com.pawatr.coinmarketcap.utils.setImageUrl
import com.pawatr.coinmarketcap.utils.toColor
import com.pawatr.coinmarketcap.utils.toCurrencyDecimalFormat
import com.pawatr.coinmarketcap.utils.toDisplayMarketCap

class CoinDetailBottomSheetDialogFragment(private val coinDetail: CoinDetail) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetCoinDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = BottomSheetCoinDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindData()
    }

    @SuppressLint("SetTextI18n")
    private fun bindData() {
        binding.apply {
            imageViewCoin.setImageUrl(coinDetail.iconUrl)
            tvCoinName.text = coinDetail.name
            tvCoinName.setTextColor(coinDetail.color.toColor())
            tvCoinSymbol.text = "(${coinDetail.symbol})"
            tvCoinPrice.text = "$ ${coinDetail.price.toCurrencyDecimalFormat()}"
            tvMarketCap.text = "$ ${coinDetail.marketCap.toDisplayMarketCap()}"
            tvDetail.text = coinDetail.detail
            textViewGoToWebsite.setOnClickListener {
                openWebsite(coinDetail.url)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openWebsite(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}