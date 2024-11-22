package be.heh.projetapphyb.util

import android.app.Activity
import android.content.Intent
import android.util.Log
import be.heh.projetapphyb.CreateUserActivity
import be.heh.projetapphyb.MainActivity
import be.heh.projetapphyb.MenuActivity
import be.heh.projetapphyb.UserListActivity
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
                MATERIAL_LIST -> {i = Intent(activity, MenuActivity::class.java)}
            }
            activity.startActivity(i.putExtra("user", JsonConvertor.fromUserToJson(user)))
        }
    }
}