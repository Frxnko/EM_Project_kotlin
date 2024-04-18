package com.example.emcontrol.ui.detail.progress.adapter

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getString
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.em.emproject.R
import com.em.emproject.data.repository.ProjectRepository
import com.em.emproject.databinding.ItemProgressBinding
import com.em.emproject.domain.model.Progress
import com.em.emproject.ui.projects.detailAssignedProject.DetailActivity
import com.em.emproject.ui.projects.detailAssignedProject.DetailActivity.Companion.CODE_PROJECT_DETAIL
import com.em.emproject.ui.projects.detailAssignedProject.progress.ProgressViewModel
import com.em.emproject.ui.projects.detailAssignedProject.progress.ProgressViewModel.Companion.list_item_progress
import com.em.emventas.data.network.FireDataBaseService.Companion.PATH_PROGRESS
import com.em.emventas.data.network.FireDataBaseService.Companion.PATH_PROJECT
import com.example.emcontrol.domain.model.ItemProgress
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class ProgressViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private var binding = ItemProgressBinding.bind(view)

    private lateinit var btnSaveProgress: Button

    private lateinit var itemAux: ItemProgress

    private var progressTotal: Double = 0.00
    private var progressTitle: Double = 0.00
    private var orderTitle: Int = 0


    fun render(itemProgress: ItemProgress) {
        itemAux = itemProgress

        styleItem(
            itemProgress.type,
            itemProgress.progress,
            itemProgress.incidence,
            itemProgress.status
        )

        binding.tvItem.text = itemProgress.item
        binding.tvDescription.text = itemProgress.description

        binding.tvProgress.setOnClickListener {
            if (itemProgress.type == "P") {
                showDialog(itemProgress.description, itemProgress.progress)
            }
        }


    }

    private fun showDialog(title: String, progress: Double) {

        val viewAux = LayoutInflater.from(binding.tvProgress.context)
            .inflate(R.layout.dialog_modifyprogress, null)

        val tvTitleDialog: TextView = viewAux.findViewById(R.id.tvTitleDialog)
        val etPercentPrevious: EditText = viewAux.findViewById(R.id.etProgress)
        btnSaveProgress = viewAux.findViewById(R.id.btSaveProgress)

        val dialog = Dialog(viewAux.context)

        tvTitleDialog.text = title

        etPercentPrevious.hint = progress.toString()

        dialog.setContentView(viewAux)
        etPercentPrevious.requestFocus()

//        val imm = getSystemService(InputMethodManager::class.java)
        val imm = getSystemService(binding.tvProgress.context, InputMethodManager::class.java)
        imm?.showSoftInput(viewAux, InputMethodManager.HIDE_IMPLICIT_ONLY)



        dialog.show()

        btnSaveProgress.setOnClickListener {
            if (etPercentPrevious.text.isNullOrBlank()) {
                Toast.makeText(
                    binding.tvDescription.context,
                    getString(binding.tvDescription.context, R.string.noValueInserted),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                when (etPercentPrevious.text.toString().toDouble()) {
                    in 101.00..999999.00 -> {
                        Toast.makeText(
                            binding.tvDescription.context,
                            getString(binding.tvDescription.context, R.string.valueSuperior),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    in -99999.00..-0.0001 -> {
                        Toast.makeText(
                            binding.tvDescription.context,
                            getString(binding.tvDescription.context, R.string.valueInferior),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    progress -> {
                        Toast.makeText(
                            binding.tvDescription.context,
                            getString(binding.tvDescription.context, R.string.valueEqual),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> {
                        updateProgressDB(itemAux, etPercentPrevious.text.toString().toDouble())
                        dialog.dismiss()
                    }

                }
            }

        }
    }


    private fun updateProgressDB(itemAux: ItemProgress, progress: Double) {
        getProgress(itemAux, progress)

        val listItemToModify: HashMap<String, Any> = HashMap()
        val listTitleToModify: HashMap<String, Any> = HashMap()
        val listProgressProjectModify: HashMap<String, Any> = HashMap()
        val listProgressDateAdd: HashMap<String, Progress> = HashMap()

        val valueAux = ((progress * itemAux.incidence * 100.00).roundToLong() / 100.00)
        listItemToModify["progress"] = progress
        listItemToModify["status"] = valueAux


        listTitleToModify["status"] = progressTitle

        listProgressProjectModify["progress"] = progressTotal

        listProgressDateAdd["progress"] = Progress(dateToday(), progressTotal)


        val db = Firebase.firestore
        val docRef = db.collection(PATH_PROGRESS).document(CODE_PROJECT_DETAIL)


        val fieldsUpdate = mapOf("progress" to progress, "status" to valueAux)

        runBlocking {
            val document = docRef.get().await()
            if (document != null) {
                val list = document.get("listProgress") as List<Map<String, Any>>


                val orderString = (itemAux.order).toString()

                val indexAux = itemAux.order - 1
//                val indexAux = list.indexOfFirst {it["order"] == itemAux.order }
//
//                if (indexAux != -1) {
                val objectUpdate = list[indexAux].toMutableMap()
                objectUpdate["progress"] = progress
                objectUpdate["status"] = valueAux

                val listUpdate = list.toMutableList()
                listUpdate[indexAux] = objectUpdate
                docRef.update("listProgress", listUpdate).await()
//                }
            }
        }

//        FirebaseFirestore.getInstance()
//            .document("${PATH_PROGRESS}/${CODE_PROJECT_DETAIL}/listProgress/${orderTitle}")
//            .update(listTitleToModify)
//
//        FirebaseFirestore.getInstance().document("${PATH_PROJECT}/${CODE_PROJECT_DETAIL}")
//            .update(listProgressProjectModify)
//
//        FirebaseFirestore.getInstance()
//            .document("${PATH_PROGRESS}/${CODE_PROJECT_DETAIL}/listRegister").update(
//            listProgressDateAdd as Map<String, Progress>
//        )

//        val db = Firebase.firestore
//        val docRef= db.collection(PATH_PROGRESS).document(CODE_PROJECT_DETAIL)
//        docRef.

    }

    private fun dateToday(): String {
        val calendar: Calendar = Calendar.getInstance()
        val year: Int = calendar.get(Calendar.YEAR)
        val month: Int = calendar.get(Calendar.MONTH)
        val day: Int = calendar.get(Calendar.DAY_OF_MONTH)

        return (year.toString()) + "-" + (month + 1).toString() + "-" + day.toString()
    }

    private fun getProgress(itemAux: ItemProgress, progress: Double) {
        val mutableList: MutableList<ItemProgress> = list_item_progress.toMutableList()

        progressTotal = 0.00
        progressTitle = 0.00
        orderTitle = 0

        mutableList.find { it.order == itemAux.order }?.status =
            ((progress * itemAux.incidence * 100.00).roundToInt() / 100.00)

        orderTitle = mutableList.first { it.group == itemAux.group && it.type == "T" }.order


        progressTitle =
            mutableList.filter { it.group == itemAux.group && it.type == "P" }.sumOf { it.status }

        progressTotal = mutableList.filter { it.type == "P" }.sumOf { it.status }

        progressTotal = (progressTotal * 100.00).roundToInt() / 100.00


        ProgressViewModel.progress_Project = progressTotal

    }


    private fun styleItem(type: String, progress: Double, incidence: Double, status: Double) {

        if (type == "T") {
            binding.tvItem.setTextAppearance(R.style.TextTitleItem01)
            binding.tvDescription.setTextAppearance(R.style.TextTitleItem01)
            binding.tvStatus.setTextAppearance(R.style.TextTitleItem01)
            binding.tvProgress.text = ""
            binding.tvStatus.text = ((status * 100.00).roundToInt() / 100.00).toString()
        } else {
            binding.tvItem.setTextAppearance(R.style.TextItem01)
            binding.tvDescription.setTextAppearance(R.style.TextItem01)
            binding.tvProgress.setTextAppearance(R.style.TextItem01)
            binding.tvStatus.setTextAppearance(R.style.TextItem01)
            binding.tvProgress.text = ((progress * 100.00).roundToInt() / 100.00).toString()
            binding.tvStatus.text =
                ((progress * incidence * 100.00).roundToInt() / 100.00).toString()
        }

    }


}