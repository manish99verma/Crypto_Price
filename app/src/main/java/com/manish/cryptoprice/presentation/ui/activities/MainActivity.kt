package com.manish.cryptoprice.presentation.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.google.gson.Gson
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.manish.cryptoprice.R
import com.manish.cryptoprice.data.model.coinsList.CoinsListItem
import com.manish.cryptoprice.databinding.ActivityMainBinding
import com.manish.cryptoprice.presentation.ui.adapters.CryptoListAdapter
import com.manish.cryptoprice.presentation.ui.view_models.MainViewModel
import com.manish.cryptoprice.presentation.ui.view_models.MainViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var factory: MainViewModelFactory

    @Inject
    lateinit var cryptoListAdapter: CryptoListAdapter
    private var firstSubmit = true

    private var currentSortedIdx = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*
                val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }

                val client: OkHttpClient = OkHttpClient.Builder().apply {
                    addInterceptor(interceptor)
                }.build()

                val retrofit = Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val api = retrofit.create(CoinGeckoService::class.java)

                val webDataSource = CoinsWebDataSourceImpl(api)
                val repository = CoinsRepositoryImpl(webDataSource)

                CoroutineScope(IO).launch {
                    repository.getCoinsList().collect {
                        Log.d("TAG", "onCreate: ${it.size}")
                    }
                }*/

        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        binding.swipeRefreshLayout.setOnRefreshListener {
            lifecycleScope.launch(IO) {
                delay(2000)
                withContext(Dispatchers.Main) { getCoinListItems() }
            }
        }

        getCoinListItems()

        //Sorting
        binding.btnApplyFilter.setOnClickListener {
            onClickSortBy()
        }

    }

    @SuppressLint("CheckResult")
    private fun onClickSortBy() {
        val myItems =
            listOf("Capitalization", "~Capitalization", "Volume", "~Volume", "Price", "~Price")

        MaterialDialog(this).show {
            listItemsSingleChoice(
                items = myItems,
                initialSelection = currentSortedIdx
            ) { _, index, text ->
                currentSortedIdx = index
                binding.txtSortByCurrent.text = text.toString()
                Log.d("Dialog", "onClickSortBy: $index/$text")
                Toast.makeText(
                    this@MainActivity,
                    "Server limit exceeded. Due to low budget :)",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun getCoinListItems() {
        binding.loadingBar.visibility = View.VISIBLE
        viewModel.getCoinsList().observe(this) {
            setUpAdapter(it)
            binding.loadingBar.visibility = View.INVISIBLE
        }
    }

    private fun setUpAdapter(list: List<CoinsListItem>) {
        if (firstSubmit) {
            binding.rvMain.layoutManager = LinearLayoutManager(this@MainActivity)
            binding.rvMain.setHasFixedSize(true)
            binding.rvMain.setItemViewCacheSize(0)
            binding.rvMain.adapter = cryptoListAdapter

            cryptoListAdapter.setGraphFunction { graphVeiw, isIncreasing, id ->
//                setUpGraph(graphVeiw, isIncreasing, id)
            }


            cryptoListAdapter.setOpenDetailsListener {
                val detailsIntent = Intent(this, DetailsActivity::class.java)
                Log.d("TAG", "Clicked: ${it.name}")
                detailsIntent.putExtra("coin_list_item", Gson().toJson(it))
                startActivity(detailsIntent)
            }
        }

        cryptoListAdapter.setList(list)
        binding.swipeRefreshLayout.isRefreshing = false
    }

    private fun setUpGraph(graphView: GraphView, isIncreasing: Boolean, id: String) {
//        Log.d("TAG", "setUpGraph: Loading $id ")

        viewModel.get24HoursPricesList(id).observe(this@MainActivity) {

            lifecycleScope.launch(IO) {
                val seriesData = arrayOfNulls<DataPoint>(it.prices.size)
//                Log.d("TAG", "setUpGraph: Loaded $id -> ${it.prices.size}")

                var i = 0
                it.prices.forEach { curr ->
                    seriesData[i++] = DataPoint(curr[0], curr[1])
                }

                val series = LineGraphSeries(seriesData)

                if (isIncreasing) series.color = getColor(R.color.gain_increased)
                else series.color = getColor(R.color.gain_decreased)

                withContext(Dispatchers.Main){
                    graphView.addSeries(series)
                }
            }
        }
    }

}