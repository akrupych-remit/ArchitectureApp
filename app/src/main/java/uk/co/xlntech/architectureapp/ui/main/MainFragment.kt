package uk.co.xlntech.architectureapp.ui.main

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.main_fragment.*
import org.koin.android.architecture.ext.viewModel
import uk.co.xlntech.architectureapp.R

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModel() // koin injection

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?) =
            inflater.inflate(R.layout.main_fragment, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = TipsAdapter(context)
        }
        viewModel.feed.observe(this, Observer { it?.let { tips ->
            if (tips.isNotEmpty()) progressBar.visibility = View.GONE
            (recyclerView.adapter as TipsAdapter).setTips(tips)
        }})
        viewModel.errors.observe(this, Observer { it?.message?.let { message ->
            Snackbar.make(contentView, message, Snackbar.LENGTH_LONG).show()
        }})
    }
}
