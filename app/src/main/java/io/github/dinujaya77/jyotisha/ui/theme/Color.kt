package io.github.dinujaya77.jyotisha.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/** Semantic Celestial Archive colors approved by DA-004. */
@Immutable
class CelestialArchiveColors internal constructor(
    val archiveBackground: Color,
    val archiveSurface: Color,
    val archiveSurfaceVariant: Color,
    val contentPrimary: Color,
    val contentSecondary: Color,
    val actionPrimary: Color,
    val onActionPrimary: Color,
    val actionSecondary: Color,
    val onActionSecondary: Color,
    val statusActiveContainer: Color,
    val statusActiveContent: Color,
    val statusCautionContainer: Color,
    val statusCautionContent: Color,
    val statusErrorContainer: Color,
    val statusErrorContent: Color,
    val statusInfoContainer: Color,
    val statusInfoContent: Color,
    val statusUnavailableContainer: Color,
    val statusUnavailableContent: Color,
    val outline: Color,
    val focus: Color,
) {
    val statusLaterContainer: Color get() = statusInfoContainer
    val statusLaterContent: Color get() = statusInfoContent
    val statusPendingContainer: Color get() = statusInfoContainer
    val statusPendingContent: Color get() = statusInfoContent
    val statusCompletedContainer: Color get() = statusUnavailableContainer
    val statusCompletedContent: Color get() = statusUnavailableContent
}

internal val LightCelestialArchiveColors = CelestialArchiveColors(
    archiveBackground = Color(0xFFF7F1E5),
    archiveSurface = Color(0xFFFFF9EF),
    archiveSurfaceVariant = Color(0xFFEDE4D4),
    contentPrimary = Color(0xFF201C17),
    contentSecondary = Color(0xFF514A40),
    actionPrimary = Color(0xFF6E5A1E),
    onActionPrimary = Color(0xFFFFFFFF),
    actionSecondary = Color(0xFF334563),
    onActionSecondary = Color(0xFFFFFFFF),
    statusActiveContainer = Color(0xFFE7EFE5),
    statusActiveContent = Color(0xFF1D3A22),
    statusCautionContainer = Color(0xFFFFF0CC),
    statusCautionContent = Color(0xFF4B3400),
    statusErrorContainer = Color(0xFFFCE8E6),
    statusErrorContent = Color(0xFF5B1A18),
    statusInfoContainer = Color(0xFFE7EEF8),
    statusInfoContent = Color(0xFF17324E),
    statusUnavailableContainer = Color(0xFFECE8E0),
    statusUnavailableContent = Color(0xFF3E3932),
    outline = Color(0xFF776F63),
    focus = Color(0xFF334563),
)

internal val DarkCelestialArchiveColors = CelestialArchiveColors(
    archiveBackground = Color(0xFF101827),
    archiveSurface = Color(0xFF182235),
    archiveSurfaceVariant = Color(0xFF263248),
    contentPrimary = Color(0xFFF4EBDD),
    contentSecondary = Color(0xFFCCC3B5),
    actionPrimary = Color(0xFFD8BD69),
    onActionPrimary = Color(0xFF302700),
    actionSecondary = Color(0xFFB7C7E6),
    onActionSecondary = Color(0xFF18263C),
    statusActiveContainer = Color(0xFF193521),
    statusActiveContent = Color(0xFFD7F0DA),
    statusCautionContainer = Color(0xFF3D2E07),
    statusCautionContent = Color(0xFFFFE5A6),
    statusErrorContainer = Color(0xFF5B1917),
    statusErrorContent = Color(0xFFFFDAD6),
    statusInfoContainer = Color(0xFF183047),
    statusInfoContent = Color(0xFFD6E4FA),
    statusUnavailableContainer = Color(0xFF302D29),
    statusUnavailableContent = Color(0xFFE2DDD4),
    outline = Color(0xFF928B80),
    focus = Color(0xFFB7C7E6),
)
