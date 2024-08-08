package com.example.localapp.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.localapp.database.ResultEntity
import com.example.localapp.databinding.ActivityJobDetailsBinding
import com.example.localapp.model.ResultData
import com.example.localapp.utility.JobDataHolder

class JobDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJobDetailsBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val job: ResultData? = JobDataHolder.selectedJob
        val bookmarkjob: ResultEntity? = JobDataHolder.bookmarkedob

        if (job != null) {
            displayJobDetails(job)
        } else if (bookmarkjob != null) {
            displayBookmarkJobDetails(bookmarkjob)
        } else {
            displayEmptyState()
        }

        setupJobDetailsToggle()
        setupCallButton(job?.whatsapp_no)
    }

    private fun displayJobDetails(job: ResultData) {
        binding.apply {
            jobTitle.text = job.title?.takeIf { it.isNotBlank() } ?: "N/A"
            companyName.text = job.company_name?.takeIf { it.isNotBlank() } ?: "N/A"
            location.text = "Location: ${job.primary_details?.Place ?: "N/A"}"
            jobType.text = "Job Type: ${job.job_hours?.takeIf { it.isNotBlank() } ?: "N/A"}"
            experience.text = "Experience: ${job.experience?.toString() ?: "N/A"} years"
            openingsCount.text = "Openings: ${job.openings_count?.toString() ?: "N/A"}"
            createdOn.text = "Created On: ${job.created_on?.takeIf { it.isNotBlank() } ?: "N/A"}"
            expiresOn.text = "Expires On: ${job.expire_on?.takeIf { it.isNotBlank() } ?: "N/A"}"
            jobCategory.text = "Category: ${job.job_category?.takeIf { it.isNotBlank() } ?: "N/A"}"
            jobRole.text = "Role: ${job.job_role?.takeIf { it.isNotBlank() } ?: "N/A"}"
            numApplications.text = "Applications: ${job.num_applications?.toString() ?: "N/A"}"
            jobLocation.text = "Location: ${job.job_location_slug?.takeIf { it.isNotBlank() } ?: "N/A"}"
            views.text = "Views: ${job.views?.toString() ?: "N/A"}"
            shares.text = "Shares: ${job.shares?.toString() ?: "N/A"}"

            if (!job.button_text.isNullOrEmpty()) {
                callButton.text = job.button_text
            }
        }
    }

    private fun displayBookmarkJobDetails(bookmarkjob: ResultEntity) {
        binding.apply {
            jobTitle.text = bookmarkjob.title?.takeIf { it.isNotBlank() } ?: "N/A"
            companyName.text = bookmarkjob.company_name?.takeIf { it.isNotBlank() } ?: "N/A"
            location.text = "Location N/A"
            jobType.text = "Job Type: ${bookmarkjob.job_hours?.takeIf { it.isNotBlank() } ?: "N/A"}"
            experience.text = "Experience: ${bookmarkjob.experience?.toString() ?: "N/A"} years"
            openingsCount.text = "Openings: ${bookmarkjob.openings_count?.toString() ?: "N/A"}"
            createdOn.text = "Created On: ${bookmarkjob.created_on?.takeIf { it.isNotBlank() } ?: "N/A"}"
            expiresOn.text = "Expires On: ${bookmarkjob.expire_on?.takeIf { it.isNotBlank() } ?: "N/A"}"
            jobCategory.text = "Category: ${bookmarkjob.job_category?.takeIf { it.isNotBlank() } ?: "N/A"}"
            jobRole.text = "Role: ${bookmarkjob.job_role?.takeIf { it.isNotBlank() } ?: "N/A"}"
            numApplications.text = "Applications: ${bookmarkjob.num_applications?.toString() ?: "N/A"}"
            jobLocation.text = "Location: ${bookmarkjob.job_location_slug?.takeIf { it.isNotBlank() } ?: "N/A"}"
            views.text = "Views: ${bookmarkjob.views?.toString() ?: "N/A"}"
            shares.text = "Shares: ${bookmarkjob.shares?.toString() ?: "N/A"}"

            if (!bookmarkjob.button_text.isNullOrEmpty()) {
                callButton.text = bookmarkjob.button_text
            }
        }
    }

    private fun displayEmptyState() {

        binding.jobTitle.text = "No job details available"
    }

    private fun setupJobDetailsToggle() {
        binding.jobDetails.setOnClickListener {
            binding.jobDetailsContent.visibility = if (binding.jobDetailsContent.visibility == View.VISIBLE) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
    }

    private fun setupCallButton(phoneNumber: String?) {
        binding.callButton.setOnClickListener {
            phoneNumber?.takeIf { it.isNotBlank() }?.let { number ->
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
                startActivity(intent)
            }
        }
    }
}