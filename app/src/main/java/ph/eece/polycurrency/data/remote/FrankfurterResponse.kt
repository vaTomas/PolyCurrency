package ph.eece.polycurrency.data.remote

// Response for: /v1/latest
// /v1/currencies returns a simple Map<String, String>, so no need for a custom class.
data class FrankfurterLatestResponse(
    val amount: Double,
    val base: String,
    val date: String,
    val rates: Map<String, Double> // Maps "USD" -> 0.017, "EUR" -> 0.015
)
