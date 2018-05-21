package uk.co.xlntech.architectureapp.data.database

import android.arch.paging.DataSource
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import uk.co.xlntech.architectureapp.data.entities.TipSummary

@Dao
interface TipsDao {
    @Query("SELECT * FROM tips")
    fun getTips(): DataSource.Factory<Int, TipSummary>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTips(tips: List<TipSummary>)

    @Query("DELETE FROM tips")
    fun clearTips()
}