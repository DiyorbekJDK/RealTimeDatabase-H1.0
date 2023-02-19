package org.diyorbek.realtimedatabase_h10.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import org.diyorbek.realtimedatabase_h10.R
import org.diyorbek.realtimedatabase_h10.activity.MainActivity
import org.diyorbek.realtimedatabase_h10.databinding.FragmentLoginBinding
import org.diyorbek.realtimedatabase_h10.databinding.FragmentRegisterBinding
import org.diyorbek.realtimedatabase_h10.model.User
import org.diyorbek.realtimedatabase_h10.util.makeText
import org.diyorbek.realtimedatabase_h10.util.toast
import java.util.*

class RegisterFragment : Fragment(R.layout.fragment_register) {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val auth by lazy { FirebaseAuth.getInstance() }
    private var photoUri: Uri? = null
    private val db by lazy { FirebaseDatabase.getInstance() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        allCode()
    }

    private fun allCode() {
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.imageView.setOnClickListener {
            photoLauncher.launch("image/*")
        }
        binding.loginButton.setOnClickListener {
            val name = binding.nameEditText.makeText()
            val email = binding.emailEditText.makeText()
            val password = binding.passwordEditText.makeText()
            if (email.isNotBlank() && password.isNotBlank() && name.isNotBlank()) {
                binding.loadingProgressBar.isIndeterminate = true
                register(name,email,password)
            }else{
                toast("Enter full information")
            }
        }
    }

    private fun register(name: String, email: String, password: String) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    addToDatabase(name,email,password,auth.currentUser?.uid!!)
                }
                .addOnFailureListener {
                    toast(it.message.toString())
                    Log.d("&&&", "addToDatabase: ${it.message.toString()}")

                }
    }

    private fun addToDatabase(name: String, email: String, password: String,uid: String) {
        val fileName = UUID.randomUUID().toString()
                val ref = FirebaseStorage.getInstance().getReference("image/$fileName")
        ref.putFile(photoUri!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    db.getReference("users/$uid")
                        .setValue(User(name, email, password, uid,it.toString()))
                        .addOnSuccessListener {
                            activity?.startActivity(Intent(requireContext(),MainActivity::class.java))
                            binding.loadingProgressBar.isIndeterminate = false
                            toast("User created")
                        }
                }
                    .addOnFailureListener {
                        toast(it.message.toString())
                        binding.loadingProgressBar.isIndeterminate = false
                        Log.d("@@@", "addToDatabase: ${it.message.toString()}")
                    }
            }
    }

    private val photoLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let { uri ->
            photoUri = uri
            binding.imageView.setImageURI(uri)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}