package com.example.todoapp.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.todoapp.db.ImportanceEntity

@Entity(
    tableName = "todo",
    indices = [Index("id")],
    foreignKeys = [
        ForeignKey(
            entity = ImportanceEntity::class,
            parentColumns = ["id"],
            childColumns = ["importance_id"]
        )
    ]
)
data class TodoEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "importance_id") var importanceId: Int,
    var text: String,
    var deadline: Long?,
    var done: Boolean,
    val createdAt: Long,
    var changedAt: Long
)
