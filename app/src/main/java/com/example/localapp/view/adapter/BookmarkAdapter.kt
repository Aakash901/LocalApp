package com.example.localapp.view.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.localapp.utility.JobDataHolder
import com.example.localapp.database.ResultEntity
import com.example.localapp.databinding.ItemJobBinding
import com.example.localapp.view.activity.JobDetailsActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookmarkAdapter(
    private val onRemoveBookmark: suspend (ResultEntity) -> Unit
) : RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder>() {
    private var jobs: List<ResultEntity> = emptyList()
    private val adapterScope = CoroutineScope(Dispatchers.Main)

    inner class BookmarkViewHolder(private val binding: ItemJobBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(job: ResultEntity) {
            binding.textViewTitle.text = job.title.takeUnless { it.isNullOrEmpty() } ?: "Not available"
            binding.textViewLocation.text = job.job_location_slug.takeUnless { it.isNullOrEmpty() } ?: "Not available"

            val salaryText = if (job.salary_min == 0 && job.salary_max == 0) {
                "Salary not disclosed"
            } else {
                "${job.salary_min} - ${job.salary_max}"
            }
            binding.textViewSalary.text = salaryText

            binding.buttonBookmark.setOnClickListener {
                adapterScope.launch {
                    onRemoveBookmark(job)
                }
            }

            binding.textViewPhone.text = job.whatsapp_no.takeUnless { it.isNullOrEmpty() } ?: "Not available"

            binding.root.setOnClickListener {
                JobDataHolder.bookmarkedob = job
                val intent = Intent(binding.root.context, JobDetailsActivity::class.java)
                binding.root.context.startActivity(intent)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        val binding = ItemJobBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookmarkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        val job = jobs[position]
        holder.bind(job)
    }

    override fun getItemCount() = jobs.size

    fun submitList(newJobs: List<ResultEntity>) {
        jobs = newJobs
        notifyDataSetChanged()
    }
}