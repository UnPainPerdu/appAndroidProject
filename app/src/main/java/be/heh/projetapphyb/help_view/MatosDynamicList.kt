package be.heh.projetapphyb.help_view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import be.heh.projetapphyb.R
import be.heh.projetapphyb.db.Matos

class MatosDynamicList(private val dataSet: ArrayList<Matos>) :
    RecyclerView.Adapter<MatosDynamicList.ViewHolder>()
{

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val textView: TextView
        val button : Button

        init
        {
            // Define click listener for the ViewHolder's View
            textView = view.findViewById(R.id.textViewMatos)
            button = view.findViewById(R.id.buttonMatos)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder
    {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.text_row_item_matos, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int)
    {
        val MatosToDisplay : Matos = dataSet[position]
        val matosId = MatosToDisplay.matosId
        val name = MatosToDisplay.name
        val refNumber = MatosToDisplay.refNumber
        var tempTxt = "$matosId \n $refNumber \n $name "

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textView.text = tempTxt
        viewHolder.button.text = "Afficher le matériel " + refNumber
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
}