package com.em.emproject.domain.model

import com.em.emproject.R

sealed class StatusProject(val description: Int, val codeStatus: Int, val colorStatus: Int) {

    data object NoStarted :
        StatusProject(R.string.notStarted, R.string.notStartedCode, R.color.notStarted)

    data object OnProgress :
        StatusProject(R.string.onProgress, R.string.onProgressCode, R.color.onProgress)

    data object Paralyzed :
        StatusProject(R.string.paralyzed, R.string.paralyzedCode, R.color.paralyzed)

    data object Finalized :
        StatusProject(R.string.finalized, R.string.finalizedCode, R.color.finalized)

}