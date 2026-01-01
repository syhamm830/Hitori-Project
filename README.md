# 🎮 Hitori - Jeu de Puzzle Japonais

> **Projet académique** | Implémentation complète du jeu Hitori en Java avec JavaFX

---

## 📋 Table des Matières

- [À Propos](#-à-propos)
- [Règles du Jeu](#-règles-du-jeu)
- [Fonctionnalités](#-fonctionnalités)
- [Architecture](#-architecture)
- [Installation](#-installation)
- [Utilisation](#-utilisation)
- [Exemples de Grilles](#-exemples-de-grilles)
- [Tests](#-tests)
- [Technologies Utilisées](#-technologies-utilisées)
- [Limitations Connues](#-limitations-connues)
- [FAQ](#-faq)

---

## 🎯 À Propos

**Hitori** (ひとり, "seul" en japonais) est un jeu de logique où le joueur doit noircir certaines cases d'une grille pour respecter trois règles fondamentales. Ce projet implémente le jeu complet avec :

- ✅ **3 niveaux de difficulté** : Facile (5×5), Moyen (7×7), Difficile (9×9)
- ✅ **Interface graphique moderne** en JavaFX
- ✅ **Système de scores** avec top 10
- ✅ **Sauvegarde/Chargement** de parties
- ✅ **Validation en temps réel** des règles
- ✅ **Architecture MVC** stricte

### 🆕 Spécificités de cette Implémentation

- **Toutes les cases sont modifiables** : Cliquez sur n'importe quelle case pour la noircir/blanchir
- **Feedback instantané** : Validation immédiate si vous créez des cases noires adjacentes
- **Animations fluides** : Effets visuels modernes avec JavaFX
- **Pas de cases "fixes"** : Contrairement au Hitori classique, toutes les cases peuvent être modifiées

---

## 📐 Règles du Jeu

Le Hitori se joue sur une grille carrée remplie de chiffres. Le but est de **noircir certaines cases** pour que la grille respecte **trois règles obligatoires** :

### 1️⃣ **Pas de Doublons**
Aucune ligne ni colonne ne doit contenir de **chiffres identiques** parmi les cases **blanches** (non noircies).

**Exemple :**
```
Ligne : [1 2 3 2 5]
         ↑     ↑
    Il faut noircir l'un des deux "2"
```

### 2️⃣ **Pas de Noires Adjacentes**
Deux cases noircies ne peuvent **jamais** être côte à côte horizontalement ou verticalement.

**Exemple :**
```
❌ INTERDIT :        ✅ AUTORISÉ :
    ● ●                 ● □
    □ □                 □ ●
```

### 3️⃣ **Connexité des Blanches**
Toutes les cases **blanches** doivent former **un seul bloc connecté**. Il doit être possible de se déplacer d'une case blanche à n'importe quelle autre en ne passant que par des cases blanches.

**Exemple :**
```
❌ INTERDIT (2 îlots) :    ✅ AUTORISÉ (1 bloc) :
    □ □ ● □ □                  □ □ ● □ □
    □ □ ● □ □                  □ □ ● □ □
    ● ● ● ● ●                  □ ● ● ● □
    □ □ ● □ □                  □ □ ● □ □
    □ □ ● □ □                  □ □ ● □ □
```

---

## ✨ Fonctionnalités

### Jeu

- 🎮 **Interface intuitive** : Clic gauche pour noircir/blanchir
- ⏱️ **Chronomètre** : Temps de jeu enregistré
- 🎯 **Compteur de coups** : Nombre d'actions effectuées
- 💡 **Système d'indices** : Aide contextuelle
- ✅ **Validation temps réel** : Empêche les cases noires adjacentes
- 🎊 **Animation de victoire** : Confettis et célébration

### Gestion des Parties

- 💾 **Sauvegarde** : État complet (grille + temps + coups)
- 📂 **Chargement** : Reprise exacte de la partie
- 🔄 **Recommencer** : Reset de la grille
- ↩️ **Navigation** : Retour au menu fluide

### Scores

- 🏆 **Top 10** : Classement des meilleurs temps
- 📊 **Médailles** : 🥇🥈🥉 pour le podium
- 💾 **Persistance** : Scores dans `scores.txt`

---

## 🏗️ Architecture

Le projet suit une **architecture MVC stricte** :

```
📦 Hitori-Project
│
├── 📂 src/
│   ├── 📂 main/
│   │   ├── 📂 java/
│   │   │   ├── 📂 app/
│   │   │   │   └── Main.java
│   │   │   │       # Point d’entrée de l’application JavaFX.
│   │   │   │       # Initialise la fenêtre principale et lance le jeu.
│   │   │   │
│   │   │   ├── 📂 controller/
│   │   │   │   └── HitoriGame.java
│   │   │   │       # Contrôleur principal.
│   │   │   │       # Gère les actions du joueur : clics sur cases, reset, validation de la grille, suivi du chrono et du compteur de coups.
│   │   │   │
│   │   │   ├── 📂 model/
│   │   │   │   ├── Cell.java
│   │   │   │   │   # Représente une case individuelle de la grille.
│   │   │   │   │   # Stocke la valeur numérique et l’état (WHITE ou BLACK) et fournit des méthodes pour changer et vérifier l’état.
│   │   │   │   ├── Grid.java
│   │   │   │   │   # Représente la grille complète.
│   │   │   │   │   # Contient la logique de validation des 3 règles Hitori (doublons, cases noires adjacentes, connexité des blanches).
│   │   │   │   └── ScoreEntry.java
│   │   │   │       # Modèle pour une entrée de score.
│   │   │   │       # Stocke les informations d’un joueur : temps, nombre de coups, niveau, et permet la comparaison pour le Top 10.
│   │   │   │
│   │   │   ├── 📂 ui/
│   │   │   │   ├── GameUI.java
│   │   │   │   │   # Interface principale du jeu.
│   │   │   │   │   # Affiche la grille, gère les boutons, le chrono et les animations (victoire, confettis, feedback instantané).
│   │   │   │   ├── LevelSelectorUI.java
│   │   │   │   │   # Menu de sélection des niveaux.
│   │   │   │   │   # Permet de choisir Facile, Moyen ou Difficile et de lancer la grille correspondante.
│   │   │   │   └── ScoreBoardUI.java
│   │   │   │       # Interface d’affichage des scores.
│   │   │   │       # Affiche le Top 10, les médailles et permet de consulter les performances passées.
│   │   │   │
│   │   │   ├── 📂 util/
│   │   │   │   └── FileUtils.java
│   │   │   │       # Classe utilitaire pour la gestion des fichiers.
│   │   │   │       # Lecture/écriture des grilles, sauvegardes de parties, et stockage persistant des scores.
│   │   │   │
│   │   │   └── 📂 exception/
│   │   │       └── InvalidMoveException.java
│   │   │           # Exception métier levée lorsqu’un coup est invalide.
│   │   │           # Par exemple : deux cases noires adjacentes ou tentative de violation des règles Hitori.
│   │   │
│   │   └── 📂 resources/
│   │       ├── 📂 grids/
│   │       │   ├── grid_easy.txt
│   │       │   ├── grid_medium.txt
│   │       │   └── grid_hard.txt
│   │       │       # Fichiers texte contenant les grilles prédéfinies pour chaque niveau.
│   │       │       # Chaque ligne représente une rangée de la grille.
│   │       │
│   │       └── scores.txt
│   │           # Stockage persistant du Top 10 des scores.
│   │           # Mis à jour à chaque fin de partie et lu au lancement du jeu.
│   │
│   └── 📂 test/
│       └── 📂 java/
│           └── 📂 controller/
│               └── HitoriGameTest.java
│                   # Tests unitaires (JUnit 5) pour la logique du contrôleur.
│                   # Vérifie le basculement des cases, la validation des règles et le suivi du score.
│
├── pom.xml
│   # Configuration Maven du projet.
│   # Déclare les dépendances (JavaFX, JUnit) et les paramètres de build.
│
└── README.md
    # Documentation complète du projet (règles, installation, utilisation, architecture, roadmap, FAQ)

```

---

## 🚀 Installation

### Prérequis

- **Java JDK 21+**
- **Maven 3.6+**

### Installation Rapide

```bash
# 1. Cloner
git clone https://github.com/syhamm830/Hitori-Project.git

# 2. Compiler
mvn clean compile

# 3. Lancer
mvn javafx:run
```

## 🎮 Utilisation

### Démarrage

```bash
mvn javafx:run
```

### Niveaux Disponibles

| Niveau | Taille | Difficulté |
|--------|--------|-----------|
| 🌱 **FACILE** | 5×5 | Débutant |
| ⚡ **MOYEN** | 7×7 | Intermédiaire |
| 🔥 **DIFFICILE** | 9×9 | Expert |

### Commandes

- **Clic** : Noircir/Blanchir une case
- **Bouton ✓** : Vérifier la solution
- **Bouton 💾** : Sauvegarder la partie
- **Bouton ↻** : Recommencer
- **Bouton ←** : Retour au menu

---

## 📊 Exemples de Grilles

### Grille Facile (5×5)

**Problème :**
```
3 2 5 5 1
4 4 2 3 2
4 5 2 1 1
1 1 4 5 4
1 3 4 2 2
```

**Solution ● = case noire) :**
```
3 2 5 ● 1
● 4 ● 3 2
4 5 2 1 ●
● 1 ● 5 4
1 3 4 2 ●
```

---

## 🧪 Tests

### Lancer les tests

```bash
mvn test
```

### Tests Implémentés

- ✅ `testLoadGrid()` - Chargement des grilles
- ✅ `testToggleCellValid()` - Basculement d'état
- ✅ `testToggleCellInvalidAdjacentBlacks()` - Cases adjacentes
- ✅ `testGameWonInitiallyFalse()` - État initial
- ✅ `testResetGrid()` - Réinitialisation
- ✅ `testLoadDifferentLevels()` - Tous les niveaux
- ✅ `testGridValidation()` - Validation des règles
- ✅ `testCellStates()` - États des cellules

## 🛠️ Technologies Utilisées

- **Java 21** - Langage principal
- **JavaFX 21.0.2** - Interface graphique
- **Maven 3.11** - Build tool
- **JUnit 5.10.1** - Tests unitaires

### Dépendances Maven

```xml
<dependencies>
    <!-- JavaFX -->
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-controls</artifactId>
        <version>21.0.2</version>
    </dependency>
    
    <!-- JUnit 5 -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-api</artifactId>
        <version>5.10.1</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

---

## ⚠️ Limitations Connues

1. **Interface non responsive** : L'UI peut être coupée sur petits écrans
2. **Couleurs des boutons** : Peuvent ne pas s'afficher sur certains systèmes (problème CSS)
3. **Pas de génération aléatoire** : Grilles prédéfinies uniquement
4. **Pas de mode multijoueur** : Jeu solo uniquement

---

## ❓ FAQ

**Q : Pourquoi toutes les cases sont modifiables ?**  
R : C'est une variante de Hitori pour simplifier le gameplay. Le joueur a plus de liberté.

**Q : Les couleurs des boutons ne s'affichent pas ?**  
R : Problème connu avec JavaFX CSS. Les styles inline devraient résoudre ça dans les prochaines versions.

**Q : Puis-je créer mes propres grilles ?**  
R : Oui ! Créez un fichier `.txt` dans `resources/grids/` :
```
1 2 3 4 5
2 3 4 5 1
3 4 5 1 2
4 5 1 2 3
5 1 2 3 4
```

## 🎯 Roadmap

### ✅ Version 1.0 (Actuelle)
- Jeu fonctionnel 3 niveaux
- Système de scores
- Sauvegarde/Chargement
- Interface JavaFX moderne

### 🔜 Version 2.0
- Génération aléatoire de grilles
- Système d'indices intelligent
- Mode contre-la-montre
- Thèmes personnalisables
- Interface responsive

---

## 👥 Auteurs

- **Syrine Hammami** -

---

*Bon jeu ! 🎮✨*
