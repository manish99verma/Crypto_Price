package com.manish.cryptoprice.presentation.ui.activities

import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.parseAsHtml
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.manish.cryptoprice.R
import com.manish.cryptoprice.data.model.coinsList.CoinsListItem
import com.manish.cryptoprice.databinding.ActivityDetailsBinding
import com.manish.cryptoprice.presentation.ui.view_models.DetailsViewModel
import com.manish.cryptoprice.presentation.ui.view_models.DetailsViewModelFactory
import com.manish.cryptoprice.presentation.utils.Utility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DecimalFormat
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details)

        detailsViewModel =
            ViewModelProvider(this, detailsViewModelFactory)[DetailsViewModel::class.java]

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
            lifecycleScope.launch(Dispatchers.IO) {
                val seriesData = arrayOfNulls<DataPoint>(it.prices.size)
                Log.d("TAG", "setUpGraph: Loaded ${coin.id} -> ${it.prices.size}")

                var i = 0
                it.prices.forEach { curr ->
                    seriesData[i++] = DataPoint(curr[0], curr[1])
                }

                val series = LineGraphSeries(seriesData)

                if (coin.price_change_24h > 0) series.color = getColor(R.color.gain_increased)
                else series.color = getColor(R.color.gain_decreased)

                withContext(Dispatchers.Main) {
                    binding.graphView.addSeries(series)
                }
            }
        }
    }
}