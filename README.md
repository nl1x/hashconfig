# `⚙️ HashConfig v0.0.1 - Guide d'utilisation`

## Description de la librairie
Cette librairie est faîte pour manipuler des fichiers de configuration ainsi que des .env plus facilement.

---

## `HashConfig`

### Prototype
```java
HashConfig(String resourcePath, String outputPath, boolean withDotEnv);
```

### Description
Classe principale permettant de charger et manipuler un fichier de configuration facilement.

### Paramètres
`String resourcePath`: Le chemin du fichier de configuration se trouvant dans votre `.jar`. *(Appelé ressource)*
`String outputPath`: Le chemin vers la sauvegarde locale du fichier de configuration.
`boolean withDotEnv`: Si il faut charger le fichier d'environnement ou non.

- `⚠️` **Le fichier de configuration ne sera pas chargé/recréé depuis les ressources si il existe déjà en local. Il sera uniquement chargé depuis le fichier local.**

---

## `Exemple`

### Fichiers utilisés pour les exemples qui suivent

**Structure du serveur**
```
server/
├─ ...
├─ plugins/
│  ├─ TonPlugin.jar
├─ ...
```

**Structure du .jar**
```
TonPlugin.jar/
├─ fr/
│  ├─ .../
├─ configuration_file/
│  ├─ config.yml
```

**Fichier de configuration: `config.yml`:**
```yaml
users:
    1:
        username: L1x
        password: 1234
    2:
        username: Epitoch
        password: 5678
```

**Fichier de variable d'environnement: `.env`:**
```env
TOKEN=YOUR_TOKEN
```

---

### Codes d'exemple

**Modifier / Accéder aux valeurs du fichier de configuration:**
```java
HashConfig config = new HashConfig("configuration_file/config.yml", "plugins/TonPlugin/config.yml", false);
YamlFile yaml = config.getYaml();

String username1 = yaml.getString("users.1.username");
System.out.println(username1); // Affiche "L1x"

yaml.set("users.1.password", "9101112"); // Définition d'un nouveau mot de passe pour L1x.
config.save(); // Sauvegarde du fichier de configuration après l'avoir modifié.
```
> Nouveau fichier de configuration après l'exécution de ce code:
```yaml
users:
    1:
        username: L1x
        password: 9101112
    2:
        username: Epitoch
        password: 5678
```

---

**Accéder aux variables d'environnement:**
```java
HashConfig config = new HashConfig("...", "...", true);
Dotenv env = config.getEnv();
String token = env.get("TOKEN");

System.out.println("Token: " + token); // Affiche "YOUR_TOKEN"
```

---

**Modification manuelle du fichier de configuration / Comment reload ?**

Si vous avez modifié votre fichier de configuration à la main et que vous souhaitez le recharger sans pour autant devoir restart le serveur, vous pouvez utiliser la méthode suivante :
```java
HashConfig config = new HashConfig(...);
config.reload();
```

---

# `⚠️ RAPPEL DES WARNINGS`
- Le fichier de configuration ne sera pas chargé/recréé depuis les ressources si il existe déjà en local. Il sera uniquement chargé depuis le fichier local.
