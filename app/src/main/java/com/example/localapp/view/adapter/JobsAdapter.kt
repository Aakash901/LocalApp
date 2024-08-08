package com.example.localapp.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.localapp.utility.JobDataHolder
import com.example.localapp.R
import com.example.localapp.databinding.ItemJobBinding
import com.example.localapp.model.ResultData
import com.example.localapp.view.activity.JobDetailsActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JobsAdapter(
    private val onBookmarkClick: (ResultData) -> Unit,
    private val isJobBookmarked: suspend (Int) -> Boolean,
    private val context: Context
) : RecyclerView.Adapter<JobsAdapter.JobViewHolder>() {

    private var jobs: List<ResultData> = listOf()
    private val adapterScope = CoroutineScope(Dispatchers.Main)

    private fun updateBookmarkIcon(imageButton: ImageButton, isBookmarked: Boolean) {
        val colorRes = if (isBookmarked) com.google.android.material.R.color.mtrl_tabs_colored_ripple_color else R.color.black
        val color = ContextCompat.getColor(imageButton.context, colorRes)
        imageButton.setColorFilter(color)
        if (isBookmarked) {
            imageButton.visibility = View.GONE
        }
    }

    fun updateBookmarkStatus(jobId: Int) {
        val position = jobs.indexOfFirst { it.id == jobId }
        if (position != -1) {
            notifyItemChanged(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val binding = ItemJobBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobViewHolder(binding)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val job = jobs[position]

        holder.binding.textViewTitle.text = job.title ?: "Title not available"

        holder.binding.textViewLocation.text = job.job_location_slug ?: "Location not available"

        val salaryText = if (job.salary_min == 0 && job.salary_max == 0) {
            "Salary not disclosed"
        } else {
            "${job.salary_min} - ${job.salary_max}"
        }
        holder.binding.textViewSalary.text = salaryText

        holder.binding.buttonBookmark.setOnClickListener {
            Toast.makeText(context, "job saved", Toast.LENGTH_SHORT).show()
            onBookmarkClick(job)
            adapterScope.launch {
                val isBookmarked = withContext(Dispatchers.IO) { isJobBookmarked(job.id) }
                updateBookmarkIcon(holder.binding.buttonBookmark, isBookmarked)
            }
        }

        holder.binding.textViewPhone.text = job.whatsapp_no ?: "Phone number not available"

        holder.itemView.setOnClickListener {
            JobDataHolder.selectedJob = job
            adapterScope.launch {
                val isBookmarked = withContext(Dispatchers.IO) { isJobBookmarked(job.id) }
                updateBookmarkIcon(holder.binding.buttonBookmark, isBookmarked)
            }
            val intent = Intent(holder.itemView.context, JobDetailsActivity::class.java)
            holder.itemView.context.startActivity(intent)
        }

        adapterScope.launch {
            val isBookmarked = withContext(Dispatchers.IO) { isJobBookmarked(job.id) }
            updateBookmarkIcon(holder.binding.buttonBookmark, isBookmarked)
        }
    }

    inner class JobViewHolder(val binding: ItemJobBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return jobs.size
    }

    fun submitList(newJobs: List<ResultData>) {
        jobs = newJobs
        notifyDataSetChanged()
    }
}