package com.em.emproject.ui.projects.allProjects

import com.em.emproject.domain.model.Project

sealed class ExistProjectState {
    data object Loading:ExistProjectState()
    data class Exist(val error:String):ExistProjectState()
    data class NoExist( val projectCode:String):ExistProjectState()
}