package com.em.emproject.ui.projects.assignedProjects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.em.emproject.domain.model.ProjectAssignedList
import com.em.emproject.domain.useCase.GetProjectUseCase
import com.em.emproject.domain.useCase.GetProjectUseCase.Companion.listProjectGlobal
import com.em.emventas.data.network.FireDataBaseService
import com.em.emventas.data.network.FirebaseClient
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AssignedProjectsViewModel @Inject constructor(
    private val getProjectUseCase: GetProjectUseCase,
    private val fireStore: FirebaseFirestore
) :
    ViewModel() {

    private var _projects = MutableStateFlow<List<ProjectAssignedList>>(emptyList())
    val projects: StateFlow<List<ProjectAssignedList>> = _projects

    private lateinit var projectList: ArrayList<ProjectAssignedList>

    init {
        viewModelScope.launch {
//            _projects.value =  getProjectUseCase.invoke()
//            _projects.value = getProjectUseCase.invoke()
//            listProjectGlobal = _projects.value

            insertProjectFromListToFirebase03()


        }

    }


    private fun insertProjectFromListToFirebase03(): List<ProjectAssignedList> {
        val ref = fireStore.collection(FireDataBaseService.PATH_PROJECT)

        ref.addSnapshotListener { snapshot, e ->
            e?.let {
                return@addSnapshotListener
            }
            snapshot?.let {
                val projectList = mutableListOf<ProjectAssignedList>()
                for (document in snapshot) {
                    //Verificar que el supervisor val documents = document.get("projectStatusItem")

                    // Obtener el campo name del documento y agregarlo a la lista
                    val projectName = document.getString("projectName")
                    val towerHeight = document.getString("towerHeight")
                    val towerSupplier = document.getString("towerSupplier")
                    val towerType = document.getString("towerType")
                    val contractor = document.getString("contractor")
                    val dateStart = document.getString("dateStart")
                    val dateEnd = document.getString("dateEnd")
                    val status = document.getString("status")
                    val image = document.getString("image")
                    val progress = document.getDouble("progress")
                    val lastReportContractor = document.getString("lastReportContractor")
                    val lastReportSupervision = document.getString("lastReportSupervision")

//                    val documents = document.get("projectStatusItem")

                    projectList.add(
                        ProjectAssignedList(
                            projectName!!,
                            towerHeight,
                            towerSupplier,
                            towerType,
                            contractor,
                            dateStart,
                            dateEnd,
                            status,
                            image,
                            progress,
                            lastReportContractor,
                            lastReportSupervision
                        )
                    )
                }
                _projects.value = projectList.toList()
                GetProjectUseCase.listProjectGlobal = projectList.toList()
            }
        }
        return GetProjectUseCase.listProjectGlobal
    }


}