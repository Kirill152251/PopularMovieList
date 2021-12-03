package com.example.retrofitrxjava.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.retrofitrxjava.R

class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this,navController)
    }
    override fun onSupportNavigateUp(): Boolean {
        navController = this.findNavController(R.id.myNavHostFragment)
        return navController.navigateUp()
    }

    override fun onBackPressed() {
        navController = this.findNavController(R.id.myNavHostFragment)
        navController.navigate(MovieDetailsFragmentDirections.actionMovieDetailsFragmentToPopularMoviesFragment())
    }
}