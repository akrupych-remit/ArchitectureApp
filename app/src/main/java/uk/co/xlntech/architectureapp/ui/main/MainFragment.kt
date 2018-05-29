package uk.co.xlntech.architectureapp.ui.main

import android.Manifest
import android.arch.lifecycle.Observer
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.SearchView
import kotlinx.android.synthetic.main.main_fragment.*
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.android.inject
import uk.co.xlntech.architectureapp.R
import uk.co.xlntech.architectureapp.data.MyLocationManager

class MainFragment : Fragment() {

    companion object {
        private const val REQUEST_LOCATION = 0
    }

    private val viewModel: MainViewModel by viewModel() // koin injection
    private lateinit var tipsAdapter: TipsAdapter
    private val locationManager: MyLocationManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        requestPermissions()
        lifecycle.addObserver(locationManager)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?) =
            inflater.inflate(R.layout.main_fragment, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            tipsAdapter = TipsAdapter(context)
            adapter = tipsAdapter
        }
        swipeToRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
        viewModel.feed.observe(this, Observer { it?.let { tips ->
            swipeToRefresh.isRefreshing = false
            if (tips.isNotEmpty()) progressBar.visibility = View.GONE
            tipsAdapter.submitList(tips)
        }})
        viewModel.errors.observe(this, Observer { it?.message?.let { message ->
            swipeToRefresh.isRefreshing = false
            Snackbar.make(contentView, message, Snackbar.LENGTH_LONG).show()
        }})
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.search(query).observe(this@MainFragment, Observer { it?.let { tips ->
                    tipsAdapter.submitList(tips)
                }})
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()) {
                    tipsAdapter = TipsAdapter(searchView.context)
                    recyclerView.adapter = tipsAdapter
                    tipsAdapter.submitList(viewModel.feed.value)
                }
                else viewModel.filter(newText).observe(this@MainFragment, Observer { it?.let { tips ->
                    tipsAdapter.submitList(tips)
                }})
                return true
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(locationManager)
    }

    private fun requestPermissions() {
        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity!!,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    REQUEST_LOCATION)
        }
    }
}
