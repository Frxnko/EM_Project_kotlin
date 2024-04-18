package com.example.emcontrol.ui.projects.adapter

import android.content.res.Resources
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getString
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.em.emproject.R
import com.em.emproject.databinding.ItemProjectBinding
import com.em.emproject.di.ComparateClass
import com.em.emproject.domain.model.ProjectAssignedList
import com.em.emproject.domain.model.StatusProject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class ProjectsAssignedViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemProjectBinding.bind(view)
    private var statusProject: String = ""

    fun render(project: ProjectAssignedList, onItemSelected: (ProjectAssignedList) -> Unit) {
        binding.tvProject.text = project.projectName
        binding.tvContractor.text = project.contractor
        binding.tvProgress.text = project.progress.toString().format("##.00")
        binding.tvHeightTower.text =
            heightTower(project.towerHeight, project.towerSupplier, project.towerType)

        setDaysProgress(project.dateEnd, project.dateStart, project.status)

        setDaysReport(project.lastReportContractor, project.status)

        statusProjectStyle(project.status)
        binding.tvStatus.text = statusProject



        binding.parentProjects.setOnClickListener { onItemSelected(project) }
    }

    private fun setDaysReport(lastReportContractor: String?, status: String?) {

        when (status) {
            getString(binding.tvProject.context,StatusProject.NoStarted.codeStatus) -> {
                binding.mcReportDaily.isVisible = false
            }

            getString(binding.tvProject.context, StatusProject.OnProgress.codeStatus) -> {
                textDaysReport(getDays(null, lastReportContractor)?.toInt())
//                binding.mcReportDaily.isVisible = true
            }

            getString(binding.tvProject.context, StatusProject.Paralyzed.codeStatus) -> {
                textDaysReport(getDays(null, lastReportContractor)?.toInt())
                binding.mcReportDaily.isVisible = false
            }

            getString(binding.tvProject.context, StatusProject.Finalized.codeStatus) -> {
                textDaysReport(getDays(null, lastReportContractor)?.toInt())
                binding.mcReportDaily.isVisible = false
            }

            else -> {
                binding.mcReportDaily.isVisible = false
            }
        }


    }

    private fun textDaysReport(days: Int?) {
        val colorAux: Int

        if (days != null) {
            binding.tvDaysReport.text = days.toString()
            binding.mcReportDaily.isVisible = days != 0

            colorAux = when (days.toInt()) {
                1 -> ContextCompat.getColor(binding.tvProject.context, R.color.oneDaysReport)
                in 2..5 -> ContextCompat.getColor(
                    binding.tvProject.context,
                    R.color.twoFiveDaysReport
                )

                else -> ContextCompat.getColor(
                    binding.tvProject.context,
                    R.color.moreFiveDaysReport
                )
            }

        } else {
            binding.tvDaysReport.text = "N/R"
            colorAux = ContextCompat.getColor(
                binding.tvProject.context,
                R.color.moreFiveDaysReport
            )
        }

        binding.mcReportDaily.setCardBackgroundColor(colorAux)

    }

    private fun setDaysProgress(dateEnd: String?, dateStart: String?, status: String?) {
        var daysReport: Int?

        when (status) {
            getString(binding.tvProject.context, StatusProject.NoStarted.codeStatus) -> {
                binding.imDateStart.isVisible = false
                binding.tvDateStart.isVisible = false
            }

            getString(binding.tvProject.context, StatusProject.OnProgress.codeStatus) -> {
                binding.imDateStart.isVisible = true
                if (getDays(dateEnd, dateStart) != null) {
                    binding.tvDateStart.text = "$dateStart (${getDays(dateEnd, dateStart)})"
                }
            }

            getString(binding.tvProject.context, StatusProject.Paralyzed.codeStatus) -> {
                binding.imDateStart.isVisible = true
                if (getDays(dateEnd, dateStart) != null) {
                    binding.tvDateStart.text = "$dateStart (${getDays(dateEnd, dateStart)})"
                }
            }

            getString(binding.tvProject.context, StatusProject.Finalized.codeStatus) -> {
                binding.imDateStart.isVisible = true
                if (getDays(dateEnd, dateStart) != null) {
                    binding.tvDateStart.text = "$dateStart (${getDays(dateEnd, dateStart)})"
                }
            }

            else -> {
                binding.imDateStart.isVisible = false
                binding.tvDateStart.isVisible = false
            }
        }

    }

    private fun getDays(dateEnd: String?, dateStart: String?): Long? {
        val dateStarAux: Date
        val dateEndAux: Date
        val diffDays: Long
        val c = Calendar.getInstance()

        if (dateEnd.isNullOrBlank() && dateStart.isNullOrBlank()) return null
        else {
            return if (dateStart.isNullOrBlank()) {
                null
            } else {
                if (dateEnd.isNullOrBlank()) {
                    dateEndAux = c.time
                    dateStarAux = SimpleDateFormat("yyyy-MM-dd").parse(dateStart)
                    diffDays = (dateEndAux.time - dateStarAux.time) / 86400000
                    diffDays
                } else {
                    dateEndAux = SimpleDateFormat("yyyy-MM-dd").parse(dateEnd)
                    dateStarAux = SimpleDateFormat("yyyy-MM-dd").parse(dateStart)
                    diffDays = (dateEndAux.time - dateStarAux.time) / 86400000
                    diffDays
                }
            }
        }
    }


    private fun statusProjectStyle(status: String?) {
        return when (status) {
            getString(binding.tvProject.context, R.string.onProgressCode) -> {
                statusProject = getString(binding.tvProject.context, R.string.onProgress)
                binding.mcStatusProject.setCardBackgroundColor(
                    ContextCompat.getColor(
                        binding.tvProject.context,
                        R.color.onProgress
                    )
                )
            }

            getString(binding.tvProject.context, R.string.paralyzedCode) -> {
                statusProject = getString(binding.tvProject.context, R.string.paralyzed)
                binding.mcStatusProject.setCardBackgroundColor(
                    ContextCompat.getColor(
                        binding.tvProject.context,
                        R.color.paralyzed
                    )
                )
            }

            getString(binding.tvProject.context, R.string.finalizedCode) -> {
                statusProject = getString(binding.tvProject.context, R.string.finalized)
                binding.mcStatusProject.setCardBackgroundColor(
                    ContextCompat.getColor(
                        binding.tvProject.context,
                        R.color.finalized
                    )
                )
            }

            else -> {
                statusProject = getString(binding.tvProject.context, R.string.notStarted)
                binding.mcStatusProject.setCardBackgroundColor(
                    ContextCompat.getColor(
                        binding.tvProject.context,
                        R.color.notStarted
                    )
                )
            }

        }

    }

    private fun imageCharge(): Int {
        return R.drawable.ic_perfil
    }

    private fun heightTower(height: String?, supplier: String?, typeTower: String?): String {

        val isNumber = isNumber(height ?: "-")

        if (isNumber) {
            return "$height - $typeTower - $supplier"
        } else {
            return height ?: "-"
        }

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