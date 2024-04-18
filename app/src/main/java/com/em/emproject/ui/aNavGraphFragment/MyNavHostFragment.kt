package com.em.emproject.ui.aNavGraphFragment

import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.fragment.NavHostFragment
import com.em.emproject.R

class MyNavHostFragment : NavHostFragment() {

    override fun onCreateNavHostController(navHostController: NavHostController) {
        super.onCreateNavHostController(navHostController)
        // Obtener el id del fragmento que se está mostrando
        val fragmentId = id

        // Crear un NavInflater
        val navInflater = navController.navInflater

        // Asignar el navigation graph que corresponda al fragmento
//        when (fragmentId) {
//            R.id.authFragment -> {
//                // Usar el navigation graph de autenticación
//                val graph = navInflater.inflate(R.navigation.nav_graph_auth)
//                navController.graph = graph
//            }
//            R.id.settingsFragment -> {
//                // Usar el navigation graph de configuración
//                val graph = navInflater.inflate(R.navigation.nav_graph_settings)
//                navController.graph = graph
//            }
//            R.id.mainFragment -> {
//                // Usar el navigation graph principal
//                val graph = navInflater.inflate(R.navigation.nav_graph_main)
//                navController.graph = graph
//            }
//        }
    }

//    override fun createGraph(): NavGraph {
//        // Obtener el id del fragmento que se está mostrando
//        val fragmentId = id
//
//        // Crear un NavController
//        val navController = NavController(requireContext())
//
//        // Asignar el navigation graph que corresponda al fragmento
//        when (fragmentId) {
//            R.id.authFragment -> {
//                // Usar el navigation graph de autenticación
//                navController.setGraph(R.navigation.nav_graph_auth)
//            }
//            R.id.settingsFragment -> {
//                // Usar el navigation graph de configuración
//                navController.setGraph(R.navigation.nav_graph_settings)
//            }
//            R.id.mainFragment -> {
//                // Usar el navigation graph principal
//                navController.setGraph(R.navigation.nav_graph_main)
//            }
//        }
//
//        // Devolver el NavController con el navigation graph asignado
//        return navController.graph
//    }
}