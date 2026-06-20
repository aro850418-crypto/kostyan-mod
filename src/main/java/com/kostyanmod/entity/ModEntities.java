package com.kostyanmod.entity;

import com.kostyanmod.KostyanMod;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, KostyanMod.MOD_ID);

    public static final RegistryObject<EntityType<KostyanEntity>> KOSTYAN =
            ENTITY_TYPES.register("kostyan",
                    () -> EntityType.Builder.<KostyanEntity>of(KostyanEntity::new, MobCategory.CREATURE)
                            .sized(0.6f, 0.85f)
                            .clientTrackingRange(10)
                            .build("kostyan"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
