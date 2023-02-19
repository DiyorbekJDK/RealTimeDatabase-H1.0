package org.diyorbek.realtimedatabase_h10.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentContainer
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import org.diyorbek.realtimedatabase_h10.R
import org.diyorbek.realtimedatabase_h10.activity.FragmentContainerActivity
import org.diyorbek.realtimedatabase_h10.activity.MainActivity
import org.diyorbek.realtimedatabase_h10.databinding.FragmentHomeBinding
import org.diyorbek.realtimedatabase_h10.databinding.FragmentLoginBinding
import org.diyorbek.realtimedatabase_h10.util.makeText
import org.diyorbek.realtimedatabase_h10.util.toast

class LoginFragment : Fragment(R.layout.fragment_login) {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val auth by lazy { FirebaseAuth.getInstance() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (auth.currentUser == null){
            allCode()
        }else{
            activity?.startActivity(Intent(requireContext(), MainActivity::class.java))
            activity?.finish()
        }
    }

    private fun allCode() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.makeText()
            val password = binding.passwordEditText.makeText()
            if (email.isNotBlank() && password.isNotBlank()) {
                binding.loadingProgressBar.isIndeterminate = true
                auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        toast("Successfully logged in")
                        activity?.startActivity(Intent(requireContext(), MainActivity::class.java))
                        activity?.finish()


                    }
                    .addOnFailureListener {
                        toast(it.message.toString())
                        binding.loadingProgressBar.isIndeterminate = false
                    }
            } else {
                toast("Enter Full information")
            }
        }
        binding.registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}