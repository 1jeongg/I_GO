package com.igoapp.i_go.feature_note.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.igoapp.i_go.R
import com.igoapp.i_go.ui.theme.button_color


@Composable
fun MiddleImage(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ){
        Column(
            modifier = Modifier
                .align(Alignment.Center)
        ){
            Image(
                painterResource(id = R.drawable.hospital),
                contentDescription = "hospital",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 20.dp)
            )
            Row{
                Text(
                    text = "의료진, 환자간\n위치 파악 시스템",
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier
                        .padding(15.dp)
                        .align(Alignment.CenterVertically),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "아이고",
                    style = MaterialTheme.typography.h2,
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    color = button_color,
                    textAlign = TextAlign.Center,
                )
            }


        }
    }
}