# UI Specification

| Field | Value |
|---|---|
| Status | Proposed |
| Version | 0.3 |
| Last updated | 2026-08-02 |
| Owner role | UI/UX Designer |
| Approval state | PA-002 architecture and DA-001 Direction A approved; detailed specification remains stopped pending explicit authorization plus applicable V1.0 calculation/terminology approval |

## Screen template

### UI-000 — Screen/state

- Linked IA, user story, requirement, and domain-rule IDs:
- Purpose and entry/exit behavior:
- Content hierarchy and component semantics:
- Actions, validation, confirmation, and error recovery:
- Loading, empty, offline, partial, and permission states:
- Compact/medium/expanded and orientation behavior:
- TalkBack order, labels, headings, live regions, and keyboard behavior:
- Large text, high contrast, reduced motion, RTL, and locale states:
- Semantic tokens and motion tokens:
- Required Compose previews and screenshot checks:
- Analytics/privacy notes, if approved:

## Implementation gate

No screen redesign is authorized by this template alone. PA-002 and DA-001 satisfy the architecture/direction prerequisites. Detailed V1.0 specification still requires explicit authorization and approved V1.0 terminology/copy; CP-003/RK plus golden vectors are required before Rahu can function. CP-002/KH/PK is explicitly not a V1.0 UI gate and may appear only in a non-normative future Version 1.1 appendix.

Required screen specifications are first use, Now, Day, location selection/recovery, Method, solar unavailable, and all Rahu status states. Required previews/evidence include compact 320dp portrait, compact landscape, medium/expanded, light/dark, 200% font with long approved/pseudolocale strings, precise/approximate/manual/default/stale location, solar unavailable, all seven Rahu states, reduced-motion end states, a Day boundary state, and one future-theme test mapping. Pair previews with semantics-tree tests and manual TalkBack evidence.
