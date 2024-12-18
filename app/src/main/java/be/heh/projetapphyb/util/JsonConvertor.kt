package be.heh.projetapphyb.util

import be.heh.projetapphyb.db.Matos
import be.heh.projetapphyb.db.User
import com.google.gson.Gson

class JsonConvertor
{
    companion object
    {
        private final val gson : Gson = Gson()

        fun fromJsonToUser(json : String) : User
        {
            return gson.fromJson(json, User::class.java)
        }

        fun fromUserToJson(user : User) : String
        {
            return gson.toJson(user).toString()
        }

        fun fromJsonToMatos(json : String) : Matos
        {
            return gson.fromJson(json, Matos::class.java)
        }

        fun fromMatosToJson(user : Matos) : String
        {
            return gson.toJson(user).toString()
        }
    }
}