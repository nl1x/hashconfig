# ⚙️ HashConfig - How to use
## Class prototype
```java
HashConfig(String resourcePath, String outputPath, boolean withDotEnv);
```
### Parameters:
- `String resourcePath`: The path of the <config>.yml file in the JAR archive.
- `String outputPath`: The path of the <config>.yml file in the server.
    - **‼️ WARNING ‼️** The relative directory is the directory where you started the server.
- `boolean withDotEnv`: `true` to load the `.env` file, otherwise `false`.

### Exceptions
- `IOException`: If the load of the <config>.yml file failed, or the resource file cannot be found, or the default configuration file cannot be created.

## Methods
### 🔄 Reload
```java
instance.reload();
```
### Exceptions
- `IOException`: If the load of the <config>.yml file failed, or the resource file cannot be found, or the default configuration file cannot be created.

----

### 💾 Save
```java
instance.save();
```
### Exceptions
- `IOException`: If the load of the <config>.yml file cannot be saved.

----

### 🔍 Getter
```java
instance.getInstance();
instance.getYaml();
instance.getEnv(); // See Warning section.
```
### Exception
- `InstanceNotFoundException`: If their is no instance of the HashConfig.

### ‼️ Warning
- `instance.getEnv()`: This return null if `withDotEnv` has been set to false in the constructor.