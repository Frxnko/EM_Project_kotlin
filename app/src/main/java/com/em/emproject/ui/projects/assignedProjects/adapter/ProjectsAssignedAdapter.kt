package com.example.emcontrol.ui.projects.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.em.emproject.R
import com.em.emproject.domain.model.ProjectAssignedList

class ProjectsAssignedAdapter(
    private var projectList: List<ProjectAssignedList> = emptyList(),
    private val onItemSelected: (ProjectAssignedList) -> Unit
) : RecyclerView.Adapter<ProjectsAssignedViewHolder>() {

    fun updateList(list: List<ProjectAssignedList>) {
        projectList = list
        notifyDataSetChanged()//se cambiara todo el listado solo una vez
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectsAssignedViewHolder {
        return ProjectsAssignedViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_project, parent, false)
        )
    }

    override fun getItemCount() = projectList.size

    override fun onBindViewHolder(holder: ProjectsAssignedViewHolder, position: Int) {
        holder.render(projectList[position], onItemSelected)
    }

    fun updateListFilter(list: List<ProjectAssignedList>){
        this.projectList = list
        notifyDataSetChanged()//Para que se pueda pintar nuevamente
    }
}