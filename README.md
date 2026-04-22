
# Soulless

Soulless is a NeoForge mod for Minecraft 1.21.1.

## Features

- Adds new items and equipment:
  - **Lost Souls** (`soulless:lost_souls`): Dropped by undead mobs when killed by a player.
  - **Bat** (`soulless:bat`): A wooden melee weapon with extra knockback. Is used for the "Home Run" advancement.
  - **Fireball** (`soulless:fireball`): Throwable item that launches a custom fireball entity, similar to a ghast fireball.
  - **Ghostpowder** (`soulless:ghostpowder`): Crafting material.
  - **Ghost Heart** (`soulless:ghost_heart`): Crafting material.
  - **Soulsteel Ingot** (`soulless:soulsteel_ingot`): Crafting material.
  - **Soulsteel Block** (`soulless:soulsteel_block`): New block.
  - **Soul Reaper** (`soulless:soul_reaper`): Doubles Lost Souls drops and deals double damage to undead mobs.
  - **Ghost Orb** (`soulless:ghost_orb`): Throwable no-gravity projectile with 15s cooldown, applies Poison, Blindness, and Glowing in a 5-block radius for 15s, spawns soul particles, plays wither-skull sounds, and converts blocks by hardness rules.
  - **RIP** (`soulless:rip`): TNT-like block crafted from Nether Star, TNT, and Soulsteel Blocks. Manual ignition only, triggers a 50-block Soul Burst, applies effects, and drops Ghost Heart.
  - **Undead Core** (`soulless:undead_core`): Crafted from Ghost Heart, Obsidian, and Diamonds. Pulses every 2 seconds in a 50-block radius, applies effects to undead, and emits particles and sound.
  - **Boneyard** (`soulless:boneyard`): Spawns a skeleton when players approach in low block light, can spawn bow or stone-sword skeletons, drops bones when broken without Silk Touch.

- Adds custom crafting progression:
  - Lost Souls conversion recipes
  - Ghostpowder and Soulsteel crafting recipes
  - Soulsteel block packing/unpacking (9 <-> 1)
  - Soul Reaper and Bat crafting recipes

- Adds custom advancements:
  - **Soulless**: Obtain Lost Souls.
  - **(Don't Fear) The Reaper**: Use the Soul Reaper to kill undead.
  - **The Orb of Dreamers**: Use the Ghost Orb.
  - **The GhostLands**: Detonate a RIP block.
  - **Deathwish**: Place the Undead Core.
  - **Home Run**: Deflect a fireball with a bat.

- Adds worldgen and structures:
  - **Ruins**: New structure with three variants (small, tiny, medium), spawns in temperate land biomes, uses Bone Chest loot, and avoids ocean-surface generation.

- Adds a custom creative tab for all Soulless items.

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
