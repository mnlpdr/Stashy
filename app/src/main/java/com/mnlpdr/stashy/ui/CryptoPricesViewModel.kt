package com.mnlpdr.stashy.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mnlpdr.stashy.data.CoinCapApiService
import com.mnlpdr.stashy.data.CoinGeckoApiService
import com.mnlpdr.stashy.data.CryptoPrice
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import coil.ImageLoader
import coil.request.ImageRequest
import com.mnlpdr.stashy.R

class CryptoPricesViewModel(apiKey: String) : ViewModel() {

    // Lista de moedas que o usuário possui
    private val _userHoldings = mutableListOf<CryptoPrice>()
    val userHoldings: List<CryptoPrice> get() = _userHoldings

    // Método para adicionar uma moeda ao portfólio do usuário
    fun addHolding(cryptoPrice: CryptoPrice) {
        _userHoldings.add(cryptoPrice)
    }

    // Método para calcular o saldo total em USD
    fun calculateTotalBalanceInUSD(): Double {
        return _userHoldings.sumOf { it.quantity * it.currentPrice }
    }

    // Método para buscar preços de criptomoedas (exemplo simplificado)
    fun fetchCryptoPrices() {
        viewModelScope.launch {
            // Lógica para buscar preços de criptomoedas e atualizar userHoldings
            // Exemplo simplificado:
            val fetchedPrices = listOf(
                CryptoPrice("Bitcoin", "BTC", 2.0, 30000.0, iconResId = getIconResId("BTC") ), // Exemplo de dados
                CryptoPrice("Ethereum", "ETH", 5.0, 2000.0, iconResId = getIconResId("ETH"))  // Exemplo de dados
            )
            _userHoldings.clear()
            _userHoldings.addAll(fetchedPrices)
        }
    }

    private val _prices = MutableStateFlow<List<CryptoPrice>>(emptyList())
    val prices: StateFlow<List<CryptoPrice>> = _prices

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private val apiService = CoinCapApiService.create(apiKey)
//    private val geckoApiService = CoinGeckoApiService.create()

    init {
        fetchCryptoPricesFromAPI()
    }

    private fun fetchCryptoPricesFromAPI() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                val response = apiService.getCryptoPrices()
                val cryptoPrices = response.data.map { data ->
                    CryptoPrice(
                        coinTicker = data.symbol.uppercase(),
                        cryptoName = data.name,
                        quantity = 0.0,
                        currentPrice = data.priceUsd.toDouble(),
                        iconResId = getIconResId(data.symbol.uppercase()) // Mapeia o ícone
                    )
                }
                _prices.value = cryptoPrices
            } catch (e: Exception) {
                // Lida com o erro
            } finally {
                _isRefreshing.value = false
            }
        }
    }


    val coinIconMap = mapOf(
        "AAVE" to R.drawable.aave,
        "ADA" to R.drawable.ada,
        "ALGO" to R.drawable.algo,
        "APT" to R.drawable.apt,
        "AR" to R.drawable.ar,
        "ARB" to R.drawable.arb,
        "ATOM" to R.drawable.atom,
        "AVAX" to R.drawable.avax,
//    "AXS" to R.drawable.axs,
        "BCH" to R.drawable.bch,
        "BEAM" to R.drawable.beam,
        "BGB" to R.drawable.bgb,
        "BNB" to R.drawable.bnb,
        "BONK" to R.drawable.bonk,
        "BRETT" to R.drawable.brett,
        "BSV" to R.drawable.bsv,
        "BTC" to R.drawable.btc,
        "BTT" to R.drawable.btt,
        "CORE" to R.drawable.core,
        "CRO" to R.drawable.cro,
        "DAI" to R.drawable.dai,
        "DOGE" to R.drawable.doge,
        "DOT" to R.drawable.dot,
        "EETH" to R.drawable.eeth,
        "EGLD" to R.drawable.egld,
        "EOS" to R.drawable.eos,
        "ETC" to R.drawable.etc,
        "ETH" to R.drawable.eth,
        "EZETH" to R.drawable.ezeth,
        "FDUSD" to R.drawable.fdusd,
        "FET" to R.drawable.fet,
        "FIL" to R.drawable.fil,
        "FLOKI" to R.drawable.floki,
        "FLOW" to R.drawable.flow,
        "FLR" to R.drawable.flr,
        "FTM" to R.drawable.ftm,
        "FTN" to R.drawable.ftn,
        "GALA" to R.drawable.gala,
        "GRT" to R.drawable.grt,
        "GT" to R.drawable.gt,
        "HBAR" to R.drawable.hbar,
        "HNT" to R.drawable.hnt,
        "ICP" to R.drawable.icp,
        "IMX" to R.drawable.imx,
        "INJ" to R.drawable.inj,
        "JASMY" to R.drawable.jasmy,
        "JUP" to R.drawable.jup,
        "KAS" to R.drawable.kas,
        "KCS" to R.drawable.kcs,
        "KLAY" to R.drawable.klay,
        "LDO" to R.drawable.ldo,
        "LEO" to R.drawable.leo,
        "LINK" to R.drawable.link,
        "LTC" to R.drawable.ltc,
        "MATIC" to R.drawable.matic,
        "METH" to R.drawable.meth,
        "MKR" to R.drawable.mkr,
        "MNT" to R.drawable.mnt,
        "NEAR" to R.drawable.near,
        "NOT" to R.drawable.not,
        "OKB" to R.drawable.okb,
        "OM" to R.drawable.om,
        "ONDO" to R.drawable.ondo,
        "OP" to R.drawable.op,
        "PEPE" to R.drawable.pepe,
        "PYTH" to R.drawable.pyth,
        "PYUSD" to R.drawable.pyusd,
        "QNT" to R.drawable.qnt,
        "RENDER" to R.drawable.render,
        "RETH" to R.drawable.reth,
        "RUNE" to R.drawable.rune,
        "SEI" to R.drawable.sei,
        "SHIB" to R.drawable.shib,
        "SOL" to R.drawable.sol,
        "SOLVBTC" to R.drawable.solvbtc,
        "STETH" to R.drawable.steth,
        "STX" to R.drawable.stx,
        "SUI" to R.drawable.sui,
        "TAO" to R.drawable.tao,
        "THETA" to R.drawable.theta,
        "TIA" to R.drawable.tia,
        "TON" to R.drawable.ton,
        "TRX" to R.drawable.trx,
        "UNI" to R.drawable.uni,
        "USDC" to R.drawable.usdc,
        "USDD" to R.drawable.usdd,
        "USDE" to R.drawable.usde,
        "USDT" to R.drawable.usdt,
        "VET" to R.drawable.vet,
        "WBT" to R.drawable.wbt,
        "WBTC" to R.drawable.wbtc,
        "WEETH" to R.drawable.weeth,
        "WETH" to R.drawable.weth,
        "WIF" to R.drawable.wif,
        "WSTETH" to R.drawable.wsteth,
        "XLM" to R.drawable.xlm,
        "XMR" to R.drawable.xmr,
        "XRP" to R.drawable.xrp
    )

    fun getIconResId(coinTicker: String): Int {
        return coinIconMap[coinTicker] ?: R.drawable.crypto_placeholder
    }



    fun refreshPrices(){
        fetchCryptoPricesFromAPI()
    }

}