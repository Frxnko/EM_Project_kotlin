package com.em.emproject.domain.model

data class Project(
    val codeProject: String = "",
    val projectName: String = "",
    val nodeType: String = "",
    val red: String = "",
    val address: String? = "",
    val locality: String? = "",
    val district: String? = "",
    val province: String? = "",
    val region: String? = "",
    val approvedCandidateRFT: String? = "",
    val latitude: Double? = 0.0,
    val longitude: Double? = 0.0,

    val bathAssociated: String? = "",

    val towerType: String? = "",
    val towerHeight: String? = "",
    val towerSupplier: String? = "",
    val civilDesignAvailability: String?,
    val nasVersion: String?,
    val linkNewProject: String?,
    val linkUpdate: String?,
    val linkHighSPAT: String?,

    val dateStart: String? = "",
    val dateEnd: String? = "",
    val contractor: String? = "",
    val status: String? = "",
    val image: String? = "",
    val progress: Double? = 0.0,
    val dateRegisterProgress: String? = "",
    val lastReportContractor: String? = "",
    val lastReportSupervision: String? = "",
    val comments: String? = "",
    val additional: String? = "",
    val comments02: String? = "",
    val pma: String? = "",
    val papRealized: String? = "",

    val zone: String? = "",

    val projectStatusItem: List<ProjectStatusItem?> = listOf(),
    val mistakes: List<Mistakes?> = listOf(),
    val supervisor: List<Supervisor?> = listOf(),
    val paralyzed: List<Paralyzed?> = listOf(),

    val created: String = ""


)
