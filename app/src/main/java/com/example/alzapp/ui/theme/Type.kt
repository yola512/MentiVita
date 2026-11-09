package com.example.alzapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.example.alzapp.R
import com.example.alzapp.data.settings.FontSize

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

// either Poppins + Merriweather or Open Sans + Merriweather Sans
// i feel like the 1st one is more legible...
val bodyFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Poppins"),
        fontProvider = provider,
    )
)

val displayFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Merriweather"),
        fontProvider = provider,
    )
)

private val baseline = Typography()

fun getTypography(fontSize: FontSize): Typography {
    return Typography(
        displayLarge = baseline.displayLarge.copy(fontFamily = bodyFontFamily, fontSize = fontSize.displaySize),
        displayMedium = baseline.displayMedium.copy(fontFamily = bodyFontFamily, fontSize = fontSize.displaySize),
        displaySmall = baseline.displaySmall.copy(fontFamily = bodyFontFamily, fontSize = fontSize.displaySize),
        headlineLarge = baseline.headlineLarge.copy(fontFamily = displayFontFamily, fontSize = fontSize.headlineSize),
        headlineMedium = baseline.headlineMedium.copy(fontFamily = displayFontFamily, fontSize = fontSize.headlineSize),
        headlineSmall = baseline.headlineSmall.copy(fontFamily = displayFontFamily, fontSize = fontSize.headlineSize),
        titleLarge = baseline.titleLarge.copy(fontFamily = bodyFontFamily, fontSize = fontSize.headlineSize),
        titleMedium = baseline.titleMedium.copy(fontFamily = bodyFontFamily, fontSize = fontSize.bodySize),
        titleSmall = baseline.titleSmall.copy(fontFamily = bodyFontFamily, fontSize = fontSize.bodySize),
        bodyLarge = baseline.bodyLarge.copy(fontFamily = bodyFontFamily, fontSize = fontSize.bodySize),
        bodyMedium = baseline.bodyMedium.copy(fontFamily = bodyFontFamily, fontSize = fontSize.bodySize),
        bodySmall = baseline.bodySmall.copy(fontFamily = bodyFontFamily, fontSize = fontSize.bodySize),
        labelLarge = baseline.labelLarge.copy(fontFamily = bodyFontFamily, fontSize = fontSize.bodySize),
        labelMedium = baseline.labelMedium.copy(fontFamily = bodyFontFamily, fontSize = fontSize.bodySize),
        labelSmall = baseline.labelSmall.copy(fontFamily = bodyFontFamily, fontSize = fontSize.bodySize),
    )
}
