package com.igoapp.i_go.feature_note.domain.util

import android.util.Log

fun String.log(header: String = "MYIGO") {
    Log.d(header, this)
}