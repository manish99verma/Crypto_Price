package com.manish.cryptoprice.presentation.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.google.gson.Gson
import com.manish.cryptoprice.data.model.coinsList.CoinsList
import com.manish.cryptoprice.data.model.coinsList.CoinsListItem
import com.manish.cryptoprice.data.repository.SortBy
import com.manish.cryptoprice.databinding.ActivityMainBinding
import com.manish.cryptoprice.presentation.ui.adapters.CryptoListAdapter
import com.manish.cryptoprice.presentation.ui.view_models.MainViewModel
import com.manish.cryptoprice.presentation.ui.view_models.MainViewModelFactory
import com.manish.cryptoprice.presentation.utils.Utility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    //Root View
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var factory: MainViewModelFactory

    //Adapter
    @Inject
    lateinit var cryptoListAdapter: CryptoListAdapter
    private var firstSubmit = true

    //Sort Dialog
    private var currentSortedIdx = 0
    private lateinit var sortByDialogItems: List<String>
    private lateinit var sortByStringCode: Array<String>

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
                withContext(Main) { getCoinListItems() }
            }
        }

        //Sorting
        sortByDialogItems =
            listOf("Capitalization", "~Capitalization", "Volume", "~Volume")
        sortByStringCode =
            arrayOf(SortBy.MarketCapDesc, SortBy.MarketCapAsc, SortBy.VolumeDesc, SortBy.VolumeAsc)

        binding.btnApplyFilter.setOnClickListener {
            onClickSortBy()
        }

        getCoinListItems()
    }

    @SuppressLint("CheckResult")
    private fun onClickSortBy() {
        MaterialDialog(this).show {
            listItemsSingleChoice(
                items = sortByDialogItems,
                initialSelection = currentSortedIdx
            ) { _, index, text ->
                currentSortedIdx = index
                binding.txtSortByCurrent.text = text.toString()
                Log.d("Dialog", "onClickSortBy: $index/$text")

                binding.loadingBar.visibility = View.VISIBLE
                binding.rvMain.visibility = View.INVISIBLE
                getCoinListItems()
            }
        }
    }

    private fun getCoinListItems(){
        binding.rvMain.visibility = View.INVISIBLE
        binding.loadingBar.visibility = View.VISIBLE
        binding.swipeRefreshLayout.isRefreshing = false

        val sentTime = System.currentTimeMillis()
        viewModel.getCoinsList(sortByStringCode[currentSortedIdx]).observe(this) { response ->
            if (!response.isSuccessful) {
                Toast.makeText(this@MainActivity, response.msg, Toast.LENGTH_SHORT).show()
                return@observe
            }

            lifecycleScope.launch(IO) {
                if (System.currentTimeMillis() - sentTime < 300) {
                    delay(700)
                }

                withContext(Main) {
                    binding.apply {
                        loadingBar.visibility = View.INVISIBLE
                        swipeRefreshLayout.isRefreshing = false
                        rvMain.visibility = View.VISIBLE
                    }

                    setUpAdapter(response.data as CoinsList)
                }
            }
        }
    }

    private fun setUpAdapter(list: List<CoinsListItem>) {
        if (firstSubmit) {
            binding.rvMain.layoutManager = LinearLayoutManager(this@MainActivity)
            binding.rvMain.setHasFixedSize(true)
            binding.rvMain.adapter = cryptoListAdapter

            cryptoListAdapter.setOpenDetailsListener {
                val detailsIntent = Intent(this, DetailsActivity::class.java)
                Log.d("TAG", "Clicked: ${it.name}")
                detailsIntent.putExtra("coin_list_item", Gson().toJson(it))
                startActivity(detailsIntent)
            }
        }
        cryptoListAdapter.setList(list)
    }

}