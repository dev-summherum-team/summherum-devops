Summherum – Digitales Reisetagebuch 🐝

DevOps-Semesterprojekt: ein web-basiertes, digitales Reisetagebuch. Dieses Repository enthält den Anwendungscode sowie die vollständige Entwicklungsumgebung. Die weiteren Details sind in der Projektdokumentation beschrieben.

Struktur:
.
├── .devcontainer/          # Entwicklungsumgebung
│   ├── devcontainer.json
│   └── docker-compose.yml
├── .github/                # CI/CD-Pipeline
├── monitoring/             # Monitoring-Setup 
│   ├── docker-compose.yml
│   └── prometheus.yml
├── src/
│   ├── main/
│   │   ├── java/summherum/
│   │   │   ├── model/      # Datenmodell 
│   │   │   ├── service/    # Services 
│   │   │   └── Main.java   # Einstiegspunkt & Routing
│   │   └── resources/public/   # Frontend 
│   └── test/java/summherum/    # Automatisierte Tests
├── dockerfile              # Container-Definition der Anwendung
├── pom.xml                 # Maven-Konfiguration & Abhängigkeiten
└── README.md