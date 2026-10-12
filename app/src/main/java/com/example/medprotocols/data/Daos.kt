package com.example.medprotocols.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories ORDER BY sortOrder, title")
    fun observeAll(): Flow<List<Category>>

    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getById(id: Long): Category?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<Category>): List<Long>

    @Query("DELETE FROM categories")
    suspend fun clear()
}

@Dao
interface ProtocolDao {
    @Query("SELECT * FROM protocols WHERE categoryId = :categoryId ORDER BY sortOrder, title")
    fun observeByCategory(categoryId: Long): Flow<List<Protocol>>

    @Query("SELECT * FROM protocols WHERE id = :id")
    suspend fun getById(id: Long): Protocol?

    @Query("SELECT * FROM protocol_sections WHERE protocolId = :protocolId ORDER BY sortOrder")
    suspend fun getSections(protocolId: Long): List<ProtocolSection>

    @Query("SELECT * FROM protocols WHERE title LIKE '%' || :query || '%' ORDER BY title")
    fun searchRaw(query: String): Flow<List<Protocol>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProtocols(protocols: List<Protocol>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSections(sections: List<ProtocolSection>)

    @Query("DELETE FROM protocols")
    suspend fun clearProtocols()

    @Query("DELETE FROM protocol_sections")
    suspend fun clearSections()
}
