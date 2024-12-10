package be.heh.projetapphyb.help_view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import be.heh.projetapphyb.R
import be.heh.projetapphyb.db.User

class CustomDynamicList(private val dataSet: ArrayList<User>) :
    RecyclerView.Adapter<CustomDynamicList.ViewHolder>()
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
            textView = view.findViewById(R.id.textView)
            button = view.findViewById(R.id.button)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder
    {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.text_row_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int)
    {
        val userToDisplay : User = dataSet[position]
        val mail = userToDisplay.mail
        val isAdmin = userToDisplay.isAdmin
        val hasPrivilege = userToDisplay.hasPrivilege
        var tempTxt = mail

        if (isAdmin)
        {
            tempTxt += "\n Admin : true"
        }
        else if(hasPrivilege)
        {
            tempTxt += "\n Privilégié : true"
        }

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textView.text = tempTxt
        viewHolder.button.text = "Modifier l'utilisateur " + mail
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
}