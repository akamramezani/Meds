package com.example.medprotocols.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val colorHex: String = "#0F6E5E",
    val sortOrder: Int = 0
)

@Entity(
    tableName = "protocols",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("categoryId")]
)
data class Protocol(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val categoryId: Long,
    val title: String,
    val summary: String = "",
    val sortOrder: Int = 0
)

@Entity(
    tableName = "protocol_sections",
    foreignKeys = [
        ForeignKey(
            entity = Protocol::class,
            parentColumns = ["id"],
            childColumns = ["protocolId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("protocolId")]
)
data class ProtocolSection(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val protocolId: Long,
    val label: String,
    val content: String,
    val sortOrder: Int = 0
)

data class ProtocolWithSections(
    val protocol: Protocol,
    val sections: List<ProtocolSection>
)
