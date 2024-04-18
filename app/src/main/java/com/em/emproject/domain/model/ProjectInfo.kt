package com.em.emproject.domain.model

data class ProjectInfo (
    val codeProject: String,
    val projectName: String,
    val nodeType: String,
    val red: String,
    val region: String,
    val province: String,
    val district: String,
    val locality: String,
    val approvedCandidateRFT: String?,
    val latitude: Double?,
    val longitude: Double?,
    val towerType: String?,
    val towerHeight: String?,
    val towerSupplier: String?,
    val nasVersion: String?,
    val civilDesignAvailability: String?,
    val linkNewProject: String?,
    val linkUpdate: String?,
    val linkHighSPAT: String?
)