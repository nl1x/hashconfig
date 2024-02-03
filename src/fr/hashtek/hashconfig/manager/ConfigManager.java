package fr.hashtek.hashconfig.manager;

import fr.hashtek.hashconfig.exception.InstanceNotFoundException;
import io.github.cdimascio.dotenv.Dotenv;
import org.bspfsystems.yamlconfiguration.configuration.InvalidConfigurationException;
import org.bspfsystems.yamlconfiguration.file.YamlConfiguration;

import java.io.*;


public class ConfigManager
{

    private static ConfigManager instance = null;

    private Dotenv env = null;
    private YamlConfiguration yaml;
    private final String filepath;


    /**
     * @param filepath The ABSOLUTE path of the configuration file to load. !! WARNING !! The file must be present in the package (PluginName.jar).
     * @throws IOException If the plugin can't read the file
     * @throws InvalidConfigurationException If the configuration is invalid
     */
    public ConfigManager(String filepath, boolean withDotEnv) throws IOException, InvalidConfigurationException
    {
        ConfigManager.instance = this;
        this.filepath = filepath;
        this.load(withDotEnv);
    }


    private void load(boolean withDotEnv) throws IOException, InvalidConfigurationException
    {
        this.createFileIfNotExists();
        this.yaml = new YamlConfiguration();
        this.yaml.load(this.filepath);
        if (withDotEnv)
            this.env = Dotenv.load();
        else
            this.env = null;
    }

    /**
     * @throws IOException If the file configuration failed to load.
     * @throws InvalidConfigurationException If the new configuration file has an incorrect format.
     */
    public void reload() throws IOException, InvalidConfigurationException
    {
        this.load(this.env != null);
    }

    /**
     * @throws IOException If the configuration has failed to save.
     */
    public void saveConfig() throws IOException
    {
        this.yaml.save(this.filepath);
    }

    private void createFileIfNotExists() throws IOException
    {
        File configFile = new File(this.filepath);
        InputStream stream = null;

        if (configFile.exists()) return;

        if (!configFile.createNewFile())
            throw new IOException("Cannot create the default configuration file '"
                                  + configFile.getName()
                                  + "'.");

        stream = getClass().getResourceAsStream("/" + this.filepath);
        if (stream == null)
            throw new IOException("The default '"
                                  + this.filepath
                                  + "' file has not been found.");

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


    /**
     * @throws InstanceNotFoundException If there is no instance available.
     * @return The last instance created. !! WARNING !! If you create multiple instance of
     *           ConfigManager, then it returns only the last instance created.
     */
    public static ConfigManager getInstance() throws InstanceNotFoundException
    {
        if (instance == null)
            throw new InstanceNotFoundException();
        return instance;
    }

    /**
     * @throws InstanceNotFoundException If there is no instance available.
     * @return The last instance YAML configuration. !! WARNING !! If you create multiple instance
     *           of ConfigManager, then it returns only the last instance YAML configuration.
     */
    public static YamlConfiguration getYaml() throws InstanceNotFoundException
    {
        if (instance == null)
            throw new InstanceNotFoundException();
        return instance.yaml;
    }

    /**
     * @throws InstanceNotFoundException If there is no instance available.
     * @return The last instance Dotenv.
     */
    public static Dotenv getEnv() throws InstanceNotFoundException
    {
        if (instance == null)
            throw new InstanceNotFoundException();
        return instance.env;
    }

}
