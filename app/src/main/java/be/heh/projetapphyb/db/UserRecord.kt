package be.heh.exokotlin.db

import androidx.room.*
@Entity(tableName = "UserTable")
data class UserRecord(
    @ColumnInfo(name="user_id") @PrimaryKey(autoGenerate = false) var userId: Int=0,
    @ColumnInfo(name="mail") var mail : String,
    @ColumnInfo(name="pswd") var pswd: String,
    @ColumnInfo(name="has_privilege") var has_privilege: Boolean,
    @ColumnInfo(name="is_admin") var is_admin: Boolean
)