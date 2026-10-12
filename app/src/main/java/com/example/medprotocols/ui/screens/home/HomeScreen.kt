package com.example.medprotocols.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.medprotocols.R
import com.example.medprotocols.data.Protocol
import com.example.medprotocols.data.ProtocolRepository
import com.example.medprotocols.ui.components.CategoryCard
import com.example.medprotocols.ui.components.ProtocolCard
import com.example.medprotocols.ui.components.SearchField

@Composable
fun HomeScreen(
    repository: ProtocolRepository,
    onCategoryClick: (Long) -> Unit,
    onProtocolClick: (Long) -> Unit
) {
    val categories by remember(repository) { repository.categories() }.collectAsState(initial = emptyList())
    var query by remember { mutableStateOf("") }
    val searchResults by produceState(initialValue = emptyList<Protocol>(), key1 = query, repository) {
        value = emptyList()
        repository.search(query).collect { value = it }
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text(stringResource(R.string.home_title), style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(12.dp))
        SearchField(value = query, onValueChange = { query = it }, placeholder = stringResource(R.string.search_hint))
        Spacer(Modifier.height(16.dp))

        when {
            query.isNotBlank() -> {
                if (searchResults.isEmpty()) {
                    Text(stringResource(R.string.no_protocols), style = MaterialTheme.typography.bodyMedium)
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(0.dp)) {
                        items(searchResults) { protocol ->
                            ProtocolCard(title = protocol.title, summary = protocol.summary, onClick = { onProtocolClick(protocol.id) })
                        }
                    }
                }
            }
            categories.isEmpty() -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        stringResource(R.string.no_protocols),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
            else -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(0.dp)) {
                    items(categories) { category ->
                        CategoryCard(title = category.title, colorHex = category.colorHex, onClick = { onCategoryClick(category.id) })
                    }
                }
            }
        }
    }
}
