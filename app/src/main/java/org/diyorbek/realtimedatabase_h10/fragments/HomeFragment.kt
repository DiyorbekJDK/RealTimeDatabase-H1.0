package org.diyorbek.realtimedatabase_h10.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.api.Distribution.BucketOptions.Linear
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.diyorbek.realtimedatabase_h10.R
import org.diyorbek.realtimedatabase_h10.adapter.ProblemAdapter
import org.diyorbek.realtimedatabase_h10.databinding.FragmentHomeBinding
import org.diyorbek.realtimedatabase_h10.model.Problem

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val db by lazy { FirebaseDatabase.getInstance().getReference("problems/") }
    private val problemAdapter  by lazy { ProblemAdapter() }
    private val problemList = mutableListOf<Problem>()
    private val uid by lazy { FirebaseAuth.getInstance().currentUser?.uid }




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rv.apply{
            adapter = problemAdapter
            layoutManager = GridLayoutManager(requireContext(),2,LinearLayoutManager.VERTICAL,false)
        }
        db.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                problemList.clear()
                for (sn in snapshot.children){
                    val probleem = sn.getValue(Problem::class.java)
                    if (probleem?.uid != uid){
                        problemList.add(probleem!!)
                    }
                }
                binding.progressBar.isVisible = false
                problemAdapter.submitList(problemList)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}