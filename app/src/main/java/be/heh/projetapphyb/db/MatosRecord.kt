package be.heh.exokotlin.db

import androidx.room.*
@Entity(tableName = "MatosTable")
data class MatosRecord(
    @ColumnInfo(name="matos_id") @PrimaryKey(autoGenerate = false) var matosId: Int=0,
    @ColumnInfo(name="type") var type : String,
    @ColumnInfo(name="name") var name: String,
    @ColumnInfo(name="link") var link: String,
    @ColumnInfo(name = "refNumber") var refNumber : String,
    @ColumnInfo(name="is_available") var is_available: Boolean
)