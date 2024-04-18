package com.example.emcontrol.domain.model

data class ItemProgress(
    val item: String = "",
    val description: String = "",
    val incidence: Double = 0.00,
    val progress: Double = 0.00,
    var status: Double = 0.00,
    val order: Int = 0,
    val type: String = "",
    val group: String = ""
)

fun ItemProgressInfo.toDomain() = ItemProgress(
    item = item,
    description = description,
    incidence = incidence,
    progress = progress,
    status = 0.00,
    order = order,
    type = type,
    group = group
)