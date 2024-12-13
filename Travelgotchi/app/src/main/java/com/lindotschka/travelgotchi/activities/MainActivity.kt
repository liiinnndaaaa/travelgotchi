package com.lindotschka.travelgotchi.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.appbar.AppBarLayout
import com.lindotschka.travelgotchi.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var topAppBar: MaterialToolbar
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var backButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        topAppBar = findViewById(R.id.topAppBar)
        topAppBar.setNavigationOnClickListener {
            Toast.makeText(this, "Navigation Icon Clicked", Toast.LENGTH_SHORT).show()
        }

        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search -> {
                    Toast.makeText(this, "Search action Clicked", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.message -> {
                    Toast.makeText(this, "Message action Clicked", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.add -> {
                    Toast.makeText(this, "Add action Clicked", Toast.LENGTH_SHORT).show()
                    true
                }

                else -> {
                    false
                }
            }
        }

        val navigationHost =
            supportFragmentManager.findFragmentById(R.id.host_fragment) as NavHostFragment
        val navController = navigationHost.navController
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.btm_nav)

        appBarLayout = findViewById(R.id.appBarLayout)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.homeFragment) {
                appBarLayout.visibility = View.VISIBLE
            } else {
                appBarLayout.visibility = View.GONE
            }
        }

        NavigationUI.setupWithNavController(bottomNavigation, navController)
    }
}