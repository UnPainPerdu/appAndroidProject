package be.heh.projetapphyb

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import be.heh.projetapphyb.databinding.ActivityMatosListBinding
import be.heh.projetapphyb.db.Matos
import be.heh.projetapphyb.help_view.MatosDynamicList
import be.heh.projetapphyb.util.ActivityTraveling
import be.heh.projetapphyb.util.JsonConvertor
import be.heh.projetapphyb.util.db.MatosDbToolBox
import be.heh.projetapphyb.util.db.UserDbToolBox
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MatosListActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityMatosListBinding
    private var user = UserDbToolBox.defaultUser
    private var matosList : List<Matos> = ArrayList<Matos>().toList()
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
        binding = ActivityMatosListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallBack)
        this.user = JsonConvertor.fromJsonToUser(intent.getStringExtra("user").toString())
        lifecycleScope.launch(Dispatchers.IO)
        {
            this@MatosListActivity.matosList = MatosDbToolBox.getAllMatos(applicationContext);
            displayMatosList(this@MatosListActivity.matosList)
        }
        Log.i("PROJETAPPHYB", "MatosListActivity started")
    }

    fun onMatosClickManager(view : View)
    {
        when(view.id)
        {
            R.id.buttonMatos ->{lifecycleScope.launch(Dispatchers.IO) {matosDisplayDispatcher(view)}}
            binding.btMatoslist1.id -> ActivityTraveling.sentToWithUser(ActivityTraveling.MENU, this.user, this)
            binding.btMatoslist2.id -> matosCreationRedirection()
        }
    }

    private suspend fun displayMatosList(matosList : List<Matos>)
    {
        var dataset : ArrayList<Matos> = ArrayList()
        for (matos in matosList)
        {
            dataset.add(matos)
        }
        withContext(Dispatchers.Main)
        {
            val customAdapter = MatosDynamicList(dataset)

            val recyclerView: RecyclerView = findViewById(R.id.recycler_view_matos)
            recyclerView.layoutManager = LinearLayoutManager(this@MatosListActivity)
            recyclerView.adapter = customAdapter
        }
    }

    private fun matosDisplayDispatcher(view : View)
    {
        val button : Button = view as Button
        val refNumberTemp : String = button.text.toString().replace("Afficher le matériel ","")
        val matosDisplayer : Matos = MatosDbToolBox.getMatosByRefNumber(applicationContext, refNumberTemp)
        ActivityTraveling.sentToDisplayMatos(this.user, matosDisplayer, this@MatosListActivity)
    }

    private fun matosCreationRedirection()
    {
        if (this.user.hasPrivilege)
        {
            ActivityTraveling.sentToWithUser(ActivityTraveling.CREATE_MATERIAL, this.user, this)
        }
        else
        {
            val dlgAlert = AlertDialog.Builder(this)
            dlgAlert.setMessage(getString(R.string.dlg_matoslist_1_1))
            dlgAlert.setTitle(R.string.dlg_matoslist_1_0)
            dlgAlert.setPositiveButton("OK", null)
            dlgAlert.setCancelable(true)
            dlgAlert.create().show()
        }
    }
}