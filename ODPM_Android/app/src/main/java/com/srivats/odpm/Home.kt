package com.srivats.odpm

import android.app.Application
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.lifecycle.viewmodel.compose.viewModel
import com.srivats.odpm.DB.PwdItem
import com.srivats.odpm.DB.PwdViewModel
import com.srivats.odpm.DB.PwdViewModelFactory

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
    ExperimentalMaterialApi::class)
@Composable
fun HomeUi(){
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    val mPwdViewModel: PwdViewModel = viewModel(
        factory = PwdViewModelFactory(context.applicationContext as Application)
    )

    val items = mPwdViewModel.readAllData.observeAsState(listOf()).value

    BottomSheetScaffold(
        backgroundColor = MaterialTheme.colorScheme.surface,
        topBar = {
            SmallTopAppBar(
                title = { Text("ODPM") },
            )
        },
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = { BottomSheetContent(scope, bottomSheetScaffoldState) },
        scaffoldState = bottomSheetScaffoldState,
        content = {
            PwdDisplay(list = items, mPwdViewModel = mPwdViewModel)
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PwdDisplay(
    list: List<PwdItem>,
    mPwdViewModel: PwdViewModel
) {
    val context = LocalContext.current
    val siteName = remember { mutableStateOf("") }
    val sitePwd = remember { mutableStateOf("") }
    val openPwdDialog = remember { mutableStateOf(false) }

    LazyColumn() {
        items(list) { pwd ->
            ListItem(
                modifier = Modifier.clickable{
                    openPwdDialog.value = true
                    siteName.value = pwd.siteName
                    sitePwd.value = pwd.sitePwd
                                             },
                text = { Text(text = pwd.siteName, color = MaterialTheme.colorScheme.onSurface) },
                trailing = {
                    IconButton(onClick = {
                        mPwdViewModel.deleteSitePwd(pwd)
                    }) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = null,
                            tint = Color.Red
                        )
                    }
                },
            )
            Divider(
                modifier = Modifier.padding(start = 15.dp, end = 15.dp)
            )
        }
    }
    
    if(openPwdDialog.value){
        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
                openPwdDialog.value = false
            },
            title = {
                Text(text = siteName.value)
            },
            text = {
                Text(text = "Password: ${sitePwd.value}")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openPwdDialog.value = false
                        val sendIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, "Site: ${siteName.value}, " +
                                    "Password: ${sitePwd.value}")
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, null)
                        context.startActivity(shareIntent)
                    }
                ) {
                    Text("Share")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openPwdDialog.value = false
                    }
                ) {
                    Text("Close")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetContent(scope: CoroutineScope, state: BottomSheetScaffoldState) {
    var siteName by rememberSaveable{ mutableStateOf("") }
    var sitePwd by rememberSaveable{ mutableStateOf("") }
    val isError = remember { mutableStateOf(false) }

    val context = LocalContext.current
    val mPwdViewModel: PwdViewModel = viewModel(
        factory = PwdViewModelFactory(context.applicationContext as Application)
    )

    Box(
        Modifier
            .height(500.dp)
            .background(MaterialTheme.colorScheme.onSecondary)
    ) {
        LazyColumn{
            item {
                Row(
                modifier = Modifier
                    .height(55.dp)
                    .padding(start = 15.dp)
                    .clickable(onClick = {
                        scope.launch {
                            if (state.bottomSheetState.isCollapsed) {
                                state.bottomSheetState.expand()
                            } else {
                                state.bottomSheetState.collapse()
                            }
                        }
                    }),
                verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Add Password",
                        Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.onSecondaryContainer)
                    if(state.bottomSheetState.isExpanded){
                        IconButton(onClick = {
                            scope.launch {
                                state.bottomSheetState.collapse()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Filled.ExpandMore,
                                contentDescription = "Close",
                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                    if(state.bottomSheetState.isCollapsed){
                        IconButton(onClick = {
                            scope.launch {
                                state.bottomSheetState.expand()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Filled.ExpandLess,
                                contentDescription = "Close",
                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
            }
            item {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    value = siteName,
                    onValueChange = { siteName = it },
                    label = {Text("Site Name")},
                    placeholder = {Text("Eg: Google.com")},
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onSecondaryContainer),
                )
            }
            item {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    value = sitePwd,
                    onValueChange = { sitePwd = it },
                    label = {Text("Site Password")},
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onSecondaryContainer),
                )
            }
            item {
                FilledTonalButton(onClick = {
                    if(siteName.isEmpty() || sitePwd.isEmpty()){
                        isError.value = true
                    }
                    else{
                        insertSitePwd(siteName = siteName, sitePwd = sitePwd,
                            mPwdViewModel = mPwdViewModel)
                        scope.launch {
                            state.bottomSheetState.collapse()
                        }
                    }
                                            },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp)) {
                    Text("Save", color = MaterialTheme.colorScheme.onSecondaryContainer)
                }
            }
        }
    }

    if(isError.value){
        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
                isError.value = false
            },
            title = { Text(text = "Error") },
            text = { Text(text = "Fields cannot be empty") },
            confirmButton = {
                TextButton(
                    onClick = {
                        isError.value = false
                    }
                ) {
                    Text("Close")
                }
            },
        )
    }
}

private fun insertSitePwd(siteName: String, sitePwd: String, mPwdViewModel: PwdViewModel){
    if(siteName.isNotEmpty() && sitePwd.isNotEmpty()){
        val pwdItem = PwdItem(
            siteName = siteName,
            sitePwd = sitePwd
        )

        mPwdViewModel.addSitePwd(pwdItem = pwdItem)
    }
}