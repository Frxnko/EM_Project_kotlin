package com.em.emproject.ui.login

import android.content.Intent
import android.os.Bundle
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
import com.em.emproject.databinding.FragmentLoginBinding
import com.em.emproject.domain.model.UserClass
import com.em.emproject.ui.principal.PrincipalActivity
import com.em.emventas.di.FormatText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    @Inject
    lateinit var formatText: FormatText

    private val loginAppViewModel: LoginAppViewModel by activityViewModels()

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        initListener()
//        initUIState()
    }

    private fun initListener() {
        binding.pbLogin.isVisible = false
        binding.tvRegister.text = formatText.textLikable(getString(R.string.Register))
        binding.tvRegister.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }

        binding.btLogin.setOnClickListener {
            loginAppViewModel.loginUser(
                binding.edMail.text.toString().trim(),
                binding.edPassword.text.toString().trim(),
                binding.btLogin.context
            )

            loginUserState()

        }
    }

    private fun loginUserState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginAppViewModel.state.collect {
                    when (it) {
                        is LoginState.Error -> errorState(it.error)
                        LoginState.Loading -> loadingState()
                        is LoginState.Success -> {}
                        is LoginState.SuccessLogin -> successState(it.userClass)
                    }
                }
            }
        }
    }

    private fun successState(registered: UserClass) {
        binding.pbLogin.isVisible = false
        binding.tvRegister.isVisible = true
        binding.edMail.isEnabled = true
        binding.edPassword.isEnabled = true

        binding.edMail.setText("")
        binding.edPassword.setText("")
        Toast.makeText(
            binding.pbLogin.context,
            "Welcome\n ${
                registered.name.split(" ")[0] + " " + registered.surname.split(
                    " "
                )[0]
            }", Toast.LENGTH_SHORT
        ).show()

        goToPrincipalMenu(registered)

    }

    private fun goToPrincipalMenu(userLogged: UserClass) {

//        val homeIntent = Intent(binding.pbLogin.context, MainActivity::class.java).apply {
//            putExtra(BUNDLE_NAME, loginAppViewModel.infoCurrentUser(userLogged))
//        }
        startActivity(Intent(binding.pbLogin.context, PrincipalActivity::class.java))
        requireActivity().finishAffinity()
    }


    private fun errorState(error: String) {
        binding.pbLogin.isVisible = false
        binding.tvRegister.isVisible = true
        binding.edMail.isEnabled = true
        binding.edPassword.isEnabled = true
        Toast.makeText(
            binding.pbLogin.context,
            error,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun loadingState() {
        binding.pbLogin.isVisible = true
        binding.tvRegister.isVisible = false
        binding.edMail.isEnabled = false
        binding.edPassword.isEnabled = false
        Toast.makeText(
            binding.pbLogin.context,
            getText(R.string.messageLoading),
            Toast.LENGTH_SHORT
        ).show()
    }
}