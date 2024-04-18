package com.em.emproject.ui.projects.allProjects

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.navArgs
import com.em.emproject.R
import com.em.emproject.databinding.ActivityDetailAllBinding
import com.em.emproject.di.RegisterTime
import com.em.emproject.domain.model.Mistakes
import com.em.emproject.domain.model.Paralyzed
import com.em.emproject.domain.model.Project
import com.em.emproject.domain.model.ProjectInfo
import com.em.emproject.domain.model.ProjectStatusItem
import com.em.emproject.domain.model.Supervisor
import com.em.emproject.ui.login.LoginAppViewModel.Companion.USER_CURRENT
import com.em.emventas.di.FormatText
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class DetailAllActivity : AppCompatActivity() {
    @Inject
    lateinit var formatText: FormatText

    @Inject
    lateinit var registerTime: RegisterTime

    private lateinit var binding: ActivityDetailAllBinding
    private val detailAllViewModel: DetailAllViewModel by viewModels()

    private val args: DetailAllActivityArgs by navArgs()
    private lateinit var projectInfoInitial: ProjectInfo

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
//    private lateinit var dbRef: DatabaseReference

    private var _coordinate: String = ""
    val coordinate: String = _coordinate


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailAllBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponent()

        detailAllViewModel.getProject(args.codeProject)
        initUI()
    }

    private fun initComponent() {
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)
    }

    private fun initUI() {
        initListeners()
        initUIState()
    }

    private fun initListeners() {
        binding.chipCoordinates.setOnClickListener { showMap() }

        binding.tvLinkIngAllProject.text =
            formatText.textLikable(binding.tvLinkIngAllProject.text.toString())
        binding.tvLinkIngAllProject.setOnClickListener {
            detailAllViewModel.openLink(
                binding.tvLinkIngAllProject.text.toString(),
                binding.tvLinkIngAllProject.context
            )
        }
        binding.tvLinkHighSPAT.text = formatText.textLikable(binding.tvLinkHighSPAT.text.toString())
        binding.tvLinkHighSPAT.setOnClickListener {
            detailAllViewModel.openLink(
                binding.tvLinkHighSPAT.text.toString(),
                binding.tvLinkHighSPAT.context
            )
        }

        binding.pbSaveProject.isVisible = false

        binding.btnSaveProject.setOnClickListener {
            //Se realizaran dos procesos. uno para verificar si se tiene agregado el proyecto y otro

            existProjectState()
        }

    }

    private fun existProjectState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                detailAllViewModel.existProject(args.codeProject)

                detailAllViewModel.stateExistProject.collect {
                    when (it) {
                        is ExistProjectState.Exist -> existProject()
                        ExistProjectState.Loading -> existLoading()
                        is ExistProjectState.NoExist -> noExistProject()
                    }
                }
            }
        }
    }

    private fun noExistProject() {
//        binding.pbSaveProject.isVisible = false
        saveProjectNew(args.codeProject)
    }

    private fun saveProjectNew(codeProject: String) {
        val projectAux: Project = projectCreate()
        detailAllViewModel.saveProject(projectAux)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                detailAllViewModel.stateSaveProject.collect {
                    when (it) {
                        SaveProjectState.Loading -> loadingSaveState()
                        is SaveProjectState.Error -> errorSaveState()
                        is SaveProjectState.Success -> successSaveState(it)
                    }
                }
            }
        }


    }

    private fun successSaveState(it: SaveProjectState.Success) {
        binding.pbSaveProject.isVisible = false
        binding.btnSaveProject.isEnabled = true
        Toast.makeText(this, "El proyecto ha sido agregado", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun errorSaveState() {
        binding.pbSaveProject.isVisible = false
        binding.btnSaveProject.isEnabled = true
        Toast.makeText(this, "Se ha presentado un error. No se ha guardado", Toast.LENGTH_SHORT)
            .show()
    }

    private fun loadingSaveState() {
        binding.pbSaveProject.isVisible = true
        binding.btnSaveProject.isEnabled = false
    }

    private fun projectCreate(): Project {

        return Project(
            projectInfoInitial.codeProject,
            projectInfoInitial.projectName,
            projectInfoInitial.nodeType,
            projectInfoInitial.red,
            "",
            projectInfoInitial.locality,
            projectInfoInitial.district,
            projectInfoInitial.province,
            projectInfoInitial.region,
            projectInfoInitial.approvedCandidateRFT,
            projectInfoInitial.latitude,
            projectInfoInitial.longitude,
            "",
            projectInfoInitial.towerType,
            projectInfoInitial.towerHeight,
            projectInfoInitial.towerSupplier,
            projectInfoInitial.civilDesignAvailability,
            projectInfoInitial.nasVersion,
            projectInfoInitial.linkNewProject,
            projectInfoInitial.linkUpdate,
            projectInfoInitial.linkHighSPAT,
            "",
            "",
            "",
            "",
            "",
            0.00,
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            titlesProject(projectInfoInitial.towerHeight),
            mistakeList(),
            supervisorList(),
            paralyzedList(),
            registerTime.getTimeRegister()
        )


    }

    private fun mistakeList(): List<Mistakes?> {
        return emptyList()
    }


    private fun paralyzedList(): List<Paralyzed?> {
        return emptyList()
    }

    private fun supervisorList(): List<Supervisor?> {
        return listOf(
            Supervisor(
                USER_CURRENT!!.userName,
                USER_CURRENT!!.name + " " + USER_CURRENT!!.surname,
                "",
                "",
                isExpert = true,
                showProject = true
            )
        )//todo pendiente que sea agregado por admin y las fechas
    }

    private fun titlesProject(towerHeight: String?): List<ProjectStatusItem?> {
        return listOf(
            ProjectStatusItem("I", "ACTIVIDADES PRELIMINARES", "", true, 1),
            ProjectStatusItem("II", "CIMENTACION DE TORRE", "", true, 2),
            ProjectStatusItem("III", "MONTAJE DE TORRE", "", true, 3),
            ProjectStatusItem("Iv", "LOSA DE EQUIPOS", "", true, 4),
            ProjectStatusItem("V", "CERCO PERIMETRICO", "", true, 5),
            ProjectStatusItem("VI", "IIEE-SPAT", "", true, 6),
            ProjectStatusItem("VII", "ACABADOS", "", true, 7),
        )
    }


    private fun existLoading() {
        binding.pbSaveProject.isVisible = true
    }

    private fun existProject() {
        binding.pbSaveProject.isVisible = false
        Toast.makeText(this, "El proyecto ya ha sido agregado anteriormente", Toast.LENGTH_SHORT)
            .show()
    }


    private fun initUIState() {//para que se enganche a un estado
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                detailAllViewModel.state.collect {
                    when (it) {
                        DetailAllState.Loading -> loadingState()
                        is DetailAllState.Error -> errorState()
                        is DetailAllState.Success -> successState(it)
                    }
                }
            }
        }
    }

    private fun loadingState() {
//        binding.pb.isVisible = true
        binding.tvCodeProject.text = args.codeProject
    }

    private fun successState(projectDetailState: DetailAllState.Success) {
//        binding.pb.isVisible = false

        binding.tvCodeProject.text = projectDetailState.project.projectName
        binding.tvTypeNode.text = projectDetailState.project.nodeType

        binding.tvLocation.text = projectDetailState.project.locality
        binding.tvDistrictInfoAll.text = projectDetailState.project.district
        binding.tvProvinceInfoAll.text = projectDetailState.project.province
        binding.tvLatitude.text = projectDetailState.project.latitude.toString()
        binding.tvLongitude.text = projectDetailState.project.longitude.toString()

        binding.tvHeightTower.text = projectDetailState.project.towerHeight.toString()
        binding.tvTypeTower.text = projectDetailState.project.towerType
        binding.tvSupplierTower.text = projectDetailState.project.towerSupplier

        binding.tvcandidateRFT.text = projectDetailState.project.approvedCandidateRFT
        binding.tvVersionAllProject.text = projectDetailState.project.nasVersion
        binding.tvLinkIngAllProject.text = linkProject(
            projectDetailState.project.linkNewProject,
            projectDetailState.project.linkUpdate
        )

        binding.tvTypeIng.text = typeIng(
            projectDetailState.project.linkNewProject,
            projectDetailState.project.linkUpdate
        )
        binding.tvHighSPAT.text = highSPAT(projectDetailState.project.linkHighSPAT)
        binding.tvLinkHighSPAT.text = projectDetailState.project.linkHighSPAT


        _coordinate =
            "geo:${projectDetailState.project.latitude},${projectDetailState.project.longitude}"

        projectInfoInitial = projectDetailState.project
    }

    private fun linkProject(linkNewProject: String?, linkUpdate: String?): String? {
        return if (!linkNewProject.isNullOrEmpty()) linkNewProject
        else linkUpdate
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

    private fun typeIng(linkNewProject: String?, linkUpdate: String?): String {
        return if (!linkNewProject.isNullOrEmpty()) "New Project"
        else "Update Project"
    }

    private fun errorState() {

    }

    private fun showMap() {
//        val intent = Intent(Intent.ACTION_VIEW).apply {
//            data = geoLocation
//        }
//        if (intent.resolveActivity(packageManager) != null) {
//            startActivity(intent)
//        }

//        val mapIntent: Intent = Uri.parse(
//            "geo:0,0?q=1600+Amphitheatre+Parkway,+Mountain+View,+California"
//        ).let { location ->
//            // Or map point based on latitude/longitude
//            //val location: Uri = Uri.parse("geo:37.422219,-122.08364?z=14") // z param is zoom level
//            Intent(Intent.ACTION_VIEW, location)
//        }

        val mapIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(_coordinate)
        )

        startActivity(mapIntent)
    }
}