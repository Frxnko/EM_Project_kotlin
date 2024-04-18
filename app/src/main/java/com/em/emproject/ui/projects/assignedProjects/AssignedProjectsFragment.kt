package com.em.emproject.ui.projects.assignedProjects

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.em.emproject.databinding.FragmentAssignedProjectBinding
import com.em.emproject.domain.useCase.GetProjectUseCase.Companion.listProjectGlobal
import com.example.emcontrol.ui.projects.adapter.ProjectsAssignedAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AssignedProjectsFragment : Fragment() {

    private val assignedProjectsViewModel by viewModels<AssignedProjectsViewModel>()
    private lateinit var projectAdapter: ProjectsAssignedAdapter

    private var _binding: FragmentAssignedProjectBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAssignedProjectBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        initListener()
        initUIState()
    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                assignedProjectsViewModel.projects.collect {
                    projectAdapter.updateList(it)

                }
            }
        }
    }

    private fun initListener() {

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()
                val projectsFilter = listProjectGlobal.filter { node ->
                    node.projectName.contains(query ?: "")
                }
                projectAdapter.updateListFilter(projectsFilter)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val projectsFilter = listProjectGlobal.filter { node ->
                    node.projectName.contains(newText ?: "")
                }
                projectAdapter.updateListFilter(projectsFilter)
                return false
            }

        })

        projectAdapter = ProjectsAssignedAdapter(onItemSelected = {
            findNavController().navigate(
                AssignedProjectsFragmentDirections.actionAssignedProjectsFragmentToDetailActivity(it.projectName)
            )
        })

        binding.rvProjects.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = projectAdapter
        }
    }

}