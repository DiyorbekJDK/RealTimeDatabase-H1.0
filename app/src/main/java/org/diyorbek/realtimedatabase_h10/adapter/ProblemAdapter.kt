package org.diyorbek.realtimedatabase_h10.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.diyorbek.realtimedatabase_h10.databinding.ProblemLayoutBinding
import org.diyorbek.realtimedatabase_h10.model.Problem
import java.util.*


class ProblemAdapter : ListAdapter<Problem, ProblemAdapter.ProblemViewHolder>(DiffCallBack()) {
    lateinit var onClick: (Problem) -> Unit

    private class DiffCallBack : DiffUtil.ItemCallback<Problem>() {
        override fun areItemsTheSame(oldItem: Problem, newItem: Problem): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(oldItem: Problem, newItem: Problem): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProblemViewHolder {
        return ProblemViewHolder(
            ProblemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ProblemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ProblemViewHolder(private val binding: ProblemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(problem: Problem) {
            binding.apply {
                problemName.text = problem.problemName
                problemAddres.text = problem.problemAddress
                problemDate.text = problem.problemDate
                if (problem.isFinished) {
                    binding.isItInProgress.setCardBackgroundColor(Color.parseColor("#0BE314"))
                    binding.inProgressText.text = "Finished"
                } else {
                    binding.isItInProgress.setCardBackgroundColor(Color.parseColor("#FF5722"))
                    binding.inProgressText.text = "In Progress"

                }
                binding.cardView.setCardBackgroundColor(randomColor())

            }
            itemView.setOnClickListener {
                onClick(problem)
            }
        }
    }

    private fun randomColor(): Int {
        val random = Random()
        return Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
    }
}