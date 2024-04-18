package com.em.emproject.ui.projects.detailAssignedProject.Information

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.em.emproject.data.repository.ProjectRepository
import com.em.emproject.domain.model.Project
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class InformationViewModel @Inject constructor(private val projectRepository: ProjectRepository) :
    ViewModel() {

    private var _state =
        MutableStateFlow<InformationState>(InformationState.Loading)
    val state: StateFlow<InformationState> = _state

    fun getProject(codeProject: String) {
        viewModelScope.launch {
            //HIlo principal
            _state.value = InformationState.Loading
            val resultado =
                withContext((Dispatchers.IO)) { getInformation(codeProject) } //Hilo Secundario
            if (resultado != null) {
                _state.value = InformationState.Success(resultado, resultado.projectName)
            } else {
                _state.value = InformationState.Error("Ha ocurrido un error")
            }
            //HIlo principal
        }
    }

    private suspend fun getInformation(codeProject: String): Project? {
        return projectRepository.getProject(codeProject)

    }

    fun getDays(dateEnd: String?, dateStart: String?): String {
        val dateStarAux: Date
        val dateEndAux: Date
        val diffDays: Long
        val c = Calendar.getInstance()

        if (dateEnd.isNullOrBlank() && dateStart.isNullOrBlank()) return "-"
        else {
            return if (dateStart.isNullOrBlank()) {
                ""
            } else {
                if (dateEnd.isNullOrBlank()) {
                    dateEndAux = c.time
                    dateStarAux = SimpleDateFormat("yyyy-MM-dd").parse(dateStart)
                    diffDays = (dateEndAux.time - dateStarAux.time) / 86400000
                    diffDays.toString()
                } else {
                    dateEndAux = SimpleDateFormat("yyyy-MM-dd").parse(dateEnd)
                    dateStarAux = SimpleDateFormat("yyyy-MM-dd").parse(dateStart)
                    diffDays = (dateEndAux.time - dateStarAux.time) / 86400000
                    diffDays.toString()
                }
            }
        }
    }

//    fun editLocation(editProjectMap: HashMap<String, Any>) :Boolean{
//        return projectService.editInformation(editProjectMap)
//
//    }

}