package ph.eece.polycurrency.ui.currency

data class CurrencyData(
    val code: String,
    val name: String,
    val country: String,
    val flagEmoji: String,
    val rateToPHP: Double // Temporary
)

// Temporary data for display TODO Integrate with DB
val worldCurrencies = listOf(
    CurrencyData("AFN", "Afghan Afghani", "Afghanistan", "🇦🇫", 1.0),
    CurrencyData("ALL", "Albanian Lek", "Albania", "🇦🇱", 1.0),
    CurrencyData("DZD", "Algerian Dinar", "Algeria", "🇩🇿", 1.0),
    CurrencyData("USD", "United States Dollar", "America (USA)", "🇺🇸", 0.01696208398),
    CurrencyData("AUD", "Australian Dollar", "Australia", "🇦🇺", 1.0),
    CurrencyData("BHD", "Bahraini Dinar", "Bahrain", "🇧🇭", 1.0),
    CurrencyData("BSD", "Bahamian Dollar", "Bahamas", "🇧🇸", 1.0),
    CurrencyData("BRL", "Brazilian Real", "Brazil", "🇧🇷", 1.0),
    CurrencyData("CAD", "Canadian Dollar", "Canada", "🇨🇦", 1.0),
    CurrencyData("CNY", "Chinese Yuan", "China", "🇨🇳", 1.0),
    CurrencyData("EUR", "Euro", "European Union", "🇪🇺", 0.01438041755),
    CurrencyData("GBP", "British Pound", "Great Britain", "🇬🇧", 1.0),
    CurrencyData("JPY", "Japanese Yen", "Japan", "🇯🇵", 1.0),
    CurrencyData("PHP", "Philippine Peso", "Philippines", "🇵🇭", 1.0),
    CurrencyData("VND", "Vietnamese Dong", "Vietnam", "🇻🇳", 440.5286344)
).sortedBy { it.country } // Pre-sort by Country name


fun filterCurrencies(query: String, list: List<CurrencyData>): List<CurrencyData> {
    if (query.isBlank()) return list
    val cleanQuery = query.trim().lowercase()
    return list.filter {
        it.code.lowercase().contains(cleanQuery) ||
                it.name.lowercase().contains(cleanQuery)
    }
}
