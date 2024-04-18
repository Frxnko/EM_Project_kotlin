package com.em.emproject.ui.projects.allProjects

import com.em.emproject.domain.model.Project
import com.em.emproject.domain.model.ProjectInfo

sealed class SaveProjectState {
    data object Loading : SaveProjectState()
    data class Error(val error: String) : SaveProjectState()
    data class Success(val project: Project, val projectCode: String) : SaveProjectState()
}