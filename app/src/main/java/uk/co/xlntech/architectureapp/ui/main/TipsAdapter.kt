package uk.co.xlntech.architectureapp.ui.main

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import uk.co.xlntech.architectureapp.R
import uk.co.xlntech.architectureapp.data.entities.TipSummary

class TipsAdapter(context: Context) : RecyclerView.Adapter<TipViewHolder>() {

    private val inflater = LayoutInflater.from(context)
    private var tips = listOf<TipSummary>()

    override fun getItemCount() = tips.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            TipViewHolder(inflater.inflate(R.layout.layout_tip, parent, false))

    override fun onBindViewHolder(holder: TipViewHolder, position: Int) {
        holder.bind(tips[position])
    }

    fun setTips(tips: List<TipSummary>) {
        this.tips = tips
        notifyDataSetChanged()
    }
}