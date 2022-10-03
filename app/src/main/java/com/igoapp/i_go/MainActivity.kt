package com.igoapp.i_go

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.igoapp.i_go.feature_note.presentation.SplashScreen
import com.igoapp.i_go.feature_note.presentation.add_edit_patient.AddEditPatientScreen
import com.igoapp.i_go.feature_note.presentation.alarms.AlarmScreen
import com.igoapp.i_go.feature_note.presentation.doctors.DoctorScreen
import com.igoapp.i_go.feature_note.presentation.login.AgreeScreen
import com.igoapp.i_go.feature_note.presentation.login.LoginScreen
import com.igoapp.i_go.feature_note.presentation.patients.PatientsScreen
import com.igoapp.i_go.feature_note.presentation.util.Screen
import com.igoapp.i_go.ui.theme.I_GOTheme
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Firebase.messaging.isAutoInitEnabled = true

       setContent {
            I_GOTheme {
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.SplashScreen.route
                    ) {
                        composable(route = Screen.SplashScreen.route) {
                            SplashScreen(navController = navController)
                        }
                        composable(route = Screen.LoginScreen.route) {
                            LoginScreen(navController = navController)
                        }
                        composable(route = Screen.PatientsScreen.route) {
                            PatientsScreen(navController = navController)
                        }
                        composable(route = Screen.DoctorScreen.route) {
                            DoctorScreen(navController = navController)
                        }
                        composable(route = Screen.AlarmScreen.route){
                            AlarmScreen(navController = navController)
                        }
                        composable(route=  Screen.AgreeScreen.route){
                            AgreeScreen(navController = navController)
                        }
                        composable(
                            route = Screen.AddEditPatientScreen.route +
                                    "?patientId={patientId}&patientImage={patientImage}",
                            arguments = listOf(
                                navArgument(
                                    name = "patientId"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                },
                                navArgument(
                                    name = "patientImage"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                }
                            )
                        ) {
                            val image = it.arguments?.getInt("patientImage") ?: -1
                            AddEditPatientScreen(
                                navController = navController,
                                patientImage = image
                            )
                        }
                    }
                }
            }
        }
    }
}


