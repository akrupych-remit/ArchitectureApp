package uk.co.xlntech.architectureapp.ui.main

import android.arch.paging.PagedListAdapter
import android.content.Context
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import uk.co.xlntech.architectureapp.R
import uk.co.xlntech.architectureapp.data.entities.TipSummary

class TipsAdapter(context: Context) : PagedListAdapter<TipSummary, TipViewHolder>(
        object : DiffUtil.ItemCallback<TipSummary>() {
            override fun areItemsTheSame(oldItem: TipSummary, newItem: TipSummary) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: TipSummary, newItem: TipSummary) = oldItem == newItem
        }
) {
    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            TipViewHolder(inflater.inflate(R.layout.layout_tip, parent, false))

    override fun onBindViewHolder(holder: TipViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }
}