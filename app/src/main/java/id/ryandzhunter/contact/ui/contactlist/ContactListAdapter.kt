package id.ryandzhunter.contact.ui.contactlist

import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.bumptech.glide.request.target.Target
import id.ryandzhunter.contact.R
import id.ryandzhunter.contact.di.module.GlideApp
import id.ryandzhunter.contact.model.ContactRealm
import jp.wasabeef.glide.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.item_contacts.view.*

/**
 * Created by ryandzhunter on 1/3/17.
 */
class ContactListAdapter(val data: List<ContactRealm>, val listener: (ContactRealm) -> Unit) : RecyclerView.Adapter<ContactListAdapter.ContactListViewHolder>() {

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

        viewHolder.bind(data[position],listener)
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

    class ContactListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: ContactRealm, listener: (ContactRealm) -> Unit) = with(itemView) {
            itemView.textContactAlphabet.text = item.firstName?.toUpperCase()?.get(0).toString()
            itemView.textName.text = item.firstName + " " + item.lastName
            setOnClickListener { listener(item) }

            itemView.textContactAlphabet.visibility = View.VISIBLE
            itemView.imageContact.background = ContextCompat.getDrawable(itemView.context, R.drawable.circle)
            if (!item.profilePic.equals("/images/missing.png")){
                GlideApp.with(imageContact.context)
                        .load(item.profilePic)
                        .listener(object : RequestListener<Drawable> {
                            override fun onResourceReady(resource: Drawable?, model: Any?,
                                                         target: Target<Drawable>?, dataSource: DataSource?,
                                                         isFirstResource: Boolean): Boolean {
                                itemView.textContactAlphabet.visibility = View.INVISIBLE
                                return false
                            }
                            override fun onLoadFailed(e: GlideException?, model: Any?,
                                                      target: com.bumptech.glide.request.target.Target<Drawable>?,
                                                      isFirstResource: Boolean): Boolean {
                                return false
                            }
                        })
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .apply(bitmapTransform(CropCircleTransformation()))
                        .into(itemView.imageContact)
            } else {
                itemView.imageContact.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.circle))
            }
        }
    }
}


