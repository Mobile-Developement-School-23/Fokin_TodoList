package com.example.todoapp.data.db

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "revision",
    indices = [Index("id")]
)
data class RevisionEntity(
    @PrimaryKey val id: Int,
    var revision: Int
)
