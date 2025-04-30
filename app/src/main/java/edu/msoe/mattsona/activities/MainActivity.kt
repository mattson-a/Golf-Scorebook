package edu.msoe.mattsona.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import edu.msoe.mattsona.GolfViewModel
import edu.msoe.mattsona.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewmodel: GolfViewModel by viewModels()
        //viewmodel.clearDb()
    }
}