package com.em.emproject.domain.model

import com.example.emcontrol.domain.model.ItemProgress

data class RegisterProgress(
    val projectName: String,
    val listProgress: List<ItemProgress>,
    val listRegister: List<Progress>
)

