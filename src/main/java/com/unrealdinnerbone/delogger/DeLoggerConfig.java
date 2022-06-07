package com.unrealdinnerbone.delogger;

import com.mojang.authlib.exceptions.AuthenticationUnavailableException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DeLoggerConfig {

    public String dontChangeMe = "RandomString";
    public boolean printWarningMessage = true;
    public boolean printLoggerTypes = false;
    public List<String> whitelistLoggersTypes = Collections.singletonList("DebugFile");
    public List<String> loggers = Arrays.asList(
            "net.minecraft.Util",
            "net.minecraft.commands.Commands",
            "net.minecraftforge.registries.GameData",
            "net.minecraftforge.common.ForgeConfigSpec",
            "net.minecraftforge.fml.VersionChecker",
            "net.minecraft.world.entity.EntityType",
            "net.minecraft.client.ClientRecipeBook",
            "net.minecraft.client.resources.model.ModelBakery",
            "com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService",
            "net.minecraft.world.level.chunk.storage.ChunkSerializer",
            "net.minecraft.client.renderer.block.model.BlockModel",
            "net.minecraft.client.renderer.texture.TextureAtlas",
            "net.minecraft.world.level.storage.loot.LootTable");

    public List<String> ignoredExceptions = Arrays.asList(
            AuthenticationUnavailableException.class.getCanonicalName()
    );

}
