package com.em.emproject.ui.projects.detailAssignedProject.progress

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.em.emproject.databinding.FragmentProgressBinding
import com.em.emproject.ui.projects.AssignedProjectsShareViewModel
import com.em.emproject.ui.projects.detailAssignedProject.DetailActivity
import com.em.emproject.ui.projects.detailAssignedProject.DetailActivity.Companion.CODE_PROJECT_DETAIL
import com.example.emcontrol.domain.model.ItemProgress
import com.example.emcontrol.ui.detail.progress.adapter.ProgressAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProgressFragment : Fragment() {

    private var _binding: FragmentProgressBinding? = null
    private val binding get() = _binding!!

    private val progressViewModel by viewModels<ProgressViewModel>()
    private val assignedProjectsShareViewModel: AssignedProjectsShareViewModel by activityViewModels()

    private var _codeProject: String = ""
    private val codeProject get() = _codeProject

    private lateinit var progressAdapter: ProgressAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProgressBinding.inflate(layoutInflater, container, false)
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

    private fun initListener() {
        binding.btAddList.setOnClickListener {
            saveItemsToDB()
        }

        progressAdapter = ProgressAdapter(emptyList(), binding.etDateProgress.text.toString())
        binding.rvProgress.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = progressAdapter
        }
    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                progressViewModel.getProgressList(CODE_PROJECT_DETAIL)
                progressViewModel.state.collect {
                    when (it) {
                        is LoadProgressItemState.Error -> errorState(it.error)
                        LoadProgressItemState.Loading -> loadingState()
                        is LoadProgressItemState.Success -> successState(it.listProgress)
                    }

                }
            }
        }
    }

    private fun successState(listProgress: List<ItemProgress>) {

        binding.pbProgress.isVisible = false

        if (listProgress.isEmpty()) {
            binding.llHaveItem.isVisible = false
            binding.rvProgress.isVisible = false
            binding.llNoItem.isVisible = true
        } else {
            binding.llHaveItem.isVisible = true
            binding.rvProgress.isVisible = true
            binding.llNoItem.isVisible = false
            progressAdapter.updateList(listProgress)
        }

    }

    private fun errorState(error: String) {
        binding.pbProgress.isVisible = false
    }

    private fun loadingState() {
        binding.pbProgress.isVisible = true

        binding.llHaveItem.isVisible = false
        binding.rvProgress.isVisible = false
        binding.llNoItem.isVisible = false
    }



    private fun saveItemsToDB() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                progressViewModel.saveProgressItem(CODE_PROJECT_DETAIL)
                progressViewModel.stateSaved.collect {
                    when (it) {
                        is SavedProgressState.Error -> errorSaveState(it.error)
                        SavedProgressState.Loading -> loadingSaveState()
                        is SavedProgressState.Success -> successSaveState(it.listProgress)
                        is SavedProgressState.NoSuccess -> noSuccessSaveState()
                    }

                }
            }
        }

    }

    private fun noSuccessSaveState() {
        binding.pbProgress.isVisible = false

        binding.llHaveItem.isVisible = false
        binding.rvProgress.isVisible = false
        binding.llNoItem.isVisible = true
    }

    private fun successSaveState(listProgress: List<ItemProgress>) {
        binding.pbProgress.isVisible = false

        binding.llHaveItem.isVisible = true
        binding.rvProgress.isVisible = true
        binding.llNoItem.isVisible = false

//        progressAdapter.updateList(listProgress)
    }

    private fun loadingSaveState() {
        binding.pbProgress.isVisible = true

        binding.llHaveItem.isVisible = false
        binding.rvProgress.isVisible = false
        binding.llNoItem.isVisible = false
    }

    private fun errorSaveState(error: String) {
        binding.pbProgress.isVisible = false

        binding.llHaveItem.isVisible = false
        binding.rvProgress.isVisible = false
        binding.llNoItem.isVisible = true
    }
}