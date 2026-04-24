# E-Learning Platform - Spring Boot Backend

Plateforme complète d'apprentissage en ligne développée avec **Spring Boot 3.2.0** et **MySQL 8.0**

## 🎯 Fonctionnalités

- ✅ Authentification JWT
- ✅ Gestion des utilisateurs et rôles
- ✅ Gestion des cours
- ✅ Système de notation
- ✅ API REST documentée avec Swagger
- ✅ Sécurité avec Spring Security
- ✅ Validation des données
- ✅ Envoi d'emails

## 📋 Prérequis

- **Java 17+**
- **Maven 3.8+**
- **MySQL 8.0+**
- **Spring Tools Suite (STS)** ou tout autre IDE compatible

## ⚙️ Configuration MySQL

### 1. Démarrer MySQL

**Windows:**
```bash
net start MySQL80
```

**Linux/Mac:**
```bash
brew services start mysql@8.0
# ou
sudo systemctl start mysql
```

### 2. Configuration de la base de données

#### Option A : Laissez Spring créer la base de données automatiquement
La base `elearning` sera créée automatiquement au démarrage.

#### Option B : Créer manuellement
```sql
CREATE DATABASE elearning CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE elearning;
```

### 3. Configuration des identifiants

Le fichier `src/main/resources/application.yml` contient :
```yaml
Username: root
Password: 123456
Database: elearning
Port: 3306
Host: localhost
```

**Pour modifier les identifiants**, éditez le fichier `application.yml`.

## 🚀 Démarrer le projet

### Option 1 : Avec Maven (Terminal)

```bash
# Compiler et télécharger les dépendances
mvn clean install

# Lancer l'application
mvn spring-boot:run
```

### Option 2 : Avec Spring Tools Suite

1. Ouvrez le projet dans **STS**
2. Clic droit sur le projet → **Run As** → **Spring Boot App**
3. Vérifiez la console pour les messages de démarrage

### Option 3 : Build et exécution JAR

```bash
mvn clean package
java -jar target/elearning-platform-1.0.0.jar
```

## ✅ Vérifier la connexion

Après le démarrage, vérifiez dans la console :

```
HikariPool-1 - Starting...
HikariPool-1 - Start completed.
E-Learning Platform started successfully!
```

Si vous voyez ces messages, la connexion à MySQL est établie ! ✅

## 📍 URLs d'accès

- **API Backend** : `http://localhost:8080/api`
- **Documentation Swagger/OpenAPI** : `http://localhost:8080/api/swagger-ui.html`
- **Base de données** : `localhost:3306/elearning`

## 📁 Structure du projet

```
com.elearning
├── ElearningPlatformApplication.java    # Classe principale
├── controller/                          # REST Controllers
├── service/                             # Logique métier
├── repository/                          # Accès données (JPA)
├── model/                               # Entités JPA
├── dto/                                 # Data Transfer Objects
├── security/                            # Authentification JWT
├── config/                              # Configurations
└── exception/                           # Gestion d'erreurs
```

## 🔧 Dépendances principales

- **Spring Boot Web** : API REST
- **Spring Data JPA** : Accès base de données
- **Spring Security** : Authentification/Autorisation
- **JWT (jjwt)** : Tokens d'authentification
- **MySQL Connector** : Driver MySQL
- **Lombok** : Réduction du code boilerplate
- **ModelMapper** : Mappage DTO/Entity
- **Validation** : Validation des données
- **Swagger/OpenAPI** : Documentation API interactive
- **Spring Mail** : Envoi d'emails

## 🐛 Dépannage

### "Connection refused" ou "Can't connect to MySQL"

✓ Vérifiez que MySQL est en cours d'exécution
✓ Vérifiez le port (3306)
✓ Vérifiez les identifiants (root:123456)

### "Database 'elearning' not found"

✓ La base sera créée automatiquement au premier démarrage
✓ Ou créez-la manuellement avec le SQL ci-dessus

### Port 8080 déjà utilisé

Modifiez le port dans `application.yml` :
```yaml
server:
  port: 8081
```

## 📝 Logs

Les logs sont configurés pour afficher :
- Les requêtes SQL
- Les requêtes HTTP
- Les erreurs de sécurité

Niveau de log par défaut : **INFO** (changez en DEBUG pour plus de détails)

## 🔐 Sécurité

- JWT pour l'authentification
- Spring Security pour l'autorisation
- Validation des données en entrée
- CORS configuré pour le frontend

## 📚 Ressources utiles

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA Guide](https://spring.io/projects/spring-data-jpa)
- [Spring Security Documentation](https://spring.io/projects/spring-security)
- [MySQL Documentation](https://dev.mysql.com/doc/)

## 👨‍💻 Développement

### Ajouter une nouvelle entité

1. Créez une classe dans `model/`
2. Annotez avec `@Entity` et `@Table`
3. Créez une interface Repository
4. Créez un Service et Controller
5. Restart l'application

### Utiliser Swagger

Visitez `http://localhost:8080/api/swagger-ui.html` pour :
- Voir tous les endpoints
- Tester les requêtes API
- Consulter la documentation

## 📄 Licence

Projet sous licence MIT

## ✉️ Support

Pour toute question ou problème, veuillez créer une issue.
