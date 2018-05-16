package uk.co.xlntech.architectureapp.ui.main

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import uk.co.xlntech.architectureapp.R
import uk.co.xlntech.architectureapp.data.entities.TipSummary

open class TipViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val context = itemView.context

    val image = itemView.findViewById<ImageView>(R.id.tipImage)
    val name = itemView.findViewById<TextView>(R.id.tipName)
    val place = itemView.findViewById<TextView>(R.id.tipPlace)
    val description = itemView.findViewById<TextView>(R.id.tipDescription)
    val author = itemView.findViewById<TextView>(R.id.tipAuthor)

    lateinit var tip: TipSummary

    fun bind(tip: TipSummary) {
        this.tip = tip
        Glide.with(context).load(tip.image).into(image)
        name.text = tip.name
        place.text = tip.placeName
        description.text = tip.description
        author.text = tip.profileName
    }
}