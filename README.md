# Potions Please
Server-side fabric mod for Minecraft 1.21.5 to alter vanilla potion durations by injecting the POTION_DURATION_SCALE Component.

## Potion Sources
This mod only affects potions generated through the following primary sources:
 - Brewing
 - Villager Trading
 - Loot Tables (set_potion function)
 - Tipped Arrow crafting

Potions that is created from any other source such as the /give command or the creative inventory do not have the durations modified.
This is because the mod is unable to inject the POTION_DURATION_SCALE Component to the items from those sources.

## Configuration
Opening the game for the first time generates a file to `config/potionsplease.json5`. Configuring this file is used to alter the potion durations.

The JSON keys match [minecraft's potion IDs](https://minecraft.wiki/w/Potion#Item_data) and the next 4 values define the duration in **TICKS** for each item type.
 - DEFAULT_DURATION -> Potion
 - SPLASH_DURATION -> Splash Potion
   - *(Vanilla splash potions have the same duration as regular potions)*
 - LINGER_DURATION -> Lingering Potion
   - *(Vanilla lingering potions only apply 25% of the regular potion duration)*
 - TIPPED_DURATION -> Tipped Arrow
   - *(Vanilla tipped arrows only apply 12.5% of the regular potion duration)*
```json
{
  "minecraft:long_strength" : {
    "DEFAULT_DURATION" : 9600,
    "SPLASH_DURATION" : 9600,
    "LINGER_DURATION" : 2400,
    "TIPPED_DURATION" : 1200
  }
}
```
The above example uses the vanilla long_strength (Extended strength) potion durations. In this case drinking the potion regularly will grant the player strength for 8 minutes.
Maybe this isn't long enough so we can change the values to following to instead have default potion grant 45 minutes of strength.
To calculate the number of ticks for the potion duration, you can simply follow this equation: <mark>MINUTES × 60<sub>(seconds in a minute)</sub> × 20<sub>(ticks per second)</sub></mark>
```json
{
  "minecraft:long_strength" : {
    "DEFAULT_DURATION" : 54000, // 45 × 60 × 20 = 45 minutes in ticks
    "SPLASH_DURATION" : 18000, // 15 × 60 × 20 = 15 minutes in ticks
    "LINGER_DURATION" : 9600, // 8 × 60 × 20 = 8 minutes in ticks
    "TIPPED_DURATION" : 3600 // 3 × 60 × 20 = 3 minutes in ticks
  }
}
```

Removing a potion from the config disables the duration injection logic for that potion.
