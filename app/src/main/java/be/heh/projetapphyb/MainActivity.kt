package be.heh.projetapphyb

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import be.heh.exokotlin.db.MyDB
import be.heh.projetapphyb.databinding.ActivityMainBinding
import be.heh.projetapphyb.db.User
import be.heh.projetapphyb.util.ActivityTraveling.Companion.sentTo
import be.heh.projetapphyb.util.HashMaker
import be.heh.projetapphyb.util.ToastMaker

class MainActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.i("PROJETAPPHYB", "MainActivity started")
    }

    fun onMainClickManager(view : View)
    {
        when(view.id)
        {
            binding.btMain1.id ->{ AsyncTask.execute({tryToLogin()})}
            binding.btMain2.id ->{sentTo("create_user", this)}
        }
    }

    private fun tryToLogin()
    {
        Log.i("PROJETAPPHYB", "Start try login")
        val mail : String = binding.etMain1.text.toString()
        val pswd : String= HashMaker.hashPswd(binding.etMain2.text.toString())
        val potentialMatchingUser : User = getUserByMail(mail)
        if (potentialMatchingUser.pswd == pswd)
        {
            Log.i("PROJETAPPHYB", "Connection success")
            ToastMaker.makeToastAsync(this, "Connection réussie", Toast.LENGTH_LONG)
        }
        else
        {
            Log.i("PROJETAPPHYB", "Connection fail")
            //binding.tvMain3.visibility = View.VISIBLE crash car pas main thread
            ToastMaker.makeErrorAsync(this)
        }
    }

    fun getUserByMail(mail : String): User
    {
        Log.i("PROJETAPPHYB","getting user by mail")
        val db = Room.databaseBuilder(
            applicationContext,
            MyDB::class.java, "MyDataBase"
        ).build()
        Log.i("PROJETAPPHYB","db intialized")
        val dao = db.userDao()
        Log.i("PROJETAPPHYB","dao intialized")
        val dbL = dao.get(mail)
        Log.i("PROJETAPPHYB","get mail trigger")
        val user = User(dbL?.userId?:-1, dbL?.mail?:"INDEFINI", dbL?.pswd?:"INDEFINI", dbL?.has_privilege?:false, dbL?.is_admin?:false)
        Log.i("PROJETAPPHYB","user got")
        return user
    }
}