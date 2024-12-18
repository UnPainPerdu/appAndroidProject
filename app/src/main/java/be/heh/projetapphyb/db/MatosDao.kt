package be.heh.exokotlin.db

import androidx.room.*

@Dao
interface MatosDao
{
    @Query("SELECT * FROM MatosTable")
    fun get(): List<MatosRecord>
    @Query("SELECT * FROM MatosTable WHERE matos_id = :matosId")
    fun get(matosId: Int): MatosRecord
    @Query("SELECT * FROM MatosTable WHERE refNumber = :refNumber")
    fun get(refNumber: String): MatosRecord
    @Query("SELECT * FROM MatosTable WHERE type = :type")
    fun getAllType(type: String): List<MatosRecord>
    @Insert
    fun insertMatos(vararg listCategories: MatosRecord)
    @Update
    fun updateMatos(task: MatosRecord)
    @Delete
    fun deleteMatos(task: MatosRecord)
}