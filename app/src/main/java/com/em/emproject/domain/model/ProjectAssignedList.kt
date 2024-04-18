package com.em.emproject.domain.model

data class ProjectAssignedList(
    val projectName: String = "",
    val towerHeight: String? = "",
    val towerSupplier: String? = "",
    val towerType: String? = "",
    val contractor: String? = "",
    val dateStart: String? = "",
    val dateEnd: String? = "",
    val status: String? = "",
    val image: String? = "",
    val progress: Double? = 0.0,
    val lastReportContractor: String? = "",
    val lastReportSupervision: String? = "",

    )