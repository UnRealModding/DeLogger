package com.unrealdinnerbone.delogger;

import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import net.minecraft.client.renderer.model.BlockModel;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.util.ClientRecipeBook;
import net.minecraft.command.Commands;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootTableManager;
import net.minecraft.resources.SimpleReloadableResourceManager;
import net.minecraft.util.Util;
import net.minecraft.world.chunk.storage.ChunkSerializer;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.VersionChecker;
import net.minecraftforge.registries.GameData;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DeLoggerConfig {

    public String dontChangeMe = "RandomString";
    public boolean printWarningMessage = true;
    public boolean enableDebugLog = false;
    public List<String> whitelistLoggersTypes = Collections.singletonList("DebugFile");
    public List<String> loggers = Arrays.asList(
            BlockModel.class.getCanonicalName(),
            AtlasTexture.class.getCanonicalName(),
            YggdrasilAuthenticationService.class.getCanonicalName(),
            ModelBakery.class.getCanonicalName(),
            ClientRecipeBook.class.getCanonicalName(),
            Commands.class.getCanonicalName(),
            Util.class.getCanonicalName(),
            EntityType.class.getCanonicalName(),
            LootTableManager.class.getCanonicalName(),
            ChunkSerializer.class.getCanonicalName(),
            GameData.class.getCanonicalName(),
            ForgeConfigSpec.class.getCanonicalName(),
            VersionChecker.class.getCanonicalName());
    public List<String> ignoredExceptions = Arrays.asList(
            AuthenticationUnavailableException.class.getCanonicalName()
    );

}
