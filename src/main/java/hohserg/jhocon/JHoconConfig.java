package hohserg.jhocon;

import com.github.dahaka934.jhocon.JHocon;
import com.github.dahaka934.jhocon.JHoconBuilder;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.FMLInjectionData;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

@Mod(modid = Main.MODID, name = Main.NAME, version = Main.VERSION)
public class Main {
    public static final String MODID = "jhocon";
    public static final String NAME = "JHocon";
    public static final String VERSION = "1.0";
    private static JHocon jhocon = new JHoconBuilder()
            .withComments()
            .registerDefaultValidators()
            .throwErrorOnValidationFail(true)
            .create();

    public static <Config> Config getOrCreateConfig(String modid, Supplier<Config> defaultConfig) {
        File root = (File) FMLInjectionData.data()[6];
        File configFile = new File(root.getAbsolutePath() + "/config/" + modid + ".cfg");
        Config config = defaultConfig.get();
        try {
            String lines = FileUtils.readFileToString(configFile, StandardCharsets.UTF_8);

            config = jhocon.<Config>fromHocon(lines, "config", config.getClass());

        } catch (IOException e) {
            e.printStackTrace();

            String hocon = jhocon.toHocon("config", config);
            try {
                FileUtils.writeStringToFile(configFile, hocon, StandardCharsets.UTF_8);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return config;
    }
}
