package ph.eece.polycurrency.ui.currency

data class CurrencyData(
    val code: String,
    val name: String,
    val country: String,
    val flagEmoji: String
)

// Temporary data for display TODO Integrate with DB
val worldCurrencies = listOf(
    CurrencyData("AFN", "Afghan Afghani", "Afghanistan", "🇦🇫"),
    CurrencyData("ALL", "Albanian Lek", "Albania", "🇦🇱"),
    CurrencyData("DZD", "Algerian Dinar", "Algeria", "🇩🇿"),
    CurrencyData("USD", "United States Dollar", "America (USA)", "🇺🇸"),
    CurrencyData("AUD", "Australian Dollar", "Australia", "🇦🇺"),
    CurrencyData("BHD", "Bahraini Dinar", "Bahrain", "🇧🇭"),
    CurrencyData("BSD", "Bahamian Dollar", "Bahamas", "🇧🇸"),
    CurrencyData("BRL", "Brazilian Real", "Brazil", "🇧🇷"),
    CurrencyData("CAD", "Canadian Dollar", "Canada", "🇨🇦"),
    CurrencyData("CNY", "Chinese Yuan", "China", "🇨🇳"),
    CurrencyData("EUR", "Euro", "European Union", "🇪🇺"),
    CurrencyData("GBP", "British Pound", "Great Britain", "🇬🇧"),
    CurrencyData("JPY", "Japanese Yen", "Japan", "🇯🇵"),
    CurrencyData("PHP", "Philippine Peso", "Philippines", "🇵🇭"),
    CurrencyData("VND", "Vietnamese Dong", "Vietnam", "🇻🇳")
).sortedBy { it.country } // Pre-sort by Country name