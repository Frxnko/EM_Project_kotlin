package com.em.emproject.domain.model

data class Paralyzed(
    val dateStart: String,
    val dateEnd: String,
    val daysParalyzed: String,
    val description: String,
    val isResolved: Boolean,
    val descriptionResolved: String,
)