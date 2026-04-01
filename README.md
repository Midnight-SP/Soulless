
# Soulless

Soulless is a NeoForge mod for Minecraft 1.21.1.

## Features

- Adds a new item: Lost Souls (`soulless:lost_souls`).
- Undead mobs drop Lost Souls when killed by a player.
- Adds six shapeless crafting recipes that use Lost Souls.

## Project Info

- Mod ID: `soulless`
- Name: `Soulless`
- Version: `1.0.0`
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
