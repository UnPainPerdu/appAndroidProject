package be.heh.projetapphyb

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import be.heh.projetapphyb.databinding.ActivityUserModificationBinding
import be.heh.projetapphyb.util.ActivityTraveling
import be.heh.projetapphyb.util.JsonConvertor
import be.heh.projetapphyb.util.db.UserDbToolBox
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserModificationActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityUserModificationBinding
    private var user = UserDbToolBox.defaultUser
    private var userModifier = user
    private var userModified = UserDbToolBox.defaultUser

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityUserModificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.user = JsonConvertor.fromJsonToUser(intent.getStringExtra("user").toString())
        this.userModifier = JsonConvertor.fromJsonToUser(intent.getStringExtra("userModifier").toString())
        this.userModified = JsonConvertor.fromJsonToUser(intent.getStringExtra("userModifier").toString())
        initializeUserModifier()
        Log.i("PROJETAPPHYB", "UserModificationActivity started")
    }

    fun initializeUserModifier()
    {
        var tempTxt = binding.tvUsermodification1.text.toString() +  userModifier.mail
        binding.tvUsermodification1.text = tempTxt
        tempTxt = binding.tvUsermodification4.text.toString() + " " + userModifier.hasPrivilege.toString()
        binding.tvUsermodification4.text = tempTxt
        tempTxt = binding.tvUsermodification5.text.toString() + " " + userModifier.isAdmin.toString()
        binding.tvUsermodification5.text = tempTxt
    }

    fun onMainClickManager(view : View)
    {
        when(view.id)
        {
            binding.btUsermodification1.id -> {booleanSwitchPrivilege()}
            binding.btUsermodification2.id -> {booleanSwitchAdmin()}
            binding.btUsermodification3.id -> {lifecycleScope.launch(Dispatchers.IO) {saveChangeToDB()}}
            binding.btUsermodification4.id -> {ActivityTraveling.sentToWithUser(ActivityTraveling.USER_LIST, this.user, this@UserModificationActivity)}
            binding.btUsermodification5.id -> {lifecycleScope.launch(Dispatchers.IO) {deleteUser()}}
        }
    }

    fun booleanSwitchPrivilege()
    {
        var tempTxt : String = binding.tvUsermodification4.text.toString()

        if(userModified.hasPrivilege)
        {
            userModified.hasPrivilege = false
            tempTxt = tempTxt.replace("true","false")
        }
        else
        {
            userModified.hasPrivilege = true
            tempTxt = tempTxt.replace("false","true")
        }
        Log.i("PROJETAPPHYB", "Change privelege to " + userModified.hasPrivilege.toString())
        binding.tvUsermodification4.text = tempTxt
    }

    fun booleanSwitchAdmin()
    {
        var tempTxt : String = binding.tvUsermodification5.text.toString()

        if(userModified.isAdmin)
        {
            userModified.isAdmin = false
            tempTxt = tempTxt.replace("true","false")
        }
        else
        {
            userModified.isAdmin = true
            tempTxt = tempTxt.replace("false","true")
        }
        Log.i("PROJETAPPHYB", "Change admin to " + userModified.isAdmin.toString())
        binding.tvUsermodification5.text = tempTxt
    }

    suspend fun saveChangeToDB()
    {
        Log.i("PROJETAPPHYB", "Start save to db")

        var error = false
        var flagMailChanged = false
        var flagPswdChanged = false
        var textError : String = "Erreur : "
        if(binding.etUsermodification1.text.toString().isNotBlank())
        {
            if (!mailIsValid(binding.etUsermodification1.text.toString()))
            {
                error = true
                textError += "mail invalid"
                Log.i("PROJETAPPHYB", "Error : invalid mail")
            }
            flagMailChanged = true
        }
        else if(binding.etUsermodification2.text.toString().isNotBlank())
        {
            if (!pswdIsValid(binding.etUsermodification2.text.toString()))
            {
                error = true
                textError += "password invalid"
                Log.i("PROJETAPPHYB", "Error : invalid pswd")
            }
            flagPswdChanged = true
        }
        else if(user.userId == userModifier.userId)
        {
            Log.i("PROJETAPPHYB", "self modification detected")
            if (userModifier.hasPrivilege != userModified.hasPrivilege
                ||userModifier.isAdmin != userModified.isAdmin)
            {
                error = true
                textError += "admin can't change his self permission"
                Log.i("PROJETAPPHYB", "admin can't change his self permission")
            }
        }
        if (!error)
        {
            if(flagMailChanged)
            {
                userModified.mail = binding.etUsermodification1.text.toString()
            }
            if (flagPswdChanged)
            {
                userModified.pswd = binding.etUsermodification2.text.toString()
            }
            Log.i("PROJETAPPHYB", "Save modification of : \n $userModified \n on :toto@hotmail.be \n $userModifier")
            UserDbToolBox.modifieUser(applicationContext, userModifier, userModified)
            ActivityTraveling.sentToWithUser(ActivityTraveling.USER_LIST, this.user, this@UserModificationActivity)
        }
        else
        {
            val dlgAlert = AlertDialog.Builder(this)
            dlgAlert.setMessage(textError)
            dlgAlert.setTitle("Error when save to db")
            dlgAlert.setPositiveButton("OK", null)
            dlgAlert.setCancelable(true)
            withContext(Dispatchers.Main)
            {
                dlgAlert.create().show()
            }
        }
    }

    private fun mailIsValid(mail: String): Boolean
    {
        Log.i("PROJETAPPHYB","checking mail")
        val mailGet = UserDbToolBox.getUser(applicationContext ,mail).mail
        val isIndefinite = mailGet == "INDEFINI" || mailGet == "null"
        Log.i("PROJETAPPHYB","mail not used : $isIndefinite , sended : $mailGet")
        val hasGoodMailFormat = android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()
        Log.i("PROJETAPPHYB","mail in good format : $hasGoodMailFormat")
        val mailIsValid = isIndefinite && hasGoodMailFormat
        return mailIsValid
    }

    private fun pswdIsValid(pswd: String): Boolean
    {
        Log.i("PROJETAPPHYB","checking pswd")
        return (
                (pswd.replace(" ", "").equals(pswd))
                        && pswd.length>=4
                )
    }

    private suspend fun deleteUser()
    {
        var error = false
        var errorText = "Error : "
        if (user.userId == userModifier.userId)
        {
            Log.i("PROJETAPPHYB", "self deleting detected")
            error = true
            errorText += "User can't delete them self"
        }
        else if (userModifier.isAdmin)
        {
            Log.i("PROJETAPPHYB", "admin deleting detected")
            error = true
            errorText += "User can't delete admin user"
        }
        else if (userModifier.userId == 0)
        {
            Log.i("PROJETAPPHYB", "original admin deleting detected")
            error = true
            errorText += "User can't delete original admin user"
        }

        if (!error)
        {
            Log.i("PROJETAPPHYB", "deleting user with id : " + userModifier.userId.toString())
            val dlgAlert = AlertDialog.Builder(this)
            dlgAlert.setMessage("Are you sure you want to delete this user ?")
            dlgAlert.setTitle("WARNING")
            dlgAlert.setPositiveButton("Yes", { _, _ ->
                run{
                        lifecycleScope.launch(Dispatchers.IO) {
                            UserDbToolBox.deleteUser(applicationContext, userModifier)
                            withContext(Dispatchers.Main)
                            {
                                ActivityTraveling.sentToWithUser(ActivityTraveling.USER_LIST, this@UserModificationActivity.user, this@UserModificationActivity)
                            }
                        }
                    }
                }
            )
            dlgAlert.setNegativeButton("No", null)
            dlgAlert.setCancelable(true)
            withContext(Dispatchers.Main)
            {
                dlgAlert.create().show()
            }
        }
        else
        {
            val dlgAlert = AlertDialog.Builder(this)
            dlgAlert.setMessage(errorText)
            dlgAlert.setTitle("Error delete user")
            dlgAlert.setPositiveButton("OK", null)
            dlgAlert.setCancelable(true)
            withContext(Dispatchers.Main)
            {
                dlgAlert.create().show()
            }
        }
    }
}