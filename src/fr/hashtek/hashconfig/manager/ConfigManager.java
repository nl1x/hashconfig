package fr.hashtek.hashconfig.manager;

import io.github.cdimascio.dotenv.Dotenv;
import org.bspfsystems.yamlconfiguration.configuration.InvalidConfigurationException;
import org.bspfsystems.yamlconfiguration.file.YamlConfiguration;

import java.io.*;


public class ConfigManager
{

    private static ConfigManager instance;

    private Dotenv env;
    private YamlConfiguration yaml;
    private final String filepath;


    public ConfigManager(String filepath) throws IOException, InvalidConfigurationException
    {
        ConfigManager.instance = this;
        this.filepath = filepath;
        this.load();
    }


    public void load() throws IOException, InvalidConfigurationException
    {
        this.createFileIfNotExists();
        this.env = Dotenv.load();
        this.yaml = new YamlConfiguration();
        this.yaml.load(this.filepath);
    }

    public void reload() throws IOException, InvalidConfigurationException
    {
        this.load();
    }

    public void saveConfig() throws IOException
    {
        this.yaml.save(this.filepath);
    }

    private void createFileIfNotExists() throws IOException
    {
        File configFile = new File("config.yml");
        InputStream stream = null;

        if (configFile.exists()) return;

        if (!configFile.createNewFile())
            throw new IOException("Cannot create the default configuration file.");

        stream = getClass().getResourceAsStream("/" + this.filepath);
        if (stream == null)
            throw new IOException("The default 'config.yml' file has not been found.");

        writeStreamToFile(stream, configFile);

        stream.close();
    }

    private void writeStreamToFile(InputStream stream, File file) throws IOException
    {
        FileWriter writer = new FileWriter(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        BufferedWriter writerBuffer = new BufferedWriter(writer);
        String line = null;

        while ((line = reader.readLine()) != null) {
            writerBuffer.write(line);
            writerBuffer.newLine();
        }

        writerBuffer.close();
        writer.close();
        reader.close();
    }


    public static ConfigManager getInstance()
    {
        return instance;
    }

    public static YamlConfiguration getYaml()
    {
        return instance.yaml;
    }

    public static Dotenv getEnv()
    {
        return instance.env;
    }

}
