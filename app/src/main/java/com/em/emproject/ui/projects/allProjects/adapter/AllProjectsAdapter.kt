package com.example.emcontrol.ui.allProjects.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.em.emproject.R
import com.em.emproject.domain.model.ProjectInfo

class AllProjectsAdapter(
    private var projectList: List<ProjectInfo> = emptyList(),
    private val onItemSelected: (ProjectInfo) -> Unit
) :
    RecyclerView.Adapter<AllProjectsViewHolder>() {

    fun updateList(list: List<ProjectInfo>) {
        projectList = list
        notifyDataSetChanged()//se cambiara todo el listado solo una vez
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllProjectsViewHolder {
        return AllProjectsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_allproject, parent, false)
        )
    }

    override fun getItemCount() = projectList.size

    override fun onBindViewHolder(holder: AllProjectsViewHolder, position: Int) {
        holder.render(projectList[position], onItemSelected)
    }

    fun updateListFilter(list: List<ProjectInfo>){
        this.projectList = list
        notifyDataSetChanged()//Para que se pueda pintar nuevamente
    }
}