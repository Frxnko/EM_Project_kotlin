package com.em.emproject.ui.projects.detailAssignedProject.progress

import com.example.emcontrol.domain.model.ItemProgress

sealed class LoadProgressItemState {
    data object Loading : LoadProgressItemState()
    data class Error(val error: String) : LoadProgressItemState()
    data class Success(val listProgress: List<ItemProgress>) : LoadProgressItemState()
}