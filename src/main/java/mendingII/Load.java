package mendingII;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Load implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("Mending II");

    @Override
    public void onInitialize() {
        LOGGER.info("Mending II loaded");
    }
}
