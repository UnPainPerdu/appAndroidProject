package be.heh.projetapphyb

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import be.heh.projetapphyb.databinding.ActivityListUserBinding
import be.heh.projetapphyb.db.User
import be.heh.projetapphyb.util.JsonConvertor
import be.heh.projetapphyb.util.db.UserDbToolBox
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserListActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityListUserBinding
    private var user = UserDbToolBox.defaultUser
    private var userList : List<User> = ArrayList<User>().toList()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityListUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.user = JsonConvertor.fromJsonToUser(intent.getStringExtra("user").toString())
        lifecycleScope.launch(Dispatchers.IO) {
            this@UserListActivity.userList = UserDbToolBox.getAllUser(applicationContext);
            displayUserList(this@UserListActivity.userList)
        }
    }

    private suspend fun displayUserList(userList : List<User>)
    {
        Log.i("test", userList.toString())
    }

}