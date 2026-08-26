package io.github.dinujaya77.jyotisha.ui.preview

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.github.dinujaya77.jyotisha.ui.about.AboutScreen
import io.github.dinujaya77.jyotisha.ui.method.MethodCallbacks
import io.github.dinujaya77.jyotisha.ui.method.MethodScreen
import io.github.dinujaya77.jyotisha.ui.theme.JyotishaTheme

@Preview(name = "M3 Method unavailable context", widthDp = 360, heightDp = 800, showBackground = true)
@Composable
private fun MethodPreview() {
    JyotishaTheme {
        MethodScreen(callbacks = MethodCallbacks({}), innerPadding = PaddingValues())
    }
}

@Preview(name = "M3 About and privacy", widthDp = 360, heightDp = 800, showBackground = true)
@Composable
private fun AboutPreview() {
    JyotishaTheme {
        AboutScreen(
            openMethod = {},
            onBackToSettings = {},
            innerPadding = PaddingValues(),
        )
    }
}
