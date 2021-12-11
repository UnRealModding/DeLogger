package com.unrealdinnerbone.delogger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

@Mod(DeLogger.MOD_ID)
public class DeLogger {

    public static final String MOD_ID = "delogger";

    public DeLogger() {

    }

}
