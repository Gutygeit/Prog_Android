package fr.uha.hassenforder.team.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import fr.uha.hassenforder.android.database.DatabaseTypeConverters
import fr.uha.hassenforder.team.model.Driver
import fr.uha.hassenforder.team.model.Shift
import fr.uha.hassenforder.team.model.ShiftDriverAssociation

@TypeConverters(DatabaseTypeConverters::class)
@Database(
    entities = [
        Driver::class,
        Shift::class,
        ShiftDriverAssociation::class
    ],
    version = 1,
    exportSchema = false
)

abstract class ShiftDatabase : RoomDatabase() {

    abstract fun driverDAO() : DriverDao

    abstract fun shiftDAO() : ShiftDao

    companion object {
        private lateinit var instance : ShiftDatabase

        fun create (context : Context) : ShiftDatabase {
            instance = Room.databaseBuilder(context, ShiftDatabase::class.java, "shift.db").build()
            return instance
        }

        fun get() : ShiftDatabase {
            return instance
        }

    }
}