package com.igoapp.i_go.feature_note.presentation.doctors

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.igoapp.i_go.R
import com.igoapp.i_go.feature_note.data.storage.idStore
import com.igoapp.i_go.feature_note.domain.util.log
import com.igoapp.i_go.feature_note.presentation.doctors.hospitals.HospitalViewModel
import com.igoapp.i_go.feature_note.presentation.doctors.hospitals.HospitalsViewModel
import com.igoapp.i_go.feature_note.presentation.util.ExceptionMessage
import com.igoapp.i_go.feature_note.presentation.util.Screen
import com.igoapp.i_go.feature_note.presentation.util.addFocusCleaner
import com.igoapp.i_go.ui.theme.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@Composable
fun DoctorScreen (
    navController : NavHostController,
    hospitalsViewsModel: HospitalsViewModel = hiltViewModel(),
    hospitalViewModel: HospitalViewModel = hiltViewModel(),
    doctorViewModel: DoctorViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current

    var context = LocalContext.current
    var scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    var expanded by remember { mutableStateOf(false) }

    val icon = if (expanded){
        Icons.Filled.KeyboardArrowUp
    } else {
        Icons.Filled.KeyboardArrowDown
    }

    val FCM_TOKEN: SharedPreferences =
        context.getSharedPreferences("FCM_TOKEN", Context.MODE_PRIVATE)

    "This is FCM_TOKEN: ${FCM_TOKEN.getString("FCM_TOKEN", "").toString()}".log()


    val userIdKey = stringPreferencesKey("user")
    var userId = flow<String> {

        context.idStore.data.map {
            it[userIdKey]
        }.collect {
            if (it != null) {
                this.emit(it)
            }
        }
    }.collectAsState(initial = "")


    LaunchedEffect(key1 = true) {
        doctorViewModel.setFCMToken(FCM_TOKEN.getString("FCM_TOKEN", "").toString())
        doctorViewModel.getUserInfo(if (userId.value.isEmpty()) 1 else userId.value.toInt())

        doctorViewModel.eventFlow.collectLatest { event ->
            when (event) {
                is DoctorViewModel.UiEvent.SaveDoctor -> {
               //     scaffoldState.snackbarHostState.showSnackbar("의료진 저장")
                    "의료진 SUCCESS!!".log()
                    navController.navigate(Screen.PatientsScreen.route)
                }
                is DoctorViewModel.UiEvent.ShowSnackbar -> {
                    "의료진 ERROR!!".log()
                    scaffoldState.snackbarHostState.showSnackbar("의료진 저장 실패")
                }
            }
        }
    }


    Scaffold(
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .addFocusCleaner(focusManager)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    text = "의료진 정보",
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.Bold,
                    color = primary,
                    textAlign = TextAlign.Center
                )
            }
            Divider(color = primary, modifier = Modifier.shadow(8.dp), thickness = 2.dp)
            Profile()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp)
                    .padding(end = 10.dp),
                verticalAlignment = CenterVertically
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "이름",
                    modifier = Modifier
                        .padding(start = 30.dp),
                    color = recruit_city
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 40.dp)
                        .padding(end = 40.dp)
                        .padding(top = 10.dp)
                        .padding(bottom = 10.dp)
                ) {
                    MakeRectangular()
                    BasicTextField(
                        value = doctorViewModel.user.value.name,
                        onValueChange = {
                            doctorViewModel.onEvent(DoctorEvent.EnteredName(it), userId.value.toInt())
                        },
                        modifier = Modifier
                            .padding(10.dp)
                            .align(BottomCenter),
                        textStyle = MaterialTheme.typography.body1,
                        singleLine = true,
                    )

                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp),
                verticalAlignment = CenterVertically
            ) {
                Text(
                    text = "전공",
                    modifier = Modifier
                        .padding(start = 30.dp),
                    color = recruit_city
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 40.dp)
                        .padding(end = 40.dp)
                        .padding(top = 20.dp)
                        .padding(bottom = 20.dp)
                ) {
                    MakeRectangular()
                    BasicTextField(
                        value = doctorViewModel.user.value.subjects,
                        onValueChange = {
                            doctorViewModel.onEvent(DoctorEvent.EnteredMajor(it), userId.value.toInt())
                        },
                        modifier = Modifier
                            .padding(10.dp)
                            .align(BottomCenter),
                        textStyle = MaterialTheme.typography.body1,
                        singleLine = true,
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 5.dp),
                verticalAlignment = CenterVertically
            ) {
                Text(
                    text = "시설",
                    modifier = Modifier
                        .padding(start = 30.dp),
                    color = recruit_city
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 40.dp)
                        .padding(end = 40.dp)
                        .padding(top = 10.dp)
                        .padding(bottom = 10.dp)
                ) {
                    MakeRectangular(modifier = Modifier.padding(top = 4.dp))
                    Column(
                        horizontalAlignment = CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ){
                        Box {
                                TextButton(
                                    onClick = { expanded = !expanded },
                                    colors= ButtonDefaults.buttonColors(backgroundColor = card_color),
                                ) {
                                Row {
                                    Text(
                                        text = hospitalViewModel.getHospitalDetail(doctorViewModel.user.value.hospital).name!!,
                                        modifier = Modifier.align(CenterVertically),
                                        color = Black,
                                        maxLines = 1,
                                        style = MaterialTheme.typography.body1,
                                    )
                                    Icon(
                                        icon,
                                        contentDescription = "",
                                        Modifier
                                            .padding(start = 3.dp)
                                            .clickable { expanded = !expanded }
                                            .background(card_color),
                                        tint = button_color
                                    )

                                }
                            }
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = {
                                    expanded = false
                                },
                                modifier = Modifier.width(160.dp)
                            ) {

                                hospitalsViewsModel.state.value.hospitalDTOs.forEach { label ->
                                    DropdownMenuItem(
                                        onClick = {
                                        doctorViewModel.onEvent(DoctorEvent.EnteredHospital(label.id!!), userId.value.toInt())
                                        expanded = false
                                    }) {
                                        Text(text = label.name!!)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = {
                    scope.launch {
                        if (doctorViewModel.user.value.name.isEmpty()) ExceptionMessage(doctorViewModel.user.value.name, "이름을 입력해주세요", scaffoldState)
                        else if (doctorViewModel.user.value.subjects.isEmpty())  ExceptionMessage(doctorViewModel.user.value.subjects, "전공을 입력해주세요", scaffoldState)
                        else doctorViewModel.onEvent(DoctorEvent.SaveDoctor, userId.value.toInt())
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = call_color),
                modifier = Modifier
                    .align(CenterHorizontally)
                    .padding(20.dp)
                    .width(220.dp)
                    .height(50.dp)
                    .clip(shape = RoundedCornerShape(26.dp, 26.dp, 26.dp, 26.dp))
            ) {
                Text(
                    text = if (doctorViewModel.state.value.userResponseDTO.name!!.isNotEmpty()){ "수정하기" } else{ "저장하기" },
                    color = Color.White,
                    style = MaterialTheme.typography.h4,
                    fontSize = 20.sp
                )
            }
        }
    }

}

@Composable
fun Profile() {
    Column(
        modifier = Modifier
            .padding(top = 60.dp, bottom = 60.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = CircleShape,
            modifier = Modifier
                .padding(8.dp)
                .size(150.dp)
        ) {
            Image(
                painterResource(id = R.drawable.diagnosis),
                contentDescription = "painting"
            )
        }
    }
}

@Composable
fun MakeRectangular(modifier: Modifier = Modifier) {
    val cornerRadius: Dp = 10.dp
    val cutCornerSize: Dp = 0.dp
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .shadow(elevation = 5.dp, shape = RoundedCornerShape(10.dp))
    ) {
        val clipPath = Path().apply {
            lineTo(size.width - cutCornerSize.toPx(), 0f)
            lineTo(size.width, cutCornerSize.toPx())
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }

        clipPath(clipPath) {
            drawRoundRect(
                color = card_color,
                size = size,
                cornerRadius = CornerRadius(cornerRadius.toPx())
            )
        }
    }
}
