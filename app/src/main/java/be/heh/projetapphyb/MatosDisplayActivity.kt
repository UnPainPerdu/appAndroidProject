package be.heh.projetapphyb

import android.content.Intent
import android.graphics.Point
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import be.heh.projetapphyb.databinding.ActivityMatosDisplayBinding
import be.heh.projetapphyb.db.Matos
import be.heh.projetapphyb.util.ActivityTraveling
import be.heh.projetapphyb.util.JsonConvertor
import be.heh.projetapphyb.util.db.MatosDbToolBox
import be.heh.projetapphyb.util.db.UserDbToolBox


class MatosDisplayActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityMatosDisplayBinding
    private var user = UserDbToolBox.defaultUser
    private var matos : Matos = MatosDbToolBox.defaultMatos
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
        binding = ActivityMatosDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallBack)
        this.user = JsonConvertor.fromJsonToUser(intent.getStringExtra("user").toString())
        this.matos = JsonConvertor.fromJsonToMatos(intent.getStringExtra("matos").toString())
        setupInformation()
        Log.i("PROJETAPPHYB", "MatosDisplayActivity started")
    }

    fun onMatosDisplayClickManager(view : View)
    {
        when(view.id)
        {
            binding.btMatosdisplay2.id -> {ActivityTraveling.sentToWithUser(ActivityTraveling.MATERIAL_LIST, this.user, this) }
            binding.btMatosdisplay1.id -> {sentToModificationMatos()}
            binding.btMatosdisplay5.id -> {sentToInternet()}
        }
    }

    private fun sentToInternet()
    {
        val uri = Uri.parse(this.matos.link)
        startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    private fun sentToModificationMatos()
    {
        if (user.hasPrivilege || user.isAdmin)
        {
            ActivityTraveling.sentToMatosModification(this.user, this.matos, this@MatosDisplayActivity)
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

    private fun setupInformation()
    {
        val id = this.matos.matosId
        val refNumber = this.matos.refNumber
        val type = this.matos.type
        val name = this.matos.name
        val link = this.matos.link
        val isAvailable = this.matos.isAvailable

        var tmpTxt = binding.tvMatosdisplay1.text.toString() + " " + refNumber
        binding.tvMatosdisplay1.text = tmpTxt

        tmpTxt = binding.tvMatosdisplay2.text.toString() + " " + id
        binding.tvMatosdisplay2.text = tmpTxt

        tmpTxt = binding.tvMatosdisplay3.text.toString() + " " + type
        binding.tvMatosdisplay3.text = tmpTxt

        tmpTxt = binding.tvMatosdisplay4.text.toString() + " " + name
        binding.tvMatosdisplay4.text = tmpTxt

        tmpTxt = binding.btMatosdisplay5.text.toString() + " " + link
        binding.btMatosdisplay5.text = tmpTxt

        tmpTxt = binding.tvMatosdisplay6.text.toString() + " " + isAvailable
        binding.tvMatosdisplay6.text = tmpTxt

        val point = Point()

        // getting width and
        // height of a point
        val width: Int = point.x
        val height: Int = point.y


        // generating dimension from width and height.
        var dimen = if (width < height) width else height
        dimen = dimen * 3 / 4

        val qrgEncoder = QRGEncoder(this.matos.toString(), null, QRGContents.Type.TEXT, dimen)
        try
        {
            // getting our qrcode in the form of bitmap.
            val bitmap = qrgEncoder.getBitmap()
            // the bitmap is set inside our image
            // view using .setimagebitmap method.
            binding.ivMatosdisplay1.setImageBitmap(bitmap)
        }
        catch (e : Exception)
        {
            // this method is called for
            // exception handling.
            Log.e("Tag", e.toString())
        }
    }
}