package lare.potionsplease;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class PotionsPlease implements ModInitializer {
	public static final String MOD_ID = "potionsplease";
	public static final Path DATA_PATH = FabricLoader.getInstance().getConfigDir().resolve("potionsplease.json5");
	public static final PotionConfigModel CONFIG = new PotionConfigModel();
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {}
}