package com.em.emproject.ui.projects.statusProjects

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.em.emproject.R
import com.em.emproject.databinding.FragmentStatusProjectsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatusProjectsFragment : Fragment() {

    private var _binding: FragmentStatusProjectsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatusProjectsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

}