package com.example.medprotocols.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class ProtocolRepository(
    private val categoryDao: CategoryDao,
    private val protocolDao: ProtocolDao
) {
    fun categories(): Flow<List<Category>> = categoryDao.observeAll()

    fun protocolsIn(categoryId: Long): Flow<List<Protocol>> = protocolDao.observeByCategory(categoryId)

    fun search(query: String): Flow<List<Protocol>> =
        if (query.isBlank()) flowOf(emptyList()) else protocolDao.searchRaw(query)

    suspend fun protocolDetail(protocolId: Long): ProtocolWithSections? {
        val protocol = protocolDao.getById(protocolId) ?: return null
        val sections = protocolDao.getSections(protocolId)
        return ProtocolWithSections(protocol, sections)
    }

    suspend fun categoryTitle(categoryId: Long): String = categoryDao.getById(categoryId)?.title ?: ""
}
