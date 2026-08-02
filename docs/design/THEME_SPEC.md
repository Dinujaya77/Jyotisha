# Theme Specification

| Field | Value |
|---|---|
| Status | Approved |
| Version | 1.0 |
| Last updated | 2026-08-02 |
| Owner role | Lead Coordinator; UI/UX Designer read-only reviewer |
| Approval state | DA-001 Direction A and DA-004 Version 1.0 token system Approved on 2026-08-02 |

## Theme contract

The approved Direction A narrative is implemented as **Celestial Archive**: midnight-blue or parchment surfaces, restrained antique-gold actions, archival rules, and abstract celestial geometry. Material 3 supplies the accessible component behavior. Feature composables consume semantic tokens only; no feature hard-codes palette, type, spacing, shape, elevation, duration, or easing values.

Dynamic color is disabled in Version 1.0 so the reviewed hierarchy and contrast remain deterministic. Light and dark schemes are both required. Future theme packs may replace token values at the theme boundary only; they may not change hierarchy, labels, semantics, non-color cues, or contrast requirements.

## Color tokens

Contrast ratios below use WCAG relative luminance for the stated foreground/background pair. Text pairs exceed 4.5:1; large display text is not used to justify a lower threshold.

### Light scheme

| Semantic token | Value | Paired content/background | Measured contrast |
|---|---:|---:|---:|
| `archiveBackground` | `#F7F1E5` | `contentPrimary` `#201C17` | 15.06:1 |
| `archiveSurface` | `#FFF9EF` | `contentPrimary` `#201C17` | 16.17:1 |
| `archiveSurfaceVariant` | `#EDE4D4` | `contentPrimary` `#201C17` | 13.43:1 |
| `contentPrimary` | `#201C17` | primary text role | — |
| `contentSecondary` | `#514A40` | on `archiveBackground` | 7.77:1 |
| `actionPrimary` | `#6E5A1E` | `#FFFFFF` | 6.68:1 |
| `actionSecondary` | `#334563` | `#FFFFFF` | 9.67:1 |
| `statusActiveContainer` | `#E7EFE5` | `#1D3A22` | 10.64:1 |
| `statusCautionContainer` | `#FFF0CC` | `#4B3400` | 10.38:1 |
| `statusErrorContainer` | `#FCE8E6` | `#5B1A18` | 11.10:1 |
| `statusInfoContainer` | `#E7EEF8` | `#17324E` | 11.22:1 |
| `statusUnavailableContainer` | `#ECE8E0` | `#3E3932` | 9.36:1 |
| `outline` | `#776F63` | background/surface | 4.40:1 / 4.73:1 |
| `focus` | `#334563` | 2dp on background/surface | 8.59:1 / 9.23:1 |

### Dark scheme

| Semantic token | Value | Paired content/background | Measured contrast |
|---|---:|---:|---:|
| `archiveBackground` | `#101827` | `contentPrimary` `#F4EBDD` | 15.04:1 |
| `archiveSurface` | `#182235` | `contentPrimary` `#F4EBDD` | 13.47:1 |
| `archiveSurfaceVariant` | `#263248` | `contentPrimary` `#F4EBDD` | 10.89:1 |
| `contentPrimary` | `#F4EBDD` | primary text role | — |
| `contentSecondary` | `#CCC3B5` | on `archiveBackground` | 10.19:1 |
| `actionPrimary` | `#D8BD69` | `#302700` | 8.05:1 |
| `actionSecondary` | `#B7C7E6` | `#18263C` | 8.92:1 |
| `statusActiveContainer` | `#193521` | `#D7F0DA` | 11.04:1 |
| `statusCautionContainer` | `#3D2E07` | `#FFE5A6` | 10.68:1 |
| `statusErrorContainer` | `#5B1917` | `#FFDAD6` | 10.18:1 |
| `statusInfoContainer` | `#183047` | `#D6E4FA` | 10.52:1 |
| `statusUnavailableContainer` | `#302D29` | `#E2DDD4` | 10.13:1 |
| `outline` | `#928B80` | background/surface | 5.27:1 / 4.72:1 |
| `focus` | `#B7C7E6` | 2dp on background/surface | 10.43:1 / 9.34:1 |

`statusActive`, `statusCaution`, `statusError`, `statusInfo`, and `statusUnavailable` always include explicit text and an icon, outline, or weight cue. The remaining states are deterministic aliases: `statusLaterContainer/content = statusInfoContainer/content`; `statusPendingContainer/content = statusInfoContainer/content`; `statusCompletedContainer/content = statusUnavailableContainer/content`. Their explicit labels and icons/outline distinguish meaning; color alone never conveys time state. Disabled content uses the Material 3 disabled treatment only after component-level contrast and legibility review.

## Typography

Use the Android system sans-serif family. No downloadable, bundled, or new font dependency is approved.

| Token | Size / line height | Weight | Use |
|---|---|---:|---|
| `display` | 40sp / 48sp | 600 | one dominant current ruler or primary time |
| `headline` | 28sp / 36sp | 600 | screen heading |
| `title` | 22sp / 28sp | 600 | section and card title |
| `body` | 16sp / 24sp | 400 | normal copy and values |
| `bodySmall` | 14sp / 20sp | 400 | provenance and supporting copy |
| `label` | 14sp / 20sp | 600 | buttons, navigation, and state labels |
| `timePrimary` | 32sp / 40sp | 600 | start/end/countdown emphasis |

The minimum body/label size is 14sp. Time values may request tabular numerals when the platform font supports them, but alignment and correctness must not depend on that feature. At 200% font scale, containers grow and time pairs stack; no fixed-height timing card is permitted.

## Layout tokens

- Spacing: `spaceXs=4dp`, `spaceSm=8dp`, `spaceMd=12dp`, `spaceLg=16dp`, `spaceXl=24dp`, `space2xl=32dp`, `space3xl=48dp`.
- Screen gutters: `gutterCompact=16dp`, `gutterMedium=24dp`, `gutterExpanded=32dp`.
- Shapes: `shapeSmall=8dp`, `shapeMedium=12dp`, `shapeLarge=16dp`, `shapeExtraLarge=24dp`.
- Elevation: `elevationBase=0dp`, `elevationGrouped=1dp`, `elevationTransient=3dp`. Elevation is never the sole boundary cue.
- Targets/focus: `touchTargetMinimum=48dp`; `focusOutlineWidth=2dp`, with a shape cue that survives grayscale and color-vision differences.

## Gradients, iconography, and illustration

The decorative token `backgroundWash` maps to the static pair `backgroundWashStart=#F7F1E5` and `backgroundWashEnd=#FFF9EF` in light mode, or `#101827` and `#182235` in dark mode. In constrained, high-contrast, reduced-motion, or screenshot-fixture contexts it maps explicitly to `none`. Gradients are decorative only: they may not sit behind critical text unless the entire measured area passes contrast, and they may not encode state.

Use bundled Material/vector icons already available to the project, generally a 24dp glyph within a 48dp target. Outline style is preferred for the archival character. Every actionable icon has a visible text label or accessible name; state icons accompany explicit state text.

Illustration is limited to abstract celestial arcs, dots, horizon rules, and archival linework. Do not depict deities, sacred figures, astrological promises, or culture-specific religious symbols. Decorative illustration is hidden from accessibility services and disappears before content under constrained height or large text.

## Motion

- `motionInstant=0ms`.
- `motionQuick=120ms` for pressed/focus feedback.
- `motionStandard=180ms` for state/content replacement.
- `motionEmphasized=240ms` for navigation/container emphasis.
- `easingStandard=CubicBezierEasing(0.2, 0, 0, 1)` and `easingExit=CubicBezierEasing(0.4, 0, 1, 1)`.
- Reduced motion maps every transition duration to `motionInstant` while retaining focus, labels, and state changes.
- No continuous starfield, pulse, parallax, countdown animation, or auto-scrolling motion is approved.

## Implementation verification gate

DA-004 approves this specification after documentation QA; it does not claim that application evidence already exists. During M2/M3 and before their acceptance/release, verify every component pairing (including outline, focus, disabled, pressed, and selected states), light/dark previews, 200% font scale, compact landscape, TalkBack order, keyboard/D-pad focus, reduced motion, and grayscale/non-color state recognition. Record the produced evidence against the UI IDs in `UI_SPEC.md`, `ACCESSIBILITY.md`, and the assigned delivery tasks.
