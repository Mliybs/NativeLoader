package com.mlinetles.nativeloader;

import com.google.common.collect.ImmutableSet;
import com.mlinetles.nativeloader.minecraft.NativeResourcePackProvider;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class NativeLoader implements ModInitializer {
	public static final String MOD_ID = "native-loader";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static final HashSet<ResourcePack> LOADED = new HashSet<>();
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
        LOADED.forEach(NativeLoader::onLoad);
	}

    public static void loadResourcePacks(ResourcePackManager manager) {
        if (!LOADED.isEmpty()) return;
        System.setProperty("jna.encoding", "UTF-8");
        LOGGER.info("NativeLoader开始加载！");

        var fabric = FabricLoader.getInstance();
        var packs = fabric.getGameDir().resolve("nativepacks");
        try {
            Files.createDirectories(packs);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (var paths = Files.walk(packs, 1)) {
            paths.forEach(x -> {
                if (x == packs) return;

                if ((Files.isRegularFile(x) && x.toString().endsWith(".zip")))
                    try (var pack = new ZipResourcePack.ZipBackedFactory(x.toFile(), true).open(x.getFileName().toString())) {
                        LOADED.add(pack);
                    } catch (Exception e) {
                        LOGGER.error(e.toString());
                    }
                else if (Files.isDirectory(x))
                    try (var pack = new DirectoryResourcePack.DirectoryBackedFactory(x, true).open(x.getFileName().toString())) {
                        LOADED.add(pack);
                    } catch (Exception e) {
                        LOGGER.error(e.toString());
                    }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            var field = ResourcePackManager.class
                    .getDeclaredField(fabric.getMappingResolver().mapFieldName("intermediary",
                            "net.minecraft.class_3283",
                            "field_14227",
                            "Ljava/util/Set;"));
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            var providers = (Set<ResourcePackProvider>)field.get(manager);
            if (providers.stream().noneMatch(x -> x instanceof NativeResourcePackProvider))
                field.set(manager, new ImmutableSet.Builder<ResourcePackProvider>()
                    .add(new NativeResourcePackProvider(LOADED))
                    .addAll(providers).build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void onLoad(ResourcePack pack) {
        pack.getNamespaces(ResourceType.CLIENT_RESOURCES).forEach(y -> {
            pack.findResources(ResourceType.CLIENT_RESOURCES, y, "natives", (z, supplier) -> {
                if (!(z.getPath().endsWith(".dll") || z.getPath().endsWith(".so") || z.getPath().endsWith(".dylib")))
                    return;

                try (var stream = supplier.get()) {
                    var path = z.getPath();
                    if (LoadedLibraries.getLibraries().contains(path)) {
                        LOGGER.info("忽略重复加载：{}:{}", z.getNamespace(), path);
                        return;
                    }
                    LOGGER.info("正在加载：{}:{}", z.getNamespace(), path);
                    var index = path.lastIndexOf((int)'.');
                    var temp = java.io.File.createTempFile(path.substring(0, index), path.substring(index));
                    temp.deleteOnExit();
                    Files.copy(stream, temp.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    LoadedLibraries.Load(path, temp.getAbsolutePath(), FabricLoader.getInstance().getMappingResolver(), LOGGER);
                    LOGGER.info("加载成功：{}:{}", z.getNamespace(), path);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        });
    }
}