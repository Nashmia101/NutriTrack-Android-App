package com.example.nashmia_34091904.data.database
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.nashmia_34091904.data.entities.FoodIntake
import com.example.nashmia_34091904.data.entities.NutriCoachTip
import com.example.nashmia_34091904.data.entities.Patient

@Database(entities = [Patient::class, FoodIntake::class, NutriCoachTip::class], version = 3 )
abstract class AppDatabase : RoomDatabase() {
    abstract fun patientDao(): PatientDao
    abstract fun foodIntakeDao(): FoodIntakeDao
    abstract fun nutriCoachTipDao(): NutriCoachTipDao
    companion object {
        @Volatile private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}