# Theme Specification

| Field | Value |
|---|---|
| Status | Ready |
| Version | 0.4 |
| Last updated | 2026-08-02 |
| Owner role | UI/UX Designer |
| Approval state | DA-001 Direction A selected; its semantic mapping is the basis for the later UI specification, while exact token values and evidence remain unapproved |

## Semantic token groups

- Surfaces: background, surface, elevated, inverse, scrim.
- Content: primary, secondary, muted, inverse, link.
- Actions: primary, secondary, destructive, disabled, focus.
- Status: informative, positive, caution, critical, each with icon/text treatment.
- Domain: celestial accent plus one direction-specific accent role only where semantically defined.
- Typography, shape, spacing, elevation, iconography, and motion scales.
- Timing hierarchy: `timingPrimaryContainer/content`, `solarAnchorContainer/content`, `concurrentStatusContainer/content`, and `timelineDivider`.
- Timing/status: `statusActive`, `statusLater`, `statusCompleted`, `statusPending`, `statusUnavailable`, `validationCaution`, `activeOutline`, and `focus`, each paired with text/icon/shape semantics.

## Phase B direction mapping

Material 3 is the accessible base. Approved Direction A maps midnight/parchment/antique-gold character; superseded alternatives B and C remain preserved references only. Direction A's exact values still require contrast testing and later UI approval. Feature composables must not hard-code brand colours, typefaces, shapes, spacing, durations, or easing. Light/dark and later theme packs replace token values only at the theme boundary without changing hierarchy, semantics, icon/text cues, or contrast.

No display font, asset, icon library, palette value, dynamic-color policy, ruler-specific colour, or theme dependency is approved in Phase B.

## Verification

Test contrast, dynamic/alternate theme behavior if supported, large font scales, disabled/focus states, OLED dark surfaces, and reduced motion before approval.
