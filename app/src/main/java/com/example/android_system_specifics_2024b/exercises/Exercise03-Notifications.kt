package com.example.android_system_specifics_2024b.exercises

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.android_system_specifics_2024b.R



class Exercise03Notifications : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }
        setContent {
            TimerApp()
        }
    }

    fun showNotification() {

        createNotificationChannel()
        // Basis Intent (Tippaktion) um Aktivität zu öffen, wenn user auf die Benachrichtung tippt
        val intent = Intent(this, Exercise03Notifications::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)



        //Erstellen der Benachrichtigung
        val builder = NotificationCompat.Builder(this, "TIMER_CHANNEL")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Icon
            .setContentTitle("Timer fertig!") // Titel
            .setContentText("Dein Timer ist fertig") // Untertext
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) // Priorität(Android7.1 und niedriger)(Ab 8.0 mit NotificationChannel)
            .setContentIntent(pendingIntent)//zuweisung für den Basis Intent
            .setAutoCancel(true) //entfernt benachrichtigung sobald user drauftippt
            .setSound(Uri.parse("android.resource://${this.packageName}/${R.raw.timer_melody}")) // Sound hinzufügen

        //Überprüfung, ob app die berechtigung zum senden von Benachrichtigungen hat und anschließendes senden der notification
        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@Exercise03Notifications,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
            }
            notify(1, builder.build()) // senden der notification
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Timer Channel"
            val descriptionText = "Channel for Timer notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("TIMER_CHANNEL", name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

fun startTimer(timeInMillis: Long, onTick: (Long) -> Unit, onFinish: () -> Unit) {
    val timer = object : CountDownTimer(timeInMillis, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            onTick(millisUntilFinished)
        }

        override fun onFinish() {
            onFinish()
        }
    }

    timer.start()
}
@Preview
@Composable
fun TimerApp() {
    var time by remember { mutableStateOf("") }
    var secondsRemaining by remember { mutableStateOf(0L) }
    var isRunning by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = time,
            onValueChange = { time = it },
            label = { Text("Sekunden eingeben") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                val timeInMillis = time.toLongOrNull()
                if (timeInMillis != null) {
                    isRunning = true
                    startTimer(timeInMillis * 1000,
                        onTick = { remainingTime ->
                            secondsRemaining = remainingTime / 1000
                        },
                        onFinish = {
                            isRunning = false
                            showNotification()
                        }
                    )
                }
            },
            enabled = !isRunning
        ) {
            Text("Timer starten")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = if (isRunning) "$secondsRemaining Sekunden" else "Timer vorbei",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}}