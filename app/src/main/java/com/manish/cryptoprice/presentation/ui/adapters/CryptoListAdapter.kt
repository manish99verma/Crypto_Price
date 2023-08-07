package com.manish.cryptoprice.presentation.ui.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.GridLabelRenderer
import com.manish.cryptoprice.R
import com.manish.cryptoprice.data.model.coinsList.CoinsListItem
import com.manish.cryptoprice.databinding.ItemCryptoBinding
import com.manish.cryptoprice.presentation.utils.Utility
import java.text.DecimalFormat


class CryptoListAdapter() : RecyclerView.Adapter<CryptoListAdapter.MyViewHolder>() {
    private val mDiffer: AsyncListDiffer<CoinsListItem> =
        AsyncListDiffer(this, DIFF_CALLBACK)
    private var setGraph: ((GraphView, Boolean, String) -> Unit)? = null
    private var openDetailsListener: ((CoinsListItem) -> Unit?)? = null
    private lateinit var context: Context

    object DIFF_CALLBACK : DiffUtil.ItemCallback<CoinsListItem>() {
        override fun areItemsTheSame(oldItem: CoinsListItem, newItem: CoinsListItem): Boolean {
            return oldItem.market_cap_rank == newItem.market_cap_rank
        }

        override fun areContentsTheSame(oldItem: CoinsListItem, newItem: CoinsListItem): Boolean {
            return oldItem.current_price == newItem.current_price
        }
    }

    fun setList(list: List<CoinsListItem>) {
        mDiffer.submitList(list)
    }

    fun setGraphFunction(func: (GraphView, Boolean, String) -> Unit) {
        this.setGraph = func
    }

    fun setOpenDetailsListener(listener: (CoinsListItem) -> Unit?) {
        this.openDetailsListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        this.context = parent.context
        return MyViewHolder(
            ItemCryptoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return mDiffer.currentList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(mDiffer.currentList[position])
    }

    inner class MyViewHolder(private val binding: ItemCryptoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(coin: CoinsListItem) {
            binding.txtCoinName.text = coin.name
            binding.txtCoinSymbol.text = coin.symbol

            val buffer = StringBuilder().append("$").append(coin.current_price)
            binding.txtCurrentPrice.text = buffer.toString()

            //Price Change %
            if (coin.price_change_percentage_24h > 0)
                binding.txtGainPercent.setTextColor(context.getColor(R.color.gain_increased))
            else
                binding.txtGainPercent.setTextColor(context.getColor(R.color.gain_decreased))

            buffer.setLength(0)
            buffer.apply {
                if (coin.price_change_percentage_24h > 0)
                    append("+")
                append(df.format(coin.price_change_percentage_24h))
                append("%")
            }
            binding.txtGainPercent.text = buffer.toString()

            //Price Change
            buffer.setLength(0)
            buffer.apply {
                if (coin.price_change_24h > 0)
                    append("+")
                append(Utility.formatPrice(coin.price_change_24h))
            }
            binding.txtGainPrice.text = buffer.toString()


            //Image
            Glide.with(context)
                .load(coin.image)
                .placeholder(R.drawable.ic_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(RequestOptions().override(100, 100))
                .into(binding.imgCoinIcon)


            //Graph
            binding.idGraphView.apply {
                removeAllSeries()
                gridLabelRenderer.isVerticalLabelsVisible = false
                gridLabelRenderer.isHorizontalLabelsVisible = false
                gridLabelRenderer.gridStyle = GridLabelRenderer.GridStyle.NONE
            }

            setGraph?.let { it(binding.idGraphView, coin.price_change_24h > 0, coin.id) }


            //Click Listener
            binding.imgCoinIcon.setOnClickListener {
                binding.root.callOnClick()
            }
            binding.idGraphView.setOnClickListener {
                binding.root.callOnClick()
            }
            binding.root.setOnClickListener {
                openDetailsListener?.let { it(coin) }
            }
        }
    }

    companion object {
        val df = DecimalFormat("#.##")
        val TAG: String = "CryptoListAdapter"
    }
}

