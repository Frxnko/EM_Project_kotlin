package com.em.emproject.domain.useCase

import com.em.emproject.data.repository.ProjectRepository
import com.em.emproject.domain.model.Project
import com.em.emproject.domain.model.ProjectAssignedList
import javax.inject.Inject

class GetProjectUseCase @Inject constructor(private val repository: ProjectRepository) {

    companion object {
        var listProjectGlobal: List<ProjectAssignedList> = emptyList()
//        var listObsProject: List<Observation> = emptyList()
    }


    suspend operator fun invoke(): List<ProjectAssignedList> {
        val projects = repository.getAllProjectFromNetwork()

        return if (projects.isNotEmpty()) {
//             todo falta completar como se actualiza en la base de datos local
//            repository.clearProjects()//borra la tabla de base de datos
//            repository.insertProjects(projects.map { it.toDatabase() }) //Inserta los datos a la base de datos
            projects
        } else {
            repository.getAllProjectFromDataBase()

        }
    }
}