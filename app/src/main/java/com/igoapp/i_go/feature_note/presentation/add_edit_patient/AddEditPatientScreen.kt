package com.igoapp.i_go.feature_note.presentation.add_edit_patient

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.igoapp.i_go.feature_note.data.remote.responseDTO.PatientByIdDTO
import com.igoapp.i_go.feature_note.data.remote.responseDTO.PatientByIdDTO.Companion.patient_image_real
import com.igoapp.i_go.feature_note.data.storage.idStore
import com.igoapp.i_go.feature_note.presentation.add_edit_patient.components.PatientMap
import com.igoapp.i_go.feature_note.presentation.add_edit_patient.components.TransparentHintTextField
import com.igoapp.i_go.feature_note.presentation.doctors.MakeRectangular
import com.igoapp.i_go.feature_note.presentation.util.ExceptionMessage
import com.igoapp.i_go.feature_note.presentation.util.Screen
import com.igoapp.i_go.feature_note.presentation.util.addFocusCleaner
import com.igoapp.i_go.ui.theme.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AddEditPatientScreen(
    navController: NavController,
    patientImage: Int,
    viewModel: AddEditPatientViewModel = hiltViewModel()
) {
    val nameState = viewModel.patientName.value
    val sexState = viewModel.patientSex.value
    val ageState = viewModel.patientAge.value
    val bloodTypeState = viewModel.patientBloodType.value
    val bloodRhState = viewModel.patientBloodRh.value
    val diseasesState = viewModel.patientDiseases.value
    val extraState = viewModel.patientExtra.value
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val focusRequester by remember { mutableStateOf(FocusRequester()) }
    val focusManager = LocalFocusManager.current

    val pagerState = rememberPagerState(
        pageCount = 5,
        initialPage = if (patientImage > 0) patientImage-1 else PatientByIdDTO.patientImages.random()
    )
    var context = LocalContext.current

    val doctorIdKey = stringPreferencesKey("user")
    var doctorId = flow<String> {
        context.idStore.data.map {
            it[doctorIdKey]
        }.collect {
            if (it != null) {
                this.emit(it)
            }
        }
    }.collectAsState(initial = "")

    LaunchedEffect(key1 = true) {
        viewModel.setDoctorId(if (doctorId.value.toInt() < 1) 1 else doctorId.value.toInt())
        viewModel.getPatient(if (doctorId.value.toInt() < 1) 1 else doctorId.value.toInt())

        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddEditPatientViewModel.UiEvent.SavePatient -> {
                    navController.navigate(Screen.PatientsScreen.route){
                        popUpTo(0)
                    }
                }
                is AddEditPatientViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = "환자 저장 실패"
                    )
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
                    text = "환자 정보",
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.Bold,
                    color = primary,
                    textAlign = TextAlign.Center
                )
            }
            Divider(color = primary, modifier = Modifier.shadow(8.dp), thickness = 2.dp)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .addFocusCleaner(focusManager)
                    .verticalScroll(rememberScrollState())
            ) {
                PatientMap(Modifier)
                Spacer(modifier = Modifier.height(10.dp))
                Divider(
                    modifier = Modifier.fillMaxWidth().height(1.5.dp).shadow(2.dp),
                    color = primary
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    HorizontalPager(
                        state = pagerState,
                        verticalAlignment = Alignment.Top
                    ) { page ->
                        Column(
                            modifier = Modifier
                                .padding(top = 30.dp, bottom = 8.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Card(
                                shape = CircleShape,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(150.dp),
                            ) {
                                Image(
                                    painterResource(
                                        id = patient_image_real[page]
                                    ),
                                    contentDescription = "painting"
                                )
                            }
                        }
                    }
                    HorizontalPagerIndicator(
                        pagerState = pagerState,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(30.dp),
                        activeColor = call_color
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
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
                            .padding(bottom = 20.dp)
                    ) {
                        MakeRectangular()
                        TransparentHintTextField(
                            text = nameState.text,
                            hint = nameState.hint,
                            modifier = Modifier
                                .padding(8.dp)
                                .focusRequester(focusRequester = focusRequester),

                            onValueChange = {
                                viewModel.onEvent(AddEditPatientEvent.EnteredName(it),)// doctorId.value.toInt())
                            },
                            onFocusChange = {
                                viewModel.onEvent(AddEditPatientEvent.ChangeNameFocus(it),) // doctorId.value.toInt())
                            },
                            isHintVisible = nameState.isHintVisible,
                            singleLine = true,
                            textStyle = MaterialTheme.typography.body1
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "성별",
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
                        Column(
                            modifier = Modifier.fillMaxSize().align(Center),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = CenterHorizontally
                        ) {
                            val selectedGender = remember { mutableStateOf("") }
                            Row {
                                Row (
                                    modifier = Modifier.weight(1f).align(CenterVertically),
                                ){
                                    RadioButton(
                                        onClick = {
                                            selectedGender.value = Gender.male
                                            viewModel.onEvent(
                                                AddEditPatientEvent.EnteredSex(
                                                    selectedGender.value
                                                ),
                                                //doctorId.value.toInt()
                                            )
                                        },
                                        selected = (sexState.bool_text || selectedGender.value == Gender.male),
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = call_color,
                                            unselectedColor = call_color
                                        )
                                    )
                                    Text( text = Gender.male, modifier = Modifier.align(CenterVertically))
                                }
                                Row (
                                    modifier = Modifier.weight(1f).align(CenterVertically)
                                ){
                                    RadioButton(
                                        onClick = {
                                            selectedGender.value = Gender.female
                                            viewModel.onEvent(
                                                AddEditPatientEvent.EnteredSex(
                                                    selectedGender.value
                                                ),
                                                //doctorId.value.toInt()
                                            )
                                        },
                                        selected = (!sexState.bool_text || selectedGender.value == "여자"),
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = call_color,
                                            unselectedColor = call_color
                                        )
                                    )
                                    Text( text = Gender.female, modifier = Modifier.align(CenterVertically))
                                }

                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "나이",
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
                        TransparentHintTextField(
                            text = ageState.text,
                            hint = ageState.hint,
                            modifier = Modifier
                                .padding(8.dp)
                                .focusRequester(focusRequester = focusRequester),
                            onValueChange = {
                                if (ageState.text.length < 3) viewModel.onEvent(AddEditPatientEvent.EnteredAge(it))
                            },
                            onFocusChange = {
                                viewModel.onEvent(AddEditPatientEvent.ChangeAgeFocus(it))
                            },
                            isHintVisible = ageState.isHintVisible,
                            singleLine = true,
                            textStyle = MaterialTheme.typography.body1,
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 5.dp)
                ) {
                    Text(
                        text = "혈액형",
                        modifier = Modifier
                            .padding(start = 25.dp, top = 32.dp),
                        color = recruit_city
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 10.dp)
                            .padding(end = 40.dp)
                            .padding(top = 20.dp)
                            .padding(bottom = 20.dp)
                    ) {

                        Column(
                            modifier = Modifier.fillMaxSize().align(Center),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = CenterHorizontally
                        ) {
                            Column() {
                                val selectBloodType = remember { mutableStateOf("") }
                                Row {
                                    Row(
                                        modifier = Modifier.weight(1f).align(CenterVertically)
                                    ) {
                                        RadioButton(
                                            onClick = {
                                                selectBloodType.value = BloodType.A
                                                viewModel.onEvent(
                                                    AddEditPatientEvent.EnteredBloodType(
                                                        selectBloodType.value
                                                    ), //doctorId.value.toInt()
                                                )
                                            },
                                            selected = (bloodTypeState.int_text == 0 || selectBloodType.value == BloodType.A),
                                            colors = RadioButtonDefaults.colors(
                                                selectedColor = call_color,
                                                unselectedColor = call_color
                                            )
                                        )
                                        Text(BloodType.A, modifier = Modifier.align(CenterVertically))
                                    }
                                    Row(
                                        modifier = Modifier.weight(1f).align(CenterVertically)
                                    ) {
                                        RadioButton(
                                            onClick = {
                                                selectBloodType.value = BloodType.B
                                                viewModel.onEvent(
                                                    AddEditPatientEvent.EnteredBloodType(
                                                        selectBloodType.value
                                                    ), //doctorId.value.toInt()
                                                )
                                            },
                                            selected = (bloodTypeState.int_text == 1 || selectBloodType.value == BloodType.B),
                                            colors = RadioButtonDefaults.colors(
                                                selectedColor = call_color,
                                                unselectedColor = call_color
                                            )
                                        )
                                        Text(BloodType.B,  modifier = Modifier.align(CenterVertically))
                                    }
                                }
                                Row {
                                    Row(
                                        modifier = Modifier.weight(1f).align(CenterVertically)
                                    ) {
                                        RadioButton(
                                            onClick = {
                                                selectBloodType.value = BloodType.O
                                                viewModel.onEvent(
                                                    AddEditPatientEvent.EnteredBloodType(
                                                        selectBloodType.value
                                                    ), //doctorId.value.toInt()
                                                )
                                            },
                                            selected = (bloodTypeState.int_text == 2 || selectBloodType.value == BloodType.O),
                                            colors = RadioButtonDefaults.colors(
                                                selectedColor = call_color,
                                                unselectedColor = call_color
                                            )
                                        )
                                        Text(BloodType.O, modifier = Modifier.align(CenterVertically))
                                    }
                                    Row(
                                        modifier = Modifier.weight(1f).align(CenterVertically)
                                    ) {
                                        RadioButton(
                                            onClick = {
                                                selectBloodType.value = BloodType.AB
                                                viewModel.onEvent(
                                                    AddEditPatientEvent.EnteredBloodType(
                                                        selectBloodType.value
                                                    ), //doctorId.value.toInt()
                                                )
                                            },
                                            selected = (bloodTypeState.int_text == 3 || selectBloodType.value == BloodType.AB),
                                            colors = RadioButtonDefaults.colors(
                                                selectedColor = call_color,
                                                unselectedColor = call_color
                                            )
                                        )
                                        Text(BloodType.AB, modifier = Modifier.align(CenterVertically))
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = CenterHorizontally
                            ) {
                                val selectRh = remember { mutableStateOf("") }
                                Row {
                                    Row(
                                        modifier = Modifier.weight(1f).align(CenterVertically)
                                    ) {
                                        RadioButton(
                                            onClick = {
                                                selectRh.value = BloodType.Rh_minus
                                                viewModel.onEvent(
                                                    AddEditPatientEvent.EnteredBloodRh(
                                                        selectRh.value
                                                    ), //doctorId.value.toInt()
                                                )
                                            },
                                            selected = (!bloodRhState.bool_text || selectRh.value == BloodType.Rh_minus),
                                            colors = RadioButtonDefaults.colors(
                                                selectedColor = call_color,
                                                unselectedColor = call_color
                                            )
                                        )
                                        Text(BloodType.Rh_minus, modifier = Modifier.align(CenterVertically))
                                    }
                                    Row(
                                        modifier = Modifier.weight(1f).align(CenterVertically)
                                    ) {
                                        RadioButton(
                                            onClick = {
                                                selectRh.value = BloodType.Rh_plus
                                                viewModel.onEvent(
                                                    AddEditPatientEvent.EnteredBloodRh(
                                                        selectRh.value
                                                    ), //doctorId.value.toInt()
                                                )
                                            },
                                            selected = (bloodRhState.bool_text || selectRh.value == BloodType.Rh_plus),
                                            colors = RadioButtonDefaults.colors(
                                                selectedColor = call_color,
                                                unselectedColor = call_color
                                            )
                                        )
                                        Text(BloodType.Rh_plus, modifier = Modifier.align(CenterVertically))
                                    }
                                }
                            }
                        }

                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "질병",
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
                        TransparentHintTextField(
                            text = diseasesState.text,
                            hint = diseasesState.hint,
                            modifier = Modifier
                                .padding(8.dp)
                                .focusRequester(focusRequester = focusRequester),

                            onValueChange = {
                                viewModel.onEvent(AddEditPatientEvent.EnteredDiseases(it),)// doctorId.value.toInt())
                            },
                            onFocusChange = {
                                viewModel.onEvent(AddEditPatientEvent.ChangeDiseasesFocus(it), )//doctorId.value.toInt())
                            },
                            isHintVisible = diseasesState.isHintVisible,
                            singleLine = true,
                            textStyle = MaterialTheme.typography.body1
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 5.dp)
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "기타사항",
                        modifier = Modifier
                            .padding(start = 12.dp, top = 25.dp),
                        color = recruit_city
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 30.dp)
                            .padding(end = 40.dp)
                            .padding(top = 20.dp)
                            .padding(bottom = 20.dp)
                            .height(150.dp)
                    ) {
                        val cornerRadius: Dp = 10.dp
                        val cutCornerSize: Dp = 0.dp
                        Canvas(
                            modifier = Modifier
                                .matchParentSize()
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
                        TransparentHintTextField(
                            text = extraState.text,
                            hint = extraState.hint,
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxSize()
                                .focusRequester(focusRequester = focusRequester),

                            onValueChange = {
                                viewModel.onEvent(AddEditPatientEvent.EnteredExtra(it), )//doctorId.value.toInt())
                            },
                            onFocusChange = {
                                viewModel.onEvent(AddEditPatientEvent.ChangeExtraFocus(it),)// doctorId.value.toInt())
                            },
                            isHintVisible = extraState.isHintVisible,

                            textStyle = MaterialTheme.typography.body2
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {
                        scope.launch {
                            if (nameState.text.isEmpty()) ExceptionMessage(nameState.text, "이름을 입력해주세요.", scaffoldState)
                            else if (ageState.text.isEmpty()) ExceptionMessage(ageState.text, "나이를 입력해주세요.", scaffoldState)
                            else if (diseasesState.text.isEmpty()) ExceptionMessage(diseasesState.text, "질병을 입력해주세요.", scaffoldState)
                            else if (extraState.text.isEmpty()) ExceptionMessage(extraState.text, "기타사항을 입력해주세요.", scaffoldState)
                            else {
                                viewModel.onEvent(AddEditPatientEvent.ChangeImage(pagerState.currentPage))
                                viewModel.onEvent(AddEditPatientEvent.SavePatient)
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = call_color),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(20.dp)
                        .width(220.dp)
                        .height(50.dp)
                        .clip(shape = RoundedCornerShape(26.dp, 26.dp, 26.dp, 26.dp))
                ) {
                    Text(
                        text = if(viewModel.patientId.value > 0){ "수정하기" } else{ "추가하기" },
                        color = Color.White,
                        style = MaterialTheme.typography.h4,
                        fontSize = 20.sp
                    )
                }
              //  Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}
object Gender{
    const val male = "남자"
    const val female = "여자"
}
object BloodType{
    const val A = "A형"
    const val B = "B형"
    const val AB = "AB형"
    const val O = "O형"
    const val Rh_minus = "Rh -"
    const val Rh_plus = "Rh +"
}
