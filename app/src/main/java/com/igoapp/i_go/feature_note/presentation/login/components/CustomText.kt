package com.igoapp.i_go.feature_note.presentation.login.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.igoapp.i_go.ui.theme.recruit_city
import com.igoapp.i_go.ui.theme.text_gray

@Composable
fun CustomText(
    text: String = "",
    value: String = "",
    onValueChange: (String) -> Unit = {},
    isPassword: Boolean = false,
    alertMessage: String = ""
){
    Column{
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp)
            .padding(end = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = text,
            modifier = Modifier
                .padding(start = 15.dp),
            color = recruit_city
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 30.dp)
                .padding(end = 15.dp)
                .padding(top = 10.dp)

        ) {
            LoginRectangular()
            if (isPassword) {
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .align(Alignment.TopStart),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = PasswordVisualTransformation()
                )
            } else {
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.TopStart),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    textStyle = MaterialTheme.typography.h5
                )
            }

        }
        }
        Text(
            text = alertMessage,
            color = Color.White,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.End).padding(0.dp, 5.dp, 25.dp, 5.dp)
        )
    }
}