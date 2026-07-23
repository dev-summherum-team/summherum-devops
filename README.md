# Summherum – Digitales Reisetagebuch 🐝

DevOps-Semesterprojekt: ein webbasiertes, digitales Reisetagebuch.  
Dieses Repository enthält den Anwendungscode sowie die vollständige Entwicklungsumgebung.  
Weitere Details sind in der Projektdokumentation beschrieben.

## Struktur

```text
.
├── .devcontainer/          # Entwicklungsumgebung
│   ├── devcontainer.json
│   └── docker-compose.yml
├── .github/                # CI/CD-Pipeline
├── .gitignore              # Ausgeschlossene Dateien
├── LICENSE                 # Lizenzdatei
├── monitoring/             # Monitoring-Setup
│   ├── docker-compose.yml
│   └── prometheus.yml
├── src/
│   └── main/
│       ├── java/summherum/
│       │   ├── model/      # Datenmodell
│       │   ├── service/    # Services
│       │   └── Main.java   # Einstiegspunkt & Routing
│       └── resources/public/   # Frontend
├── tests/                  # Automatisierte Tests
├── dockerfile              # Container-Definition der Anwendung
├── pom.xml                 # Maven-Konfiguration & Abhängigkeiten
└── README.md