package com.em.emproject.ui.login

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.em.emproject.R
import com.em.emproject.data.repository.UserRepository
import com.em.emproject.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    @Inject
    lateinit var userRepository: UserRepository

    private val loginAppViewModel: LoginAppViewModel by activityViewModels()

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        initListener()
    }

    private fun initListener() {
        binding.pbRegister.isVisible = false

        binding.btSignUp.setOnClickListener {
            val entryComplete = informationValidation()
            if (entryComplete) createUserAdmin(
                binding.edMail.text.toString().trim(),
                binding.edPassword.text.toString().trim(),
                binding.edNames.text.toString().trim(),
                binding.edSurname.text.toString().trim()
            )

        }
    }

    private fun informationValidation(): Boolean {
        val names = binding.edNames.text.toString().trim()
        val email = binding.edMail.text.toString().trim()
        val surname = binding.edSurname.text.toString().trim()
        val password = binding.edPassword.text.toString().trim()
        val rPassword = binding.edRepeatPassword.text.toString().trim()

        var allEntryComplete: Boolean = false
        if (names.isEmpty()) {
            binding.edNames.error = "Ingrese Nombres"
            binding.edNames.requestFocus()
        } else if (surname.isEmpty()) {
            binding.edSurname.error = "Ingrese apellidos"
            binding.edSurname.requestFocus()
            val surnameAux = surname.split(" ")
            if (surnameAux.size < 2) binding.edSurname.error = "Ingrese apellidos completos"

        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edMail.error = "Ingrese correo electronico valido"
            binding.edMail.requestFocus()
        } else if (password.isEmpty()) {
            binding.edPassword.error = "Ingrese password"
            binding.edPassword.requestFocus()
        } else if (password.length < 6) {

            binding.edPassword.error = "La contraseña debe tener mas de 6 caracteres"
            binding.edPassword.requestFocus()

        } else if (rPassword.isEmpty()) {
            binding.edRepeatPassword.error = "Repetir password"
            binding.edRepeatPassword.requestFocus()

        } else if (rPassword != password) {

            binding.edPassword.error = "Las password no coinciden"
            binding.edPassword.requestFocus()
        } else {
            allEntryComplete = true
        }
        return allEntryComplete

//
    }


    private fun createUserAdmin(email: String, password: String, names: String, surname: String) {

        loginAppViewModel.createUserDB(email, password, names, surname)
        saveUserState()


    }

    private fun saveUserState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginAppViewModel.state.collect {
                    when (it) {
                        is LoginState.Error -> errorState()
                        LoginState.Loading -> loadingState()
                        is LoginState.Success -> successState(it.registered)
                        is LoginState.SuccessLogin -> {}
                    }
                }
            }
        }
    }

    private fun successState(registered: String) {
        binding.pbRegister.isVisible = false
        Toast.makeText(
            binding.pbRegister.context,
            registered + "\n" + getText(R.string.messageUserOkSave),
            Toast.LENGTH_SHORT
        ).show()
        findNavController().popBackStack()
    }


    private fun errorState() {
        binding.pbRegister.isVisible = false
        Toast.makeText(
            binding.btSignUp.context,
            getString(R.string.messageUserNoCreate),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun loadingState() {
        binding.pbRegister.isVisible = true
        Toast.makeText(
            binding.pbRegister.context,
            getText(R.string.messageLoading),
            Toast.LENGTH_SHORT
        ).show()
    }
}