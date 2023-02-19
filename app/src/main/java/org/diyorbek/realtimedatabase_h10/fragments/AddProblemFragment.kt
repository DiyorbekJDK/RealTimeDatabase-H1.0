package org.diyorbek.realtimedatabase_h10.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.diyorbek.realtimedatabase_h10.R
import org.diyorbek.realtimedatabase_h10.databinding.FragmentDashboardBinding
import org.diyorbek.realtimedatabase_h10.model.Problem
import org.diyorbek.realtimedatabase_h10.util.makeText
import org.diyorbek.realtimedatabase_h10.util.toast

class AddProblemFragment : Fragment(R.layout.fragment_dashboard) {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val dbRef by lazy { FirebaseDatabase.getInstance().reference }
    private val auth by lazy { FirebaseAuth.getInstance() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val uid = auth.currentUser?.uid!!

        binding.addBtn.setOnClickListener {
            val name = binding.nameEditText.makeText()
            val addres = binding.adresEditText.makeText()
            val date = binding.dateEditText.makeText()
            val switcher = binding.switch1
            dbRef.child("problems").push()
                .setValue(Problem(uid,name,switcher.isChecked,addres,date))
                .addOnSuccessListener {
                    binding.nameEditText.text?.clear()
                    binding.adresEditText.text?.clear()
                    binding.dateEditText.text?.clear()
                    binding.switch1.isChecked = false
                    toast("Saved!")
                }
                .addOnFailureListener {
                    toast(it.message.toString())
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}