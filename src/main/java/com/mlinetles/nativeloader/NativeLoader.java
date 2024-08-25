package com.mlinetles.nativeloader;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
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

		LOGGER.info("NativeLoader开始加载！");
        // var identifier = '_' + System.getProperty("os.name").toLowerCase() + '_' + System.getProperty("os.arch");
        var fabric = FabricLoader.getInstance();
        var resolver = fabric.getMappingResolver();
        var dir = fabric.getGameDir().resolve("nativeloader");
        var packs = fabric.getGameDir().resolve("resourcepacks");
        try {
            Files.createDirectories(dir);
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
                            LoadedLibraries.Load(file.toAbsolutePath().toString(), resolver);
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
                    } catch (Exception e) {
                        LOGGER.error(e);
                    }
                else if (Files.isDirectory(x))
                    try (var pack = new DirectoryResourcePack(x.toFile())) {
                        onLoad.accept(pack);
                    } catch (Exception e) {
                        LOGGER.error(e);
                    }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
	}
}