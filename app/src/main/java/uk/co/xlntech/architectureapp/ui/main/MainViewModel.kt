package uk.co.xlntech.architectureapp.ui.main

import android.arch.lifecycle.ViewModel
import uk.co.xlntech.architectureapp.data.MainRepository

class MainViewModel(private val repository: MainRepository) : ViewModel() {

    val feed = repository.feed
    val errors = repository.errors

    fun filter(query: String) = repository.filter(query)

    fun search(query: String) = repository.search(query)

    fun refresh() = repository.refresh()
}
