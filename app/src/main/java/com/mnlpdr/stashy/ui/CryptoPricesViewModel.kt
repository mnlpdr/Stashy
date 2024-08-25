package com.mnlpdr.stashy.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mnlpdr.stashy.data.CoinCapApiService
import com.mnlpdr.stashy.data.CoinGeckoApiService
import com.mnlpdr.stashy.data.CryptoPrice
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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
                CryptoPrice("Bitcoin", "BTC", 2.0, 30000.0), // Exemplo de dados
                CryptoPrice("Ethereum", "ETH", 5.0, 2000.0)  // Exemplo de dados
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
    private val geckoApiService = CoinGeckoApiService.create()

    init {
        fetchCryptoPricesFromAPI()
    }

    private fun fetchCryptoPricesFromAPI() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                val response = apiService.getCryptoPrices()
                val cryptoPrices = response.data.map { data ->
                    // Cria o objeto CryptoPrice sem o ícone inicialmente
                    CryptoPrice(
                        coinTicker = data.symbol.uppercase(),
                        cryptoName = data.name,
                        quantity = 0.0,
                        currentPrice = data.priceUsd.toDouble(),
                        iconUrl = null  // O ícone será preenchido posteriormente
                    )
                }

                // Agora buscamos os ícones de cada cripto
                val updatedCryptoPrices = cryptoPrices.map { crypto ->
                    try {
                        val geckoResponse = geckoApiService.getCoinDetails(crypto.cryptoName.lowercase())
                        crypto.copy(iconUrl = geckoResponse.image.large)  // Atualiza com o ícone
                    } catch (e: Exception) {
                        crypto // Se falhar, retorna o objeto sem o ícone
                    }
                }

                _prices.value = updatedCryptoPrices
            } catch (e: Exception) {
                // Aqui você pode lidar com o erro, como exibir uma mensagem de erro
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    fun refreshPrices() {
        fetchCryptoPricesFromAPI()
    }
}
