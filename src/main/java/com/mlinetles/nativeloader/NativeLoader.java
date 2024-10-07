package com.mlinetles.nativeloader;

import com.google.common.collect.ImmutableSet;
import com.mlinetles.nativeloader.minecraft.NativeResourcePackProvider;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.function.Consumer;

public class NativeLoader implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LogManager.getLogger("native-loader");
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
	}

    public static void loadResourcePacks(ResourcePackManager manager) {
        System.setProperty("jna.encoding", "UTF-8");
        LOGGER.info("NativeLoader开始加载！");

        var fabric = FabricLoader.getInstance();
        var resolver = fabric.getMappingResolver();
        var loaded = new HashSet<ResourcePack>();
        var dir = fabric.getGameDir().resolve("nativeloader").resolve("temp");
        var packs = fabric.getGameDir().resolve("nativepacks");
        try {
            Files.createDirectories(dir);
            Files.createDirectories(packs);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Consumer<AbstractFileResourcePack> onLoad = pack -> {
            pack.getNamespaces(ResourceType.CLIENT_RESOURCES).forEach(y -> {
                var resources = pack.findResources(ResourceType.CLIENT_RESOURCES, y, "natives", 1,
                        x -> x.endsWith(".dll") || x.endsWith(".so") || x.endsWith(".dylib"));
                if (resources.isEmpty())
                    try (var files = Files.walk(dir.resolve(pack.getName()))) {
                        files.sorted(Comparator.reverseOrder()).forEach(x -> {
                            try {
                                Files.delete(x);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                else {
                    resources.forEach(x -> {
                        try (var stream = pack.open(ResourceType.CLIENT_RESOURCES, x)) {
                            var namespace = dir.resolve(pack.getName()).resolve(x.getNamespace());
                            Files.createDirectories(namespace.resolve("natives"));
                            var file = namespace.resolve(x.getPath());
                            Files.copy(stream, file, StandardCopyOption.REPLACE_EXISTING);
                            LOGGER.info("正在加载：{}", file.toAbsolutePath());
                            LoadedLibraries.Load(file.toAbsolutePath().toString(), resolver, LOGGER);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            });
        };

        try (var paths = Files.walk(packs, 1)) {
            paths.forEach(x -> {
                if (x == packs) return;

                if ((Files.isRegularFile(x) && x.toString().endsWith(".zip")))
                    try (var pack = new ZipResourcePack(x.toFile())) {
                        onLoad.accept(pack);
                        loaded.add(pack);
                    } catch (Exception e) {
                        LOGGER.error(e);
                    }
                else if (Files.isDirectory(x))
                    try (var pack = new DirectoryResourcePack(x.toFile())) {
                        onLoad.accept(pack);
                        loaded.add(pack);
                    } catch (Exception e) {
                        LOGGER.error(e);
                    }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            var field = ResourcePackManager.class.getDeclaredField("providers");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            var providers = (Set<ResourcePackProvider>)field.get(manager);
            field.set(manager, new ImmutableSet.Builder<ResourcePackProvider>()
                    .add(new NativeResourcePackProvider(loaded))
                    .addAll(providers).build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}