package com.manish.cryptoprice.presentation.ui.activities

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.parseAsHtml
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jjoe64.graphview.GridLabelRenderer
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.manish.cryptoprice.R
import com.manish.cryptoprice.data.model.chart.GraphValues
import com.manish.cryptoprice.data.model.coinsList.CoinsListItem
import com.manish.cryptoprice.databinding.ActivityDetailsBinding
import com.manish.cryptoprice.presentation.ui.view_models.DetailsViewModel
import com.manish.cryptoprice.presentation.ui.view_models.DetailsViewModelFactory
import com.manish.cryptoprice.presentation.utils.Utility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject


@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding

    @Inject
    lateinit var detailsViewModelFactory: DetailsViewModelFactory
    lateinit var detailsViewModel: DetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        detailsViewModel =
            ViewModelProvider(this, detailsViewModelFactory)[DetailsViewModel::class.java]

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

        val type = object : TypeToken<CoinsListItem?>() {}.type
        val json = intent.getStringExtra("coin_list_item")
        val coin: CoinsListItem = Gson().fromJson(json, type)

        val buffer = StringBuilder()

        //Title
        buffer.apply {
            append(coin.name)
            append("(")
            append(coin.symbol.uppercase(Locale.ROOT))
            append(")")
        }
        binding.txtTitle.text = buffer.toString()

        //Current Price
        binding.txtCurrentPrice.text = coin.current_price.toString()

        //Price Gain
        buffer.setLength(0)
        buffer.apply {
            if (coin.price_change_24h > 0)
                append("+")
            append(Utility.formatPrice(coin.price_change_24h))
        }
        binding.txtPriceGain.text = buffer.toString()


        //Price Gain %
        if (coin.price_change_percentage_24h > 0)
            binding.txtPriceGainPercentage.setTextColor(getColor(R.color.gain_increased))
        else
            binding.txtPriceGainPercentage.setTextColor(getColor(R.color.gain_decreased))

        buffer.setLength(0)
        val df = DecimalFormat("#.##")
        buffer.apply {
            if (coin.price_change_percentage_24h > 0)
                append("+")
            append(df.format(coin.price_change_percentage_24h))
            append("%")
        }
        binding.txtPriceGainPercentage.text = buffer.toString()


        //High-Low
        binding.txtHighPrice.text = coin.high_24h.toString()
        binding.txtLowPrice.text = coin.low_24h.toString()

        //Details
        detailsViewModel.getCoinDetails(coin.id).observe(this) {
            Log.d("TAG", "onCreate: description downloaded ${coin.id}")
            binding.loadingBarDesc.visibility = View.GONE

            binding.txtDescription.text = it.description.en.parseAsHtml()
            binding.txtDescription.movementMethod = LinkMovementMethod.getInstance()
        }

        //Graph
        detailsViewModel.get24HoursPricesList(coin.id).observe(this) {
            Log.d("TAG", "setUpGraph: Loaded ${coin.id} -> ${it.prices.size}")
            setUpGraph(coin.price_change_24h > 0, it)
            binding.loadingBarGraph.visibility = View.GONE
            setDate()
        }
    }

    private fun setDate() {
        val sTime = System.currentTimeMillis()
        var time = System.currentTimeMillis()
        val dayMilli = 24 * 60 * 60 * 1000L
        time -= dayMilli

        val dateFormatter = SimpleDateFormat("dd MMM yyyy")

        var dateYesterday = dateFormatter.format(time)
        if (dateYesterday[0] == '0')
            dateYesterday = dateYesterday.substring(1)

        time -= 364L * dayMilli
        var dateLastYear = dateFormatter.format(time)
        if (dateLastYear[0] == '0')
            dateLastYear = dateLastYear.substring(1)

        val builder = StringBuilder().apply {
            append(dateLastYear)
            append(" - ")
            append(dateYesterday)
        }

        Log.d("TAG", "setDate: TimeTaken: ${System.currentTimeMillis() - sTime} ms")

        binding.txt1year.text = builder
        binding.txt1year.visibility = View.VISIBLE
    }

    private fun setUpGraph(isGreen: Boolean, graphValues: GraphValues) {
        binding.graphView.apply {
            visibility = View.VISIBLE
            removeAllSeries()
            gridLabelRenderer.isVerticalLabelsVisible = false
            gridLabelRenderer.isHorizontalLabelsVisible = false
            gridLabelRenderer.gridStyle = GridLabelRenderer.GridStyle.NONE
        }

        CoroutineScope(IO).launch {
            val seriesData = arrayOfNulls<DataPoint>(graphValues.prices.size)

            graphValues.prices.forEachIndexed { i, g ->
                seriesData[i] = DataPoint(i + 1.0, g[1])
            }

            val series = LineGraphSeries(seriesData)

            if (isGreen) series.color = getColor(R.color.gain_increased)
            else series.color = getColor(R.color.gain_decreased)

            withContext(Main) {
                binding.graphView.addSeries(series)
            }
        }

    }
}