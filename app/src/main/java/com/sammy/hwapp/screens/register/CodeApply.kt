package com.sammy.hwapp.screens.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.montanainc.simpleloginscreen.components.MyTextFieldComponent
import com.montanainc.simpleloginscreen.ui.theme.AccentColor

@Composable
fun CodeApplyDialog(
    code: String,
    isError: Boolean,
    onDismiss: () -> Unit,
    onCodeChange: (String) -> Unit,
    onConfirm: () -> Unit,
    isCodeConfirmed: Boolean = false
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = !isCodeConfirmed,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isCodeConfirmed) Color.Gray else AccentColor,
                    contentColor = Color.White,
                    disabledContainerColor = Color.LightGray,
                    disabledContentColor = Color.DarkGray
                )
            ) {
                Text("ОК")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        },
        title = {
            Text("Подтверждение почты")
        },
        text = {
            Column {
                Text("Код отправлен на вашу почту.\nПроверьте папку \"Спам\".")
                Spacer(modifier = Modifier.height(12.dp))
                MyTextFieldComponent(
                    labelValue = "Код подтверждения",
                    icon = Icons.Default.Lock,
                    textValue = code,
                    onValueChange = onCodeChange,
                    isError = isError
                )
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}
