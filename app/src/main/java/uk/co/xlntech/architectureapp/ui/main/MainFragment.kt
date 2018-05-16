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
import uk.co.xlntech.architectureapp.data.Failure
import uk.co.xlntech.architectureapp.data.Success

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModel() // koin injection

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?) =
            inflater.inflate(R.layout.main_fragment, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = TipsAdapter(context)
        }
        viewModel.feed.observe(this, Observer { result ->
            progressBar.visibility = View.GONE
            when (result) {
                is Success -> (recyclerView.adapter as TipsAdapter).setTips(result.items)
                is Failure -> Snackbar.make(contentView, result.message, Snackbar.LENGTH_LONG).show()
            }
        })
    }
}
