package com.example.medprotocols.data

import android.content.Context
import android.net.Uri
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class ImportFile(val categories: List<ImportCategory> = emptyList())

@Serializable
data class ImportCategory(
    val title: String,
    val colorHex: String = "#0F6E5E",
    val protocols: List<ImportProtocol> = emptyList()
)

@Serializable
data class ImportProtocol(
    val title: String,
    val summary: String = "",
    val sections: List<ImportSection> = emptyList()
)

@Serializable
data class ImportSection(
    val label: String,
    val content: String
)

/**
 * Reads a JSON file shaped like ImportFile and replaces whatever is currently
 * in the database with it. See README.md for the expected JSON shape.
 */
class ContentImporter(
    private val categoryDao: CategoryDao,
    private val protocolDao: ProtocolDao
) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun importFromText(text: String): Result<Pair<Int, Int>> = runCatching {
        val parsed = json.decodeFromString(ImportFile.serializer(), text)

        categoryDao.clear()
        protocolDao.clearSections()
        protocolDao.clearProtocols()

        var protocolCount = 0
        parsed.categories.forEachIndexed { catIndex, cat ->
            val categoryId = categoryDao.insertAll(
                listOf(Category(title = cat.title, colorHex = cat.colorHex, sortOrder = catIndex))
            ).first()

            cat.protocols.forEachIndexed { protoIndex, proto ->
                val protocolId = protocolDao.insertProtocols(
                    listOf(
                        Protocol(
                            categoryId = categoryId,
                            title = proto.title,
                            summary = proto.summary,
                            sortOrder = protoIndex
                        )
                    )
                ).first()
                protocolCount++

                if (proto.sections.isNotEmpty()) {
                    protocolDao.insertSections(
                        proto.sections.mapIndexed { secIndex, sec ->
                            ProtocolSection(
                                protocolId = protocolId,
                                label = sec.label,
                                content = sec.content,
                                sortOrder = secIndex
                            )
                        }
                    )
                }
            }
        }
        parsed.categories.size to protocolCount
    }

    suspend fun importFromAssets(context: Context, assetName: String): Result<Pair<Int, Int>> {
        val text = runCatching {
            context.assets.open(assetName).bufferedReader().use { it.readText() }
        }.getOrElse { return Result.failure(it) }
        return importFromText(text)
    }

    suspend fun importFromUri(context: Context, uri: Uri): Result<Pair<Int, Int>> {
        val text = runCatching {
            context.contentResolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() }
                ?: error("Could not open file")
        }.getOrElse { return Result.failure(it) }
        return importFromText(text)
    }
}
