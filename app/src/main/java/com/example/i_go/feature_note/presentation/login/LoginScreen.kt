package com.example.i_go.feature_note.presentation.login

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.i_go.R
import com.example.i_go.feature_note.domain.util.log
import com.example.i_go.feature_note.presentation.add_edit_patient.AddEditPatientEvent
import com.example.i_go.feature_note.presentation.add_edit_patient.Gender
import com.example.i_go.feature_note.presentation.login.components.CustomText
import com.example.i_go.feature_note.presentation.util.ExceptionMessage
import com.example.i_go.feature_note.presentation.util.Screen
import com.example.i_go.feature_note.presentation.util.WrongPasswordExcept
import com.example.i_go.feature_note.presentation.util.addFocusCleaner
import com.example.i_go.ui.theme.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel(),
    viewModel2: SignInViewModel = hiltViewModel()

) {
    val focusManager = LocalFocusManager.current
    var scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    var isLogIn by rememberSaveable { mutableStateOf(true) }
    var isMaxSize by rememberSaveable { mutableStateOf(false) }
    val state = rememberCollapsingToolbarScaffoldState()

    var isAgree by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is LoginViewModel.UiEvent.Error -> {
                    scaffoldState.snackbarHostState.showSnackbar("로그인 실패")
                    "LOGIN ERROR!!".log()
                }
                is LoginViewModel.UiEvent.Login -> {
                    "LOGIN SUCCESS!!".log()
                    //    scaffoldState.snackbarHostState.showSnackbar("로그인 성공")
                    navController.navigate(Screen.DoctorScreen.route)
                }
            }
        }
        viewModel2.eventFlow.collectLatest { event ->
            when (event) {
                is SignInViewModel.UiEvent.Error -> {
                    scaffoldState.snackbarHostState.showSnackbar("회원가입 실패")
                    "SignIn ERROR!!".log()
                }
                is SignInViewModel.UiEvent.SignIn -> {
                    "SignIn Success!!".log()
                    //     scaffoldState.snackbarHostState.showSnackbar("회원가입 성공")
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            Box(
                modifier = Modifier
                    .background(call_color)
                    .fillMaxWidth()
                    .clickable { isMaxSize = !isMaxSize }
            ) {
                Image(
                    painterResource(id = R.drawable.image_splash),
                    contentDescription = "image_splash",
                    modifier = Modifier
                        .padding(10.dp)
                        .padding(bottom = if (isLogIn) 100.dp else 0.dp)
                        .size(50.dp)
                )
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(call_color),
            contentAlignment = BottomCenter
        ) {
            CollapsingToolbarScaffold(
                modifier = Modifier.fillMaxSize(),
                state = state,
                scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
                toolbarModifier = if (isMaxSize) Modifier
                    .animateContentSize()
                    .size(0.dp) else Modifier.animateContentSize(),
                toolbar = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .pin()
                    )
                    MiddleImage(
                        Modifier
                            .padding(top = if (isLogIn) 0.dp else 100.dp)
                            .padding(bottom = 100.dp)
                            .fillMaxWidth()
                            .align(Center)
                            .parallax(0.5f)
                            .graphicsLayer {
                                alpha = state.toolbarState.progress
                            }
                            .clickable { isMaxSize = !isMaxSize }
                    )
                }
            ) {
                Column(
                    horizontalAlignment = CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp))
                        .verticalScroll(rememberScrollState())
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background(card_color)
                        .padding(10.dp)
                        .addFocusCleaner(focusManager)
                        .animateContentSize()
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Spacer(modifier = Modifier.padding(if (isLogIn) 30.dp else 10.dp))

                        if (isLogIn) {
                            Text(
                                text = "로그인",
                                style = MaterialTheme.typography.h1,
                                modifier = Modifier
                                    .align(CenterHorizontally)
                                    .clickable { isMaxSize = !isMaxSize }
                            )
                        } else {
                            Text(
                                text = "회원가입",
                                style = MaterialTheme.typography.h1,
                                modifier = Modifier
                                    .align(CenterHorizontally)
                                    .clickable { isMaxSize = !isMaxSize }
                            )

                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        Divider(
                            modifier = Modifier
                                .align(CenterHorizontally)
                                .width(if (isLogIn) 70.dp else 90.dp)
                                .height(3.dp)
                                .background(call_color)
                        )

                    }
                    Spacer(modifier = Modifier.padding(40.dp))

                    Column(
                        horizontalAlignment = CenterHorizontally
                    ) {
                        if (isLogIn) {
                            CustomText(
                                text = "아이디   ",
                                value = viewModel.emailPw.value.username,
                                onValueChange = {
                                    viewModel.onEvent(
                                        LoginEvent.EnteredName(it),
                                        scaffoldState
                                    )
                                })
                            CustomText(
                                text = "패스워드",
                                value = viewModel.emailPw.value.password,
                                onValueChange = {
                                    viewModel.onEvent(
                                        LoginEvent.EnteredPassword(it),
                                        scaffoldState
                                    )
                                },
                                isPassword = true
                            )
                        } else {
                            CustomText(
                                text = "아이디   ",
                                value = viewModel2.signIn.value.username,
                                onValueChange = {
                                    viewModel2.onEvent(
                                        SignInEvent.EnteredUsername(it),
                                        scaffoldState
                                    )
                                },
                                alertMessage = "이전에 등록된 아이디 불가"
                            )
                            CustomText(
                                text = "이메일   ",
                                value = viewModel2.signIn.value.email,
                                onValueChange = {
                                    viewModel2.onEvent(
                                        SignInEvent.EnteredEmail(it),
                                        scaffoldState
                                    )
                                },
                                alertMessage = "이전에 등록된 이메일 불가"
                            )
                            CustomText(
                                text = "패스워드",
                                value = viewModel2.signIn.value.password,
                                onValueChange = {
                                    viewModel2.onEvent(
                                        SignInEvent.EnteredPassword(it),
                                        scaffoldState
                                    )
                                },
                                isPassword = true,
                                alertMessage = "단순하지 않으며 8자 이상, 영어가 포함"
                            )
                            CustomText(
                                text = "패스워드",
                                value = viewModel2.signIn.value.password2,
                                onValueChange = {
                                    viewModel2.onEvent(
                                        SignInEvent.EnteredPassword2(it),
                                        scaffoldState
                                    )
                                },
                                isPassword = true,
                                alertMessage = "패스워드1과 동일해야 함"
                            )
                            Row (
                                modifier = Modifier.align(CenterHorizontally),
                            ){
                                RadioButton(
                                    onClick = { isAgree = !isAgree },
                                    selected = isAgree,
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = call_color,
                                        unselectedColor = call_color
                                    )
                                )
                                Text(
                                    text = "개인 정보 수집 처리 동의",
                                    color = text_gray,
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .clickable { navController.navigate(Screen.AgreeScreen.route) }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.padding(20.dp))
                        Button(
                            onClick = {
                                scope.launch {
                                    if (!isLogIn) {
                                        if (viewModel2.signIn.value.username.isEmpty()) ExceptionMessage(
                                            viewModel2.signIn.value.username,
                                            "아이디를 입력해주세요.",
                                            scaffoldState
                                        )
                                        else if (viewModel2.signIn.value.email.isEmpty()) ExceptionMessage(
                                            viewModel2.signIn.value.email,
                                            "이메일을 입력해주세요.",
                                            scaffoldState
                                        )
                                        else if (viewModel2.signIn.value.password.isEmpty()) ExceptionMessage(
                                            viewModel2.signIn.value.password,
                                            "패스워드를 입력해주세요.",
                                            scaffoldState
                                        )
                                        else if (viewModel2.signIn.value.password2.isEmpty()) ExceptionMessage(
                                            viewModel2.signIn.value.password2,
                                            "패스워드2를 입력해주세요.",
                                            scaffoldState
                                        )
                                        else if (viewModel2.signIn.value.password != viewModel2.signIn.value.password2) {
                                            WrongPasswordExcept(
                                                viewModel2.signIn.value.password,
                                                viewModel2.signIn.value.password2,
                                                scaffoldState
                                            )
                                        }
                                        else if (!isAgree){
                                            ExceptionMessage("", "개인정보 동의를 완료해주세요", scaffoldState)
                                        }
                                        else viewModel2.onEvent(SignInEvent.SignIn, scaffoldState)
                                    } else {
                                        if (viewModel.emailPw.value.username.isEmpty())
                                            ExceptionMessage(viewModel.emailPw.value.username, "아이디를 입력해주세요.", scaffoldState)
                                        else if (viewModel.emailPw.value.password.isEmpty())
                                            ExceptionMessage(viewModel.emailPw.value.password, "패스워드를 입력해주세요.", scaffoldState)
                                        else viewModel.onEvent(LoginEvent.Login, scaffoldState)
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = call_color),
                            modifier = Modifier
                                .align(CenterHorizontally)
                                .width(220.dp)
                                .height(50.dp)
                                .clip(shape = RoundedCornerShape(26.dp))
                        ) {
                            Text(
                                text = if (isLogIn) "로그인하기" else "회원가입하기",
                                color = Color.White,
                                style = MaterialTheme.typography.h4,
                                fontSize = 20.sp
                            )
                        }
                        Text(
                            text = if (isLogIn) "회원가입 하러 가기" else "로그인 하러 가기",
                            style = MaterialTheme.typography.body1,
                            color = primary,
                            modifier = Modifier
                                .align(CenterHorizontally)
                                .padding(30.dp)
                                .clickable {
                                    isLogIn = !isLogIn
                                }
                        )
                    }
                }
            }
        }
    }
}
