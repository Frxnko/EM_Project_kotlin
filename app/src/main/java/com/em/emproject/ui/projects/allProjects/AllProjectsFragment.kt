package com.em.emproject.ui.projects.allProjects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.em.emproject.data.provider.ProjectInfoProvider
import com.em.emproject.databinding.FragmentAllProjectsBinding
import com.em.emproject.domain.model.ProjectInfo
import com.example.emcontrol.ui.allProjects.adapter.AllProjectsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllProjectsFragment : Fragment() {


    private val allProjectsViewModel by viewModels<AllProjectsViewModel>()
    private lateinit var allProjectAdapter: AllProjectsAdapter

    private var _binding: FragmentAllProjectsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllProjectsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        intiUi()
    }

    private fun intiUi() {
        initListener()
        initUIState()
    }

    private fun initListener() {
        binding.searchView.setQuery("", false)

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()
                val projectsFilter = ProjectInfoProvider.projectInfoList.filter { nodo ->
                    nodo.projectName.lowercase().contains(query ?: "")
                }
                allProjectAdapter.updateListFilter(projectsFilter)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val projectsFilter = ProjectInfoProvider.projectInfoList.filter { nodo ->
                    nodo.projectName.lowercase().contains(newText ?: "")
                }
                allProjectAdapter.updateListFilter(projectsFilter)
                return false
            }

        })

        allProjectAdapter = AllProjectsAdapter(onItemSelected = {
//            Toast.makeText(context, it.site, Toast.LENGTH_LONG).show()
//            editText.setText("")
            findNavController().navigate(
                AllProjectsFragmentDirections.actionAllProjectsFragmentToDetailAllActivity(it.projectName)
            )
        })

        binding.rvAllProjects.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = allProjectAdapter
        }
    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                allProjectsViewModel.infoProjects.collect {
                    allProjectAdapter.updateList(it)

                }
            }
        }
    }


}