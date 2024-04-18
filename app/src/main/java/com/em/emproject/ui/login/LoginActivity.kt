package com.em.emproject.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.em.emproject.R
import com.em.emproject.databinding.ActivityLoginBinding
import com.em.emproject.ui.home.MainActivity
import com.em.emproject.ui.principal.PrincipalActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginAppViewModel: LoginAppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        verifySession()

    }

    private fun verifySession() {
        val userCurrent = loginAppViewModel.getUserCurrent()
        if (userCurrent != null) {

//            val intent = Intent(this, MainActivity::class.java).apply {
//                putExtra(BUNDLE_NAME, loginAppViewModel.infoCurrentUser(userCurrent))
//            }
            LoginAppViewModel.USER_CURRENT = userCurrent
            startActivity(Intent(this, PrincipalActivity::class.java))

            Toast.makeText(
                this, getString(R.string.welcome) + "\n ${
                    userCurrent.name.split(" ")[0] + " " + userCurrent.surname.split(" ")[0]
                }",
                Toast.LENGTH_SHORT
            ).show()
            finishAffinity()
        }
    }
}