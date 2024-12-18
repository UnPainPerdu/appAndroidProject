package be.heh.projetapphyb

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import be.heh.exokotlin.db.MyDB
import be.heh.exokotlin.db.UserRecord
import be.heh.projetapphyb.databinding.ActivityCreateUserBinding
import be.heh.projetapphyb.db.User
import be.heh.projetapphyb.util.ActivityTraveling.Companion.sentTo
import be.heh.projetapphyb.util.HashMaker
import be.heh.projetapphyb.util.ToastMaker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateUserActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityCreateUserBinding
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.i("PROJETAPPHYB", "CreateUserActivity started")
    }

    fun onCreateUserClickManager(view : View)
    {
        when(view.id)
        {
            binding.btCreateuser1.id -> lifecycleScope.launch(Dispatchers.IO) { createUser() }
            binding.btCreateuser2.id -> sentTo("main", this)
            else -> Log.i("PROJETAPPHYB-ERROR", "no function start")
        }
    }

    suspend fun createUser()
    {
        Log.i("PROJETAPPHYB","Start user creation")
        var mail = binding.etCreateuser1.text.toString()
        val pswd = binding.etCreateuser2.text.toString()
        val verifPswd = binding.etCreateuser3.text.toString()
        if (mailIsValid(mail))
        {
            mail = mail.replace(" ", "")
            Log.i("PROJETAPPHYB","mail is valide")
            if(pswdIsValid(pswd))
            {
                Log.i("PROJETAPPHYB","pswd is valide")
                if (pswd.equals(verifPswd))
                {
                    Log.i("PROJETAPPHYB","pswd is verification pswd")
                    val userId : Int = createNewUserId()
                    var hasPrivilege = false
                    var isAdmin = false
                    val hashPswd = HashMaker.hashPswd(pswd)
                    if (userId == 0)
                    {
                        hasPrivilege = true
                        isAdmin = true
                        Log.i("PROJETAPPHYB","user is frist one -> Super admin")
                    }
                    val u = User(
                        userId,
                        mail,
                        hashPswd,
                        hasPrivilege,
                        isAdmin
                    )
                    Log.i("PROJETAPPHYB","création de user avec : $u")
                    val db = Room.databaseBuilder(
                        applicationContext,
                        MyDB::class.java,
                        "MyDataBase"
                    ).build()
                    Log.i("PROJETAPPHYB","db initialized")
                    val dao = db.userDao()
                    Log.i("PROJETAPPHYB","dao initialized")
                    val uR = UserRecord(u.userId, u.mail, u.pswd, u.hasPrivilege, u.isAdmin)
                    dao.insertUser(uR)
                    Log.i("PROJETAPPHYB","user record triggered with : $uR")
                    db.close()
                    showMessage("tv_createuser_3.3")
                    ToastMaker.makeToastAsync(this, "Utilisateur créé", Toast.LENGTH_LONG)
                }
                else
                {
                    ToastMaker.makeErrorAsync(this)
                    showMessage("tv_createuser_3.2")
                    Log.i("PROJETAPPHYB-ERROR", "pswd is not verification pswd")
                }
            }
            else
            {
                ToastMaker.makeErrorAsync(this)
                showMessage("tv_createuser_3.1")
                Log.i("PROJETAPPHYB-ERROR", "pswd contain space or is less than 4 characters")
            }
        }
        else
        {
            ToastMaker.makeErrorAsync(this)
            showMessage("tv_createuser_3.0")
            Log.i("PROJETAPPHYB-ERROR", "mail is already use or is not a valid mail")
        }
    }

    private suspend fun showMessage(tradId : String)
    {
        withContext(Dispatchers.Main)
        {
            when(tradId)
            {
                "tv_createuser_3.0" -> binding.tvCreateuser3.text = getString(R.string.tv_createuser_3_0)
                "tv_createuser_3.1" -> binding.tvCreateuser3.text = getString(R.string.tv_createuser_3_1)
                "tv_createuser_3.2" -> binding.tvCreateuser3.text = getString(R.string.tv_createuser_3_2)
                "tv_createuser_3.3" -> binding.tvCreateuser3.text = getString(R.string.tv_createuser_3_3)
            }
            binding.tvCreateuser3.visibility = View.VISIBLE
        }
    }

    private fun mailIsValid(mail: String): Boolean
    {
        Log.i("PROJETAPPHYB","checking mail")
        val mailGet = getUserByMail(mail).mail
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

    fun getUserById(userId : Int): User
    {
        Log.i("PROJETAPPHYB","getting user by id")
        val db = Room.databaseBuilder(
            applicationContext,
            MyDB::class.java, "MyDataBase"
        ).build()
        val dao = db.userDao()
        val dbL = dao?.get(userId)
        return User(dbL?.userId?:-1, dbL?.mail?:"INDEFINI", dbL?.pswd?:"INDEFINI", dbL?.has_privilege?:false, dbL?.is_admin?:false)
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

    fun createNewUserId(): Int
    {
        var i = -1
        var answer = 0
        while (answer != -1)
        {
            i++
            answer = getUserById(i).userId
        }
        return i
    }
}