//package com.srivats.odpm
//
//import androidx.compose.material3.AlertDialog
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextButton
//import androidx.compose.runtime.Composable
//
//@Composable
//fun DialogUi(onDismiss: () -> Unit, msg: String){
//    AlertDialog(
//        onDismissRequest = onDismiss,
//        text = {
//            Text(
//                text = msg,
//                style = MaterialTheme.typography.bodyMedium
//            )
//        },
//        confirmButton = {
//            TextButton(onClick = onDismiss) {
//                Text(text = "CLOSE")
//            }
//        }
//    )
//}