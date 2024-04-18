package com.em.emproject.ui.projects.detailAssignedProject.progress

import com.example.emcontrol.domain.model.ItemProgress

sealed class SavedProgressState {
    data object Loading : SavedProgressState()
    data class Error(val error: String) : SavedProgressState()
    data class Success(val listProgress: List<ItemProgress>) : SavedProgressState()
    data class NoSuccess(val errorNoSuccess: String) : SavedProgressState()
}