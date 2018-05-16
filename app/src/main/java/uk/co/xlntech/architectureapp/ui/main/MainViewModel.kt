package uk.co.xlntech.architectureapp.ui.main

import android.arch.lifecycle.ViewModel
import uk.co.xlntech.architectureapp.data.MainRepository

class MainViewModel(repository: MainRepository) : ViewModel() {
    val feed = repository.loadFeed() // load asap, no triggering from view
}
