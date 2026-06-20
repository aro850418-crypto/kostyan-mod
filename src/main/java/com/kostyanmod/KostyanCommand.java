package com.kostyanmod;

import com.kostyanmod.entity.KostyanEntity;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = KostyanMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class KostyanCommand {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
            Commands.literal("kostyan")
                .then(Commands.argument("question", StringArgumentType.greedyString())
                    .executes(ctx -> {
                        String question = StringArgumentType.getString(ctx, "question");
                        CommandSourceStack source = ctx.getSource();

                        try {
                            ServerPlayer player = source.getPlayerOrException();
                            ServerLevel level = source.getLevel();

                            // Ищем Костяна рядом с игроком
                            List<Entity> entities = level.getEntities(null,
                                    player.getBoundingBox().inflate(100.0),
                                    e -> e instanceof KostyanEntity);

                            if (entities.isEmpty()) {
                                player.sendSystemMessage(Component.literal(
                                    "§6[Костян] §cМеня нет рядом! Найди меня сначала! Гав?"));
                            } else {
                                KostyanEntity kostyan = (KostyanEntity) entities.get(0);
                                kostyan.answerQuestion(player, question);
                            }
                        } catch (Exception e) {
                            source.sendFailure(Component.literal("Ошибка: только игрок может разговаривать с Костяном!"));
                        }

                        return 1;
                    }))
                .executes(ctx -> {
                    CommandSourceStack source = ctx.getSource();
                    try {
                        ServerPlayer player = source.getPlayerOrException();
                        player.sendSystemMessage(Component.literal(
                            "§6[Костян] §fИспользование: §e/kostyan <вопрос>§f\n" +
                            "§fМожно спросить про: мобов, крафт, биомы, алмазы, нижний мир, зачарования и многое другое!"));
                    } catch (Exception ignored) {}
                    return 1;
                })
        );
    }
}
