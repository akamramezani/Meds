package com.example.medprotocols

import android.app.Application
import com.example.medprotocols.data.AppDatabase
import com.example.medprotocols.data.ContentImporter
import com.example.medprotocols.data.ProtocolRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MedProtocolsApp : Application() {
    lateinit var container: AppContainer
        private set

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
        // First-launch only: seed the two placeholder "sample" entries so the UI
        // isn't empty on first run. Importing real content (Settings) replaces them.
        appScope.launch {
            val hasData = container.database.categoryDao().observeAll().first().isNotEmpty()
            if (!hasData) {
                container.contentImporter.importFromAssets(this@MedProtocolsApp, "sample_protocols.json")
            }
        }
    }
}

class AppContainer(app: Application) {
    val database: AppDatabase = AppDatabase.getInstance(app)
    val repository: ProtocolRepository = ProtocolRepository(database.categoryDao(), database.protocolDao())
    val contentImporter: ContentImporter = ContentImporter(database.categoryDao(), database.protocolDao())
}
