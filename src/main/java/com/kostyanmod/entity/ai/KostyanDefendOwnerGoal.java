package com.kostyanmod.entity.ai;

import com.kostyanmod.entity.KostyanEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class KostyanDefendOwnerGoal extends TargetGoal {

    private final KostyanEntity kostyan;
    private LivingEntity target;
    private static final double DEFEND_RANGE = 20.0;
    private static final String[] DEFEND_PHRASES = {
            "Никто не трогает моего хозяина! ГАВ!",
            "Я защищаю хозяина! Пошёл прочь!",
            "За хозяина порву! РРРРР!",
            "Только попробуй тронуть его! ГАВ-ГАВ!",
            "Стоять! Я тут за главного пса!"
    };

    public KostyanDefendOwnerGoal(KostyanEntity kostyan) {
        super(kostyan, true);
        this.kostyan = kostyan;
    }

    @Override
    public boolean canUse() {
        Player nearestPlayer = this.kostyan.level().getNearestPlayer(this.kostyan, DEFEND_RANGE);
        if (nearestPlayer == null) return false;

        AABB searchArea = nearestPlayer.getBoundingBox().inflate(DEFEND_RANGE);
        List<Monster> threats = this.kostyan.level().getEntitiesOfClass(
                Monster.class, searchArea, monster -> monster.getTarget() instanceof Player
        );

        if (!threats.isEmpty()) {
            this.target = threats.get(0);
            return true;
        }
        return false;
    }

    @Override
    public void start() {
        this.mob.setTarget(this.target);

        // Отправить сообщение защиты игроку
        Player nearestPlayer = this.kostyan.level().getNearestPlayer(this.kostyan, DEFEND_RANGE);
        if (nearestPlayer instanceof ServerPlayer serverPlayer) {
            String phrase = DEFEND_PHRASES[(int) (Math.random() * DEFEND_PHRASES.length)];
            serverPlayer.sendSystemMessage(Component.literal("§6[Костян] §c" + phrase));
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.target != null && this.target.isAlive();
    }
}
