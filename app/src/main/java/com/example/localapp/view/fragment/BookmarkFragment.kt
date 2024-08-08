package com.example.localapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.localapp.viewmodel.JobsViewModel
import com.example.localapp.databinding.FragmentBookmarksBinding
import com.example.localapp.view.adapter.BookmarkAdapter
import kotlinx.coroutines.launch

class BookmarkFragment : Fragment() {
    private lateinit var jobsViewModel: JobsViewModel
    private lateinit var bookmarkAdapter: BookmarkAdapter
    private lateinit var binding: FragmentBookmarksBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookmarksBinding.inflate(inflater, container, false)
        val view = binding.root

        jobsViewModel = ViewModelProvider(this).get(JobsViewModel::class.java)

        bookmarkAdapter = BookmarkAdapter(
            onRemoveBookmark = { job ->
                viewLifecycleOwner.lifecycleScope.launch {
                    jobsViewModel.removeBookmark(job)
                }
            }
        )

        binding.recyclerViewBookmark.adapter = bookmarkAdapter
        binding.recyclerViewBookmark.layoutManager = LinearLayoutManager(context)

        jobsViewModel.bookmarkedJobs.observe(viewLifecycleOwner, Observer { jobs ->
            bookmarkAdapter.submitList(jobs)
            if (jobs.isEmpty()) {
                binding.noItem.visibility = View.VISIBLE
            } else {
                binding.noItem.visibility = View.GONE
            }
        })

        jobsViewModel.isLoadingBookmarks.observe(viewLifecycleOwner, Observer { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        })

        jobsViewModel.loadBookmarkedJobs()

        return view
    }
}