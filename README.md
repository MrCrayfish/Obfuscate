![Obfuscate Banner](https://i.imgur.com/Lkau0ir.png)

[![Download](https://img.shields.io/static/v1?label=&message=Download&color=2d2d2d&labelColor=b38000&style=for-the-badge&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAAZdEVYdFNvZnR3YXJlAHBhaW50Lm5ldCA0LjAuMjCGJ1kDAAAAzUlEQVRYR8WOAQ7CMAwD9/9PD2XqpsZxWdrUcNIhaoSd45zH/rNN+5gllFS8v8ziSireX2ZxJRX7h4p+I4iBAtxwYqACdx5ZqIJt0QNMBWxneICpIOyEoKkibIWgqcRtuUezyluf+909mhVY31dZuArrehWDVbAnLQarYE9aDFbBnrQYrII9aTGogF0pWViB9Y00th9ww3pNhB5g/oq/HnBt4XCvmmsHR3uVPDv9IFOB23APogK34R4DdxL6QzBwF6E7BAN3wHrTB4g8jw82Wi+WaJjRLwAAAABJRU5ErkJggg==)](https://mrcrayfish.com/mods?id=obfuscate) ![Minecraft](https://img.shields.io/static/v1?label=&message=1.16%20|%201.15%20|%201.14%20|%201.12&color=2d2d2d&labelColor=b38000&style=for-the-badge&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAAZdEVYdFNvZnR3YXJlAHBhaW50Lm5ldCA0LjAuMjCGJ1kDAAACoElEQVQ4T22SeU8aURTF/ULGtNRWWVQY9lXABWldIDPIMgVbNgEVtaa0damiqGBdipXaJcY2ofEf4ycbTt97pVAabzK5b27u+Z377kwXgK77QthRy7OfXbeJM+ttqKSXN8sdwbT/A0L7elmsYqrPHZmROLPh5YkV4oEBwaKuHj+yyJptLDoAhbq3O1V1XCVObY3FL24mfn5oRPrcwSCRfQOyNWcjVjZdCbtcdwcgXrXUspdOKbDN/XE9tiBJMhXHT60gUIT2dMhcDLMc3NVKQklz0QIkf5qlyEcO6Qs7yPhMJB4amDMFimQSmqNlE8SKAZFzDfxHfVILIIZ10sJ3OwIbcqSuiOjchkzNCboHev9o2YhgiUP8mxnLN24I6/3ghYdtQG5iUMpFBuCP9iKwLsfiLyeCp2rMnZgwX3NArGoxW1Ridl+BzLEVKa8KSxOqNmDdz0kFnxaLHhWEgAyZigWhHXL+pEDy2ozsDxv8vAzTnh7w5kcghqCaFmCT10of4iPIT2mRdPUh4HoCcVwBH/8Ac2kzUkEV5r3EfVSOvbAJa5NDyI0r2oDtWb1EClh+OoC3Pg7v/Bw7p939yI4rsRW2Y3lKh01eh7WpIRyKZqzyjjYgPdIvlaMWRqYuG7wWryYHsRM0sFolZiPvQ3jheIwSmSBPdkByG/B6Wi3RYiVmRX7GiAPiUCRisii8D+jZNKvPBrHCW1GY0bAz6WkDCtOaSyKQFsi4K5NqNiZtehN2Y5uAShETqolhBqJXpfdPuPsuWwAaRdHSkxdc11mPqkGnyY4pyKbpl1GyJ0Pel7yqBoFcF3zqno5f+d8ohYy9Sx7lzQpxo1eirluCDgt++00p6uxttrG4F/A39sJGZWZMfrcp6O6+5kaVzXJHAOj6DeSs8qw5o8oxAAAAAElFTkSuQmCC) ![Issues](https://img.shields.io/github/issues/MrCrayfish/Obfuscate?color=2d2d2d&labelColor=b38000&style=for-the-badge&logo=GitHub) ![Curseforge](http://cf.way2muchnoise.eu/full_obfuscate_downloads(b38000-2d2d2d-FFF-FFFFF-FFFFFF).svg?badge_style=for_the_badge)

# Obfuscate

Obfuscate is a simple library which provides useful events, utilities, and common code for mod developers. Originally created for compatibility in MrCrayfish's mod, this library is now targeted for all mod developers alike. **This mod does not have any content by default.**

### Library Features:

**Manipulate the player's model safely:**

This feature allows you to easily customise the rotations of limbs on player's model without having to worry about resetting them back to their default state. This could be used for creating a custom pose when holding a certain item, adding custom animations when swinging a sword and so much more!

![Gif 1](https://i.imgur.com/eJQMjxY.gif)![Gif 2](https://i.imgur.com/T7LPKDc.gif)![Gif 3](https://i.imgur.com/Kh2oSin.gif) 

```java
@SubscribeEvent
public void setupPlayerRotations(PlayerModelEvent.SetupAngles.Post event)
{
    if(condition)
    {
        event.getModelPlayer().getModelHead().rotateAngleX = (float) Math.toRadians(90f);
    }
}
```

**Full control over item rendering:**

This gives you the ability to handle rendering of items yourself. Obfuscate provides events that are called from the true source rather than relying on the camera transform type, like vanilla's ITESR system. This means when the event for rendering the held item on the player model is called, it's actually coming from the held rendering layer on the player model. 

```java
/* An example of controlling held item on player model. See RenderItemEvent for all events. 
 * You can implement just RenderItemEvent to control all cases. */
@SubscribeEvent
public void setupPlayerRotations(RenderItemEvent.Held.Pre event)
{
    if(condition)
    {
        event.setCancelled(true);
        //do custom rendering here
    }
}
```

**A completely new data syncing system for players:**

It's not good practice to add data parameters to entities other than your own; this often results key mismatch due to the uncontrolled initialization order of static fields. Obfuscate solves this issue and provides even more features useful features. Using this new system, you can control if the parameter should be reset on death, prevent it from syncing to all players expect the player who owns the key, prevent it from syncing at all, and even save it to the player's data!

```java
public static final SyncedDataKey<Boolean> AIMING = SyncedDataKey.builder(Serializers.BOOLEAN)
            .id(new ResourceLocation(Reference.MOD_ID, "aiming"))
            .defaultValueSupplier(() -> false)
            .resetOnDeath()
            .build();

public static void somewhereInCommonInitialization() {
    SyncedPlayerData.instance().registerKey(AIMING);
}

//Setting value
SyncedPlayerData.instance().set(player, ModSyncedDataKeys.AIMING, true);

//Getting value
SyncedPlayerData.instance().get(player, ModSyncedDataKeys.AIMING)
```

### Start Developing with this Library:
You can start using this library simply by adding this code to your **build.gradle** file. You'll want to replace the curseforge_file_id with a version of Obfuscate targeted towards your Minecraft version.

```gradle
//Minecraft 1.16.1: 3000205
//Minecraft 1.15.2: 2946425
//Minecraft 1.14.4: 2912286
//Minecraft 1.12.2: 2912288

repositories {
    maven {
        url = "https://www.cursemaven.com"
    }
}

dependencies {
    compile fg.deobf('curse.maven:obfuscate:<curseforge_file_id>')
}
```
