package com.em.emproject.ui.projects.detailAssignedProject.Information

import com.em.emproject.domain.model.Project

sealed class InformationState {
    data object Loading: InformationState()
    data class Error(val error:String): InformationState()
    data class Success(val project: Project, val projectCode:String): InformationState()
}