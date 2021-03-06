package uk.co.xlntech.architectureapp

import android.app.Application
import android.arch.persistence.room.Room
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.android.startKoin
import org.koin.dsl.module.applicationContext
import uk.co.xlntech.architectureapp.data.MainRepository
import uk.co.xlntech.architectureapp.data.MyLocationManager
import uk.co.xlntech.architectureapp.data.api.WorldLobbyApi
import uk.co.xlntech.architectureapp.data.database.WorldLobbyDatabase
import uk.co.xlntech.architectureapp.ui.main.MainViewModel

class App : Application() {

    val koinModule = applicationContext {
        viewModel { MainViewModel(get()) }
        bean { MainRepository(get(), get(), get()) }
        bean { WorldLobbyApi.create() }
        bean { Room.databaseBuilder(this@App, WorldLobbyDatabase::class.java, "world-lobby").build() }
        bean { MyLocationManager(this@App) }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(koinModule))
    }
}