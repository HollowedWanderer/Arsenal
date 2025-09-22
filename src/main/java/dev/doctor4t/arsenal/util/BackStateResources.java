package dev.doctor4t.arsenal.util;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.doctor4t.arsenal.Arsenal;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BackStateResources implements SimpleSynchronousResourceReloadListener {
    private static final Map<Identifier, BackStateData> transforms = new HashMap<>();
    private static BackStateData defaultTransform;

    @Override
    public Identifier getFabricId() {
        return Arsenal.id("backstates");
    }

    @Override
    public void reload(ResourceManager manager) {
        MinecraftClient.getInstance().execute(() -> this.actuallyLoad(manager));
    }

    private void actuallyLoad(ResourceManager manager) {
        ClientTickDelayScheduler.schedule(-1, () -> {
            transforms.clear();
            manager.findResources("backstates", path -> path.getPath().endsWith(".json")).keySet().forEach(id -> {
                if (manager.getResource(id).isPresent()) {
                    try (InputStream stream = manager.getResource(id).get().getInputStream()) {
                        JsonObject json = JsonHelper.deserialize(new InputStreamReader(stream, StandardCharsets.UTF_8));
                        DataResult<BackStateData> result = BackStateData.CODEC.parse(JsonOps.INSTANCE, json);

                        String fileName = id.getPath().substring(id.getPath().lastIndexOf("/") + 1, id.getPath().lastIndexOf("."));

                        result.resultOrPartial(Arsenal.LOGGER::error).ifPresent(data -> {
                            if (fileName.equals("default")) defaultTransform = data;
                            if (id.getPath().startsWith("backstates/tag")) {
                                Identifier tagId = Identifier.of(id.getNamespace(), fileName);
                                TagKey<Item> tag = TagKey.of(Registries.ITEM.getKey(), tagId);
                                if (tag != null) {
                                    Registries.ITEM.forEach((item) -> {
                                        Identifier itemId = Registries.ITEM.getId(item);
                                        if (item.getDefaultStack().getRegistryEntry().isIn(tag)) {
                                            transforms.putIfAbsent(itemId, data);
                                        }

                                    });
                                } else {
                                    Arsenal.LOGGER.warn("Tag #{} not found while loading item transforms!", tagId);
                                }
                            } else if (id.getPath().startsWith("backstates/item")) {
                                transforms.put(Identifier.of(id.getNamespace(), fileName), data);
                            }
                        });
                    } catch (Exception e) {
                        Arsenal.LOGGER.error("Failed to load transform for {}: {}", id, e.getMessage());
                    }
                }
            });
        });
    }

    public static BackStateData getTransform(Identifier itemId) {
        return transforms.getOrDefault(itemId, defaultTransform);
    }

    public record BackStateData(
            List<Float> translation,
            List<Float> rotation,
            List<Float> scale,
            ItemDisplayContext mode,
            Float sway
    ) {
        public static final Codec<BackStateData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.FLOAT.listOf().fieldOf("translation").orElseGet(() -> List.of(0F, 0F, 0F)).forGetter(BackStateData::translation),
                Codec.FLOAT.listOf().fieldOf("rotation").orElseGet(() -> List.of(0F, 0F, 0F)).forGetter(BackStateData::translation),
                Codec.FLOAT.listOf().fieldOf("scale").orElseGet(() -> List.of(0F, 0F, 0F)).forGetter(BackStateData::translation),
                Codec.STRING.fieldOf("mode").orElse("FIXED").xmap(ItemDisplayContext::valueOf, Enum::name).forGetter(BackStateData::mode),
                Codec.FLOAT.fieldOf("sway").orElseGet(() -> 0.35F).forGetter(BackStateData::sway)
        ).apply(instance, BackStateData::new));
    }
}