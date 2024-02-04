package fr.hashtek.common.hashconfig.manager;

import fr.hashtek.common.hashconfig.exception.InstanceNotFoundException;
import io.github.cdimascio.dotenv.Dotenv;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;

public class ConfigManager
{

    private static ConfigManager instance = null;

    private Dotenv env = null;
    private YamlConfiguration yaml;
    private final String inputPath;
    private final String outputPath;


    /**
     * @param inputPath The ABSOLUTE path of the configuration file to load. !! WARNING !! The file must be present in the package (PluginName.jar).
     * @throws IOException If the plugin can't read the file
     */
    public ConfigManager(String inputPath, String outputPath, boolean withDotEnv) throws IOException
    {
        ConfigManager.instance = this;
        this.inputPath = inputPath;
        this.outputPath = outputPath;
        this.load(withDotEnv);
    }


    private void load(boolean withDotEnv) throws IOException
    {
        File configFile = this.createFileIfNotExists();

        this.yaml = YamlConfiguration.loadConfiguration(configFile);
        if (withDotEnv)
            this.env = Dotenv.load();
        else
            this.env = null;
    }

    /**
     * @throws IOException If the file configuration failed to load.
     */
    public void reload() throws IOException
    {
        this.load(this.env != null);
    }

    /**
     * @throws IOException If the configuration has failed to save.
     */
    public void saveConfig() throws IOException
    {
        this.yaml.save(this.outputPath);
    }

    private File createFileIfNotExists() throws IOException
    {
        File configFile = new File(this.outputPath);
        InputStream stream = null;

        if (configFile.exists())
            return configFile;

        if (!configFile.getParentFile().mkdirs() && !configFile.createNewFile())
            throw new IOException("Cannot create the default configuration file '"
                                  + configFile.getName()
                                  + "'.");

        stream = getClass().getResourceAsStream("/" + this.inputPath);
        if (stream == null)
            throw new IOException("The resource file '"
                                  + this.inputPath
                                  + "' cannot not be found in the jar file.");

        writeStreamToFile(stream, configFile);

        stream.close();
        return configFile;
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
            throw new InstanceNotFoundException("The instance cannot be found.");
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
            throw new InstanceNotFoundException("The instance cannot be found.");
        return instance.yaml;
    }

    /**
     * @throws InstanceNotFoundException If there is no instance available.
     * @return The last instance Dotenv.
     */
    public static Dotenv getEnv() throws InstanceNotFoundException
    {
        if (instance == null)
            throw new InstanceNotFoundException("The instance cannot be found.");
        return instance.env;
    }

}
