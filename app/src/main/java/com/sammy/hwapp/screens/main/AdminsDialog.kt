package com.sammy.hwapp.screens.main

import LogIo
import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.sammy.hwapp.SharedPref
import kotlinx.coroutines.launch

@SuppressLint("MutableCollectionMutableState")
@Composable
fun AdminFragment(onBack: () -> Unit) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val sharedPref = SharedPref(context, "UserData")
    var admins by remember {
        mutableStateOf(sharedPref.getArray("admins").toMutableList())
    }
    var showDialogAdd by remember { mutableStateOf(false) }
    if (showDialogAdd) {
        AdminAddDialog(
            onDismissRequest = { showDialogAdd = false },
            onAddClick = { email ->
                scope.launch {
                    LogIo.addAdmin(sharedPref.get("class"), email)
                }
                admins.add(email)
                sharedPref.update(mapOf("admins" to admins.toString()))
                admins = admins.toList().toMutableList()
                Toast.makeText(
                    context,
                    "Админ был добавлен!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }

    BackHandler {
        onBack()
    }
    Card(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        if (admins.isEmpty()) Text("Администраторов ещё нет")
        admins.forEachIndexed { index, admin ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = admin,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            scope.launch {
                                LogIo.delAdmin(sharedPref.get("class"), admin)
                            }
                            admins = admins.filter { it != admin }.toMutableList()
                            sharedPref.update(mapOf("admins" to admins.toString()))
                            Toast.makeText(
                                context,
                                "Админ был удалён!",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Удалить")
                    }
                }
            }
        }
        Button(
            onClick = { showDialogAdd = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Добавить админа")
        }
    }
}

@Composable
fun AdminAddDialog(
    onDismissRequest: () -> Unit,
    onAddClick: (String) -> Unit
) {
    var email by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = "Введите email администратора",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { onDismissRequest() }) {
                        Text("Отмена")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        onAddClick(email)
                        onDismissRequest()
                    }) {
                        Text("Добавить")
                    }
                }
            }
        }
    }
}
