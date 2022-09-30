package com.igoapp.i_go.feature_note.presentation

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.igoapp.i_go.R
import com.igoapp.i_go.feature_note.presentation.login.LoginViewModel
import com.igoapp.i_go.feature_note.presentation.util.Screen
import com.igoapp.i_go.ui.theme.call_color
import kotlinx.coroutines.delay

@Composable
fun SplashScreen (navController: NavController) {
    val scale = remember {
        Animatable (0f)
    }
    val loginViewModel = hiltViewModel<LoginViewModel>()

    LaunchedEffect(key1 = true) {
        val nextScreen = when (loginViewModel.isLoggedIn()) {
            true -> Screen.PatientsScreen
            false -> Screen.LoginScreen
        }
        scale.animateTo (
            targetValue = 0.3f,
            animationSpec = tween (
                durationMillis = 500,
                easing = {
                    OvershootInterpolator(1f).getInterpolation(it)
                }
            )
        )
        delay(500L)
        navController.popBackStack()
        navController.navigate(nextScreen.route)
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
            .background(call_color)
    ) {
        Image (
            painter = painterResource(id = R.drawable.image_splash),
            contentDescription = "LOGO",
            modifier = Modifier
                .scale(scale.value)
        )
        Text (
            text = " from\n 기컴프",
            color = Color.White,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(10.dp)
                .padding(bottom = 20.dp)
        )
    }
}