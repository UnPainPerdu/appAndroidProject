package be.heh.projetapphyb

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import be.heh.projetapphyb.databinding.ActivityMenuBinding
import be.heh.projetapphyb.util.ActivityTraveling
import be.heh.projetapphyb.util.JsonConvertor
import be.heh.projetapphyb.util.ToastMaker
import be.heh.projetapphyb.util.db.UserDbToolBox

class MenuActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityMenuBinding
    private var user = UserDbToolBox.defaultUser

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.user = JsonConvertor.fromJsonToUser(intent.getStringExtra("user").toString())
    }

    fun onMainClickManager(view : View)
    {
        when(view.id)
        {
            binding.btMenu1.id ->{sentToUserList()}
            binding.btMenu2.id ->{sentToMaterialList()}
            binding.btMenu3.id ->{backToLogin()}
        }
    }

    private fun backToLogin()
    {
        binding.tvMenu1.visibility = View.GONE
        ActivityTraveling.sentTo(ActivityTraveling.MAIN, this)
    }

    private fun sentToUserList()
    {
        if (this.user.isAdmin)
        {
            binding.tvMenu1.visibility = View.GONE
            ActivityTraveling.sentToWithUser(ActivityTraveling.USER_LIST, this.user, this@MenuActivity)
        }

        else
        {
            binding.tvMenu1.visibility = View.VISIBLE
            ToastMaker.makeError(this)
        }
    }
    private fun sentToMaterialList()
    {
        binding.tvMenu1.visibility = View.GONE
        ActivityTraveling.sentToWithUser(ActivityTraveling.MATERIAL_LIST, this.user, this@MenuActivity)
    }
}