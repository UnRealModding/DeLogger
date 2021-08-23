package com.unrealdinnerbone.delogger;

import com.mojang.authlib.exceptions.AuthenticationUnavailableException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DeLoggerConfig {

    public String dontChangeMe = "RandomString";
    public boolean printWarningMessage = true;
    public boolean enableDebugLog = false;
    public List<String> whitelistLoggersTypes = Collections.singletonList("DebugFile");
    public List<String> loggers = Arrays.asList(
            "net.minecraft.client.renderer.model.BlockModel",
            "net.minecraft.client.renderer.texture.AtlasTexture",
            "com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService",
            "net.minecraft.client.renderer.model.ModelBakery",
            "net.minecraft.client.util.ClientRecipeBook",
            "net.minecraft.command.Commands",
            "net.minecraft.util.Util",
            "net.minecraft.entity.EntityType",
            "net.minecraft.loot.LootTableManager",
            "net.minecraft.world.chunk.storage.ChunkSerializer",
            "net.minecraftforge.registries.GameData",
            "net.minecraftforge.common.ForgeConfigSpec",
            "net.minecraftforge.fml.VersionChecker");
    public List<String> ignoredExceptions = Arrays.asList(
            AuthenticationUnavailableException.class.getCanonicalName()
    );

}
