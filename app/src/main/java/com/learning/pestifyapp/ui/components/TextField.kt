package com.learning.pestifyapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.pestifyapp.R
import com.learning.pestifyapp.ui.theme.PestifyAppTheme
import com.learning.pestifyapp.ui.theme.base80
import com.learning.pestifyapp.ui.theme.iconLight

@Composable
fun TextFieldValidation(
    value: String,
    placeholder: String,
    onChange: (String) -> Unit,
    isError: Boolean,
    icon: ImageVector,
    errorMessage: String,
    isPassword: Boolean = false,
    imeAction: ImeAction = ImeAction.Next,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier,
) {
    var showPassword by rememberSaveable { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier.fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier
                .height(65.dp)
                .fillMaxWidth()
                .border(
                    width = 2.dp,
                    color = if (isError) MaterialTheme.colorScheme.error else iconLight,
                    shape = RoundedCornerShape(20.dp)
                )
                .background(MaterialTheme.colorScheme.onPrimary, RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.CenterStart
        ) {
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.5f
                        ),
                    ),
                    modifier = Modifier.padding(start = 35.dp)
                )
            }

            BasicTextField(
                value = value,
                onValueChange = {
                    if (!it.contains("\n"))
                        onChange(it)
                },
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = base80),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                keyboardOptions = KeyboardOptions(
                    keyboardType = keyboardType,
                    imeAction = imeAction
                ),
                visualTransformation = if (isPassword && !showPassword) {
                    PasswordVisualTransformation()
                } else {
                    VisualTransformation.None
                },
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            innerTextField()
                        }
                        if (isPassword) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clickable(
                                        onClick = { showPassword = !showPassword },
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = if (showPassword) painterResource(id = R.drawable.eye_open) else painterResource(
                                        id = R.drawable.eye_close
                                    ),
                                    contentDescription = if (showPassword) "Show Password" else "Hide Password",
                                    tint = iconLight,
                                    modifier = Modifier
                                        .size(24.dp)
                                )
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)
            )
        }

        if (isError) {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                textAlign = TextAlign.Start
            )
        } else {
            Spacer(modifier = Modifier.height(16.dp)) // Menambahkan spacer tetap saat tidak ada error
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EmailPrev() {
    PestifyAppTheme {
        Column {
            TextFieldValidation(
                value = "emmail",
                placeholder = "Email",
                onChange = {},
                isError = false,
                icon = Icons.Filled.Email,
                errorMessage = "Email is required"
            )
            TextFieldValidation(
                value = "password",
                placeholder = "Password",
                onChange = {},
                isError = false,
                icon = Icons.Filled.Email, // Ganti dengan ikon yang sesuai
                errorMessage = "Password is required",
                isPassword = true
            )
            TextFieldValidation(
                value = "confirm password",
                placeholder = "Confirm Password",
                onChange = {},
                isError = false,
                icon = Icons.Filled.Email, // Ganti dengan ikon yang sesuai
                errorMessage = "Confirm Password is required",
                isPassword = true
            )
        }
    }
}
