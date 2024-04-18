package com.example.emcontrol.domain.model

data class ItemProgressInfo(
    val item: String,
    val description: String,
    val incidence: Double,
    val progress: Double,
    val order: Int,
    val type: String,
    val group: String
)

fun ItemProgress.toDomain() = ItemProgressInfo(
    item,
    description,
    incidence,
    progress,
    order,
    type,
    group

)