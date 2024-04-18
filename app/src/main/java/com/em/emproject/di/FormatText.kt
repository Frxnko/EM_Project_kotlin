package com.em.emventas.di

import android.text.SpannableString
import android.text.style.UnderlineSpan
import javax.inject.Inject

class FormatText @Inject constructor(){

    fun textLikable(text: String): SpannableString {
        val mSpannableString = SpannableString(text)
        // Setting underline style from position 0 till length of the spannable string
        mSpannableString.setSpan(UnderlineSpan(), 0, mSpannableString.length, 0)
        //Display this spannable string in TextView
        return mSpannableString
    }
}