package com.em.emventas.data.network

import android.content.ContentValues.TAG
import android.util.Log
import com.em.emproject.domain.model.Mistakes
import com.em.emproject.domain.model.Paralyzed
import com.em.emproject.domain.model.Progress
import com.em.emproject.domain.model.Project
import com.em.emproject.domain.model.ProjectAssignedList
import com.em.emproject.domain.model.ProjectStatusItem
import com.em.emproject.domain.model.ProviderType
import com.em.emproject.domain.model.RegisterProgress
import com.em.emproject.domain.model.StatusProject
import com.em.emproject.domain.model.UserClass
import com.em.emproject.domain.useCase.GetProjectUseCase.Companion.listProjectGlobal
import com.em.emproject.ui.login.LoginAppViewModel.Companion.USER_CURRENT
import com.em.emproject.ui.projects.detailAssignedProject.DetailActivity
import com.em.emproject.ui.projects.detailAssignedProject.DetailActivity.Companion.CODE_PROJECT_DETAIL
import com.example.emcontrol.domain.model.ItemProgress
import com.example.emcontrol.domain.model.ItemProgressInfo
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject


class FireDataBaseService @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val firebaseClient: FirebaseClient
) {

    companion object {
        const val PATH_USERS = "users"
        const val PATH_PROGRESS = "progressProject"
        const val PATH_PROJECT = "projects"
        const val FIELD_PROJECT = "projectName"
        const val FIELD_SUPERVISOR = "idSupervisor"
        var DOC_REF_PROJECT = ""
        var DOC_REF_PROJECT_PROGRESS = ""
        var FIELD_PROJECT_PROGRESS = ""
    }

    suspend fun createUser(
        email: String,
        password: String,
        name: String,
        surname: String,
        typeUser: String
    ): DocumentReference? {
        val result = firebaseClient.auth.createUserWithEmailAndPassword(email, password).await()
        val userFireBase = result.user

        if (userFireBase != null) {
            val user = hashMapOf(
                "userName" to userName(name, surname),
                "name" to name,
                "surname" to surname,
                "email" to email,
                "isExpert" to isExpert(typeUser),
                "typeUser" to "G",
                "timeRegister" to getTimeRegister(),
                "image" to "",
                "provider" to ProviderType.BASIC
            )
            val docRef = fireStore.collection(PATH_USERS).document(userFireBase.uid)
            docRef.set(user).await()
            return docRef
        } else {
            return null
        }
    }

    private fun isExpert(typeUser: String): Boolean {
        return typeUser == "Admin"
    }

    private fun userName(names: String, surnames: String): String {
        val surname = surnames.split(" ")

        return names[0].lowercase() + surname[0] + surname[1][0]

    }

    private fun getTimeRegister(): String {
        val calendar: Calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR)
        val min = calendar.get(Calendar.MINUTE)
        val seg = calendar.get(Calendar.SECOND)

        return "$year-$month-$day $hour:$min:$seg"
    }


    suspend fun loginUser(email: String, password: String): Result<UserClass> {
        return try {
            val result = firebaseClient.auth.signInWithEmailAndPassword(email, password).await()
            val user = result.user
            if (user != null) {
                //Get Object User from Firestore
                val snapshot = fireStore.collection(PATH_USERS).document(user.uid).get().await()
                val loggedUser = snapshot.toObject(UserClass::class.java)

                if (loggedUser != null) {
                    Result.success(loggedUser)
                } else {
                    Result.failure(Exception("User is null"))
                }
            } else {
                Result.failure(Exception("User is null"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logoutUser() {
        firebaseClient.auth.signOut()
    }

    fun getCurrentUser(): UserClass? {
        val user = firebaseClient.auth.currentUser
        return if (user != null) {
            val snapshot =
                runBlocking {fireStore.collection(PATH_USERS).document(user.uid).get().await() }
            snapshot.toObject(UserClass::class.java)
        } else {
            null
        }
    }

    suspend fun verifyValue(valor: Any): Boolean {
        // Crear una variable para almacenar el resultado
        var exist = false

        // Crear una referencia a la colección indicada
        val coleccionRef = fireStore.collection(PATH_PROJECT)
//        val coleccionRef = FirebaseFirestore.getInstance().collection(collection)

        // Crear una consulta que filtre los documentos que tienen el valor indicado en el campo indicado
        val query = coleccionRef.whereEqualTo(FIELD_PROJECT, valor)

        // Ejecutar la consulta de forma asíncrona y obtener un objeto Deferred<QuerySnapshot>
        val deferred = query.get()

        // Usar await para esperar el resultado de la consulta y obtener un objeto QuerySnapshot
        val querySnapshot = deferred.await()

        // Verificar si la instantánea de la consulta tiene algún documento
        exist = !querySnapshot.isEmpty

        // Devolver el resultado
        return exist
    }

    suspend fun createProject(project: Project): DocumentReference? {
        return fireStore.collection(PATH_PROJECT).add(project).await()
    }

    fun getAllProjects02(): List<ProjectAssignedList> {

        return insertProjectFromListToFirebase03()


    }

    suspend fun getAllProjects(): List<ProjectAssignedList> {

//        return insertProjectFromListToFirebase03()
//        return listProjectGlobal

        return loadDataFromFirestore()

    }

    suspend fun getProject(nameProject: String): Project? {

        val query = fireStore.collectionGroup(PATH_PROJECT)
            .whereEqualTo(FIELD_PROJECT, nameProject).limit(1)

        val deferred = query.get()

        // Usar await para esperar el resultado de la consulta y obtener un objeto QuerySnapshot
        val querySnapshot = deferred.await()
        val document = querySnapshot.documents[0]
        DOC_REF_PROJECT = document.reference.id

        if (document != null) {
            val codeProject = document.getString("codeProject")
            val projectName = document.getString("projectName")
            val nodeType = document.getString("nodeType")
            val red = document.getString("nodeType")
            val address = document.getString("address")
            val locality = document.getString("locality")
            val district = document.getString("district")
            val province = document.getString("province")
            val region = document.getString("region")
            val approvedCandidateRFT = document.getString("approvedCandidateRFT")
            val latitude = document.getDouble("latitude")
            val longitude = document.getDouble("longitude")

            val bathAssociated = document.getString("bathAssociated")

            val towerType = document.getString("towerType")
            val towerHeight = document.getString("towerHeight")
            val towerSupplier = document.getString("towerSupplier")
            val civilDesignAvailability = document.getString("civilDesignAvailability")
            val nasVersion = document.getString("nasVersion")
            val linkNewProject = document.getString("linkNewProject")
            val linkUpdate = document.getString("linkUpdate")
            val linkHighSPAT = document.getString("linkHighSPAT")

            val dateStart = document.getString("dateStart")
            val dateEnd = document.getString("dateEnd")
            val contractor = document.getString("contractor")
            val status = document.getString("status")
            val image = document.getString("image")
            val progress = document.getDouble("progress")
            val dateRegisterProgress = document.getString("dateRegisterProgress")
            val lastReportContractor = document.getString("lastReportContractor")
            val lastReportSupervision = document.getString("lastReportSupervision")
            val comments = document.getString("comments")
            val additional = document.getString("additional")
            val comments02 = document.getString("comments02")
            val pma = document.getString("pma")
            val papRealized = document.getString("papRealized")
            val zone = document.getString("zone")

            val projectStatusItem = getListStatus(document.get("projectStatusItem") as ArrayList<*>)
            val mistakes = getListMistakes(document.get("mistakes") as ArrayList<*>)
            val paralyzed = getListParalyzed(document.get("paralyzed") as ArrayList<*>)
            val created = document.getString("created")

            return Project(
                codeProject!!,
                projectName!!,
                nodeType!!,
                red!!,
                address,
                locality,
                district,
                province,
                region,
                approvedCandidateRFT,
                latitude,
                longitude,
                bathAssociated,
                towerType,
                towerHeight,
                towerSupplier,
                civilDesignAvailability,
                nasVersion,
                linkNewProject,
                linkUpdate,
                linkHighSPAT,
                dateStart,
                dateEnd,
                contractor,
                status,
                image,
                progress,
                dateRegisterProgress,
                lastReportContractor,
                lastReportSupervision,
                comments,
                additional,
                comments02,
                pma,
                papRealized,
                zone,
                projectStatusItem,
                mistakes,
                emptyList(),
                paralyzed,
                created!!
            )
        } else {
            return null
        }


    }

    private fun getListParalyzed(arrayParalyzed: ArrayList<*>): ArrayList<Paralyzed> {
        var map: HashMap<String, Any>
        val arrayAux: MutableList<Paralyzed> = arrayListOf()

        for (itemAux in arrayParalyzed) {
            map = itemAux as HashMap<String, Any>

            val dateStart = map["dateStart"] as String
            val dateEnd = map["dateEnd"] as String
            val daysParalyzed = map["daysParalyzed"] as String
            val description = map["description"] as String
            val isResolved = map["isResolved"] as Boolean
            val descriptionResolved = map["descriptionResolved"] as String
            arrayAux.add(
                Paralyzed(
                    dateStart,
                    dateEnd,
                    daysParalyzed,
                    description,
                    isResolved,
                    descriptionResolved
                )
            )
        }

        val array = arrayListOf<Paralyzed>()
        array.addAll(arrayAux)
        return array
    }

    private fun getListMistakes(arrayMistakes: ArrayList<*>): ArrayList<Mistakes> {
        var map: HashMap<String, Any>
        val arrayAux: MutableList<Mistakes> = arrayListOf()

        for (itemAux in arrayMistakes) {
            map = itemAux as HashMap<String, Any>

            val descriptionMistakes = map["descriptionMistakes"] as String
            val dateRegister = map["dateRegister"] as String
            val dateSend = map["dateSend"] as String
            val location = map["location"] as String
            val typeMistakes = map["typeMistakes"] as String
            val department = map["department"] as String
            val impact = map["impact"] as String
            val references = map["references"] as String
            val comments = map["comments"] as String
            val images = map["images"] as String
            val recommendationsConclusions = map["recommendationsConclusions"] as String

            val groupId = map["groupId"] as String
            val descriptionGroup = map["images"] as String
            val levelMistake = map["levelMistake"] as String

            val statusMistakes = map["statusMistakes"] as String

            val resident = map["resident"] as String
            val dateReparation = map["dateReparation"] as String
            val descriptionReparation = map["descriptionReparation"] as String

            arrayAux.add(
                Mistakes(
                    descriptionMistakes,
                    dateRegister,
                    dateSend,
                    location,
                    typeMistakes,
                    department,
                    impact,
                    references,
                    comments,
                    images,
                    recommendationsConclusions,
                    groupId,
                    descriptionGroup,
                    levelMistake,
                    statusMistakes,
                    resident,
                    dateReparation,
                    descriptionReparation
                )
            )
        }

        val array = arrayListOf<Mistakes>()
        array.addAll(arrayAux)
        return array
    }

    private fun getListStatus(arrayListStatus: ArrayList<*>): ArrayList<ProjectStatusItem> {
        var map: HashMap<String, Any>
        val arrayAux: MutableList<ProjectStatusItem> = arrayListOf()

        for (itemAux in arrayListStatus) {
            map = itemAux as HashMap<String, Any>

            val item = map["item"] as String
            val description = map["description"] as String
            val comments = map["comments"] as String
            val apply = map["apply"] as Boolean
            val order = map["order"] as Long
            val progress = map["progress"] as Double

            arrayAux.add(
                ProjectStatusItem(
                    item,
                    description,
                    comments,
                    apply,
                    order.toInt(),
                    progress
                )
            )
        }

        val array = arrayListOf<ProjectStatusItem>()
        array.addAll(arrayAux)
        return array
    }


    private fun insertProjectFromListToFirebase(): List<ProjectAssignedList> {
//        val projectList: ArrayList<Project> = ArrayList()

        val ref = fireStore.collection(PATH_PROJECT)


        ref.get().addOnSuccessListener { querySnapshot ->
            // Crear una lista vacía para almacenar los nombres de los usuarios
            val projectList = mutableListOf<ProjectAssignedList>()

            // Recorrer cada documento de la instantánea de la consulta
            for (document in querySnapshot) {
                // Obtener el campo name del documento y agregarlo a la lista
                val projectName = document.getString("projectName")
                val towerHeight = document.getString("name")
                val towerSupplier = document.getString("towerSupplier")
                val towerType = document.getString("towerType")
                val contractor = document.getString("towerHeight")
                val dateStart = document.getString("dateStart")
                val dateEnd = document.getString("dateEnd")
                val status = document.getString("status")
                val image = document.getString("image")
                val progress = document.getDouble("progress")
                val lastReportContractor = document.getString("lastReportContractor")
                val lastReportSupervision = document.getString("lastReportSupervision")

                projectList.add(
                    ProjectAssignedList(
                        projectName!!,
                        towerHeight!!,
                        towerSupplier,
                        towerType,
                        contractor,
                        dateStart,
                        dateEnd,
                        status,
                        image,
                        progress,
                        lastReportContractor,
                        lastReportSupervision
                    )
                )
            }
            listProjectGlobal = projectList.toList()
        }

        return listProjectGlobal
    }


    private fun insertProjectFromListToFirebase02(): List<ProjectAssignedList> {
//        val projectList: ArrayList<Project> = ArrayList()

//        val ref = fireStore.collection(PATH_PROJECT).document("SF")
        val ref = fireStore.collection(PATH_PROJECT)

        ref.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                Log.d(TAG, "Current data: ${snapshot}")
                val projectList = mutableListOf<ProjectAssignedList>()
                for (document in snapshot) {
                    // Obtener el campo name del documento y agregarlo a la lista
                    val projectName = document.getString("projectName")
                    val towerHeight = document.getString("name")
                    val towerSupplier = document.getString("towerSupplier")
                    val towerType = document.getString("towerType")
                    val contractor = document.getString("towerHeight")
                    val dateStart = document.getString("dateStart")
                    val dateEnd = document.getString("dateEnd")
                    val status = document.getString("status")
                    val image = document.getString("image")
                    val progress = document.getDouble("progress")
                    val lastReportContractor = document.getString("lastReportContractor")
                    val lastReportSupervision = document.getString("lastReportSupervision")

                    projectList.add(
                        ProjectAssignedList(
                            projectName!!,
                            towerHeight!!,
                            towerSupplier,
                            towerType,
                            contractor,
                            dateStart,
                            dateEnd,
                            status,
                            image,
                            progress,
                            lastReportContractor,
                            lastReportSupervision
                        )
                    )
                }
                listProjectGlobal = projectList.toList()


            } else {
                Log.d(TAG, "Current data: null")
            }
        }


        return listProjectGlobal
    }

    private suspend fun loadDataFromFirestore(): List<ProjectAssignedList> {
        return try {
            // Usa 'withContext' para cambiar a un hilo de fondo
            withContext(Dispatchers.IO) {
                val ref = fireStore.collection(PATH_PROJECT)
                // Realiza la consulta a Firestore y espera los resultados
                val snapshot = ref.get().await()
                val projectList = mutableListOf<ProjectAssignedList>()

                // Procesa los datos recibidos
//                val data = snapshot.documents.map { it.toObject(YourDataClass::class.java) }
                for (document in snapshot) {
                    // Obtener el campo name del documento y agregarlo a la lista
                    val projectName = document.getString("projectName")
                    val towerHeight = document.getString("name")
                    val towerSupplier = document.getString("towerSupplier")
                    val towerType = document.getString("towerType")
                    val contractor = document.getString("towerHeight")
                    val dateStart = document.getString("dateStart")
                    val dateEnd = document.getString("dateEnd")
                    val status = document.getString("status")
                    val image = document.getString("image")
                    val progress = document.getDouble("progress")
                    val lastReportContractor = document.getString("lastReportContractor")
                    val lastReportSupervision = document.getString("lastReportSupervision")

                    projectList.add(
                        ProjectAssignedList(
                            projectName!!,
                            towerHeight!!,
                            towerSupplier,
                            towerType,
                            contractor,
                            dateStart,
                            dateEnd,
                            status,
                            image,
                            progress,
                            lastReportContractor,
                            lastReportSupervision
                        )
                    )
                }
                listProjectGlobal = projectList.toList()

                // Retorna los datos
                return@withContext projectList
            }
        } catch (e: Exception) {
            // Maneja cualquier error que pueda ocurrir
            return emptyList()
//            Result.failure(e)
        }

    }

    private fun insertProjectFromListToFirebase03(): List<ProjectAssignedList> {
        val ref = fireStore.collection(PATH_PROJECT)

        ref.addSnapshotListener { snapshot, e ->
            e?.let {
                return@addSnapshotListener
            }
            snapshot?.let {
                val projectList = mutableListOf<ProjectAssignedList>()
                for (document in snapshot) {
                    // Obtener el campo name del documento y agregarlo a la lista
                    val projectName = document.getString("projectName")
                    val towerHeight = document.getString("name")
                    val towerSupplier = document.getString("towerSupplier")
                    val towerType = document.getString("towerType")
                    val contractor = document.getString("towerHeight")
                    val dateStart = document.getString("dateStart")
                    val dateEnd = document.getString("dateEnd")
                    val status = document.getString("status")
                    val image = document.getString("image")
                    val progress = document.getDouble("progress")
                    val lastReportContractor = document.getString("lastReportContractor")
                    val lastReportSupervision = document.getString("lastReportSupervision")

                    projectList.add(
                        ProjectAssignedList(
                            projectName!!,
                            towerHeight,
                            towerSupplier,
                            towerType,
                            contractor,
                            dateStart,
                            dateEnd,
                            status,
                            image,
                            progress,
                            lastReportContractor,
                            lastReportSupervision
                        )
                    )
                }
                listProjectGlobal = projectList.toList()
            }
        }
        return listProjectGlobal
    }

    private fun insertProjectFromListToFirebase2(): List<ProjectAssignedList> {
//        val projectList: ArrayList<Project> = ArrayList()

        val ref = fireStore.collection(PATH_PROJECT)
//        val query = ref.whereEqualTo(FIELD_SUPERVISOR, USER_CURRENT!!.userName)
        val query = fireStore.collectionGroup(PATH_PROJECT)
            .whereEqualTo(FIELD_SUPERVISOR, USER_CURRENT!!.userName)

        query.get().addOnSuccessListener { querySnapshot ->
            // Crear una lista vacía para almacenar los nombres de los usuarios
            val projectList = mutableListOf<ProjectAssignedList>()

            // Recorrer cada documento de la instantánea de la consulta
            for (document in querySnapshot) {
                // Obtener el campo name del documento y agregarlo a la lista
                val projectName = document.getString("projectName")
                val towerHeight = document.getString("name")
                val towerSupplier = document.getString("towerSupplier")
                val towerType = document.getString("towerType")
                val contractor = document.getString("towerHeight")
                val dateStart = document.getString("dateStart")
                val dateEnd = document.getString("dateEnd")
                val status = document.getString("status")
                val image = document.getString("image")
                val progress = document.getDouble("progress")
                val lastReportContractor = document.getString("lastReportContractor")
                val lastReportSupervision = document.getString("lastReportSupervision")

                projectList.add(
                    ProjectAssignedList(
                        projectName!!,
                        towerHeight!!,
                        towerSupplier,
                        towerType,
                        contractor,
                        dateStart,
                        dateEnd,
                        status,
                        image,
                        progress,
                        lastReportContractor,
                        lastReportSupervision
                    )
                )
            }
            listProjectGlobal = projectList.toList()
        }

        return listProjectGlobal
    }

    suspend fun getProgressProject(nameProject: String): List<ItemProgress>? {
        return getProgressProjectList(nameProject)
    }

    private suspend fun getProgressProjectList(nameProject: String): List<ItemProgress>? {
        val arrayAux: MutableList<ItemProgress> = arrayListOf()

        val query = fireStore.collectionGroup(PATH_PROGRESS)
            .whereEqualTo(FIELD_PROJECT, nameProject).limit(1)

        val deferred = query.get()

        // Usar await para esperar el resultado de la consulta y obtener un objeto QuerySnapshot
        val querySnapshot = deferred.await()

        if (querySnapshot.isEmpty) return emptyList()
        else {
            val document = querySnapshot.documents[0]
            DOC_REF_PROJECT_PROGRESS = document.reference.id

            if (document != null) {
                val projectStatusItem: List<ItemProgress> =
                    getListItemProgress(document.get("listProgress") as ArrayList<*>)

                for (item in projectStatusItem) {
                    arrayAux.add(item)
                }
                return arrayAux
            } else {
                return emptyList()
            }

        }

    }

    private fun getListItemProgress(listItemProgress: ArrayList<*>): List<ItemProgress> {
        var map: HashMap<String, Any>
        val arrayAux: MutableList<ItemProgress> = arrayListOf()

        for (itemAux in listItemProgress) {
            map = itemAux as HashMap<String, Any>

            val item = map["item"] as String
            val description = map["description"] as String
            val incidence = map["incidence"] as Double
            val progress = map["progress"] as Double
            val status = map["status"] as Double
            val order = map["order"] as Long
            val type = map["type"] as String
            val group = map["group"] as String

            arrayAux.add(
                ItemProgress(
                    item,
                    description,
                    incidence,
                    progress,
                    status,
                    order.toInt(),
                    type,
                    group
                )
            )
        }

        val array = arrayListOf<ItemProgress>()
        array.addAll(arrayAux)
        return array
    }

    suspend fun saveProgressProjectItem(registerProgress: RegisterProgress): Boolean? {
        val docRef = fireStore.collection(PATH_PROGRESS).document(CODE_PROJECT_DETAIL)
        return try {
            val document = docRef.get().await()
            if (!document.exists()) {
                docRef.set(registerProgress).await()
                true
            } else false

        } catch (e: Exception) {
            null
        }


//        for ((index, itemProgressInfo) in itemProjectTowerList.withIndex()) {
//            try {
////                docRef.collection("miSubColeccion").document("persona$index").set(persona).await()
//                fireStore.collection(PATH_PROGRESS).document(DetailActivity.CODE_PROJECT_DETAIL)
//                    .set(itemProgressInfo).await()
//            } catch (e: Exception) {
//
//            }
//        }
    }


}

