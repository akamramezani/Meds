package com.example.medprotocols.ui.screens.settings

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.medprotocols.MedProtocolsApp
import com.example.medprotocols.R
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(app: MedProtocolsApp) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val importer = app.container.contentImporter

    val successMsg = stringResource(R.string.import_success)
    val failureMsg = stringResource(R.string.import_failure)

    val pickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        if (uri == null) return@rememberLauncherForActivityResult
        scope.launch {
            importer.importFromUri(context, uri)
                .onSuccess { (catCount, protoCount) ->
                    snackbarHostState.showSnackbar(successMsg.format(catCount, protoCount))
                }
                .onFailure {
                    snackbarHostState.showSnackbar(failureMsg)
                }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(stringResource(R.string.settings), style = MaterialTheme.typography.headlineSmall)

            Button(onClick = { pickerLauncher.launch(arrayOf("application/json", "text/*")) }) {
                Text(stringResource(R.string.import_content))
            }

            Text(
                stringResource(R.string.settings_no_content_notice),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
