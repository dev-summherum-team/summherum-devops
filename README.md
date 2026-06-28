# 🌍 Reisetagebuch (DevOps Setup)

Willkommen im Projekt! Dieses Repository enthält das komplette Setup inklusive Docker-Container und MongoDB. Dank der Dev-Container-Konfiguration arbeiten wir alle in einer identischen Entwicklungsumgebung.

## 🚀 Schnellstart für Teammitglieder

1. **Voraussetzungen prüfen:** Ihr braucht Docker Desktop und VS Code mit der Erweiterung „Dev Containers“.
2.  **Projekt klonen:**
   `git clone https://github.com/an-otter/summherum-devops.git`
3. **In VS Code öffnen:**
   Öffnet den Ordner in VS Code.
4. **Container starten:**
   Klickt unten rechts auf **"Reopen in Container"**, wenn VS Code danach fragt. (Der erste Start kann ein paar Minuten dauern, da Maven alle Bibliotheken lädt).
5. **Setup testen:**
   Öffnet das Terminal im Container und tippt:
   `mvn test`
   Wenn alle Tests grün sind, ist dein System bereit!

---

## 🛠 Zusammenarbeit (Git Workflow)

Wir teilen uns den **Code**, aber jeder hat seine eigene **lokale Datenbank**. Wenn unser Tagebuch funktionstüchtig gebaut ist, können wir diese in die Cloud geben oder exportieren.

### So kommen Änderungen zu dir (Pull):
Bevor du anfängst zu arbeiten, hol dir immer den neuesten Stand vom Team:  
 
git pull

### So schickst du Änderungen ans Team (Push):

git add . \
git commit -m "Beschreibe hier kurz, was du gemacht hast" \
git push 


Java Code	🤝 Gemeinsam	Änderungen werden über GitHub geteilt. \
Infrastruktur	🔄 Identisch	Jeder nutzt exakt denselben Docker-Container (Java 21, Maven, MongoDB 7). \
Datenbank (Inhalt)	🏠 Lokal	Reisen, die du speicherst, landen in DEINER lokalen MongoDB. Dein Team sieht sie erst einmal nicht.

📁 Projektstruktur

    .devcontainer/: Die "Bauanleitung" für unsere Arbeitsumgebung.

    src/main/java/: Hier schreiben wir unseren Code.

    src/test/java/: Hier liegen die Tests.

    pom.xml: Die Verwaltung aller Java-Bibliotheken.