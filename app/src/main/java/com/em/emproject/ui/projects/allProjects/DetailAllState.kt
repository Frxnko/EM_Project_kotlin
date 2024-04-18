package com.em.emproject.ui.projects.allProjects

import com.em.emproject.domain.model.ProjectInfo

sealed class DetailAllState {
    data object Loading:DetailAllState()
    data class Error(val error:String):DetailAllState()
    data class Success(val project: ProjectInfo, val projectCode:String):DetailAllState()
}