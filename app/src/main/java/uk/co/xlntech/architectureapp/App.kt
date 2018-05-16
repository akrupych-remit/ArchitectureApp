package uk.co.xlntech.architectureapp

import android.app.Application
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.android.startKoin
import org.koin.dsl.module.applicationContext
import uk.co.xlntech.architectureapp.data.MainRepository
import uk.co.xlntech.architectureapp.data.WorldLobbyApi
import uk.co.xlntech.architectureapp.ui.main.MainViewModel

class App : Application() {

    val koinModule = applicationContext {
        viewModel { MainViewModel(get()) }
        bean { MainRepository(get()) }
        bean { WorldLobbyApi.create() }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(koinModule))
    }
}