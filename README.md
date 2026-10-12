# MedProtocols — Android app scaffold

A from-scratch Android app (Kotlin + Jetpack Compose + Room) for browsing clinical
protocols by category, with search, offline storage, and a simple dose calculator.
It ships with **no bundled prescription content** — two placeholder entries only,
clearly labeled as samples — so you can legitimately populate it with your own.

## Opening the project
1. Unzip, open the folder in Android Studio (Hedgehog / 2023.1+ or newer).
2. Let Gradle sync.
3. Run on an emulator or device (minSdk 24 / Android 7.0+).

### Honest caveats
I wrote this without access to the Android SDK, so I couldn't compile or run it
myself. I checked every file for balanced braces, matching package/folder
structure, and consistent cross-file references, but that's not the same as a
real build — treat it as a strong first draft, not a guarantee.

- **Gradle/Kotlin/Compose versions** are pinned to a combination I'm confident
  is mutually compatible: Kotlin 1.9.22, AGP 8.2.2, Compose compiler 1.5.8,
  Compose BOM 2024.02.00. If Android Studio flags a version mismatch, its
  one-click quick-fix is safe to accept.
- **App icon**: a simple original vector adaptive icon lives in
  `res/mipmap-anydpi-v26`. It covers Android 8.0+; for 7.x devices, run
  Android Studio's *New → Image Asset* wizard to generate the older PNG sizes
  (under a minute).
- **Persian font**: UI text currently uses the system default font. For nicer
  Persian rendering, add an open-licensed font like
  [Vazirmatn](https://github.com/rastikerdar/vazirmatn) (SIL OFL) as a `.ttf`
  in `res/font/` and reference it from `ui/theme/Theme.kt`. For Latin text, any
  distinctive family you like will work the same way.
- **Reading direction** is hard-set to RTL app-wide (`ui/navigation/AppNavHost.kt`)
  since the data is Persian regardless of device language. Remove that wrapper
  if you'd rather it follow the system locale.

## Adding your content
The app has **no built-in prescriptions data** — not rapidrx's, not ccapp's,
nothing scraped from anywhere. It only seeds two entries labeled "Sample" /
"نمونه" on first launch, to prove the structure works. Replace them from the
**Settings** tab → *Import content (JSON)*, pointing at a file shaped like this:

```json
{
  "categories": [
    {
      "title": "Category name",
      "colorHex": "#0F6E5E",
      "protocols": [
        {
          "title": "Protocol name",
          "summary": "One-line summary shown in lists",
          "sections": [
            { "label": "Definition", "content": "…" },
            { "label": "Diagnosis", "content": "…" },
            { "label": "Treatment", "content": "…" }
          ]
        }
      ]
    }
  ]
}
```

Section labels are free text — use Definition/Diagnosis/Treatment or whatever
structure fits your material. Importing **replaces** whatever's currently in
the database, so you can re-import as your content grows.

`app/src/main/assets/sample_protocols.json` is the file that seeds the
placeholder data on first launch — edit it directly (and reinstall) if you'd
rather bake in your content than import it at runtime.

## Structure
- `data/` — Room entities (`Category`, `Protocol`, `ProtocolSection`), DAOs,
  the database, `ProtocolRepository`, and `ContentImporter` (the JSON import
  above, via kotlinx.serialization).
- `ui/theme/` — an original color palette (clinical teal + a prescription-stamp
  wine accent, cool paper background) and type scale — not rapidrx's or
  ccapp's design.
- `ui/navigation/` — the nav graph: Home → Category → Protocol detail, plus
  Calculator and Settings tabs in the bottom bar.
- `ui/screens/` — one package per screen.
- `ui/components/` — shared composables (search field, the category/protocol
  list rows).

## Design notes
The list rows use a flat "index" treatment — a thin colored accent + hairline
rule — instead of generic rounded shadow cards, closer to a reference book's
table of contents than a typical app's card grid. Colors and type were chosen
specifically for this brief rather than defaulted.

## What's intentionally left out
No network code, no analytics, no licensing/paywall logic — this is meant to
be yours outright. The dose calculator is a plain weight × mg/kg × frequency
calculator with no drug data baked in, so it stays correct regardless of what
you load into it; it isn't a substitute for checking an actual dosing
reference, and the in-app text says so.

## Natural next steps
- Swap `FontFamily.Default` for a real Persian + Latin pairing (see above).
- Add a `ViewModel` + `SavedStateHandle` layer if the screens grow more state.
- Migrate `kapt` → `ksp` for faster Room builds once you're comfortable editing
  the Gradle setup.
- Add a light/dark theme toggle in Settings (the theme already supports dark
  mode; it just follows the system setting for now).

## Building an APK
This project now includes a proper Gradle wrapper (`gradlew` / `gradlew.bat` /
`gradle/wrapper/`), pinned to Gradle 8.2 — the version Android's own docs list
as required for AGP 8.2.2 — and a ready GitHub Actions workflow at
`.github/workflows/build-apk.yml` that builds a debug APK on every push and
on demand (Actions tab → "Build debug APK" → "Run workflow"), no local
install required. See the chat for the full walkthrough of both the
online (GitHub) and local (Android Studio) build paths.
