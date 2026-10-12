package com.example.medprotocols.ui.screens.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.medprotocols.data.ProtocolRepository
import com.example.medprotocols.data.ProtocolWithSections

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProtocolDetailScreen(
    protocolId: Long,
    repository: ProtocolRepository,
    onBack: () -> Unit
) {
    var detail by remember { mutableStateOf<ProtocolWithSections?>(null) }
    LaunchedEffect(protocolId) { detail = repository.protocolDetail(protocolId) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(detail?.protocol?.title ?: "") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        val sections = detail?.sections.orEmpty()
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(sections) { section ->
                Column {
                    Text(section.label, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.height(4.dp))
                    Text(section.content, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
