package be.heh.projetapphyb.util

import android.app.Activity
import android.os.Handler
import android.widget.Toast

class ToastMaker
{
    companion object
    {
        fun makeToastAsync(activity : Activity, text: String, length : Int)
        {
            val handler: Handler = Handler(activity.mainLooper)
            handler.post(Runnable {
                Toast.makeText(
                    activity,
                    text,
                    length
                ).show()
            })
        }

        fun makeErrorAsync(activity : Activity)
        {
            makeToastAsync(activity, "Erreur", Toast.LENGTH_LONG)
        }

        fun makeToast(activity : Activity, text: String, length : Int)
        {
            Toast.makeText(
                    activity,
                    text,
                    length
                ).show()
        }

        fun makeError(activity : Activity)
        {
            makeToast(activity, "Erreur", Toast.LENGTH_LONG)
        }
    }
}