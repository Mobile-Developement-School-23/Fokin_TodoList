package com.example.todoapp.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "importance_levels")
data class ImportanceEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "importance_name") var importanceName: String
)
