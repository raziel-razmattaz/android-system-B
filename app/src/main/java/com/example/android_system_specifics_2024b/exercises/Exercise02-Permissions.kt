package com.example.android_system_specifics_2024b.exercises


import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.android_system_specifics_2024b.R

@Composable
fun NotificationPermissionScreen() {
    var ablehnungsCount by remember { mutableIntStateOf(0) }
    var zeigeErklaerungsDialog by remember { mutableStateOf(false) }

    // Launcher, welcher die Anfragen an das Betriebesystem sendet und ein Ergebnis zurückgibt.
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (!isGranted) {
                ablehnungsCount += 1
                zeigeErklaerungsDialog = true
            }
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        Button(onClick = {

            /* TODO:
                Aufgabe 2b
                Implementiere die Abfrage, ob Build.VERSION.SDK_INT mindestens Android 13 entspricht.
                -
                Tipp:
                Build.VERSION.SDK_INT --> aktuelle version
                Build.VERSION_CODES.TIRAMISU --> Android 13
                -
                Aufgabe 2c
                Nutze den notificationPermissionLauncher und die launch() Funktion um die Anfrage auf die in der Android Manifest hinzugefügte Benachrichtigungs-permission zu erstellen.
            */

            //LÖSUNG
            //
        }) {
            Text(stringResource(id =R.string.permission_button_label))
        }
    }

    if (zeigeErklaerungsDialog) {
        ErklaerungsDialog(
            onDismiss = { zeigeErklaerungsDialog = false },
            ablehnungsCount = ablehnungsCount
        )
    }
}


/*
    TODO:
        Eine App kann eine Berechtigung nur 2 mal abfragen, dannach muss der Nutzer diese manuell in der App info ertielen. Implementiere dafür die richtigen Buttons und Erklärungstexte
        -
        Aufgabe 2d
        Lade die passenden String Ressourcen desc_app_info und desc_normal
        -
        Aufgabe 2e
        Implementiere, dass der AppInfoButton() auftaucht, sobald der Nutzer mindestens 2 mal abgelehnt hat.
*/


//Dialog, welcher dem Nutzer erklärt, weshalb die Berechtigung benötigt wird.
@Composable
fun ErklaerungsDialog(onDismiss: () -> Unit, ablehnungsCount: Int,) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(stringResource(id = R.string.erklaerungsDialog_title))
        },
        text = {

            //Wenn der Nutzer mindestens 2 mal abgelehnt hat
            if (ablehnungsCount >= 2) {
                //TODO: 2d nutze die StringResource "desc_app_info".

                //LÖSUNG
                //
            }else{
                //Wenn der Nutzer weniger als 2 mal abgelehnt hat.
                //TODO: 2d nutze die StringResource "desc_normal".

                //LÖSUNG
                //
            }
        },
        confirmButton = {
            //TODO: 2e Implementiere, dass der AppInfoButton() auftaucht, sobald der Nutzer mindestens 2 mal abgelehnt hat.

            //LÖSUNG
            //
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                if (ablehnungsCount >= 2) {
                    Text(stringResource(id =R.string.abbrechen_button_label))
                } else {
                    Text(stringResource(id =R.string.ok_button_label))
                }
            }
        }
    )
}

//Button, welcher die App info öffnet.
@Composable
fun AppInfoButton(context : Context){
    TextButton(
        onClick = {
            //Intent zum öffnen der App info wird erstellt und gestartet
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
            }
            context.startActivity(intent)
        }
    ) {
        Text(stringResource(id = R.string.app_info_button_label))
    }
}

@Preview(
    showSystemUi = true
)
@Composable
private fun NotificationPermissionPreview() {
    NotificationPermissionScreen()
}
