package uk.co.xlntech.architectureapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import uk.co.xlntech.architectureapp.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment())
                    .commitNow()
        }
    }

}
