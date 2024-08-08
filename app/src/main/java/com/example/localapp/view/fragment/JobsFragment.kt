package com.example.localapp.view.fragment

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.localapp.viewmodel.JobsViewModel
import com.example.localapp.databinding.FragmentJobsBinding
import com.example.localapp.view.adapter.JobsAdapter
import kotlinx.coroutines.launch

class JobsFragment : Fragment() {
    private lateinit var jobsViewModel: JobsViewModel
    private lateinit var jobsAdapter: JobsAdapter
    private var isLoading = false
    private lateinit var binding: FragmentJobsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJobsBinding.inflate(inflater, container, false)
        val root = binding.root

        jobsAdapter = JobsAdapter(
            onBookmarkClick = { job ->
                viewLifecycleOwner.lifecycleScope.launch {
                    jobsViewModel.toggleBookmark(job)
                    jobsAdapter.updateBookmarkStatus(job.id)
                }
            },
            isJobBookmarked = { jobId ->
                jobsViewModel.isJobBookmarked(jobId)
            },
            requireContext()
        )

        binding.recyclerViewJobs.adapter = jobsAdapter
        binding.recyclerViewJobs.layoutManager = LinearLayoutManager(context)

        jobsViewModel = ViewModelProvider(this).get(JobsViewModel::class.java)

        binding.recyclerViewJobs.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                if (!isLoading && layoutManager.findLastVisibleItemPosition() == jobsAdapter.itemCount - 1) {
                    if (isNetworkAvailable()) {
                        jobsViewModel.loadMoreJobs()
                    } else {
                        Toast.makeText(requireContext(), "No internet connection", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        jobsViewModel.jobs.observe(viewLifecycleOwner, Observer { jobs ->
            jobsAdapter.submitList(jobs)
        })

        jobsViewModel.isLoading.observe(viewLifecycleOwner, Observer { loading ->
            isLoading = loading
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        })

        return root
    }

    override fun onResume() {
        super.onResume()
        refreshJobs()
    }

    private fun refreshJobs() {
        if (isNetworkAvailable()) {
            jobsViewModel.loadMoreJobs()
        } else {
            Toast.makeText(requireContext(), "No internet connection", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
}