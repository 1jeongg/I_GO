package com.igoapp.i_go.feature_note.presentation.alarms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.igoapp.i_go.feature_note.domain.util.log
import com.igoapp.i_go.feature_note.presentation.alarms.component.AlarmItem
import com.igoapp.i_go.feature_note.presentation.util.Screen
import com.igoapp.i_go.ui.theme.primary

@Composable
fun AlarmScreen(
    navController: NavHostController,
    viewModel: AlarmViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
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
                text = "알림 모아보기",
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.Bold,
                color = primary,
                textAlign = TextAlign.Center
            )
        }
        Divider(color = primary, modifier = Modifier.shadow(8.dp), thickness = 2.dp)
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(state.notifications) { notification ->
                "알림창에서 ${notification.patient_id} ${notification.image}".log()
                AlarmItem(
                    image = notification.image - 1,
                    name = notification.name,
                    onClick = {
                        navController.navigate(Screen.AddEditPatientScreen.route +
                                "?patientId=${notification.patient_id}" +
                                "&patientImage=${notification.image}")
                    },
                    onCallClick = {
                        "patient id is ${notification.patient_id}".log()
                        viewModel.callPatients(notification.patient_id)
                    },
                    onDeleteClick = {
                        viewModel.deleteNotifications(notification)
                    }
                )
            }
        }
    }
}
