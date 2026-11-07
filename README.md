
# Soulless

Soulless is a NeoForge mod for Minecraft 1.21.1.

## Features

- Adds new items and equipment:
  - **Lost Souls** (`soulless:lost_souls`): Dropped by undead mobs when killed by a player.
  - **Gold Souls** (`soulless:gold_souls`): Crafting material.
  - **Soulpowder** (`soulless:soulpowder`): Crafting material. Right-click releases a burst of blinding soul dust in a 5-block radius (5-second Blindness on nearby entities).
  - **Bat** (`soulless:bat`): A wooden melee weapon with extra knockback. Is used for the "Home Run" advancement.
  - **Fireball** (`soulless:fireball`): Throwable explosive that can also be fired from Dispensers for automated defense turrets.
  - **Ghostpowder** (`soulless:ghostpowder`): Crafting material and brewing ingredient for Potion of Invisibility.
  - **Ghost Heart** (`soulless:ghost_heart`): Usable artifact. Right-click surges with spectral vitality, granting Health Boost IV and Regeneration II for 45 seconds (60-second cooldown).
  - **Soulsteel Ingot** (`soulless:soulsteel_ingot`): Crafting material.
  - **Soulsteel Block** (`soulless:soulsteel_block`): New block.
  - **Soul Reaper** (`soulless:soul_reaper`): Doubles Lost Souls drops and deals double damage to undead mobs.
  - **Ghost Orb** (`soulless:ghost_orb`): Throwable no-gravity projectile with 15s cooldown, applies Poison, Blindness, and Glowing in a 5-block radius for 15s, spawns soul particles, plays wither-skull sounds, and converts blocks by hardness rules.
  - **Monstrous Stew** (`soulless:monstrous_stew`): A strong food item that restores a lot of hunger, returns a bowl, and gives Nausea.
  - **Return Token** (`soulless:return_token`): Teleports you to your spawn point or world spawn and has a long cooldown.
  - **RIP** (`soulless:rip`): TNT-like block crafted from Nether Star, TNT, and Soulsteel Blocks. Manual ignition only, triggers a 50-block Soul Burst, applies effects, and drops Ghost Heart.
  - **Undead Core** (`soulless:undead_core`): Crafted from Ghost Heart, Obsidian, and Diamonds. Pulses every 2 seconds in a 50-block radius, applies effects to undead, and emits particles and sound.
  - **Boneyard** (`soulless:boneyard`): Spawns a skeleton when players approach in low block light, can spawn bow or stone-sword skeletons, drops bones when broken without Silk Touch.
  - **Ritual Blade** (`soulless:ritual_blade`): Weak weapon that can be used to perform a ritual - you hurt yourself to get Strength II and Speed II for 30 seconds, blade transforms into Bloodtaker Blade. Transmutes off-hand catalysts (Diamond -> Blood Diamond, Bow -> Crimson Bow, Water Bottle -> Potion of Vampirism).
  - **Bloodtaker Blade** (`soulless:bloodtaker_blade`): Strong weapon, when you kill a non-undead mob with it you get Blood Orbs and it transfroms into Ritual Blade.
  - **Blood Orb** (`soulless:blood_orb`): Consumable item that you receive from rituals, used for healing and brewing. Consuming 32 Blood Orbs awakens a dark thirst, granting permanent Vampirism until you die!
  - **Injection Needle** (`soulless:injection_needle`): A non-weapon tool with limited uses used to draw blood and perform rituals without transforming your weapon. Can transmute off-hand catalysts.
  - **Blood Diamond** (`soulless:blood_diamond`): Obtained by performing a ritual with a Diamond in your offhand. Used for vampiric equipment.
  - **Blood Diamond Block** (`soulless:blood_diamond_block`): Decorative and compact storage block for Blood Diamonds.
  - **Dracula's Blade** (`soulless:draculas_blade`): Sword forged from Blood Diamonds and Soulsteel. Cursed: deals self-damage and reduced damage without Dracula's Pendant. With Dracula's Pendant: empowered with +2 bonus damage, 25% lifesteal (works on undead too), Speed and Strength on kill, and is retained after death with the pendant.
  - **Dracula's Bow** (`soulless:draculas_bow`): Bow transformed in a blood ritual. Cursed: damages the archer and shoots weak arrows without Dracula's Pendant. With Dracula's Pendant: shoots empowered high-velocity arrows with +3 damage and 3.0 HP lifesteal, and is retained after death with the pendant.
  - **Gravedigger** (`soulless:gravedigger`): Soulsteel shovel with a 10% chance to drop bones when mining shovel blocks. Right-click converts soil into Boneyards with extra durability cost.
  - **Soulbreaker** (`soulless:soulbreaker`): Soulsteel pickaxe. Ghost miners shatter adjacent matching stone and ores, releasing soul particles.
  - **Beheader** (`soulless:beheader`): Soulsteel axe. Critical hits apply Slowness I, and killing blows have a chance to decapitate foes.
  - **Soulsteel Armor** (`soulless:soulsteel_helmet`, `soulless:soulsteel_chestplate`, `soulless:soulsteel_leggings`, `soulless:soulsteel_boots`): High durability armor set where each piece grants +15% damage reduction against undead.
  - **Dracula's Pendant** (`soulless:draculas_pendant`): Legendary endgame artifact. Bestows eternal Vampirism while carried in your inventory and allows blood siphoning/harvesting on undead mobs. Soulbound: kept through death unless shattered by a Wooden Stake.
  - **Wooden Stake** (`soulless:wooden_stake`): Sharpened wooden weapon that deals devastating bonus damage to undead and vampires. Piercing the heart of a player carrying Dracula's Pendant shatters their pendant forever.
  - **Potion of Vampirism**: Brewed from Awkward Potion + Blood Orb, or transmuted from a Water Bottle in a ritual. Grants the Vampirism effect (15% lifesteal per level on living targets). Redstone extends duration, Glowstone upgrades to Vampirism II.

- Adds custom crafting progression:
  - Lost Souls conversion recipes
  - Ghostpowder conversion recipes
  - Ghostpowder and Soulsteel crafting recipes
  - Gold Souls, Soulpowder, Monstrous Stew, and Return Token recipes
  - Reworked soul sand and soul soil crafting
  - Cheaper golden carrot and golden apple recipes
  - Quartz and skeleton skull alternative recipes
  - Soulsteel block packing/unpacking (9 <-> 1)
  - Blood Diamond block packing/unpacking (9 <-> 1)
  - Soul Reaper, Ritual Blade, Dracula's Blade, Wooden Stake, and Bat crafting recipes
  - Gravedigger, Soulbreaker, Beheader, Dracula's Pendant, and Soulsteel Armor recipes

- Adds custom milestone and hidden-mechanic advancements:
  - **Soulless**: Slay the undead to reap their lingering Lost Souls.
  - **Blinding Dust**: Scatter Soulpowder on right-click to blind all nearby entities in a 5-block radius.
  - **(Don't Fear) The Reaper**: Slay undead with the Soul Reaper to reap double Lost Souls.
  - **Graveyard Shift**: Right-click dirt or grass with the Gravedigger to exhume bones and carve Boneyard soil.
  - **Ghost Miners**: Mine an ore with the Soulbreaker to summon miner spirits that excavate connected veins.
  - **Off With Their Heads!**: Strike down foes with the Beheader axe to harvest their severed skulls.
  - **Soulbound Sentinel**: Equip a full suit of Soulsteel Armor to absorb 60% of damage from undead attacks.
  - **Bloody Sacrifice**: Perform a blood sacrifice with a Needle or Ritual Blade to harvest Blood Orbs.
  - **Forbidden Transmutation**: Perform a ritual while holding a Diamond, Bow, or Water Bottle in your offhand to transmute it.
  - **Creature of the Night**: Gorge on 32 Blood Orbs to awaken permanent Vampirism.
  - **Blood Feast**: As a vampire, strike living foes to satiate your hunger and saturation with lifesteal.
  - **Lord of Darkness**: Forge Dracula's Pendant to unlock soulbound immortality and the ability to drain undead.
  - **The Count's Fang**: Wield Dracula's Blade with Dracula's Pendant to empower its strikes and prevent self-harm.
  - **Stake Through the Heart**: Slay a vampire or permanently shatter an immortal's Dracula's Pendant with a Wooden Stake.
  - **The GhostLands**: Detonate a RIP block to lay waste to the surrounding terrain and extract a Ghost Heart.
  - **Surge of Vitality**: Channel a Ghost Heart on right-click for an instant burst of extra hearts and regeneration.
  - **Deathwish**: Deploy an Undead Core to erect a 50-block protective aura that suppresses and harms undead.
  - **Ticket Home**: Activate a Return Token to teleport instantly back to your spawn point.
  - **Home Run**: Strike a Ghast or deflect an incoming fireball with a Baseball Bat.
  - **The Orb of Dreamers**: Hurl a Ghost Orb to unleash a spiraling projectile of spectral fire.

- Adds worldgen and structures:
  - **Ruins**: New structure with three variants (small, tiny, medium), spawns in temperate land biomes, uses Bone Chest loot, and avoids ocean-surface generation.

- Adds a custom creative tab for all Soulless items.

## Project Info

- Mod ID: `soulless`
- Name: `Soulless`
- Version: `1.2.0`
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
