# GUIDE D'IMPORT - BASE DE DONNÉES NOMAD

## Comment importer la base de données dans MySQL

Suivez ces étapes simples pour importer le fichier `Nomad.sql` dans votre serveur MySQL.

---

## Import via SQL Workbench

### Étape 1: Ouvrir SQL Workbench

1. Lancez **SQL Workbench**
2. Vous devriez voir l'écran principal avec les onglets de connexion

### Étape 2: Vous connecter au serveur MySQL

**En haut de l'écran:**

```
File → New Connection
```

Ou si vous avez déjà une connexion:

```
Clic droit sur la connexion existante → Reconnect
```

**Configuration (si première fois):**
- **Driver:** MySQL
- **Server:** localhost
- **Port:** 3306
- **Username:** root
- **Password:** [Votre mot de passe MySQL]

Cliquez `OK` pour vous connecter.

### Étape 3: Naviguer vers "Server" et Importer le script SQL

**Dans le menu principal:**

```
Server → Data Import -> Import from self-contained file
```

✅ **C'EST BON ! La base est importée !**

---

## ✔️ Vérifier que tout fonctionne

### Dans SQL Workbench:

**Exécutez cette commande:**

```sql
USE nomad;
SHOW TABLES;
```

**Résultat attendu:**
```
applications
contracts
educations
freelancers
freelancers_skills
missions
missions_required_skills
portfolios
profiles
profiles_skills
recruiters
reviews
skills
users
work_experiences
```

**Et voila**.
