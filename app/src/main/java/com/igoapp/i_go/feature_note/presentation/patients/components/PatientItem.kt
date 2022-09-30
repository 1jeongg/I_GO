package com.igoapp.i_go.feature_note.presentation.patients.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.igoapp.i_go.feature_note.data.remote.responseDTO.PatientByIdDTO
import com.igoapp.i_go.feature_note.data.remote.responseDTO.PatientDTO
import com.igoapp.i_go.ui.theme.*

@Composable
fun PatientItem(
    name: String = "",
    gender: Boolean = true,
    age: Int = 0,
    blood_type: Int = 0,
    disease: String = "",
    image: Int = 0,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 26.dp,
    cutCornerSize: Dp = 0.dp,
    onDeleteClick: () -> Unit = {},
    onCallClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .padding(start = 15.dp)
            .padding(end = 15.dp)
            .padding(top = 15.dp)
            .clip(RoundedCornerShape(26.dp))
            .shadow(elevation = 5.dp, shape = RoundedCornerShape(26.dp))
            .clickable(onClick = onClick)

    ) {

        Canvas(modifier = Modifier
            .matchParentSize()
            .shadow(elevation = 5.dp, shape = RoundedCornerShape(26.dp))
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


        Row(
            modifier = Modifier
                .background(card_color)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box( modifier = Modifier
                            .size(70.dp)
                            .shadow(5.dp, CircleShape)
                            .clip(CircleShape)
                            .background(White)
            ) {
                Image(
                    painterResource(id = PatientByIdDTO.patient_image_real[image-1]),
                    contentDescription = "painting",
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .padding(top = 2.dp)
                        .size(60.dp),
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Fit
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ){
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = name,
                    style = MaterialTheme.typography.h4,
                    color = MaterialTheme.colors.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(11.dp))
                Row (
                    modifier = Modifier
                        .padding(start = 8.dp)
                ) {
                    Text(
                        text = if (gender) "남자" else "여자",
                        style = MaterialTheme.typography.body1,
                        color = text_gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = " / ",
                        style = MaterialTheme.typography.body1,
                        color = text_gray,
                        maxLines = 1
                    )
                    Text(
                        text = age.toString() + '세',
                        style = MaterialTheme.typography.body1,
                        color = text_gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = " / ",
                        style = MaterialTheme.typography.body1,
                        color = text_gray,
                        maxLines = 1
                    )
                    Text(
                        text = ConvertBlood(blood_type),
                        style = MaterialTheme.typography.body1,
                        color = text_gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = " / ",
                        style = MaterialTheme.typography.body1,
                        color = text_gray,
                        maxLines = 1
                    )
                    Text(
                        text = disease,
                        style = MaterialTheme.typography.body1,
                        color = text_gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }

        IconButton(
            onClick = onDeleteClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(50.dp)
                .padding(15.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete note",
                tint = Color.White
            )
        }
        TextButton (
            onClick = onCallClick,
            colors =  ButtonDefaults.buttonColors(backgroundColor = call_color),
            modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .background(call_color)
                    .fillMaxWidth()
                    .height(40.dp)
                    .clip(shape = RoundedCornerShape(0.dp, 0.dp, 26.dp, 26.dp)),
            contentPadding = PaddingValues(0.dp)
        ) {
           Text(
               text = "호출하기",
               color = White,
               style = MaterialTheme.typography.h4
           )
        }

    }
}
fun ConvertBlood(
    blood: Int
): String {
    var message = ""
    if (blood == 0) message = "A형"
    if (blood == 1) message = "B형"
    if (blood == 2) message = "O형"
    if (blood == 3) message = "AB형"

    return message
}