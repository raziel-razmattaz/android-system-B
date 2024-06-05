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
                Aufgabe 2a
                Implementiere die abfrage, ob Build.VERSION.SDK_INT mindestens Android 13 entspricht
                -
                Tipp:
                Build.VERSION.SDK_INT --> aktuelle version
                Build.VERSION_CODES.TIRAMISU --> Android 13
                -
                Aufgabe 2b
                Nutze dann den notificationPermissionLauncher um die in der Android Manifest hinzugefügte Benachrichtigungs-permission zu erfragen.
            */

            //LÖSUNG
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
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
        Eine App kann eine Berechtigung nur 2 mal abfragen, dannach muss der nutzer diese manuell in der App info ertielen.
        Aufgabe 2c
        Lade die passenden String Ressourcen desc_app_info und desc_normal
        -
        Aufgabe 2d
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
                //TODO: 2c Nutze die stringResource "desc_app_info".

                //LÖSUNG
                Text(text = stringResource(id = R.string.desc_app_info))
            }else{
                //Wenn der Nutzer weniger als 2 mal abgelehnt hat.
                //TODO: 2c Nutze die stringResource "desc_normal".

                //LÖSUNG
                Text(text = stringResource(id = R.string.desc_normal))
            }
        },
        confirmButton = {
            //TODO: 2c.3 Implementiere, dass der AppInfoButton() auftaucht, sobald der Nutzer mindestens 2 mal abgelehnt hat.

            //LÖSUNG
            if (ablehnungsCount >= 2) {
                AppInfoButton(context)
            }
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
