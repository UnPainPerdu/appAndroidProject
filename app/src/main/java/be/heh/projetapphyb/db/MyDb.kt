package be.heh.exokotlin.db

import androidx.room.*
import be.heh.exokotlin.db.UserRecord
@Database(entities = [(UserRecord::class)], version = 1)
abstract class MyDB : RoomDatabase()
{
    abstract fun userDao(): UserDao
}