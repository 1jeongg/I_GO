package com.example.i_go.feature_note.presentation.add_edit_patient

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.i_go.feature_note.domain.model.Patient.Companion.patientImages
import com.example.i_go.feature_note.domain.model.Patient.Companion.patient_image_real
import com.example.i_go.feature_note.presentation.add_edit_patient.components.TransparentHintTextField
import com.example.i_go.feature_note.presentation.doctors.MakeRectangular
import com.example.i_go.feature_note.presentation.util.Screen
import com.example.i_go.ui.theme.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.collectLatest

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
        initialPage = if (patientImage >= 0) patientImage else patientImages.random()
    )


    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddEditPatientViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is AddEditPatientViewModel.UiEvent.SaveNote -> {
                    navController.navigateUp()
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    text = "?????? ??????",
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
                        text = "??????",
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
                                viewModel.onEvent(AddEditPatientEvent.EnteredName(it))
                            },
                            onFocusChange = {
                                viewModel.onEvent(AddEditPatientEvent.ChangeNameFocus(it))
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
                        text = "??????",
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
                                RadioButton(
                                    selected = (sexState.text == Gender.male || selectedGender.value == Gender.male),
                                    onClick = {
                                        selectedGender.value = Gender.male
                                        viewModel.onEvent(
                                            AddEditPatientEvent.EnteredSex(
                                                selectedGender.value
                                            )
                                        )
                                    },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = call_color,
                                        unselectedColor = call_color
                                    )
                                )
                                Spacer(modifier = Modifier.size(16.dp))
                                Text(Gender.male)
                                Spacer(modifier = Modifier.size(16.dp))
                                RadioButton(
                                    selected = (sexState.text == Gender.female || selectedGender.value == Gender.female),
                                    onClick = {
                                        selectedGender.value = Gender.female
                                        viewModel.onEvent(
                                            AddEditPatientEvent.EnteredSex(
                                                selectedGender.value
                                            )
                                        )
                                    },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = call_color,
                                        unselectedColor = call_color
                                    )
                                )
                                Spacer(modifier = Modifier.size(16.dp))
                                Text(Gender.female)
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
                        text = "??????",
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
                                viewModel.onEvent(AddEditPatientEvent.EnteredAge(it))
                            },
                            onFocusChange = {
                                viewModel.onEvent(AddEditPatientEvent.ChangeAgeFocus(it))
                            },
                            isHintVisible = ageState.isHintVisible,
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
                    Text(
                        text = "?????????",
                        modifier = Modifier
                            .padding(start = 25.dp, top = 22.dp),
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
                                    RadioButton(
                                        selected = (bloodTypeState.text == BloodType.A || selectBloodType.value == BloodType.A),
                                        onClick = {
                                            selectBloodType.value = BloodType.A
                                            viewModel.onEvent(
                                                AddEditPatientEvent.EnteredBloodType(
                                                    selectBloodType.value
                                                )
                                            )
                                        },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = call_color,
                                            unselectedColor = call_color
                                        )
                                    )
                                    Spacer(modifier = Modifier.size(16.dp))
                                    Text(BloodType.A)
                                    Spacer(modifier = Modifier.size(30.dp))
                                    RadioButton(
                                        selected = (bloodTypeState.text == BloodType.B || selectBloodType.value == BloodType.B),
                                        onClick = {
                                            selectBloodType.value = BloodType.B
                                            viewModel.onEvent(
                                                AddEditPatientEvent.EnteredBloodType(
                                                    selectBloodType.value
                                                )
                                            )
                                        },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = call_color,
                                            unselectedColor = call_color
                                        )
                                    )
                                    Spacer(modifier = Modifier.size(25.dp))
                                    Text(BloodType.B)
                                }
                                Spacer(modifier = Modifier.height(5.dp))
                                Row {
                                    RadioButton(
                                        selected = (bloodTypeState.text == BloodType.O || selectBloodType.value == BloodType.O),
                                        onClick = {
                                            selectBloodType.value = BloodType.O
                                            viewModel.onEvent(
                                                AddEditPatientEvent.EnteredBloodType(
                                                    selectBloodType.value
                                                )
                                            )
                                        },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = call_color,
                                            unselectedColor = call_color
                                        )
                                    )
                                    Spacer(modifier = Modifier.size(16.dp))
                                    Text(BloodType.O)
                                    Spacer(modifier = Modifier.size(27.dp))
                                    RadioButton(
                                        selected = (bloodTypeState.text == BloodType.AB || selectBloodType.value == BloodType.AB),
                                        onClick = {
                                            selectBloodType.value = BloodType.AB
                                            viewModel.onEvent(
                                                AddEditPatientEvent.EnteredBloodType(
                                                    selectBloodType.value
                                                )
                                            )
                                        },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = call_color,
                                            unselectedColor = call_color
                                        )
                                    )
                                    Spacer(modifier = Modifier.size(16.dp))
                                    Text(BloodType.AB)
                                }
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = CenterHorizontally
                            ) {
                                val selectRh = remember { mutableStateOf("") }
                                Row {
                                    RadioButton(
                                        selected = (bloodRhState.text == BloodType.Rh_minus || selectRh.value == BloodType.Rh_minus),
                                        onClick = {
                                            selectRh.value = BloodType.Rh_minus
                                            viewModel.onEvent(
                                                AddEditPatientEvent.EnteredBloodRh(
                                                    selectRh.value
                                                )
                                            )
                                        },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = call_color,
                                            unselectedColor = call_color
                                        )
                                    )
                                    Spacer(modifier = Modifier.size(16.dp))
                                    Text(BloodType.Rh_minus)
                                    Spacer(modifier = Modifier.size(23.dp))
                                    RadioButton(
                                        selected = (bloodRhState.text == BloodType.Rh_plus || selectRh.value == BloodType.Rh_plus),
                                        onClick = {
                                            selectRh.value = BloodType.Rh_plus
                                            viewModel.onEvent(
                                                AddEditPatientEvent.EnteredBloodRh(
                                                    selectRh.value
                                                )
                                            )
                                        },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = call_color,
                                            unselectedColor = call_color
                                        )
                                    )
                                    Spacer(modifier = Modifier.size(16.dp))
                                    Text(BloodType.Rh_plus)
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
                        text = "??????",
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
                                viewModel.onEvent(AddEditPatientEvent.EnteredDiseases(it))
                            },
                            onFocusChange = {
                                viewModel.onEvent(AddEditPatientEvent.ChangeDiseasesFocus(it))
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
                        text = "????????????",
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
                                viewModel.onEvent(AddEditPatientEvent.EnteredExtra(it))
                            },
                            onFocusChange = {
                                viewModel.onEvent(AddEditPatientEvent.ChangeExtraFocus(it))
                            },
                            isHintVisible = extraState.isHintVisible,

                            textStyle = MaterialTheme.typography.body2
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {
                        // TODO: ????????? Patient??? ?????? ?????? ?????????

                        if(nameState.text.isNotEmpty()){
                            // ????????????
                        }else{
                            // ????????????
                        }
                        viewModel.onEvent(AddEditPatientEvent.ChangeImage(pagerState.currentPage))
                        viewModel.onEvent(AddEditPatientEvent.SavePatient)
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
                        text = if(nameState.text.isNotEmpty()){ "????????????" } else{ "????????????" },
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

fun Modifier.addFocusCleaner(focusManager: FocusManager, doOnClear: () -> Unit = {}): Modifier {
    return this.pointerInput(Unit) {
        detectTapGestures(onTap = {
            doOnClear()
            focusManager.clearFocus()
        })
    }
}
object Gender{
    const val male = "??????"
    const val female = "??????"
}
object BloodType{
    const val A = "A???"
    const val B = "B???"
    const val AB = "AB???"
    const val O = "O???"
    const val Rh_minus = "Rh -"
    const val Rh_plus = "Rh +"
}