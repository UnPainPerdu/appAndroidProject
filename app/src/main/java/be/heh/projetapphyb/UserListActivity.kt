package be.heh.projetapphyb

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import be.heh.projetapphyb.databinding.ActivityListUserBinding
import be.heh.projetapphyb.db.User
import be.heh.projetapphyb.help_view.CustomDynamicList
import be.heh.projetapphyb.util.ActivityTraveling
import be.heh.projetapphyb.util.JsonConvertor
import be.heh.projetapphyb.util.db.UserDbToolBox
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        lifecycleScope.launch(Dispatchers.IO)
        {
            this@UserListActivity.userList = UserDbToolBox.getAllUser(applicationContext);
            displayUserList(this@UserListActivity.userList)
        }
    }

    fun onUserClickManager(view : View)
    {
        when(view.id)
        {
            R.id.button ->{lifecycleScope.launch(Dispatchers.IO) {userModificationDispatcher(view)}}
        }
    }

    private suspend fun displayUserList(userList : List<User>)
    {
        var dataset : ArrayList<User> = ArrayList()
        for (user in userList)
        {
            dataset.add(user)
        }
        withContext(Dispatchers.Main)
        {
            val customAdapter = CustomDynamicList(dataset)

            val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
            recyclerView.layoutManager = LinearLayoutManager(this@UserListActivity)
            recyclerView.adapter = customAdapter
        }
    }

    private fun userModificationDispatcher(view : View)
    {
        val button : Button = view as Button
        val mailTemp : String = button.text.toString().replace("Modifier l'utilisateur ","")
        val userModifier : User = UserDbToolBox.getUser(applicationContext, mailTemp)
        ActivityTraveling.sentToModifierUser(this.user, userModifier, this@UserListActivity)
    }
}