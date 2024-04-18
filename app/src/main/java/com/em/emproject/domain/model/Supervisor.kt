package com.em.emproject.domain.model

data class Supervisor(
    val idSupervisor: String = "",
    val supervisor: String = "",
    val fromSupervision: String = "",
    val toSupervision: String = "",
    val isExpert: Boolean = false,
    val showProject: Boolean = true

)