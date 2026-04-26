# Installation du projet

Ce guide décrit l'installation et l'exécution locale de l'API **Concert Ticketing**.

## 1. Prérequis

- **Java 15** (le projet compile en source/target 15)
- **Maven 3.8+**
- **Git**
- Système: Windows, Linux ou macOS

Vérification rapide:

```bash
java -version
mvn -version
```

## 2. Récupérer le projet

```bash
git clone <url-du-repo>
cd JaxRSOpenAPI
```

## 3. Comprendre le mode base de données

Le projet utilise HSQLDB (mode serveur) en local.

Configuration JPA actuelle (fichier `src/main/resources/META-INF/persistence.xml`):

- `persistence-unit dev`: `drop-and-create`
- `persistence-unit prod`: `update`

Conséquence:

- Avec `dev`, le schéma est recréé au démarrage.
- Les données de démonstration sont injectées automatiquement via `DataInitializer`.

## 4. Lancer en une commande

### Windows

```powershell
.\run-all.bat
```

Ce script:

1. copie les dépendances Maven si besoin,
2. démarre le serveur HSQLDB,
3. ouvre le client graphique HSQLDB,
4. lance `mvn jetty:run`.

### Linux/macOS

```bash
chmod +x run-all.sh
./run-all.sh
```

## 5. Lancement manuel (alternative)

### étape A - Copier les dépendances

```bash
mvn dependency:copy-dependencies
```

### étape B - Démarrer HSQLDB server

```bash
# depuis la racine du projet
mkdir -p data
cd data
java -cp ../target/dependency/hsqldb-2.7.2.jar org.hsqldb.Server
```

### étape C - Ouvrir le client HSQLDB (optionnel)

Depuis la racine du projet:

```bash
java -cp ./target/dependency/hsqldb-2.7.2.jar org.hsqldb.util.DatabaseManagerSwing --driver org.hsqldb.jdbcDriver --url jdbc:hsqldb:hsql://localhost/ --user SA
```

### étape D - Démarrer l'API

```bash
mvn jetty:run
```

## 6. URLs utiles

- API: `http://localhost:8080`
- OpenAPI JSON: `http://localhost:8080/openapi.json`
- Swagger UI: `http://localhost:8080/api/docs`

## 7. Comptes seed (DataInitializer)

Comptes générés automatiquement en dev:

- Admin: `admin0@test.xyz` / `password0`
- Organizer: `organizer0@test.xyz` / `password0`
- Customer: `customer0@test.xyz` / `password0`

Le mot de passe est stocké hashé (BCrypt), mais la valeur de connexion reste celle ci-dessus.

## 8. Authentification JWT

1. Appeler `POST /api/auth/login`
2. Récupérer le token JWT
3. Ajouter l'en-tête:

```http
Authorization: Bearer <token>
```

Optionnel: définir une clé JWT stable via variable d'environnement:

```bash
# Linux/macOS
export JWT_SECRET="ma-cle-jwt-super-secrete"

# Windows PowerShell
$env:JWT_SECRET="ma-cle-jwt-super-secrete"
```

## 9. Vérifier la compilation

```bash
mvn -DskipTests compile
```

## 10. Dépannage rapide

### Port 8080 déjà utilisé

- Arrêter le processus qui utilise le port
- ou changer le port dans `pom.xml` (configuration `jetty-maven-plugin`)

### Swagger inaccessible

Vérifier:

- `http://localhost:8080/openapi.json`
- `http://localhost:8080/api/docs`

### Erreur base non joignable

Vérifier que HSQLDB server tourne avant `mvn jetty:run`.

## 11. Arrêt

- Arrêter Jetty: `Ctrl+C` dans le terminal
- Fermer les fenètres HSQLDB server/browser
