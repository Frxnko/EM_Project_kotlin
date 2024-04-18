package com.em.emproject.data.repository

import com.em.emproject.domain.model.Project
import com.em.emproject.domain.model.ProjectAssignedList
import com.em.emproject.domain.model.RegisterProgress
import com.em.emventas.data.network.FireDataBaseService
import com.example.emcontrol.domain.model.ItemProgress
import com.example.emcontrol.domain.model.ItemProgressInfo
import com.google.firebase.firestore.DocumentReference
import javax.inject.Inject

class ProjectRepository @Inject constructor(
    private val fireDataBaseService: FireDataBaseService
) {


    suspend fun getAllProjectFromDataBase(): List<ProjectAssignedList> {
//        val response = projectDao.getAllProjects()
//        listProjectGlobal = response.map { it.toDomain() }
//        return listProjectGlobal
        return emptyList()

    }

    suspend fun getAllProjectFromNetwork(): List<ProjectAssignedList> {
        return fireDataBaseService.getAllProjects()
    }


    suspend fun existProject(project: String): Boolean {
        return fireDataBaseService.verifyValue(project)

    }

    suspend fun getProject(nameProject: String): Project? {
        return fireDataBaseService.getProject(nameProject)
    }

    suspend fun insertProject(project: Project): DocumentReference? {
        return fireDataBaseService.createProject(project)
    }

    suspend fun getProgressProject(nameProject: String): List<ItemProgress>? {
        return fireDataBaseService.getProgressProject(nameProject)
    }

    suspend fun saveProgressProjectItem(registerProgress: RegisterProgress):Boolean? {
        return fireDataBaseService.saveProgressProjectItem(registerProgress)
    }

}