# Theme Specification

| Field | Value |
|---|---|
| Status | Awaiting Approval |
| Version | 0.2 |
| Last updated | 2026-08-01 |
| Owner role | UI/UX Designer |
| Approval state | Semantic contract proposed; final mappings deferred until A, B, or C is approved |

## Semantic token groups

- Surfaces: background, surface, elevated, inverse, scrim.
- Content: primary, secondary, muted, inverse, link.
- Actions: primary, secondary, destructive, disabled, focus.
- Status: informative, positive, caution, critical, each with icon/text treatment.
- Domain: celestial accent plus one direction-specific accent role only where semantically defined.
- Typography, shape, spacing, elevation, iconography, and motion scales.

## Phase B direction mapping

Material 3 is the accessible base. Direction A maps archive/antique-metal character, B maps instrument/celestial accents, and C maps moonlit/warm-human accents only after one direction is approved and contrast-tested. Feature composables must not hard-code brand colours, typefaces, shapes, spacing, durations, or easing. Light/dark and future themes replace token values without changing feature meaning or accessibility semantics.

No display font, asset, icon library, palette value, dynamic-color policy, or theme dependency is approved in Phase B.

## Verification

Test contrast, dynamic/alternate theme behavior if supported, large font scales, disabled/focus states, OLED dark surfaces, and reduced motion before approval.
