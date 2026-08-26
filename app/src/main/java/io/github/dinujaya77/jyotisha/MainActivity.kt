package io.github.dinujaya77.jyotisha

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import io.github.dinujaya77.jyotisha.ui.theme.JyotishaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JyotishaTheme {
                JyotishaApp()
            }
        }
    }
}
