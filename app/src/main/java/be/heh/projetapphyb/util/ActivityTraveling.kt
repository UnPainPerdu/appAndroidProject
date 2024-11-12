package be.heh.projetapphyb.util

import android.app.Activity
import android.content.Intent
import android.util.Log
import be.heh.projetapphyb.CreateUserActivity
import be.heh.projetapphyb.MainActivity

class ActivityTraveling
{
    companion object
    {
        fun sentTo(activityToSend: String, activity : Activity)
        {
            Log.i("PROJETAPPHYB", "Send to : $activityToSend")
            when(activityToSend)
            {
                "main"-> {activity.startActivity(Intent(activity, MainActivity::class.java))}
                "create_user"-> {activity.startActivity(Intent(activity, CreateUserActivity::class.java))}
                else -> Log.i("PROJETAPPHYB-ERROR", "no where to send")
            }
        }
    }
}