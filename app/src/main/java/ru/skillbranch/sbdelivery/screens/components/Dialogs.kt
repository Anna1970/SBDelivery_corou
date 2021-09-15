package ru.skillbranch.sbdelivery.screens.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.screens.root.ui.AppTheme

@Composable
fun AboutDialog(
    onDismiss: () -> Unit
){
    AlertDialog(
        onDismissRequest = onDismiss,
        contentColor = MaterialTheme.colors.primary,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ){
                Text(text = "О приложении SBDelivery")
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = onDismiss,
                    content = {
                        Icon(
                            tint = MaterialTheme.colors.onPrimary,
                            painter = painterResource(id = R.drawable.ic_baseline_close_24),
                            contentDescription = "Close"
                          )
                    }
                )
            }
        },
        text = {
             Text(
                    text = "Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum.Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum.Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum.Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum.Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum."
             )},
        buttons = {
            Row {
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Ok", color = MaterialTheme.colors.secondary)
                }
            }
        }
    )
}

@Preview
@Composable
fun AboutDialogPreview() {
    AppTheme {
        AboutDialog(onDismiss = {})
    }
}