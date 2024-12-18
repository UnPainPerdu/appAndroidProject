package be.heh.projetapphyb.util

import android.app.Activity
import android.content.Intent
import android.util.Log
import be.heh.projetapphyb.CreateMatosActivity
import be.heh.projetapphyb.CreateUserActivity
import be.heh.projetapphyb.MainActivity
import be.heh.projetapphyb.MatosListActivity
import be.heh.projetapphyb.MenuActivity
import be.heh.projetapphyb.UserListActivity
import be.heh.projetapphyb.UserModificationActivity
import be.heh.projetapphyb.db.Matos
import be.heh.projetapphyb.db.User

class ActivityTraveling
{
    companion object
    {
        const val MAIN = "main"
        const val CREATE_USER = "create_user"
        const val MENU = "menu"
        const val USER_LIST = "user_list"
        const val MATERIAL_LIST = "material_list"
        const val CREATE_MATERIAL = "create_material"
        fun sentTo(activityToSend: String, activity : Activity)
        {
            Log.i("PROJETAPPHYB", "Send to : $activityToSend")
            when(activityToSend)
            {
                MAIN -> {activity.startActivity(Intent(activity, MainActivity::class.java))}
                CREATE_USER -> {activity.startActivity(Intent(activity, CreateUserActivity::class.java))}
                else -> Log.i("PROJETAPPHYB-ERROR", "no where to send")
            }
        }

        /**
         * Send json String User
         * Never use it if identity of user is not verifie!!!
         */
        fun sentToWithUser(activityToSend: String, user : User, activity : Activity)
        {
            Log.i("PROJETAPPHYB", "Send to : $activityToSend")
            var i = Intent(activity, MainActivity::class.java)
            when(activityToSend)
            {
                MENU -> {i = Intent(activity, MenuActivity::class.java)}
                USER_LIST -> {i = Intent(activity, UserListActivity::class.java)}
                MATERIAL_LIST -> {i = Intent(activity, MatosListActivity::class.java)}
                CREATE_MATERIAL -> {i = Intent(activity, CreateMatosActivity::class.java)}
            }
            activity.startActivity(i.putExtra("user", JsonConvertor.fromUserToJson(user)))
        }

        fun sentToModifierUser(user : User, userModifier : User, activity : Activity)
        {
            Log.i("PROJETAPPHYB", "Send to : UserModification")
            var i = Intent(activity, UserModificationActivity::class.java)
            activity.startActivity(i.putExtra("user", JsonConvertor.fromUserToJson(user))
                .putExtra("userModifier", JsonConvertor.fromUserToJson(userModifier)))
        }

        fun sentToModifierMatos(user : User, matosModifier : Matos, activity : Activity)
        {
            Log.i("PROJETAPPHYB", "Send to : MatosModification")
            var i = Intent(activity, UserModificationActivity::class.java)
            activity.startActivity(i.putExtra("user", JsonConvertor.fromUserToJson(user))
                .putExtra("matosModifier", JsonConvertor.fromMatosToJson(matosModifier)))
        }
    }
}