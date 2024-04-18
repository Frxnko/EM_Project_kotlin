package com.em.emproject.di

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import javax.inject.Inject

class IntentClass @Inject constructor() {

    fun openlink(link: String, context: Context) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        context.startActivity(intent)
    }
}