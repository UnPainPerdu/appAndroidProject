package be.heh.projetapphyb

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import be.heh.projetapphyb.databinding.ActivityMainBinding
import be.heh.projetapphyb.util.ActivityTraveling.Companion.sentTo

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
            binding.btMain1.id ->{}
            binding.btMain2.id ->{sentTo("create_user", this)}
        }
    }
}