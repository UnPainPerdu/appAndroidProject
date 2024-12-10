package be.heh.projetapphyb

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import be.heh.projetapphyb.databinding.ActivityMainBinding
import be.heh.projetapphyb.db.User
import be.heh.projetapphyb.util.ActivityTraveling
import be.heh.projetapphyb.util.ActivityTraveling.Companion.sentTo
import be.heh.projetapphyb.util.HashMaker
import be.heh.projetapphyb.util.ToastMaker
import be.heh.projetapphyb.util.db.UserDbToolBox
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityMainBinding
    private val onBackPressedCallBack : OnBackPressedCallback = object : OnBackPressedCallback(true)
    {
        override fun handleOnBackPressed()
        {
            Log.i("PROJETAPPHYB", "Je fonctionne")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallBack)
        Log.i("PROJETAPPHYB", "MainActivity started")
    }

    fun onMainClickManager(view : View)
    {
        when(view.id)
        {
            binding.btMain1.id ->
                { lifecycleScope.launch(Dispatchers.IO)
                    {
                        tryToLogin()
                    }
                }
            binding.btMain2.id ->{sentTo("create_user", this)}
        }
    }

    private suspend fun tryToLogin()
    {
        Log.i("PROJETAPPHYB", "Start try login")
        val mail : String = binding.etMain1.text.toString()
        val pswd : String= HashMaker.hashPswd(binding.etMain2.text.toString())
        val potentialMatchingUser : User = getUserByMail(mail)
        if (potentialMatchingUser.pswd == pswd)
        {
            Log.i("PROJETAPPHYB", "Connection success")
            ToastMaker.makeToastAsync(this@MainActivity, "Connection réussie", Toast.LENGTH_LONG)
            withContext(Dispatchers.Main)
            {
                binding.tvMain3.visibility = View.GONE
            }
            ActivityTraveling.sentToWithUser(ActivityTraveling.MENU, potentialMatchingUser, this@MainActivity)
        }
        else
        {
            Log.i("PROJETAPPHYB", "Connection fail")
            withContext(Dispatchers.Main)
            {
                binding.tvMain3.visibility = View.VISIBLE
            }
            ToastMaker.makeErrorAsync(this@MainActivity)
        }
    }

    fun getUserByMail(mail : String): User
    {
        return UserDbToolBox.getUser(applicationContext, mail)
    }
}