package ph.eece.polycurrency.ui.currency

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ph.eece.polycurrency.ui.calculator.CalculatorViewModel
import androidx.activity.compose.BackHandler
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import ph.eece.polycurrency.ui.calculator.CalculatorEvent

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CurrencySelectionScreen(
    onBack: () -> Unit,
    viewModel: CalculatorViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    // Local UI State
    var isSearchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    // For keyboard focus handling
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    // Filter Logic

    // No filter if search is empty
    val filteredList = remember(searchQuery, state.currencyRates) {
        state.currencyRates.filter {
            it.currencyCode.contains(searchQuery, ignoreCase = true) ||
                    it.currencyName.contains(searchQuery, ignoreCase = true)
        }.sortedBy { it.currencyCode } // Alphabetical sort
    }

    // Group filtered results by initial letter
    val groupedCurrencies = remember(filteredList) {
        filteredList.groupBy { it.currencyCode.first().uppercaseChar() }
    }



    // Back return

    // Phone back button
    BackHandler(enabled = isSearchActive) {
        isSearchActive = false
        searchQuery = ""
        focusManager.clearFocus()
    }

    Scaffold(
        topBar = {
            if (isSearchActive) {
                // Search Bar
                TopAppBar(
                    title = {
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search Country or Code...") },
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester)
                        )
                        // Auto-focus to search when search opened
                        LaunchedEffect(Unit) {
                            focusRequester.requestFocus()
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            // Close Search
                            isSearchActive = false
                            searchQuery = ""
                            focusManager.clearFocus()
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Close Search")
                        }
                    },
                    actions = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Close, "Clear Text")
                            }
                        }
                    }
                )
            } else {
                // Title Header
                TopAppBar(
                    title = { Text("Manage Currencies") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back to Calculator")
                        }
                    },
                    actions = {
                        IconButton(onClick = { isSearchActive = true }) {
                            Icon(Icons.Default.Search, "Open Search")
                        }
                    }
                )
            }
        }
    ) { innerPadding ->

        // Empty List View
        if (filteredList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Text(
                    text = if (state.currencyRates.isEmpty()) "Loading internet rates..." else "No currencies found.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                groupedCurrencies.forEach { (initial, currencies) ->
                    stickyHeader {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceContainer)
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = initial.toString(),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    items(currencies) { currency ->
                        // 3. Check State to see if it is enabled
                        val isSelected = state.activeCurrencies.contains(currency.currencyCode)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    // 4. Fire the clean Architectural Event
                                    viewModel.onEvent(CalculatorEvent.OnToggleActiveCurrency(currency.currencyCode))
                                }
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = isSelected,
                                onCheckedChange = {
                                    viewModel.onEvent(CalculatorEvent.OnToggleActiveCurrency(currency.currencyCode))
                                }
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = currency.currencyCode,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = currency.currencyName, // Using the API name
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            // 5. Apply the Senior Emoji Trick
                            Text(
                                text = getFlagEmojiForCurrency(currency.currencyCode),
                                fontSize = 32.sp
                            )
                        }
                        HorizontalDivider(modifier = Modifier.padding(start = 56.dp))
                    }
                }
            }
        }
    }
}


fun getFlagEmojiForCurrency(currencyCode: String): String {
    // Currencies like EUR or cryptos don't map perfectly to one country flag
    if (currencyCode == "EUR") return "🇪🇺"
    if (currencyCode.length < 2) return "🌐"

    // Grab the first two letters (e.g., "US" from "USD")
    val countryCode = currencyCode.substring(0, 2).uppercase()

    // Convert to Regional Indicator Symbols
    val firstLetter = Character.codePointAt(countryCode, 0) - 0x41 + 0x1F1E6
    val secondLetter = Character.codePointAt(countryCode, 1) - 0x41 + 0x1F1E6

    return try {
        String(Character.toChars(firstLetter)) + String(Character.toChars(secondLetter))
    } catch (e: Exception) {
        "🌐"
    }
}