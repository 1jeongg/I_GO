package com.igoapp.i_go.feature_note.presentation.patients

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.AsyncTask
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.igoapp.i_go.R
import com.igoapp.i_go.feature_note.data.remote.responseDTO.PatientByIdDTO
import com.igoapp.i_go.feature_note.data.remote.responseDTO.PatientDTO
import com.igoapp.i_go.feature_note.data.storage.idStore
import com.igoapp.i_go.feature_note.domain.util.log
import com.igoapp.i_go.feature_note.presentation.alarms.AlarmViewModel
import com.igoapp.i_go.feature_note.presentation.patients.components.PatientItem
import com.igoapp.i_go.feature_note.presentation.util.Screen
import com.igoapp.i_go.ui.theme.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

@ExperimentalAnimationApi
@Composable
fun PatientsScreen(
    navController: NavController,
    viewModel: PatientsViewModel = hiltViewModel(),
    notificationViewModel: AlarmViewModel = hiltViewModel(),
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    var context = LocalContext.current

    val nameKey = stringPreferencesKey("user")
    var name = flow<String> {

        context.idStore.data.map {
            it[nameKey]
        }.collect {
            if (it != null) {
                this.emit(it)
            }
        }
    }.collectAsState(initial = "")

    val ID_FCM: SharedPreferences =
        context.getSharedPreferences("ID_FCM", Context.MODE_PRIVATE)

    val IMAGE_FCM: SharedPreferences =
        context.getSharedPreferences("IMAGE_FCM", Context.MODE_PRIVATE)

    val X_FCM: SharedPreferences =
        context.getSharedPreferences("X_FCM", Context.MODE_PRIVATE)

    val Y_FCM: SharedPreferences =
        context.getSharedPreferences("Y_FCM", Context.MODE_PRIVATE)

    val NAME_FCM: SharedPreferences =
        context.getSharedPreferences("NAME_FCM", Context.MODE_PRIVATE)

    if (!ID_FCM.getString("ID_FCM", "").isNullOrBlank()){
        navController.navigate(Screen.AddEditPatientScreen.route +
                "?patientId=${ID_FCM.getString("ID_FCM", "")}" +
                "&patientImage=${IMAGE_FCM.getString("IMAGE_FCM", "")}")
        notificationViewModel.addNotification(
            patient_id = ID_FCM.getString("ID_FCM", "")!!.toInt(),
            patient_image = IMAGE_FCM.getString("IMAGE_FCM", "")!!.toInt(),
            patient_x = X_FCM.getString("X_FCM", "")!!.toDouble(),
            patient_y = Y_FCM.getString("Y_FCM", "")!!.toDouble(),
            patient_name = NAME_FCM.getString("NAME_FCM", "")!!,
        )
        ID_FCM.edit().putString("ID_FCM", "").apply()
    }

    val openDialog = remember { mutableStateOf(false) }
    var patientId by remember { mutableStateOf(1) }
    var patientName by remember { mutableStateOf("") }


    LaunchedEffect(key1 = true){
        viewModel.getPatients(if (name.value.isEmpty()) 1 else name.value.toInt())
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddEditPatientScreen.route)
                },
                backgroundColor = button_color
            ) {
                Icon(imageVector = Icons.Default.Add, tint = Color.White, contentDescription = "Add note")
            }
        },
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(it) { data ->
                Snackbar(
                    actionColor = button_color,
                    snackbarData = data
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "I-GO",
                    style = MaterialTheme.typography.h1,
                    fontWeight = FontWeight.Bold,
                    color = primary,
                    modifier = Modifier.padding(start = 12.dp)
                )

                Row (
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = {
                            navController.navigate(Screen.DoctorScreen.route)
                        },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.profile),
                            contentDescription = "profile",
                            tint = dark_blue,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    IconButton(
                        onClick = {
                            navController.navigate(Screen.AlarmScreen.route)
                        },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.bell),
                            contentDescription = "Alarm",
                            tint = dark_blue,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
            Divider(color = primary, modifier = Modifier.shadow(8.dp), thickness = 2.dp)
            if (openDialog.value){
                AlertDialog(
                    onDismissRequest = { openDialog.value = false},
                    title = {
                        Text(
                            text = "$patientName 환자를 호출하시겠습니까?",
                            style = MaterialTheme.typography.body1,
                            fontSize = 22.sp,
                            color = primary,
                            lineHeight = 35.sp
                        )
                    },
                    text = {

                    },
                    confirmButton = {
                        Box {
                            Button(
                                modifier = Modifier.align(Center),
                                onClick = {
                                    //OpenWebPage(context).execute("http://google.com")

                                    viewModel.onEvent(
                                        PatientsEvent.CallPatient,
                                        doctor_id = name.value.toInt(),
                                        patient_id = patientId,
                                    )
                                    openDialog.value = false

                                },
                                colors = ButtonDefaults.buttonColors(backgroundColor = call_color)
                            ) {
                                Text("호출하기", style = MaterialTheme.typography.h4, color = White)
                            }
                        }
                    },
                    dismissButton = {
                        Box {
                            Button(
                                modifier = Modifier.align(Center),
                                onClick = {
                                    openDialog.value = false
                                },
                                colors = ButtonDefaults.buttonColors(backgroundColor = call_color)
                            ) {
                                Text("취소하기", style = MaterialTheme.typography.h4, color = White)
                            }
                        }
                    }
                )
            }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                viewModel.state.value.patientDTOS.forEach {
                    item {
                        PatientItem(
                            name = it.name!!,
                            age = it.age!!,
                            gender = it.gender!!,
                            blood_type = it.blood_type!!,
                            disease = it.disease!!,
                            image = it.image!!,
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                navController.navigate(
                                Screen.AddEditPatientScreen.route +
                                    "?patientId=${it.id}&patientImage=${it.image}"
                                 )
                            },
                            onDeleteClick = {
                                viewModel.onEvent(
                                    PatientsEvent.DeletePatient(patient = MappingPatient(it)),
                                    doctor_id = name.value.toInt(),
                                    patient_id = it.id!!
                                )
                                scope.launch{
                                    val result = scaffoldState.snackbarHostState.showSnackbar(
                                        message = "환자가 삭제되었습니다.",
                                        actionLabel = "취소하기"
                                    )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        viewModel.onEvent(
                                            PatientsEvent.RestorePatients,
                                            doctor_id = name.value.toInt(),
                                            patient_id = it.id!!
                                        )
                                    } else{
                                        viewModel.getPatients(if (name.value.isEmpty()) 1 else name.value.toInt())
                                    }
                                }
                            },
                            onCallClick = {
                                openDialog.value = true
                                patientId = it.id!!
                                patientName = it.name
                                try {
                                    OpenUrl("http://" + it.ip_address!! + "/gpio/1")
                                } catch(e: Exception){
                                }
                            }
                        )
                    }
                }
                item {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .align(CenterHorizontally)
                            .fillMaxWidth()
                            .padding(vertical = 78.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Image(
                            painter = painterResource(id = R.drawable.empty),
                            contentDescription = "empty",
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                                .size(80.dp)
                        )
                        Text(
                            text = "아직 환자가\n등록되지 않았습니다.",
                            style = MaterialTheme.typography.body1,
                            color = text_gray
                        )
                    }
                }
            }
        }
    }
}
class OpenWebPage(var context: Context) : AsyncTask<String, Void, Void>() {
    override fun doInBackground(vararg params: String?): Void? {
     //   val url = params.get(0)
        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://google.com"))
        context.startActivity(webIntent)
        return null
    }
}
fun OpenUrl(myurl: String = "http://google.com") {
    thread(start = true) {
        val url = URL(myurl)

        val huc = url.openConnection() as HttpURLConnection
        huc.requestMethod = "GET"

        if (huc.responseCode == HttpURLConnection.HTTP_OK) {
            val streamReader = InputStreamReader(huc.inputStream)
            val buffered = BufferedReader(streamReader)

            val content = StringBuilder()
            while (true) {
                val data = buffered.readLine() ?: break
                content.append(data)
            }
            buffered.close()
            huc.disconnect()
        }
    }
}
fun MappingPatient(patientById: PatientByIdDTO)
: PatientDTO {
    val patient = PatientDTO(
        name = patientById.name,
        gender = patientById.gender,
        age = patientById.age,
        blood_type = patientById.blood_type,
        blood_rh = patientById.blood_rh,
        disease = patientById.disease,
        extra = patientById.extra,
        image = patientById.image
    )
    return patient
}