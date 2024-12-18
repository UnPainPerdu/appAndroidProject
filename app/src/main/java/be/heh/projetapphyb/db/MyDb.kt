package be.heh.exokotlin.db

import androidx.room.*
@Database(entities = [(UserRecord::class), MatosRecord::class], version = 1)
abstract class MyDB : RoomDatabase()
{
    abstract fun userDao(): UserDao
    abstract fun matosDao() : MatosDao
}