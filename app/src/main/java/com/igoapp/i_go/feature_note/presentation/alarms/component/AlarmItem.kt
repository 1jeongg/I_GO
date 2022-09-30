package com.igoapp.i_go.feature_note.presentation.alarms.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.igoapp.i_go.feature_note.data.remote.responseDTO.PatientByIdDTO
import com.igoapp.i_go.ui.theme.call_color
import com.igoapp.i_go.ui.theme.card_color
import com.igoapp.i_go.ui.theme.text_gray

@Composable
fun AlarmItem(
    modifier: Modifier = Modifier,
    image: Int = 1,
    name: String = "맹구",
    onClick: () -> Unit = {},
    onCallClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(card_color),
    ) {

        Row(
            modifier = Modifier.clickable(onClick = onClick).padding(20.dp).fillMaxSize()
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.size(60.dp).align(CenterVertically),
            ) {
                Image(
                    painterResource(
                        id = PatientByIdDTO.patient_image_real[image]
                    ),
                    contentDescription = "painting"
                )
            }
            Column(
                modifier = Modifier.padding(start = 20.dp).fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "환자 호출",
                    style = MaterialTheme.typography.h4,
                    color = MaterialTheme.colors.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(11.dp))
                Text(
                    text = name + " 환자의 호출",
                    style = MaterialTheme.typography.h5,
                    color = text_gray
                )
            }
        }
        IconButton(
            onClick = onDeleteClick,
            modifier = Modifier.padding(top = 10.dp, end = 60.dp).size(20.dp).align(TopEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete note",
                tint = Color.White
            )
        }
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .align(CenterEnd)
                .width(50.dp)
                .clip(RoundedCornerShape(0.dp, 30.dp))
                .background(call_color)
                .clickable(onClick = onCallClick),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "호\n출",
                color = White,
                style = MaterialTheme.typography.h3
            )
        }
    }
}

