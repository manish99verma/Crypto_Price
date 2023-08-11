package com.manish.cryptoprice.presentation.ui.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.GridLabelRenderer
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.manish.cryptoprice.R
import com.manish.cryptoprice.data.model.coinsList.CoinsListItem
import com.manish.cryptoprice.databinding.ItemCryptoBinding
import com.manish.cryptoprice.presentation.utils.Utility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DecimalFormat


class CryptoListAdapter() : RecyclerView.Adapter<CryptoListAdapter.MyViewHolder>() {
    private val mDiffer: AsyncListDiffer<CoinsListItem> =
        AsyncListDiffer(this, DIFF_CALLBACK)
    private var openDetailsListener: ((CoinsListItem) -> Unit?)? = null
    private lateinit var context: Context

    object DIFF_CALLBACK : DiffUtil.ItemCallback<CoinsListItem>() {
        override fun areItemsTheSame(oldItem: CoinsListItem, newItem: CoinsListItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CoinsListItem, newItem: CoinsListItem): Boolean {
            return oldItem == newItem
        }
    }

    fun setList(list: List<CoinsListItem>) {
        mDiffer.submitList(list)
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
                .apply(RequestOptions().override(75, 75))
                .into(binding.imgCoinIcon)


            //Graph
            setUpGraph(binding.idGraphView, coin.price_change_24h > 0, coin.sparkline_in_7d.price)

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

        private fun setUpGraph(
            graphView: GraphView,
            isGreen: Boolean,
            list: List<Double>
        ) {
            graphView.apply {
                removeAllSeries()
                gridLabelRenderer.isVerticalLabelsVisible = false
                gridLabelRenderer.isHorizontalLabelsVisible = false
                gridLabelRenderer.gridStyle = GridLabelRenderer.GridStyle.NONE
            }

            CoroutineScope(IO).launch {
                val seriesData = arrayOfNulls<DataPoint>(list.size)

                list.forEachIndexed { index, d ->
                    seriesData[index] = DataPoint(index + 1.0, d)
                }

                val series = LineGraphSeries(seriesData)

                if (isGreen) series.color = graphView.context.getColor(R.color.gain_increased)
                else series.color = graphView.context.getColor(R.color.gain_decreased)

                withContext(Main) {
                    graphView.addSeries(series)
                }
            }
        }
    }

    companion object {
        val df = DecimalFormat("#.##")
        val TAG: String = "CryptoListAdapter"
    }
}

