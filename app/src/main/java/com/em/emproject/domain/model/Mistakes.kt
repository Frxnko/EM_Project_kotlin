package com.em.emproject.domain.model

data class Mistakes(
    val descriptionMistakes: String = "",
    val dateRegister: String = "",
    val dateSend: String = "",
    val location: String = "",
    val typeMistakes: String = "",
    val department: String = "",
    val impact:String="",
    val references: String = "",
    val comments: String = "",
    val images: String = "",
    val recommendationsConclusions: String = "",

    val groupId: String = "",
    val descriptionGroup: String = "",
    val levelMistake: String = "",

    val statusMistakes: String="",

    val resident: String = "",
    val dateReparation: String="",
    val descriptionReparation: String="",


    )

