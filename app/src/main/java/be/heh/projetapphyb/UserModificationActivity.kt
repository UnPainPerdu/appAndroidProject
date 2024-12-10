package be.heh.projetapphyb

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import be.heh.projetapphyb.databinding.ActivityUserModificationBinding
import be.heh.projetapphyb.util.JsonConvertor
import be.heh.projetapphyb.util.db.UserDbToolBox

class UserModificationActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityUserModificationBinding
    private var user = UserDbToolBox.defaultUser
    private var userModifier = user

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityUserModificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.user = JsonConvertor.fromJsonToUser(intent.getStringExtra("user").toString())
        this.userModifier = JsonConvertor.fromJsonToUser(intent.getStringExtra("userModifier").toString())
        initializeUserModifier()
        Log.i("PROJETAPPHYB", "UserModificationActivity started")
    }

    fun initializeUserModifier()
    {
        var tempTxt = binding.tvUsermodification1.text.toString() +  userModifier.mail
        binding.tvUsermodification1.text = tempTxt
        binding.etUsermodification1.hint = userModifier.mail
        tempTxt = binding.tvUsermodification4.text.toString() +  userModifier.hasPrivilege.toString()
        binding.tvUsermodification4.text = tempTxt
        tempTxt = binding.tvUsermodification5.text.toString() +  userModifier.isAdmin.toString()
        binding.tvUsermodification5.text = tempTxt
    }

    fun onMainClickManager(view : View)
    {
        when(view.id)
        {
            binding.btUsermodification1.id -> {}
            binding.btUsermodification2.id -> {}
            binding.btUsermodification3.id -> {}
        }
    }
}