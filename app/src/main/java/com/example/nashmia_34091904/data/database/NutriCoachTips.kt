package com.example.nashmia_34091904.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nashmia_34091904.data.entities.NutriCoachTip

@Dao
interface NutriCoachTipDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTip(tip: NutriCoachTip)

    @Query("SELECT * FROM NutriCoachTips WHERE userId = :userId")
    fun getTipsForUser(userId: Int): LiveData<List<NutriCoachTip>>

}