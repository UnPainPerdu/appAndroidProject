package be.heh.projetapphyb.util.db

import android.content.Context
import android.util.Log
import androidx.room.Room
import be.heh.exokotlin.db.MyDB
import be.heh.projetapphyb.db.User

class UserDbToolBox
{
    companion object
    {
        val defaultUser : User = User();
        /**
         * need to be call from coroutine
         */
        fun getUser(context: Context, mail: String): User
        {
            Log.i("PROJETAPPHYB", "getting user by mail")
            val db = Room.databaseBuilder(
                context,
                MyDB::class.java, "MyDataBase"
            ).build()
            Log.i("PROJETAPPHYB", "db intialized")
            val dao = db.userDao()
            Log.i("PROJETAPPHYB", "dao intialized")
            val dbL = dao.get(mail)
            Log.i("PROJETAPPHYB", "get mail trigger")
            val user = User(
                dbL?.userId ?: -1,
                dbL?.mail ?: "INDEFINI",
                dbL?.pswd ?: "INDEFINI",
                dbL?.has_privilege ?: false,
                dbL?.is_admin ?: false
            )
            Log.i("PROJETAPPHYB", "user got")
            return user
        }
        /**
         * need to be call from coroutine
         */

        fun getAllUser(context: Context): List<User>
        {
            Log.i("PROJETAPPHYB", "getting user by mail")
            val db = Room.databaseBuilder(
                context,
                MyDB::class.java, "MyDataBase"
            ).build()
            Log.i("PROJETAPPHYB", "db intialized")
            val dao = db.userDao()
            Log.i("PROJETAPPHYB", "dao intialized")
            val dbL = dao.get()
            Log.i("PROJETAPPHYB", "get mail trigger")

            var list : ArrayList<User> = ArrayList()
            for (userRecord in dbL)
            {
                var user = User(
                    userRecord?.userId ?: -1,
                    userRecord?.mail ?: "INDEFINI",
                    userRecord?.pswd ?: "INDEFINI",
                    userRecord?.has_privilege ?: false,
                    userRecord?.is_admin ?: false  )
                list.add(user)
            }
            Log.i("PROJETAPPHYB", "user list got")
            return list.toList()
        }
    }
}