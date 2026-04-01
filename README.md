
# Soulless

Soulless is a NeoForge mod for Minecraft 1.21.1.

## Features

- Adds a new item: Lost Souls (`soulless:lost_souls`).
- Undead mobs drop Lost Souls when killed by a player.
- Adds new materials and equipment:
	- Ghostpowder (`soulless:ghostpowder`)
	- Soulsteel Ingot (`soulless:soulsteel_ingot`)
	- Soulsteel Block (`soulless:soulsteel_block`)
	- Soul Reaper (`soulless:soul_reaper`)
- Adds custom crafting progression:
	- Lost Souls conversion recipes
	- Ghostpowder and Soulsteel crafting recipes
	- Soulsteel block packing/unpacking (9 <-> 1)
	- Soul Reaper crafting recipe
- Soul Reaper special effects:
	- Doubles Lost Souls drops from undead kills
	- Deals double damage to undead mobs
- Adds custom advancements:
	- Soulless
	- (Don't Fear) The Reaper

## Project Info

- Mod ID: `soulless`
- Name: `Soulless`
- Version: `1.0.1`
- Minecraft: `1.21.1`
- NeoForge: `21.1.222`
- Group: `com.midnightsp.soulless`
- Author: `MidnightSP`

## Development

Use the Gradle wrapper from the project root.

### Build

```powershell
./gradlew build
```

### Run Client (Dev)

```powershell
./gradlew runClient
```

### Generate Data (Recipes/Advancements)

```powershell
./gradlew runData
```

Generated files are written to:

- `src/generated/resources`

## Notes

- The project uses Mojang official mappings.
- If your IDE cache gets out of sync, try:

```powershell
./gradlew --refresh-dependencies
./gradlew clean
```

## Resources

- NeoForged Docs: https://docs.neoforged.net/
- NeoForged Discord: https://discord.neoforged.net/
