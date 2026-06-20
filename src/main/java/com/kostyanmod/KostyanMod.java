package com.kostyanmod;

import com.kostyanmod.entity.ModEntities;
import com.kostyanmod.entity.KostyanEntity;
import com.kostyanmod.item.ModItems;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(KostyanMod.MOD_ID)
public class KostyanMod {

    public static final String MOD_ID = "kostyanmod";
    public static final Logger LOGGER = LogManager.getLogger();

    public KostyanMod(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        ModEntities.register(modEventBus);
        ModItems.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::onAttributeCreate);

        MinecraftForge.EVENT_BUS.register(this);

        LOGGER.info("Kostyan Mod loaded! Kostyan is ready to help!");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Kostyan Mod - Common Setup done!");
    }

    @SubscribeEvent
    public void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(ModEntities.KOSTYAN.get(), KostyanEntity.createAttributes().build());
    }
}
