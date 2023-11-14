# Overlay

Overlay ajoute un bouton "Report" qui permet au joueur de soumettre un bug lorsqu'il en découvre un.
Une fois ce bouton cliqué, un enregistrement vidéo des dernières secondes est enregistré ainsi qu'un enregistrement de la voix pour voir la réaction du joueur. Une fois l'enregistrement terminé, l'utilisateur aura la possibilité d'ajouter un titre et une description de son bug.

## Prérequis

Pour utiliser cette application, vous devez disposer des composants suivants installés sur votre système :

### Java

- **Version :** Java 17
- **Téléchargement :** [JDK 17 - Oracle](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- **Instructions :** Suivez les instructions sur le site Web pour télécharger et installer Java 17 sur votre système d'exploitation.

### FFmpeg

- **Utilisation :** FFmpeg est utilisé pour l'enregistrement et le traitement de l'audio.
- **Téléchargement :** [FFmpeg Builds - GitHub](https://github.com/BtbN/FFmpeg-Builds/releases)
- **Instructions :** Téléchargez la dernière version de FFmpeg pour votre système d'exploitation. Décompressez le fichier téléchargé et assurez-vous que les exécutables FFmpeg sont accessibles dans le chemin de votre système (`PATH`).

## Installation de l'application

Pour installer l'application sur votre système il vous suffit de télécharger la version [release](https://github.com/NoxiiT/Overlay-Report/releases/latest) de l'application et de l'installer sur votre système.

## Utilisation

Une fois l'installation de l'application terminée vous pouvez lancer l'application à l'aide de cette commande :

```bash
java -jar Overlay.jar
```