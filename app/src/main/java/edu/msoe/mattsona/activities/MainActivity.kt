package edu.msoe.mattsona.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import edu.msoe.mattsona.GolfViewModel
import edu.msoe.mattsona.R
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewmodel: GolfViewModel by viewModels()
        //viewmodel.clearDb()

        lifecycleScope.launch {
            if (viewmodel.dbIsEmpty()) {
                viewmodel.prepareSampleData(resources.getStringArray(R.array.round_data))
            }
        }
    }
}