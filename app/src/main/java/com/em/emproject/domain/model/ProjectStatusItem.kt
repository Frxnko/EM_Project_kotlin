package com.em.emproject.domain.model

data class ProjectStatusItem(
    val item: String = "",
    val description: String = "",
    val comments: String = "",
    val apply: Boolean = true,
    val order: Int = 0,
    val progress: Double = 0.00,
)