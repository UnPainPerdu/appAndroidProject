package be.heh.projetapphyb

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import be.heh.exokotlin.db.MatosRecord
import be.heh.exokotlin.db.MyDB
import be.heh.projetapphyb.databinding.ActivityCreateMatosBinding
import be.heh.projetapphyb.db.Matos
import be.heh.projetapphyb.util.ActivityTraveling
import be.heh.projetapphyb.util.JsonConvertor
import be.heh.projetapphyb.util.db.MatosDbToolBox
import be.heh.projetapphyb.util.db.UserDbToolBox
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateMatosActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityCreateMatosBinding
    private var user = UserDbToolBox.defaultUser
    private val onBackPressedCallBack : OnBackPressedCallback = object : OnBackPressedCallback(true)
    {
        override fun handleOnBackPressed()
        {
            Log.i("PROJETAPPHYB", "Retour refusé")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateMatosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallBack)
        this.user = JsonConvertor.fromJsonToUser(intent.getStringExtra("user").toString())
        Log.i("PROJETAPPHYB", "CreateMatosActivity started")
    }

    fun onCreateMatosClickManager(view : View)
    {
        when(view.id)
        {
            binding.btCreatematos1.id -> lifecycleScope.launch(Dispatchers.IO) { createMatos() }
            binding.btCreatematos2.id -> ActivityTraveling.sentToWithUser(ActivityTraveling.MATERIAL_LIST, this.user, this@CreateMatosActivity)
            else -> Log.i("PROJETAPPHYB-ERROR", "no function start")
        }
    }

    suspend fun createMatos()
    {
        Log.i("PROJETAPPHYB","Start matos creation")
        var type = binding.etCreatematos1.text.toString()
        var name = binding.etCreatematos2.text.toString()
        var link = binding.etCreatematos3.text.toString()
        val refNumber = binding.etCreatematos4.text.toString()
        var flagError = false
        var idError = ""
        if (!isFieldFilled())
        {
            Log.i("PROJETAPPHYB","field not filled")
            flagError = true
            idError = "dlg_creatematos_3.1"
        }
        if (!isRefNumberValid(refNumber))
        {
            Log.i("PROJETAPPHYB","refNumber invalid")
            flagError = true
            idError = "dlg_creatematos_3.2"
        }
        if (!flagError)
        {
            val matosId : Int = createNewMatosId()
            val m = Matos(
                matosId,
                type,
                name,
                link,
                refNumber,
                true
            )
            Log.i("PROJETAPPHYB","création de matos avec : $m")
            val db = Room.databaseBuilder(
                applicationContext,
                MyDB::class.java,
                "MyDataBase"
            ).build()
            Log.i("PROJETAPPHYB","db initialized")
            val dao = db.matosDao()
            Log.i("PROJETAPPHYB","dao initialized")
            val mR = MatosRecord(m.matosId, m.type, m.name, m.link, m.refNumber, m.isAvailable)
            Log.i("PROJETAPPHYB","matos record triggered with : $mR")
            dao.insertMatos(mR)
            db.close()
            showMessage("dlg_creatematos_3.3")
        }
        else
        {
            showMessage(idError)
        }
    }

    fun createNewMatosId(): Int
    {
        var i = -1
        var answer = 0
        while (answer != -1)
        {
            i++
            answer = MatosDbToolBox.getMatosById(this,i).matosId
        }
        return i
    }

    private suspend fun showMessage(tradId : String)
    {
        withContext(Dispatchers.Main)
        {
            var tempText = ""
            when(tradId)
            {
                "dlg_creatematos_3.1" -> tempText = getString(R.string.dlg_creatematos_1_1)
                "dlg_creatematos_3.2" -> tempText = getString(R.string.dlg_creatematos_1_2)
                "dlg_creatematos_3.3" -> tempText = getString(R.string.dlg_creatematos_1_3)
            }

            val dlgAlert = AlertDialog.Builder(this@CreateMatosActivity)
            dlgAlert.setTitle(R.string.dlg_creatematos_1_0)
            dlgAlert.setMessage(tempText)
            dlgAlert.setPositiveButton("OK", null)
            dlgAlert.setCancelable(true)
            dlgAlert.create().show()
        }
    }

    private fun isFieldFilled() : Boolean
    {
        return binding.etCreatematos1.text.toString().isNotBlank()
                && binding.etCreatematos2.text.toString().isNotBlank()
                && binding.etCreatematos3.text.toString().isNotBlank()
                && binding.etCreatematos4.text.toString().isNotBlank()
    }

    private fun isRefNumberValid(refNumber: String): Boolean
    {
        Log.i("PROJETAPPHYB","checking refNumber")
        val refGet = MatosDbToolBox.getMatosByRefNumber(this@CreateMatosActivity ,refNumber).refNumber
        val isIndefinite = refGet == "INDEFINI" || refGet == "null"
        Log.i("PROJETAPPHYB","mail not used : $isIndefinite , sended : $refGet")
        return isIndefinite
    }
}