package com.kostyanmod;

import com.kostyanmod.entity.KostyanEntity;
import com.kostyanmod.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = KostyanMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class KostyanEventHandler {

    /**
     * Когда игрок заходит в игру — спауним Костяна рядом (если его ещё нет)
     */
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ServerLevel level = player.serverLevel();

            // Проверяем, есть ли уже Костян рядом с этим игроком
            List<Entity> existing = level.getEntities(null,
                    player.getBoundingBox().inflate(200.0),
                    e -> e instanceof KostyanEntity);

            if (existing.isEmpty()) {
                // Спауним Костяна рядом с игроком
                KostyanEntity kostyan = ModEntities.KOSTYAN.get().create(level);
                if (kostyan != null) {
                    BlockPos spawnPos = player.blockPosition().offset(2, 0, 2);
                    kostyan.moveTo(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5, 0, 0);
                    kostyan.setPersistenceRequired();
                    kostyan.setOwner(player);
                    level.addFreshEntity(kostyan);
                    kostyan.sendGreeting(player);
                }
            } else {
                // Костян уже есть — просто приветствуем игрока
                player.sendSystemMessage(Component.literal(
                    "§6[Костян] §fГав! Хозяин вернулся! Я тут, всё охраняю! " +
                    "Пиши §e/kostyan <вопрос>§f чтобы поговорить со мной!"));
            }
        }
    }

    /**
     * Если Костяна телепортировали далеко — телепортируем его обратно к игроку
     */
    @SubscribeEvent
    public static void onPlayerTick(net.minecraftforge.event.TickEvent.PlayerTickEvent event) {
        if (event.phase == net.minecraftforge.event.TickEvent.Phase.END) {
            if (event.player instanceof ServerPlayer player) {
                ServerLevel level = player.serverLevel();

                // Проверяем раз в 100 тиков (~5 сек)
                if (player.tickCount % 100 == 0) {
                    List<Entity> kostyanList = level.getEntities(null,
                            player.getBoundingBox().inflate(200.0),
                            e -> e instanceof KostyanEntity);

                    if (kostyanList.isEmpty()) {
                        // Костян потерялся — телепортируем обратно
                        List<Entity> allKostyan = level.getEntities(null,
                                player.getBoundingBox().inflate(10000.0),
                                e -> e instanceof KostyanEntity);

                        if (!allKostyan.isEmpty()) {
                            KostyanEntity kostyan = (KostyanEntity) allKostyan.get(0);
                            kostyan.teleportTo(
                                player.getX() + 2,
                                player.getY(),
                                player.getZ() + 2
                            );
                        }
                    }
                }
            }
        }
    }
}
