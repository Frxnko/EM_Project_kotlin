package com.em.emproject.ui.projects.detailAssignedProject

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.em.emproject.ui.projects.detailAssignedProject.Information.InformationFragment
import com.em.emproject.ui.projects.detailAssignedProject.itemProgress.ItemProgressFragment
import com.em.emproject.ui.projects.detailAssignedProject.moreOptions.MoreOptionsFragment
import com.em.emproject.ui.projects.detailAssignedProject.progress.ProgressFragment

class FragmentPageAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 4//numero de tabs
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> InformationFragment()
            1 -> ProgressFragment()
            2 -> ItemProgressFragment()
            else -> MoreOptionsFragment()
        }
    }
}