package com.em.emproject.ui.projects.detailAssignedProject.Information

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.em.emproject.R
import com.em.emproject.databinding.FragmentInformationBinding
import com.em.emproject.domain.model.StatusProject
import com.em.emproject.ui.projects.AssignedProjectsShareViewModel
import com.em.emproject.ui.projects.detailAssignedProject.DetailActivity.Companion.CODE_PROJECT_DETAIL
import com.em.emventas.data.network.FireDataBaseService.Companion.DOC_REF_PROJECT
import com.em.emventas.data.network.FireDataBaseService.Companion.PATH_PROJECT
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar

@AndroidEntryPoint
class InformationFragment : Fragment() {

    private val informationViewModel by viewModels<InformationViewModel>() //Delegado. Enganchar al viewModel. Se conceta el fragment al viewModel
    private val assignedProjectsShareViewModel: AssignedProjectsShareViewModel by activityViewModels()

    private var _binding: FragmentInformationBinding? = null
    private val binding get() = _binding!!

    private var latitude: Double = 0.00
    private var longitude: Double = 0.00

    private lateinit var coordinates: String
    private lateinit var pathAux: String

    private var heightTower: Int = 0

    private var _coordinate: String = ""
    val coordinate: String = _coordinate


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInformationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        assignedProjectsShareViewModel.getProject(CODE_PROJECT_DETAIL)
        initUI()
    }

    private fun initUI() {
        initList()
        initUIState()
    }

    private fun initList() {

        binding.chipCoordinates.setOnClickListener { showMap() }

        binding.tvEditLocation.text = textLikable()
        binding.tvEditLocation.setOnClickListener {
            showDialogEditLocation()
        }

        binding.tvEditProgress.text = textLikable()
        binding.tvEditProgress.setOnClickListener {
            showDialogEditProgress()
        }

        binding.tvEditTowerInformation.text = textLikable()
        binding.tvEditTowerInformation.setOnClickListener {
            showDialogEditTowerInformation()
        }

        binding.tvEditAdditionalInformation.text = textLikable()
        binding.tvEditAdditionalInformation.setOnClickListener {
            showDialogEditAdditionalInformation()
        }

        binding.tvLink.setOnClickListener {
            val copyAux: String = binding.tvLink.text.toString()
            (requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).apply {
                setPrimaryClip(ClipData.newPlainText("simple text", copyAux))
            }
        }


    }

    private fun showDialogEditAdditionalInformation() {
        val dialog = Dialog(binding.tvEditLocation.context)
        dialog.setContentView(R.layout.dialog_edit_additional_information)

        val etAdditional: EditText = dialog.findViewById(R.id.etDialogAdditional)
        val etComments01: EditText = dialog.findViewById(R.id.etDialogComments01)
        val etPMA: EditText = dialog.findViewById(R.id.etDialogPMA)
        val etCandidateRFT: EditText = dialog.findViewById(R.id.etDialogCandidateRFT)
        val etLinkInformation: EditText = dialog.findViewById(R.id.etDialogLinkInformation)
        val etSupervisor: EditText = dialog.findViewById(R.id.etDialogSupervisor)
        val etParalyzed: EditText = dialog.findViewById(R.id.etDialogParalyzed)
        val etComments02: EditText = dialog.findViewById(R.id.etDialogComments02)
        val btEdit: Button = dialog.findViewById(R.id.btDialogEditTowerInfo)

        etAdditional.setText(binding.tvAdditional.text)
        etComments01.setText(binding.tvComments01.text)
        etPMA.setText(binding.tvPMA.text)
        etCandidateRFT.setText(binding.tvRFTCandidate.text)
        etLinkInformation.setText(binding.tvLink.text)
        etSupervisor.setText(binding.tvSupervisor.text)
        etParalyzed.setText(binding.tvParalyzed.text)
        etComments02.setText(binding.tvComments02.text)


        pathAux = "${PATH_PROJECT}/${CODE_PROJECT_DETAIL}"


        btEdit.setOnClickListener {

            val additionalAux: String = if (etAdditional.text.isNullOrBlank()) ""
            else etAdditional.text.toString()

            val comments01Aux: String = if (etComments01.text.isNullOrBlank()) ""
            else etComments01.text.toString()

            val pmaAux: String = if (etPMA.text.isNullOrBlank()) ""
            else etPMA.text.toString()

            val candidateRFTAux: String = if (etCandidateRFT.text.isNullOrBlank()) ""
            else etCandidateRFT.text.toString()

            val linkInformationAux: String = if (etLinkInformation.text.isNullOrBlank()) ""
            else etLinkInformation.text.toString()

            val supervisorAux: String = if (etSupervisor.text.isNullOrBlank()) ""
            else etSupervisor.text.toString()

            val paralyzedAux: String = if (etParalyzed.text.isNullOrBlank()) ""
            else etParalyzed.text.toString()

            val comments02Aux: String = if (etComments02.text.isNullOrBlank()) ""
            else etComments02.text.toString()


            val editProjectMap: HashMap<String, Any> = HashMap()
            editProjectMap["${pathAux}/additional"] = additionalAux
            editProjectMap["${pathAux}/comments"] = comments01Aux
            editProjectMap["${pathAux}/pma"] = pmaAux
            editProjectMap["${pathAux}/approvedCandidateRFT"] = candidateRFTAux
            editProjectMap["${pathAux}/linkInformation"] = linkInformationAux
            editProjectMap["${pathAux}/supervisorId"] = supervisorAux
            editProjectMap["${pathAux}/paralyzed"] = paralyzedAux
            editProjectMap["${pathAux}/comments02"] = comments02Aux

            val editOk: Boolean = editInfo(editProjectMap, dialog)



            binding.tvAdditional.text = additionalAux
            binding.tvComments01.text = comments01Aux
            binding.tvPMA.text = pmaAux
            binding.tvRFTCandidate.text = candidateRFTAux
            binding.tvLink.text = linkInformationAux
            binding.tvSupervisor.text = supervisorAux
            binding.tvParalyzed.text = paralyzedAux
            binding.tvComments02.text = comments02Aux


        }

        dialog.show()
    }

    private fun showDialogEditTowerInformation() {
        val dialog = Dialog(binding.tvEditLocation.context)
        dialog.setContentView(R.layout.dialog_edit_tower_info)

        val etHeight: EditText = dialog.findViewById(R.id.etHeightTorreDialog)
        val etTypeTower: EditText = dialog.findViewById(R.id.etTypeTorreDialog)
        val etSupplierTower: EditText = dialog.findViewById(R.id.etDialogSupplier)
        val etTypeProject: EditText = dialog.findViewById(R.id.etDialogTypeProject)
        val btEdit: Button = dialog.findViewById(R.id.btDialogEditTowerInfo)

        etHeight.setText(heightTower.toString())
        etTypeTower.setText(binding.tvTypeTower.text)
        etSupplierTower.setText(binding.tvSupplierTower.text)
        etTypeProject.setText(binding.tvTypeProject.text)


        pathAux = "${PATH_PROJECT}/${CODE_PROJECT_DETAIL}"


        btEdit.setOnClickListener {

            val heightAux: Int = if (etHeight.text.isNullOrBlank()) 0
            else etHeight.text.toString().toInt()

            val typeTowerAux: String = if (etTypeTower.text.isNullOrBlank()) ""
            else etTypeTower.text.toString()

            val supplierAux: String = if (etSupplierTower.text.isNullOrBlank()) ""
            else etSupplierTower.text.toString()


            val typeProjectAux: String = if (etTypeProject.text.isNullOrBlank()) ""
            else etTypeProject.text.toString()


            val editProjectMap: HashMap<String, Any> = HashMap()
            editProjectMap["${pathAux}/towerHeight"] = heightAux
            editProjectMap["${pathAux}/towerType"] = typeTowerAux
            editProjectMap["${pathAux}/towerSupplier"] = supplierAux
            editProjectMap["${pathAux}/nodeType"] = typeProjectAux

            val editOk: Boolean = editInfo(editProjectMap, dialog)


            binding.tvTowerHeight.text = heightAux.toString()
            binding.tvTypeTower.text = typeTowerAux
            binding.tvSupplierTower.text = supplierAux
            binding.tvTypeProject.text = typeProjectAux


        }

        dialog.show()
    }

    private fun showDialogEditProgress() {
        val dialog = Dialog(binding.tvAddress.context)
        dialog.setContentView(R.layout.dialog_editprogress)
        val spinner: Spinner = dialog.findViewById(R.id.spinnerStatus)
        val etDateStart: EditText = dialog.findViewById(R.id.etDialogStartDate)
        val etDateEnd: EditText = dialog.findViewById(R.id.etDialogEndDate)
        val etLastReportContractor: EditText =
            dialog.findViewById(R.id.etDialogLastReportContractor)
        val etLastReportSupervisor: EditText =
            dialog.findViewById(R.id.etDialogLastReportSupervisor)
        val etContractor: EditText = dialog.findViewById(R.id.etDialogContractor)
        val btEdit: Button = dialog.findViewById(R.id.btDialogEditProgress)

        etDateStart.setText(binding.tvDateStart.text)
        etDateEnd.setText(binding.tvDateEnd.text)
        etLastReportContractor.setText(binding.tvLastReportContractor.text)
        etLastReportSupervisor.setText(binding.tvLastReportSupervisor.text)
        etContractor.setText(binding.tvContractor.text)

        val listStatus: List<String> =
            listOf(
                getString(StatusProject.NoStarted.description),
                getString(StatusProject.OnProgress.description),
                getString(StatusProject.Paralyzed.description),
                getString(StatusProject.Finalized.description)
            )

        val adapter = ArrayAdapter<String>(
            binding.tvAddress.context,
            android.R.layout.simple_spinner_item,
            listStatus
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        var statusAux = binding.tvStatus.text.toString()


        val indexSpinner: Int? = when (statusAux) {
            getString(StatusProject.NoStarted.description) -> 0
            getString(StatusProject.OnProgress.description) -> 1
            getString(StatusProject.Paralyzed.description) -> 2
            getString(StatusProject.Finalized.description) -> 3
            else -> null
        }

        if (indexSpinner != null) spinner.setSelection(indexSpinner)

//        pathAux = "${PATH_PROJECT}/${CODE_PROJECT_DETAIL}"

        btEdit.setOnClickListener {

            statusAux = when (spinner.selectedItem.toString()) {
                getString(StatusProject.NoStarted.description) -> getString(StatusProject.NoStarted.codeStatus)
                getString(StatusProject.OnProgress.description) -> getString(StatusProject.OnProgress.codeStatus)
                getString(StatusProject.Paralyzed.description) -> getString(StatusProject.Paralyzed.codeStatus)
                getString(StatusProject.Finalized.description) -> getString(StatusProject.Finalized.codeStatus)
                else -> ""
            }

            val dateStartAux: String = if (etDateStart.text.isNullOrBlank()) ""
            else etDateStart.text.toString()

            val dateEndAux: String = if (etDateEnd.text.isNullOrBlank()) ""
            else etDateEnd.text.toString()

            val lastReportContractorAux: String =
                if (etLastReportContractor.text.isNullOrBlank()) ""
                else etLastReportContractor.text.toString()

            val lastReportSupervisorAux: String =
                if (etLastReportSupervisor.text.isNullOrBlank()) ""
                else etLastReportSupervisor.text.toString()

            val contractAux: String = if (etContractor.text.isNullOrBlank()) ""
            else etContractor.text.toString()


            val editProjectMap: HashMap<String, Any> = HashMap()
            editProjectMap["status"] = statusAux
            editProjectMap["dateStart"] = dateStartAux
            editProjectMap["dateEnd"] = dateEndAux
            editProjectMap["lastReportContractor"] = lastReportContractorAux
            editProjectMap["lastReportSupervision"] = lastReportSupervisorAux
            editProjectMap["contractor"] = contractAux

            if (statusAux == getString(StatusProject.Finalized.codeStatus)) {
                editProjectMap["progress"] = 100.00
                binding.tvProgress02.text = "100.0"
            }

            val editOk: Boolean = editInfo(editProjectMap, dialog)

            binding.tvStatus.text = when (statusAux) {
                getString(StatusProject.NoStarted.codeStatus) -> getString(StatusProject.NoStarted.description)
                getString(StatusProject.OnProgress.codeStatus) -> getString(StatusProject.OnProgress.description)
                getString(StatusProject.Paralyzed.codeStatus) -> getString(StatusProject.Paralyzed.description)
                getString(StatusProject.Finalized.codeStatus) -> getString(StatusProject.Finalized.description)
                else -> ""
            }


            binding.tvDateStart.text = dateStartAux
            binding.tvDateEnd.text = dateEndAux
            binding.tvLastReportContractor.text = lastReportContractorAux
            binding.tvLastReportSupervisor.text = lastReportSupervisorAux
            binding.tvContractor.text = contractAux


            dialog.dismiss()

        }

        etDateStart.setOnClickListener { showDatePickerDialog(etDateStart, etDateStart.text) }
        etDateEnd.setOnClickListener { showDatePickerDialog(etDateEnd, etDateEnd.text) }
        etLastReportContractor.setOnClickListener {
            showDatePickerDialog(
                etLastReportContractor,
                etLastReportContractor.text
            )
        }
        etLastReportSupervisor.setOnClickListener {
            showDatePickerDialog(
                etLastReportSupervisor,
                etLastReportSupervisor.text
            )
        }


        dialog.show()


    }

    private fun showDatePickerDialog(etDateStart: EditText, text: Editable) {
        val calendar: Calendar = Calendar.getInstance()
        val year: Int
        val month: Int
        val day: Int

        if (text.isNullOrBlank()) {
            year = calendar.get(Calendar.YEAR)
            month = calendar.get(Calendar.MONTH)
            day = calendar.get(Calendar.DAY_OF_MONTH)
        } else {
            val arrayAux: List<String> = text.split("-")
            if (arrayAux.size != 3) {
                year = calendar.get(Calendar.YEAR)
                month = calendar.get(Calendar.MONTH)
                day = calendar.get(Calendar.DAY_OF_MONTH)
            } else {
                year = arrayAux[0].toInt()
                month = arrayAux[1].toInt() - 1
                day = arrayAux[2].toInt()
            }

        }


        val datePickerDialog = DatePickerDialog(
            binding.tvDateStart.context,
            { view, year1, monthOfYear, dayOfMonth ->
                val dateChoice =
                    (year1.toString()) + "-" + (monthOfYear + 1).toString() + "-" + dayOfMonth.toString()
                etDateStart.setText(dateChoice)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }


    private fun showDialogEditLocation() {
        val dialog = Dialog(binding.tvEditLocation.context)
        dialog.setContentView(R.layout.dialog_editlocation)

        val etLatitude: EditText = dialog.findViewById(R.id.etDialogLatitude)
        val etLongitude: EditText = dialog.findViewById(R.id.etDialogLongitude)
        val etAddress: EditText = dialog.findViewById(R.id.etDialogAddress)
        val etLocality: EditText = dialog.findViewById(R.id.etDialogLocality)
        val etDistrict: EditText = dialog.findViewById(R.id.etDialogDistrict)
        val etProvince: EditText = dialog.findViewById(R.id.etDialogProvince)
        val etRegion: EditText = dialog.findViewById(R.id.etDialogRegion)
        val btEdit: Button = dialog.findViewById(R.id.btDialogEditLocation)

        etLatitude.setText(latitude.toString())
        etLongitude.setText(longitude.toString())
        etAddress.setText(binding.tvAddress.text)
        etLocality.setText(binding.tvLocation.text)
        etDistrict.setText(binding.tvDistrict.text)
        etProvince.setText(binding.tvProvince.text)
        etRegion.setText(binding.tvRegion.text)


        pathAux = "${PATH_PROJECT}/${CODE_PROJECT_DETAIL}"


        btEdit.setOnClickListener {

            val latitudeAux: Double = if (etLatitude.text.isNullOrBlank()) 0.00
            else etLatitude.text.toString().toDouble()

            val longitudeAux: Double = if (etLongitude.text.isNullOrBlank()) 0.00
            else etLongitude.text.toString().toDouble()

            val addressAux: String = if (etAddress.text.isNullOrBlank()) ""
            else etAddress.text.toString()

            val locationAux: String = if (etLocality.text.isNullOrBlank()) ""
            else etLocality.text.toString()

            val districtAux: String = if (etDistrict.text.isNullOrBlank()) ""
            else etDistrict.text.toString()

            val provinceAux: String = if (etProvince.text.isNullOrBlank()) ""
            else etProvince.text.toString()

            val regionAux: String = if (etRegion.text.isNullOrBlank()) ""
            else etRegion.text.toString()


            val editProjectMap: HashMap<String, Any> = HashMap()
            editProjectMap["${pathAux}/latitude"] = latitudeAux
            editProjectMap["${pathAux}/longitude"] = longitudeAux
            editProjectMap["${pathAux}/address"] = addressAux
            editProjectMap["${pathAux}/location"] = locationAux
            editProjectMap["${pathAux}/district"] = districtAux
            editProjectMap["${pathAux}/province"] = provinceAux
            editProjectMap["${pathAux}/region"] = regionAux

            val editOk: Boolean = editInfo(editProjectMap, dialog)


            binding.tvCoordinates.text = "${latitudeAux}° , ${longitudeAux}°"
            binding.tvAddress.text = addressAux
            binding.tvLocation.text = locationAux
            binding.tvDistrict.text = districtAux
            binding.tvProvince.text = provinceAux
            binding.tvRegion.text = regionAux

        }

        dialog.show()

    }

    private fun editInfo(editProjectMap: HashMap<String, Any>, dialog: Dialog): Boolean {

        var returnAux: Boolean = false

        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection(PATH_PROJECT).document(DOC_REF_PROJECT)

        docRef.update(editProjectMap)
            .addOnSuccessListener {
                returnAux = true
                Toast.makeText(
                    binding.tvRegion.context,
                    ContextCompat.getString(
                        binding.tvRegion.context,
                        R.string.successfullyEdited
                    ),
                    Toast.LENGTH_LONG
                ).show()
                dialog.dismiss()
            }.addOnFailureListener { err ->
                Toast.makeText(
                    binding.tvRegion.context, "Error ${err.message}", Toast.LENGTH_SHORT
                ).show()
                dialog.dismiss()
            }
        return returnAux

//        return true
    }

    private fun textLikable(): SpannableString {
        val mString = getString(R.string.edit)
        val mSpannableString = SpannableString(mString)
        // Setting underline style from position 0 till length of the spannable string
        mSpannableString.setSpan(UnderlineSpan(), 0, mSpannableString.length, 0)
        //Display this spannable string in TextView
        return mSpannableString
    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                assignedProjectsShareViewModel.state.collect {
                    when (it) {
                        InformationState.Loading -> loadingState()
                        is InformationState.Error -> errorState()
                        is InformationState.Success -> successState(it)
                    }
                }
            }
        }
    }


    private fun loadingState() {
        binding.tvCodeProject.text = CODE_PROJECT_DETAIL
    }

    private fun successState(it: InformationState.Success) {
        val project = it.project
//        heightTower = project.towerHeight!!

        binding.tvCodeProject.text = project.projectName
//        binding.tvTowerInfo.text =
//            infoTower(project.towerHeight, project.towerType, project.towerSupplier)
        binding.tvComments02.text = project.comments02

        binding.tvCoordinates.text = getCoordinates(project.latitude, project.longitude)
        binding.tvAddress.text = project.address
        binding.tvLocation.text = project.locality
        binding.tvDistrict.text = project.district
        binding.tvProvince.text = project.province
        binding.tvRegion.text = project.region

        binding.tvStatus.text = when (project.status) {
            getString(StatusProject.NoStarted.codeStatus) -> getString(StatusProject.NoStarted.description)
            getString(StatusProject.OnProgress.codeStatus) -> getString(StatusProject.OnProgress.description)
            getString(StatusProject.Paralyzed.codeStatus) -> getString(StatusProject.Paralyzed.description)
            getString(StatusProject.Finalized.codeStatus) -> getString(StatusProject.Finalized.description)
            else -> ""
        }

//        binding.tvStatus.text = project.status
        binding.tvDateStart.text = project.dateStart
        binding.tvDateEnd.text = project.dateEnd
        binding.tvDaysProject.text = daysProject(project.status, project.dateStart, project.dateEnd)
        binding.tvProgress02.text = getProgress(project.progress)
        binding.tvLastReportContractor.text = project.lastReportContractor
        binding.tvLastReportSupervisor.text = project.lastReportSupervision
        binding.tvContractor.text = project.contractor

        binding.tvTowerHeight.text = project.towerHeight
        binding.tvTypeTower.text = project.towerType
        binding.tvSupplierTower.text = project.towerSupplier
        binding.tvTypeProject.text = project.nodeType

        binding.tvAdditional.text = project.additional
        binding.tvComments01.text = project.comments
        binding.tvPMA.text = project.pma
        binding.tvRFTCandidate.text = project.approvedCandidateRFT
        binding.tvVersionIng.text = project.nasVersion
        binding.tvLink.text = linkProject(project.linkNewProject, project.linkUpdate)
        binding.tvHighSPAT.text = highSPAT(project.linkHighSPAT)
        binding.tvLinkHighSPAT.text = project.linkHighSPAT

//        binding.tvSupervisor.text = project.supervisorId
//        binding.tvParalyzed.text = project.paralyzed

        latitude = project.latitude ?: 0.00
        longitude = project.longitude ?: 0.00

        _coordinate = "geo:${project.latitude},${project.longitude}"


    }

    private fun showMap() {

        val mapIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(_coordinate)
        )

        startActivity(mapIntent)
    }

    private fun highSPAT(linkHighSPAT: String?): String {
        return if (linkHighSPAT.isNullOrEmpty()) {
            binding.llLinkHighSPAT.isVisible = false
            binding.tvHighSPAT.apply {
                setTextColor(ContextCompat.getColor(context, R.color.text))
            }
            "-"
        } else {
            binding.llLinkHighSPAT.isVisible = true
            binding.tvHighSPAT.apply {
                setTextColor(ContextCompat.getColor(context, R.color.red))
            }
            "Aplica"
        }
    }

    private fun linkProject(linkNewProject: String?, linkUpdate: String?): String? {
        return if (!linkNewProject.isNullOrEmpty()) linkNewProject
        else linkUpdate
    }

    private fun getProgress(progress: Double?): String {
        return "$progress"

    }

    private fun daysProject(status: String?, dateStart: String?, dateEnd: String?): String {

        return when (status) {
            getString(StatusProject.NoStarted.codeStatus) -> "-"
            getString(StatusProject.OnProgress.codeStatus) -> informationViewModel.getDays(
                dateEnd,
                dateStart
            )

            getString(StatusProject.Paralyzed.codeStatus) -> informationViewModel.getDays(
                dateEnd,
                dateStart
            )

            getString(StatusProject.Finalized.codeStatus) -> informationViewModel.getDays(
                dateEnd,
                dateStart
            )

            else -> "-"
        }
    }


    private fun getCoordinates(latitude: Double?, longitude: Double?): String {
        coordinates = "geo:$latitude,$longitude"
        return "$latitude° , $longitude°"
    }

    private fun infoTower(towerHeight: Int?, towerType: String?, towerSupplier: String?): String {
        val textHeight: String = if (towerHeight == null) "N/A"
        else "$towerHeight m"

        val typeTowerAux: String = towerType ?: "N/A"

        val supplierAux: String = towerSupplier ?: "N/A"

        return "$textHeight - $typeTowerAux - $supplierAux"
    }


    private fun errorState() {

    }


}