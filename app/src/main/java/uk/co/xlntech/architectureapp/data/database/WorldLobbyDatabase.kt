package uk.co.xlntech.architectureapp.data.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import uk.co.xlntech.architectureapp.data.entities.TipSummary

@Database(entities = [TipSummary::class], version = 1)
abstract class WorldLobbyDatabase : RoomDatabase() {
    abstract fun tipsDao(): TipsDao
}