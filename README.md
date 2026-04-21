Voraussetzungen prüfen: Ihr braucht Docker Desktop und VS Code mit der Erweiterung „Dev Containers“.

Projekt klonen: Öffnet ein Terminal auf eurem PC und tippt:
git clone https://github.com/an-otter/summherum-devops.git

In VS Code öffnen: Startet VS Code und öffnet den frisch geklonten Ordner.

Der magische Klick: VS Code wird unten rechts fragen: „Folder contains a Dev Container configuration. Reopen in Container?“ -> Klickt auf „Reopen in Container“.

Warten: Der Computer baut jetzt im Hintergrund genau deinen Container nach.

Testen: Öffnet das Terminal in VS Code und tippt mvn test. Wenn es grün wird, sind sie 1:1 auf deinem Stand.
