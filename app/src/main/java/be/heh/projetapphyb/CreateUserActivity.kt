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
import be.heh.projetapphyb.util.HashMaker
import kotlinx.coroutines.launch

class CreateUserActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityCreateUserBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onCreateUserClickManager(view : View)
    {
        when(view.id)
        {
            binding.btCreateuser1.id -> lifecycleScope.launch{createUser()}
            else -> Log.i("PROJETAPPHYB-ERROR", "no function start")
        }
    }

    fun createUser()
    {
        Log.i("PROJETAPPHYB","Start user creation")
        val mail = binding.etCreateuser1.text.toString()
        val pswd = binding.etCreateuser2.text.toString()
        val verifPswd = binding.etCreateuser3.text.toString()
        if (mailIsValid(mail))
        {
            if(pswdIsValid(pswd))
            {
                if (pswd.equals(verifPswd))
                {
                    val userId : Int = createNewUserId()
                    var hasPrivilege = false
                    var isAdmin = false
                    val hashPswd = HashMaker.hashPswd(pswd)
                    if (userId == 0)
                    {
                        hasPrivilege = true
                        isAdmin = true
                    }
                    val u = User(
                        userId,
                        mail,
                        hashPswd,
                        hasPrivilege,
                        isAdmin
                    )

                    val db = Room.databaseBuilder(
                        applicationContext,
                        MyDB::class.java,
                        "MyDataBase"
                    ).build()
                    val dao = db.userDao()
                    val uR = UserRecord(0, u.mail, u.pswd, u.hasPrivilege, u.isAdmin)
                    dao.insertUser(uR)

                    Toast.makeText(this, "Utilisateur créé", Toast.LENGTH_LONG).show()
                }
                else
                {
                    Toast.makeText(this, "Erreur", Toast.LENGTH_LONG).show()
                }
            }
            else
            {
                Toast.makeText(this, "Erreur", Toast.LENGTH_LONG).show()
            }
        }
        else
        {
            Toast.makeText(this, "Erreur", Toast.LENGTH_LONG).show()
        }
    }

    private fun mailIsValid(mail: String): Boolean
    {
        return getUserByMail(mail).mail == "INDEFINI" && android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()
    }

    private fun pswdIsValid(pswd: String): Boolean
    {
        return (
                (pswd.replace(" ", "").equals(pswd))
                        && pswd.length>=4
                )
    }

    fun getUserById(userId : Int): User
    {
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
        val db = Room.databaseBuilder(
            applicationContext,
            MyDB::class.java, "MyDataBase"
        ).build()
        val dao = db.userDao()
        val dbL = dao?.get(mail)
        return User(dbL?.userId?:-1, dbL?.mail?:"INDEFINI", dbL?.pswd?:"INDEFINI", dbL?.has_privilege?:false, dbL?.is_admin?:false)
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