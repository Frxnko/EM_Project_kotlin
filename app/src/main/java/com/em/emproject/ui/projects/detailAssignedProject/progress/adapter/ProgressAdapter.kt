package com.example.emcontrol.ui.detail.progress.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.em.emproject.R
import com.example.emcontrol.domain.model.ItemProgress

class ProgressAdapter(
    private var itemProgressList: List<ItemProgress> = emptyList(),
    private var dateTime: String
) :
    RecyclerView.Adapter<ProgressViewHolder>() {


    fun updateList(list: List<ItemProgress>) {
        itemProgressList = list
//        ProgressViewModel.HAVE_ITEMS_PROJECT = list.isNotEmpty()
        notifyDataSetChanged()//se cambiara todo el listado solo una vez
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgressViewHolder {
        return ProgressViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_progress, parent, false)
        )
    }

    override fun getItemCount() = itemProgressList.size

    override fun onBindViewHolder(holder: ProgressViewHolder, position: Int) {
        holder.render(itemProgressList[position])
    }
}