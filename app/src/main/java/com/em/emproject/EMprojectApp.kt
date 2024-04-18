package com.em.emproject

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.persistentCacheSettings
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class EMprojectApp : Application() {

    override fun onCreate() {
        super.onCreate()

//        val db = FirebaseFirestore.getInstance()
////        val settings = FirebaseFirestoreSettings
//        val settings= firestoreSettings {
//            setLocalCacheSettings(persistentCacheSettings {
//                setCacheSizeBytes(10 * 1024 * 1024)
//            })
//        }
////            .setPersistenceEnabled(true)
////            .build()




//        db.firestoreSettings = settings

//        Firebase.firestore.persistentCacheIndexManager?.apply {
//            // Indexing is disabled by default
//            enableIndexAutoCreation()
//        } ?: println("indexManager is null")


    }
}