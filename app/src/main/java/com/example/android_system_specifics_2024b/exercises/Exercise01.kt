@file:Suppress("SpellCheckingInspection")

package com.example.android_system_specifics_2024b.exercises

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.android_system_specifics_2024b.R

/* Aufgabe 01
(a)
(b) In dem Composable UserRessource
 */

// EXERCISE 1A
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

// EXERCISE 1B

fun toggleMusic() {
    //EXERCISE SOLUTION
    println("Button toggled")
}

@Composable
fun MusicRessource( modifier: Modifier = Modifier) {
    Column {
        // EXERCISE SOLUTION
        Button(onClick = { toggleMusic() }) {
            Text(text = stringResource(id = R.string.timer_label))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MusicRessourcePreview() {
    MusicRessource()
}