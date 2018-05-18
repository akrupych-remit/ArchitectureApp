package uk.co.xlntech.architectureapp.data.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "tips")
data class TipSummary(
        @PrimaryKey
        val id: String,
        val name: String,
        val description: String,
        val image: String,
        val category: String,
        val placeName: String,
        val profileName: String
)