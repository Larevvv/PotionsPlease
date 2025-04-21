package lare.potionsplease;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import static lare.potionsplease.PotionsPlease.DATA_PATH;
import static lare.potionsplease.PotionsPlease.LOGGER;

public class PotionConfigModel {

    // Map of potion effect duration overrides.
    public Map<String, DurationModel> Potions = new HashMap<>(){{
        // Default to vanilla durations.
        Registries.POTION.streamEntries().filter((ref) -> !ref.value().getEffects().isEmpty()).forEach((ref) -> {
            put(ref.getIdAsString(), new DurationModel(ref.value().getEffects().getFirst().getDuration()));
        });
    }};

    public static class DurationModel {
        @JsonProperty("DEFAULT_DURATION")
        public int DEFAULT_DURATION;

        @JsonProperty("SPLASH_DURATION")
        public int SPLASH_DURATION;

        @JsonProperty("LINGER_DURATION")
        public int LINGER_DURATION;

        @JsonProperty("TIPPED_DURATION")
        public int TIPPED_DURATION;

        // Used by the Object mapper to load the JSON data.
        public DurationModel(@JsonProperty("DEFAULT_DURATION") int DEFAULT_DURATION, @JsonProperty("SPLASH_DURATION") int SPLASH_DURATION, @JsonProperty("LINGER_DURATION") int LINGER_DURATION, @JsonProperty("TIPPED_DURATION") int TIPPED_DURATION) {
            this.DEFAULT_DURATION = Math.max(DEFAULT_DURATION, 1);
            this.SPLASH_DURATION = Math.max(SPLASH_DURATION, 1);
            this.LINGER_DURATION = Math.max(LINGER_DURATION, 1);
            this.TIPPED_DURATION = Math.max(TIPPED_DURATION, 1);
        }

        public DurationModel(int DEFAULT_DURATION) {
            this.DEFAULT_DURATION = Math.max(DEFAULT_DURATION, 1);
            this.SPLASH_DURATION = Math.max(DEFAULT_DURATION, 1);
            // Vanilla lingering potions have 0.25 potion_duration_scale.
            this.LINGER_DURATION = Math.max(Math.round(DEFAULT_DURATION * 0.25F), 1);
            // Vanilla tipped arrows have 0.125 potion_duration_scale.
            this.TIPPED_DURATION = Math.max(Math.round(DEFAULT_DURATION * 0.125F), 1);
        }
    }

    public PotionConfigModel() {

        ObjectMapper mapper = new ObjectMapper();
        if (DATA_PATH.toFile().exists()) {
            try {
                FileInputStream inputStream = new FileInputStream(DATA_PATH.toFile());
                byte[] data = inputStream.readAllBytes();
                Potions = mapper.readValue(data, new TypeReference<HashMap<String, DurationModel>>() {});
            } catch (Exception e) {
                LOGGER.warn("Failed to load PotionsPlease config file.");
                LOGGER.error(e.toString());
            }
        } else {
            try {
                byte[] value = mapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(Potions);
                FileOutputStream outputStream = new FileOutputStream(DATA_PATH.toFile());
                outputStream.write(value);
            } catch (Exception e) {
                LOGGER.warn("Failed to create PotionsPlease config file.");
                LOGGER.error(e.toString());
            }
        }
    }

    public void ApplyPotionDuration(ItemStack stack, RegistryEntry<Potion> potion) {
        DurationModel potionOverride = Potions.get(potion.getIdAsString());
        if (potionOverride != null) {
            Item item = stack.getItem();

            int duration = potion.value().getEffects().getFirst().getDuration();
            int targetDuration = getTargetDuration(potionOverride, item);

            float scale = (float) Math.max(targetDuration, 1) / duration;
            stack.set(DataComponentTypes.POTION_DURATION_SCALE, scale);
        }
    }

    private static int getTargetDuration(DurationModel potionOverride, Item item) {
        int targetDuration = potionOverride.DEFAULT_DURATION;

        // TODO: Could change duration class to be another hashmap with item IDs being keys.
        //  This could potentially support modded stuff but this is not a priority to me.

        if (item == Items.SPLASH_POTION) {
            targetDuration = potionOverride.SPLASH_DURATION;
        } else if (item == Items.LINGERING_POTION) {
            targetDuration = potionOverride.LINGER_DURATION;
        } else if (item == Items.TIPPED_ARROW) {
            targetDuration = potionOverride.TIPPED_DURATION;
        }
        return targetDuration;
    }
}