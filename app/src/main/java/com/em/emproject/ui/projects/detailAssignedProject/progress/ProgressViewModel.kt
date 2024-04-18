package com.em.emproject.ui.projects.detailAssignedProject.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.em.emproject.data.provider.ItemProgressInfoProvider.Companion.itemProjectTowerList
import com.em.emproject.data.repository.ProjectRepository
import com.em.emproject.domain.model.RegisterProgress
import com.em.emproject.ui.projects.detailAssignedProject.DetailActivity.Companion.CODE_PROJECT_DETAIL
import com.example.emcontrol.domain.model.ItemProgress
import com.example.emcontrol.domain.model.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProgressViewModel @Inject constructor(private val projectRepository: ProjectRepository) :
    ViewModel() {

    private var _listProgress = MutableStateFlow<List<ItemProgress>?>(emptyList())
    val listProgress: StateFlow<List<ItemProgress>?> = _listProgress

    private val _state = MutableStateFlow<LoadProgressItemState>(LoadProgressItemState.Loading)
    val state: StateFlow<LoadProgressItemState> = _state

    private val _stateSaved = MutableStateFlow<SavedProgressState>(SavedProgressState.Loading)
    val stateSaved: StateFlow<SavedProgressState> = _stateSaved


    companion object {
        var HAVE_ITEMS_PROJECT: Boolean = false
        var list_item_progress: List<ItemProgress> = emptyList()
        var progress_Project: Double? = 0.00
        var progress_date: String = ""
    }

    private var _infoProgressProject = MutableStateFlow<List<ItemProgress>>(emptyList())
    val infoProgressProject: StateFlow<List<ItemProgress>> = _infoProgressProject


    private var _haveItem: Boolean = false
    val haveItem: Boolean = _haveItem


//    init {
//        viewModelScope.launch {
//
//            getProgresstList()
//            list_item_progress = _infoProgressProject.value
//            HAVE_ITEMS_PROJECT = _infoProgressProject.toList().isEmpty()
//
//        }
//    }

    fun getProgressList(projectName: String) {
        viewModelScope.launch {
            _state.value = LoadProgressItemState.Loading
            _listProgress.value = projectRepository.getProgressProject(projectName)
            val listAux = _listProgress.value

            if (listAux != null) {
                _state.value = LoadProgressItemState.Success(_listProgress.value!!)
                list_item_progress = _listProgress.value!!

            } else {
                _state.value = LoadProgressItemState.Error("Error")
            }

        }
    }

    fun saveProgressItem(nameProject: String) {
        viewModelScope.launch {
            _stateSaved.value = SavedProgressState.Loading

            val savedValues: Boolean? = projectRepository.saveProgressProjectItem(
                RegisterProgress(
                    nameProject, itemProjectTowerList.map { it.toDomain() },
                    emptyList()
                )
            )
            when (savedValues) {
                true -> {
                    _stateSaved.value = SavedProgressState.Success(itemProjectTowerList.map {
                        it.toDomain()
                    })
                    list_item_progress = itemProjectTowerList.map { it.toDomain() }
                }

                false -> {
                    _stateSaved.value = SavedProgressState.NoSuccess("No Saved")
                }

                null -> {
                    _stateSaved.value = SavedProgressState.Error("Error")
                }
            }


        }


    }


//    private fun getProjectList() {
//        val infoProgressList: ArrayList<ItemProgress> = ArrayList()
//
//        val ref = FirebaseDatabase.getInstance().getReference(TB_PROJECT_ASSIGNED_ITEMS)
//            .child(CODE_PROJECT_DETAIL)
//
//        ref.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                infoProgressList.clear()
//                for (ds in snapshot.children) {
//                    val itemProgressToRead = ds.getValue(ItemProgress::class.java)
//                    infoProgressList.add(itemProgressToRead!!)
//                }
//                _infoProgressProject.value = infoProgressList.toList()
//                list_item_progress = infoProgressList.toList()
//
//
////                list_item_progress = infoProgressList.toList()
//                _haveItem = infoProgressList.size != 0
//
////                GetProjectUseCase.listProjectGlobal =infoProgressList.toList()
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//        })
//
//
//    }
//
//    fun getInformationProject(node: String): Project {
//        val projectsFilter = listProjectGlobal.filter { project -> project.site.contains(node) }
//        return projectsFilter[0]
//    }


}