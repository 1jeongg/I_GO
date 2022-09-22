package com.example.i_go.feature_note.presentation.patients

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.Normal
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.i_go.R
import com.example.i_go.feature_note.data.remote.responseDTO.PatientByIdDTO
import com.example.i_go.feature_note.data.remote.responseDTO.PatientDTO
import com.example.i_go.feature_note.data.storage.dataStore
import com.example.i_go.feature_note.data.storage.idStore
import com.example.i_go.feature_note.domain.repository.UserRepository
import com.example.i_go.feature_note.domain.util.log
import com.example.i_go.feature_note.presentation.alarms.AlarmViewModel
import com.example.i_go.feature_note.presentation.patients.components.PatientItem
import com.example.i_go.feature_note.presentation.util.Screen
import com.example.i_go.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@Composable
fun PatientsScreen(
    navController: NavController,
    viewModel: PatientsViewModel = hiltViewModel(),
    notificationViewModel: AlarmViewModel = hiltViewModel(),
) {
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
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

    "Name is ${name.value}".log()
    val ID_FCM: SharedPreferences =
        context.getSharedPreferences("ID_FCM", Context.MODE_PRIVATE)

    val IMAGE_FCM: SharedPreferences =
        context.getSharedPreferences("IMAGE_FCM", Context.MODE_PRIVATE)
    val X_FCM: SharedPreferences =
        context.getSharedPreferences("X_FCM", Context.MODE_PRIVATE)

    val Y_FCM: SharedPreferences =
        context.getSharedPreferences("Y_FCM", Context.MODE_PRIVATE)
    "This is pref1: ${ID_FCM.getString("ID_FCM", "").toString()}".log()
    "This is pref2: ${IMAGE_FCM.getString("IMAGE_FCM", "").toString()}".log()

    if (!ID_FCM.getString("ID_FCM", "").isNullOrBlank()){
        navController.navigate(Screen.AddEditPatientScreen.route +
                "?patientId=${ID_FCM.getString("ID_FCM", "")}" +
                "&patientImage=${IMAGE_FCM.getString("IMAGE_FCM", "")}") {
            popUpTo(0)
        }
        notificationViewModel.addNotification(
            patient_id = ID_FCM.getString("ID_FCM", "")!!.toInt(),
            patient_image = IMAGE_FCM.getString("IMAGE_FCM", "")!!.toInt(),
            patient_x = X_FCM.getString("X_FCM", "")!!.toDouble(),
            patient_y = Y_FCM.getString("Y_FCM", "")!!.toDouble(),
        )
        ID_FCM.edit().putString("ID_FCM", "").apply()
    }

    val openDialog = remember { mutableStateOf(false) }
    var patientId by remember { mutableStateOf(1) }


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
                            text = "정말로 환자를 호출하시겠습니까?",
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
                                        "취소 버튼 누름".log()
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