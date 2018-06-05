package uk.co.xlntech.architectureapp.ui.main

import android.arch.paging.PagedListAdapter
import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import uk.co.xlntech.architectureapp.data.entities.TipSummary
import uk.co.xlntech.architectureapp.databinding.LayoutTipBinding

class TipsAdapter(context: Context) : PagedListAdapter<TipSummary, TipsAdapter.TipViewHolder>(
        object : DiffUtil.ItemCallback<TipSummary>() {
            override fun areItemsTheSame(oldItem: TipSummary, newItem: TipSummary) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: TipSummary, newItem: TipSummary) = oldItem == newItem
        }
) {
    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            TipViewHolder(LayoutTipBinding.inflate(inflater, parent, false))

    override fun onBindViewHolder(holder: TipViewHolder, position: Int) {
        holder.binding.apply {
            tip = getItem(position)
            executePendingBindings()
        }
    }

    class TipViewHolder(val binding: LayoutTipBinding) : RecyclerView.ViewHolder(binding.root)
}