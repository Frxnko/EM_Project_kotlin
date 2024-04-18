package com.em.emproject.ui.projects.allProjects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.em.emproject.data.provider.ProjectInfoProvider.Companion.projectInfoList
import com.em.emproject.data.repository.ProjectRepository
import com.em.emproject.domain.model.Project
import com.em.emproject.domain.model.ProjectInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AllProjectsViewModel @Inject constructor() :
    ViewModel() {

    private var _infoProjects = MutableStateFlow<List<ProjectInfo>>(emptyList())
    val infoProjects: StateFlow<List<ProjectInfo>> = _infoProjects


    init {
        _infoProjects.value = projectInfoList
//        getProject()//Se llenará lista para verificar si el nodo ha sido agregado
    }



//    private fun getProject() {
//        var projectList: ArrayList<Project> = ArrayList()
//        val ref = FirebaseDatabase.getInstance().getReference("ProjectsAssign").orderByChild("site")
//        ref.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                projectList.clear()
//                for (ds in snapshot.children) {
//                    val projectToRead = ds.getValue(Project::class.java)
//                    projectList.add(projectToRead!!)
//                }
//                listProjectGlobal = projectList.toList()
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//        })
//    }
//

}