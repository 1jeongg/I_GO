package com.example.i_go.feature_note.presentation.add_edit_patient.components

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.example.i_go.feature_note.data.storage.idStore
import com.example.i_go.feature_note.domain.util.log
import com.example.i_go.feature_note.presentation.doctors.hospitals.HospitalViewModel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.io.IOException

@Composable
fun PatientMap (
    modifier: Modifier = Modifier,
    hospitalViewModel: HospitalViewModel = hiltViewModel()
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val context = LocalContext.current
    val hospitalIdKey = stringPreferencesKey("hospital")
    var hospitalId = flow<String> {
        context.idStore.data.map {
            it[hospitalIdKey]
        }.collect {
            if (it != null) {
                this.emit(it)
            }
        }
    }.collectAsState(initial = "")

    val patientId = remember { mutableStateOf(0) }
    val ID_FCM: SharedPreferences =
        context.getSharedPreferences("ID_FCM", Context.MODE_PRIVATE)

    val X_FCM: SharedPreferences =
        context.getSharedPreferences("X_FCM", Context.MODE_PRIVATE)

    val Y_FCM: SharedPreferences =
        context.getSharedPreferences("Y_FCM", Context.MODE_PRIVATE)

    val x_fcm = X_FCM.getString("X_FCM", "").toString()
    val y_fcm = Y_FCM.getString("Y_FCM", "").toString()
    "fcm data on Screen - id: ${ID_FCM.getString("ID_FCM", "").toString()}".log()
    "fcm data on Screen - x : $x_fcm".log()
    "fcm data on Screen - y : $y_fcm".log()

    if (ID_FCM.getString("ID_FCM", "").toString().isNotBlank()) {
        patientId.value = ID_FCM.getString("ID_FCM", "")!!.toInt()
        ID_FCM.edit().putString("ID_FCM", "").apply()
    }


    val hospital = hospitalViewModel.getHospitalDetail(if (hospitalId.value.isNotEmpty()) hospitalId.value.toInt() else 1)
    val painter = rememberImagePainter(data = hospital.drawing, builder = {crossfade(true)})

    var drawing_x = remember { mutableStateOf(1) }
    val drawing_y = remember { mutableStateOf(1) }
    var station_x_1 = remember { mutableStateOf(1) }
    val station_x_2 = remember { mutableStateOf(1) }
    var station_x_3 = remember { mutableStateOf(1) }
    val station_x_4 = remember { mutableStateOf(1) }
    var station_y_1 = remember { mutableStateOf(1) }
    val station_y_2 = remember { mutableStateOf(1) }
    var station_y_3 = remember { mutableStateOf(1) }
    val station_y_4 = remember { mutableStateOf(1) }

    try{
        drawing_x.value = if(hospital.drawing_x!! > 0) hospital.drawing_x else 1
        drawing_y.value = if(hospital.drawing_y!! > 0) hospital.drawing_y else 1

        station_x_1.value = if(hospital.station_x_1!! > 0) hospital.station_x_1 else 1
        station_x_2.value = if(hospital.station_x_2!! > 0) hospital.station_x_2 else 1
        station_x_3.value = if(hospital.station_x_3!! > 0) hospital.station_x_3 else 1
        station_x_4.value = if(hospital.station_x_4!! > 0) hospital.station_x_4 else 1

        station_y_1.value = if(hospital.station_y_1!! > 0) hospital.station_y_1 else 1
        station_y_2.value = if(hospital.station_y_2!! > 0) hospital.station_y_2 else 1
        station_y_3.value = if(hospital.station_y_3!! > 0) hospital.station_y_3 else 1
        station_y_4.value = if(hospital.station_y_4!! > 0) hospital.station_y_4 else 1

    } catch(e: Exception){
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(screenWidth * drawing_y.value / drawing_x.value)
    ) {
        Image(
            painter = painter,
            contentDescription = "hospital",
        )

        // x의 범위: 0에서 682, y 범위: 0에서 484.5 사이
        MakeCircle(imageWidth = drawing_x.value, imageHeight = drawing_y.value, x = station_x_1.value, y = station_y_1.value)
        MakeCircle(imageWidth = drawing_x.value, imageHeight = drawing_y.value, x = station_x_2.value, y = station_y_2.value)
        MakeCircle(imageWidth = drawing_x.value, imageHeight = drawing_y.value, x = station_x_3.value, y = station_y_3.value)
        MakeCircle(imageWidth = drawing_x.value, imageHeight = drawing_y.value, x = station_x_4.value, y = station_y_4.value)

        PatientCircle(
            imageWidth = drawing_x.value,
            imageHeight = drawing_y.value,
            x = x_fcm.toDouble().toInt(),
            y = y_fcm.toDouble().toInt()
        )
    }

}
@Composable
fun MakeCircle(
    modifier: Modifier = Modifier,
    imageWidth: Int = 682,
    imageHeight: Int = 484,
    x: Int,
    y: Int
){
    Canvas(
        modifier = modifier.fillMaxSize()
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        drawCircle(
            color = Color.Blue,
            center = Offset(x = canvasWidth * x / imageWidth, y = canvasHeight * y / imageHeight),
            radius = 18f
        )
    }
}

@Composable
fun PatientCircle(
    modifier: Modifier = Modifier,
    imageWidth: Int = 682,
    imageHeight: Int = 484,
    x: Int,
    y: Int
){
    Canvas(
        modifier = modifier.fillMaxSize()
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        drawCircle(
            color = Color.Red,
            center = Offset(x = canvasWidth * x / imageWidth, y = canvasHeight * y / imageHeight),
            radius = 25f

        )
    }
}