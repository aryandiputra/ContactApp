package id.ryandzhunter.contact.ui.contactlist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.ryandzhunter.contact.R
import id.ryandzhunter.contact.model.ContactRealm
import kotlinx.android.synthetic.main.item_contacts.view.*

/**
 * Created by ryandzhunter on 1/3/17.
 */
class ContactListAdapter(val data: List<ContactRealm>) : RecyclerView.Adapter<ContactListAdapter.ContactListViewHolder>() {

    override fun onBindViewHolder(viewHolder: ContactListViewHolder, position: Int) {

        // set favorite and index
        if (position == 0 && data[position].favorite == true){
            viewHolder.itemView.imageStar.visibility = View.VISIBLE
            viewHolder.itemView.textIndexAlphabet.visibility = View.INVISIBLE
        } else if (data[position].favorite != true){
            viewHolder.itemView.textIndexAlphabet.text = getLabel(position)
            viewHolder.itemView.textIndexAlphabet.visibility = View.VISIBLE
            viewHolder.itemView.imageStar.visibility = View.INVISIBLE
        } else {
            viewHolder.itemView.imageStar.visibility = View.INVISIBLE
            viewHolder.itemView.textIndexAlphabet.visibility = View.INVISIBLE
        }

        viewHolder.itemView.textContactAlphabet.text = data[position].firstName?.toUpperCase()?.get(0).toString()
        viewHolder.itemView.textName.text = data[position].firstName + " " + data[position].lastName
    }

     fun getLabel(position: Int) : String {
        val currentFirstChar = data[position].firstName?.toUpperCase()?.get(0)
         val prevFirstChar = data[position-1].firstName?.toUpperCase()?.get(0)
         if (currentFirstChar != prevFirstChar){
            return currentFirstChar.toString()
        }
        return ""
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ContactListViewHolder {
        return ContactListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_contacts, parent, false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ContactListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}


