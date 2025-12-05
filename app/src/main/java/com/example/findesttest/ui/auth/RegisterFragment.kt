package com.example.findesttest.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.findesttest.R
import com.example.findesttest.databinding.FragmentRegisterBinding
import com.example.findesttest.utils.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            val username = binding.etRegUsername.text.toString()
            val email = binding.etRegEmail.text.toString()
            val password = binding.etRegPassword.text.toString()
            val confirmPassword = binding.etRegConfirmPassword.text.toString()

            if (password != confirmPassword) {
                Toast.makeText(context, "Password doesnt match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.register(username = username, email = email, password = password)
        }

        binding.tvGoToLogin.setOnClickListener {
            viewModel.resetRegisterState()
            findNavController().navigateUp()
        }

        viewModel.registerState.observe(viewLifecycleOwner) {state ->
            when(state) {
                is UiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnRegister.isEnabled = false
                }
                is UiState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, "Registration successful! Please Login", Toast.LENGTH_SHORT).show()
                    viewModel.resetRegisterState()
                    findNavController().navigateUp()
                }
                is UiState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnRegister.isEnabled = true
                    Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                }
                else -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}