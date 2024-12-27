package be.heh.projetapphyb

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import be.heh.projetapphyb.databinding.ActivityMatosModificationBinding
import be.heh.projetapphyb.db.Matos
import be.heh.projetapphyb.util.ActivityTraveling
import be.heh.projetapphyb.util.JsonConvertor
import be.heh.projetapphyb.util.db.MatosDbToolBox
import be.heh.projetapphyb.util.db.UserDbToolBox
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MatosModificationActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityMatosModificationBinding
    private var user = UserDbToolBox.defaultUser
    private var matos : Matos = MatosDbToolBox.defaultMatos
    private var matosModif : Matos = MatosDbToolBox.defaultMatos

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMatosModificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.user = JsonConvertor.fromJsonToUser(intent.getStringExtra("user").toString())
        this.matos = JsonConvertor.fromJsonToMatos(intent.getStringExtra("matos").toString())
        this.matosModif = JsonConvertor.fromJsonToMatos(intent.getStringExtra("matos").toString())
        setupInformation()
        Log.i("PROJETAPPHYB", "MatosModificationActivity started")
    }

    fun onMatosModificationClickManager(view : View)
    {
        when(view.id)
        {
            binding.btMatosmodification1.id -> {switchIsAvailable()}
            binding.btMatosmodification2.id -> {lifecycleScope.launch(Dispatchers.IO) {saveMatos()}}
            binding.btMatosmodification3.id -> {ActivityTraveling.sentToDisplayMatos(this.user, this.matos, this)}
            binding.btMatosmodification4.id -> {lifecycleScope.launch(Dispatchers.IO) {deleteMatos()}}
        }
    }

    private suspend fun saveMatos()
    {
        Log.i("PROJETAPPHYB", "Start save to db")
        var error = false
        var flagRef = false
        var flagType = false
        var flagName = false
        var flagLink = false
        var textError : String = "Erreur : "

        if(binding.etMatosmodification1.text.toString().isNotBlank())
        {
            if (!isRefUnique(binding.etMatosmodification1.text.toString()))
            {
                error = true
                textError += "mail invalid"
                Log.i("PROJETAPPHYB", "Error : invalid ref")
            }
            flagRef = true
        }
        if(binding.etMatosmodification2.text.toString().isNotBlank())
        {
            flagType = true
        }
        if(binding.etMatosmodification3.text.toString().isNotBlank())
        {
            flagName = true
        }
        if(binding.etMatosmodification4.text.toString().isNotBlank())
        {
            flagLink = true
        }

        if (error)
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
        else
        {
            if(flagRef)
            {
                matosModif.refNumber = binding.etMatosmodification1.text.toString()
            }
            if (flagType)
            {
                matosModif.type = binding.etMatosmodification2.text.toString()
            }
            if (flagName)
            {
                matosModif.name = binding.etMatosmodification3.text.toString()
            }
            if (flagLink)
            {
                matosModif.link = binding.etMatosmodification4.text.toString()
            }
            Log.i("PROJETAPPHYB", "Save modification of : \n $matosModif \n on :toto@hotmail.be \n $matos")
            MatosDbToolBox.modifieMatos(applicationContext, matosModif)
            ActivityTraveling.sentToWithUser(ActivityTraveling.MATERIAL_LIST, this.user, this@MatosModificationActivity)
        }
    }

    private fun isRefUnique(refWanted : String) : Boolean
    {
        var flag = false
        if (MatosDbToolBox.getMatosByRefNumber(this@MatosModificationActivity, refWanted).refNumber != "null")
        {
            flag = true
        }
        return flag
    }

    private fun deleteMatos()
    {
        MatosDbToolBox.deleteMatos(this@MatosModificationActivity, this.matos)
        Log.i("PROJETAPPHYB", "Matos delete")
        ActivityTraveling.sentToWithUser(ActivityTraveling.MATERIAL_LIST, this.user, this@MatosModificationActivity)
    }

    private fun switchIsAvailable()
    {
        if (matosModif.isAvailable)
        {
            matosModif.isAvailable = false
            binding.tvMatosmodification6.text = binding.tvMatosmodification6.text.toString().replace("true", "false")
        }
        else
        {
            matosModif.isAvailable = true
            binding.tvMatosmodification6.text = binding.tvMatosmodification6.text.toString().replace("false", "true")
        }
    }

    private fun setupInformation()
    {
        val id = this.matos.matosId
        val refNumber = this.matos.refNumber
        val type = this.matos.type
        val name = this.matos.name
        val link = this.matos.link
        val isAvailable = this.matos.isAvailable

        var tmpTxt = binding.tvMatosmodification1.text.toString() + " " + id
        binding.tvMatosmodification1.text = tmpTxt

        tmpTxt = binding.tvMatosmodification2.text.toString() + " " + refNumber
        binding.tvMatosmodification2.text = tmpTxt

        tmpTxt = binding.tvMatosmodification3.text.toString() + " " + type
        binding.tvMatosmodification3.text = tmpTxt

        tmpTxt = binding.tvMatosmodification4.text.toString() + " " + name
        binding.tvMatosmodification4.text = tmpTxt

        tmpTxt = binding.tvMatosmodification5.text.toString() + " " + link
        binding.tvMatosmodification5.text = tmpTxt

        tmpTxt = binding.tvMatosmodification6.text.toString() + " " + isAvailable
        binding.tvMatosmodification6.text = tmpTxt

    }
}