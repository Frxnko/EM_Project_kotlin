package com.em.emproject.ui.projects.allProjects

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.em.emproject.data.provider.ProjectInfoProvider
import com.em.emproject.data.repository.ProjectRepository
import com.em.emproject.di.IntentClass
import com.em.emproject.domain.model.Project
import com.em.emproject.domain.model.ProjectInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailAllViewModel @Inject constructor(
    private var intentClass: IntentClass,
    private val projectRepository: ProjectRepository
) : ViewModel() {


    private var _state = MutableStateFlow<DetailAllState>(DetailAllState.Loading)
    val state: StateFlow<DetailAllState> = _state


    private val _stateSaveProject = MutableStateFlow<SaveProjectState>(SaveProjectState.Loading)
    val stateSaveProject: StateFlow<SaveProjectState> = _stateSaveProject

    private val _stateExistProject = MutableStateFlow<ExistProjectState>(ExistProjectState.Loading)
    val stateExistProject: StateFlow<ExistProjectState> = _stateExistProject

    fun getProject(codeProject: String) {
        viewModelScope.launch {
            //HIlo principal
            _state.value = DetailAllState.Loading

            val resultado =
                withContext((Dispatchers.IO)) { getInformation(codeProject) } //Hilo Secundario
            if (resultado != null) {
                _state.value = DetailAllState.Success(resultado, resultado.codeProject)
            } else {
                _state.value = DetailAllState.Error("Ha ocurrido un error")
            }
            //HIlo principal

        }
    }

    private fun getInformation(node: String): ProjectInfo {
        val nodosFiltrados =
            ProjectInfoProvider.projectInfoList.filter { nodo -> nodo.projectName.contains(node) }
        return nodosFiltrados[0]
    }

    fun openLink(string: String, context: Context) {
        intentClass.openlink(string, context)
    }


    fun saveProject(project: Project) {
        viewModelScope.launch {
            _stateSaveProject.value = SaveProjectState.Loading
            withContext(Dispatchers.IO) {
                val result = projectRepository.insertProject(project)
                if (result != null) {
                    _stateSaveProject.value = SaveProjectState.Success(project, project.projectName)
                } else {
                    _stateSaveProject.value = SaveProjectState.Error("Error")
                }
            }


        }
    }

    fun existProject(project: String) {
        viewModelScope.launch {
            _stateExistProject.value = ExistProjectState.Loading
            withContext(Dispatchers.IO) {
                val result = projectRepository.existProject(project)
                if (result) {
                    _stateExistProject.value = ExistProjectState.Exist("Exist")
                } else {
                    _stateExistProject.value =
                        ExistProjectState.NoExist(project)
                }
            }
        }
    }

//    fun addProjectToBD(project: ProjectEntity) {
//        viewModelScope.launch(Dispatchers.IO) { repository.insertProjectFromList(project) }
//    }
//
//    fun addProjectToFireBase(project: Project) {
//        viewModelScope.launch(Dispatchers.IO) { serviceFirebase.insertProjectFromListToFirebase(project) }
//    }


}