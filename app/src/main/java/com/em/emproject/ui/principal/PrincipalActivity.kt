package com.em.emproject.ui.principal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import com.em.emproject.R
import com.em.emproject.databinding.ActivityPrincipalBinding
import com.em.emproject.ui.login.LoginAppViewModel
import com.em.emproject.ui.projects.ProjectsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrincipalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPrincipalBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }
    private fun initUI() {
//        initNavigation()
        initList()
    }

    private fun initList() {
        binding.imProjects.setOnClickListener{
            startActivity(Intent(this, ProjectsActivity::class.java))
        }
    }
}