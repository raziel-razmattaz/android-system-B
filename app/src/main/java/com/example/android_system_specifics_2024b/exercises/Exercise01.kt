@file:Suppress("SpellCheckingInspection")

package com.example.android_system_specifics_2024b.exercises

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.android_system_specifics_2024b.R

/* TODO: Aufgabe 01
    (a) Erstelle zunächst in der Datei /res/values/strings.xml drei String Ressourcen für die 3
    Label "Focus Time", "Short Break" und "Long Break". Erstelle dann 3 untereinanderliegende
    Textelemente in dem Composable StringAccess, welche jeweils mit einem der Labels betitelt ist.
    (b) In dem Composable MusicRessource soll nun ein Button erstellt werden.
    Der Button soll die Funktion toggleMusic() aufrufen. Die Funktion soll einen MediaPlayer erstellen,
    wenn es noch keinen gibt, und die Musik entweder starten oder pausieren. Der Song befindet sich
    im /res/raw Folder. Zur Einfachheit halber, wird für den MediaPlayer eine
    globale Variable verwendet.
    (Genutzer Song ist "Piano Melody Solo 20" von Tozan, bereitgestellt unter CC0.)
 */

// AUFGABE 1A: TIMER LABELS
@Composable
fun StringAccess( modifier: Modifier = Modifier) {
    Column {
        // EXERCISE SOLUTION
        Text(text = stringResource(id = R.string.focus_label))
        Text(text = stringResource(id = R.string.short_break_label))
        Text(text = stringResource(id = R.string.long_break_label))
    }
}

@Preview(showBackground = true)
@Composable
fun StringAccessPreview() {
        StringAccess()
}

// AUFGABE 1B: MUSIKWIEDERGABE
//Normalerweise nicht hier definieren, nur zu Übungszwecken
//Siehe auch den letzten Workshop zu MVVM
var mediaPlayer : MediaPlayer? = null

fun toggleMusic(context : Context) {
    //EXERCISE SOLUTION
    if (mediaPlayer == null) {
        //This is where you use your passed context
        mediaPlayer = MediaPlayer.create(context, R.raw.timer_melody)
    }
    if (mediaPlayer?.isPlaying == true) {
        mediaPlayer?.pause()
    } else {
        mediaPlayer?.start()
    }
}

@Composable
fun MusicRessource(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column {
        // EXERCISE SOLUTION
        Button(onClick = { toggleMusic(context) }) {
            Text(text = stringResource(id = R.string.timer_label))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MusicRessourcePreview() {
    MusicRessource()
}