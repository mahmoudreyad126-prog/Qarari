# Qarari Final

Qarari is a Saudi-focused native Android decision assistant for comparing job offers by their real financial value, career impact, work-life balance, benefits and risks.

## Included
- Native Android app (no WebView)
- Offer Vault
- Current Job baseline
- Compare 2–5 offers
- Financial / Career / Work-Life / Benefits / Risk scores
- Qarari Score + recommendation
- Red Flags engine
- Negotiation Assistant
- Scenario Mode
- Decision History
- PDF report generation and sharing
- Offline SQLite storage
- Personal scoring weights
- GitHub Actions APK build

## Architecture
- `model/` domain models
- `engine/` Qarari Decision Engine
- `data/` SQLite + profile preferences
- `ui/` native Android screens
- `util/` UI helpers and PDF engine

## AI / Cloud readiness
The final local product intentionally does not contain fake cloud credentials or hard-coded AI keys. OCR/PDF extraction and sync can be connected later through a secure backend/API layer without changing the core Decision Engine.

## Build
Requires Android SDK 35, Java 17 and Gradle 8.9. GitHub Actions builds `Qarari-Final.apk` automatically on push to `main`.
