package com.example.android_system_specifics_2024b.exercises

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
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



class Exercise03NotificationsLösung : ComponentActivity() {
    private var mediaPlayer: MediaPlayer? = null
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
        /*
         TODO 3a
              Vervollständige den builder für die Notification, es fehlt ein icon(ic_launcher_foreground),
              ein Untertext und die Zuweisung für den Basis Intent.
        */
        //Lösung 3a
        val builder = NotificationCompat.Builder(this, "TIMER_CHANNEL")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Icon
            .setContentTitle("Timer fertig!") // Titel
            .setContentText("Dein Timer ist fertig") // Untertext
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) // Priorität(Android7.1 und niedriger)(Ab 8.0 mit NotificationChannel)
            .setContentIntent(pendingIntent)//zuweisung für den Basis Intent
            .setAutoCancel(true) //entfernt benachrichtigung sobald user drauftippt

        //Überprüfung, ob app die berechtigung zum senden von Benachrichtigungen hat und anschließendes senden der notification
        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@Exercise03NotificationsLösung,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
            }
            notify(1, builder.build()) // Senden der notification
        }
        /*
        TODO 3b
             Erstelle nun einen MediaPlayer(mit der timer_melody aus aufgabe1),
             damit beim erscheinen der Notifcation ein Sound abgespielt wird.
       */

        //Lösung 3b
        mediaPlayer = MediaPlayer.create(this, R.raw.timer_melody)
        mediaPlayer?.start()

    }
    /*TODO 3c
            Als nächstes erstellen wir eine zweite Notification mit einem Fortschrittsbalken.
            Hierfür muss ersteinmal eine normale Notification mit icon, titel, untertext und priorität erstellt werden.
            Dazu müsst ihr dann noch eine Progressbar hinzufügen und den Untertext die verbleibenden Sekunden
            anzeigen lassen (hierfür könnt ihr die Parameter progress und maxProgress benutzen,
            welche von der Timer Methode übergeben werden)
            Am Ende muss noch die Notification gesendet werden
            (TIPP: die progressNotification braucht eine andere id als die Notification,
            damit die Notifications getrennt voneinander und geschlossen werden können)
            (dies wird weiter unten in der onFinisch Methode von StartTimer behandelt)*/
    //Lösung 3c
    private fun showProgressNotification(progress: Int, maxProgress: Int) {
        val builder = NotificationCompat.Builder(this, "TIMER_CHANNEL")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Timer läuft...")
            .setContentText("Verbleibende Zeit: $progress Sekunden")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setProgress(maxProgress, progress, false) //Fortschrittsbalken

        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@Exercise03NotificationsLösung,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(2, builder.build())
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
    override fun onDestroy() {     //Methode um den Sound zu beenden, wenn auf die Benachrichtgung getippt wird
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
    private fun startTimer(timeInMillis: Long, onTick: (Long) -> Unit, onFinish: () -> Unit) {
        val timer = object : CountDownTimer(timeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                onTick(millisUntilFinished)
                val secondsRemaining = millisUntilFinished / 1000
                showProgressNotification(secondsRemaining.toInt(), (timeInMillis / 1000).toInt()) //Starten und Aktualisierung der FortschrittsbalkenNotificiation mit den Werten des Timers
            }

            override fun onFinish() {
                onFinish()
                showNotification() //Nach ablaufen des Times wird die Notification gestartet
                with(NotificationManagerCompat.from(this@Exercise03NotificationsLösung)) {
                    cancel(2)   //Nach ablaufen des Timers wird die ProgressNotification automatisch geschlossen
                }
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
    }    }