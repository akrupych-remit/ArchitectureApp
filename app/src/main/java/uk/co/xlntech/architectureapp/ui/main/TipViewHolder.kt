package uk.co.xlntech.architectureapp.ui.main

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import uk.co.xlntech.architectureapp.R
import uk.co.xlntech.architectureapp.data.entities.TipSummary

open class TipViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val image: ImageView = itemView.findViewById(R.id.tipImage)
    private val name: TextView = itemView.findViewById(R.id.tipName)
    private val place: TextView = itemView.findViewById(R.id.tipPlace)
    private val description: TextView = itemView.findViewById(R.id.tipDescription)
    private val author: TextView = itemView.findViewById(R.id.tipAuthor)

    fun bind(tip: TipSummary?) { tip?.let {
        Glide.with(itemView.context).load(it.image).into(image)
        name.text = it.name
        place.text = it.placeName
        description.text = it.description
        author.text = it.profileName
    }}
}