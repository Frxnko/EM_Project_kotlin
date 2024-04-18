package com.example.emcontrol.ui.allProjects.adapter


import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.em.emproject.R
import com.em.emproject.databinding.ItemAllprojectBinding
import com.em.emproject.domain.model.ProjectInfo

class AllProjectsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemAllprojectBinding.bind(view)

    fun render(projectInfo: ProjectInfo, onItemSelected: (ProjectInfo) -> Unit) {
        binding.tvProject.text = projectInfo.projectName
        binding.tvTypeNode.text = projectInfo.nodeType
        binding.tvLocation.text = "${projectInfo.locality} - ${projectInfo.district}"
        binding.tvProvince.text = projectInfo.province
        binding.tvHeight.text = projectInfo.towerHeight?.let { heightTower(it) }

        binding.parentAllProjects.setOnClickListener { onItemSelected(projectInfo) }
    }

    private fun imageCharge(): Int {
        return R.drawable.ic_tower
    }

    private fun heightTower(height: String): String {
        val heightAux = isNumber(height)

        return if (!heightAux) height
        else "$height m"
    }

    private fun isNumber(s: String): Boolean {
        return try {
            s.toInt()
            true
        } catch (ex: NumberFormatException) {
            false
        }
    }

}