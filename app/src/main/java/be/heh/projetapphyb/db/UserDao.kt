package be.heh.exokotlin.db

import androidx.room.*

@Dao
interface UserDao
{
    @Query("SELECT * FROM UserTable")
    fun get(): List<UserRecord>
    @Query("SELECT * FROM UserTable WHERE user_id = :userId")
    fun get(userId: Int): UserRecord
    @Query("SELECT * FROM UserTable WHERE mail = :mail")
    fun get(mail: String): UserRecord
    @Insert
    fun insertUser(vararg listCategories: UserRecord)
    @Update
    fun updateUser(task: UserRecord)
    @Delete
    fun deleteUser(task: UserRecord)
}