package com.kostyanmod.entity;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KostyanEntity extends PathfinderMob {

    private UUID ownerUUID = null;
    private Player ownerPlayer = null;

    // Знания Костяна о Майнкрафте
    private static final Map<String, String> KNOWLEDGE = new HashMap<>();

    static {
        // Базовое выживание
        KNOWLEDGE.put("еда", "В Майнкрафте есть много еды! Лучшая — золотые яблоки и стейк. Не забывай есть, иначе умрёшь от голода на сложном режиме!");
        KNOWLEDGE.put("food", "There's tons of food in Minecraft! Best options are golden apples and steak. Always keep your hunger bar full on Hard mode!");
        KNOWLEDGE.put("выживание", "Первое правило выживания: найди дерево, сделай инструменты, построй укрытие до ночи. Ночью появляются монстры!");
        KNOWLEDGE.put("survival", "First rule of survival: find wood, craft tools, build shelter before nightfall. Monsters spawn in the dark!");

        // Крафт
        KNOWLEDGE.put("крафт", "Крафтинг — основа игры! Открой верстак (нажми E), расположи ресурсы по рецепту. Совет: зелье силы + меч = мощный воин!");
        KNOWLEDGE.put("crafting", "Crafting is the foundation! Open your crafting table (press E), arrange resources by recipe. Tip: Strength potion + sword = powerful warrior!");
        KNOWLEDGE.put("верстак", "Верстак делается из 4 деревянных досок. Без него не сделать почти ничего полезного!");
        KNOWLEDGE.put("алмазы", "Алмазы находятся на уровнях Y от -58 до -64! Используй кирку с зачарованием Удача 3 для максимальной добычи. Не копай прямо вниз!");
        KNOWLEDGE.put("diamonds", "Diamonds are found at Y levels -58 to -64! Use a Fortune 3 pickaxe for max yield. Never dig straight down!");

        // Мобы
        KNOWLEDGE.put("крипер", "Крипер — зелёный молчаливый враг. Шипит и ВЗРЫВАЕТСЯ! Убивай мечом с откатом назад. Щит тоже помогает!");
        KNOWLEDGE.put("creeper", "Creeper — a silent green menace. It hisses and EXPLODES! Kill it with a sword while stepping back. Shield helps too!");
        KNOWLEDGE.put("эндермен", "Эндермен телепортируется и атакует, если смотришь ему в глаза. Надень тыкву на голову — и он тебя не тронет!");
        KNOWLEDGE.put("enderman", "Enderman teleports and attacks if you look it in the eyes. Wear a carved pumpkin on your head and it won't aggro!");
        KNOWLEDGE.put("зомби", "Зомби горят на солнце! Если прижали — беги на улицу и жди рассвета. Дроп: гнилая плоть, иногда железо.");
        KNOWLEDGE.put("скелет", "Скелет стреляет стрелами. Прячься за деревьями или атакуй зигзагом. Дроп: кости и стрелы — очень полезно!");
        KNOWLEDGE.put("паук", "Пауки ночью агрессивны, днём — нейтральны. Дроп: нити (нужны для луков) и паучьи глаза.");
        KNOWLEDGE.put("дракон", "Дракон Края — финальный босс! Уничтожь кристаллы на башнях, потом бей дракона. Нужна хорошая броня и много стрел!");
        KNOWLEDGE.put("виверн", "Иссушитель создаётся из 4 блоков душевого песка в форме Т и 3 голов иссушителя. Очень опасен! Дроп: звезда Нижнего Мира.");

        // Биомы
        KNOWLEDGE.put("биомы", "В Майнкрафте огромное количество биомов: леса, пустыни, тундры, джунгли, болота... Каждый уникален! В джунглях есть храмы с сокровищами!");
        KNOWLEDGE.put("нижний мир", "Нижний Мир (Ад) — опасное измерение! Там есть лавовые озёра, гасты, пиглины. Нужен портал из обсидиана. Там добывают незерит!");
        KNOWLEDGE.put("край", "Край — конечное измерение с Драконом Края. Попасть туда можно через Крепость. Ищи эндерпёрл и глаза Края!");
        KNOWLEDGE.put("незерит", "Незерит — самый прочный материал! Ищи древние обломки в Нижнем Мире на уровнях Y 8-22. Нужна кирка из алмаза или незерита.");

        // Строительство
        KNOWLEDGE.put("строительство", "Лучшие блоки для строительства: гладкий камень, кирпич, дерево. Используй разные блоки для красоты! Стекло, заборы и ступеньки делают дома уютными.");
        KNOWLEDGE.put("дом", "Хороший дом должен быть освещён (факелы или лампы), иметь кровать, сундуки и верстак. Не забудь про дверь — от зомби спасёт!");

        // Зачарования
        KNOWLEDGE.put("зачарование", "Зачарования делают предметы мощнее! Нужен стол зачарований и лазурит. Лучшие: Острота 5 на меч, Защита 4 на броню, Удача 3 на кирку!");
        KNOWLEDGE.put("enchanting", "Enchantments make items powerful! You need an enchanting table and lapis lazuli. Best ones: Sharpness 5 on sword, Protection 4 on armor, Fortune 3 on pickaxe!");

        // Фермерство
        KNOWLEDGE.put("ферма", "Фермы дают бесконечную еду! Пшеница, морковь, картофель — самые простые. Нужна вода рядом с землёй. Автоматические фермы с поршнями — высший уровень!");
        KNOWLEDGE.put("животные", "Животных можно приручить! Лошади — седло, собаки — кость, кошки — рыба. Разводи животных для еды и ресурсов!");

        // Редстоун
        KNOWLEDGE.put("редстоун", "Редстоун — это электричество Майнкрафта! Можно делать автоматические фермы, ловушки, двери с паролем. Это целая наука, начни с простых рычагов и кнопок!");

        // Общее
        KNOWLEDGE.put("привет", "Привет! Я Костян, твой верный пёс-помощник! Спроси меня что угодно про Майнкрафт, я всё знаю! Гав!");
        KNOWLEDGE.put("помощь", "Я могу помочь с чем угодно! Спроси про: еду, крафт, мобов, биомы, строительство, зачарования, редстоун или что-то ещё!");
        KNOWLEDGE.put("кто ты", "Я Костян! Бессмертная прирученная собака, знаток Майнкрафта и твой лучший друг. Я всегда защищу тебя и отвечу на любой вопрос!");
        KNOWLEDGE.put("спасибо", "Всегда пожалуйста, хозяин! Гав-гав! Ради тебя всё что угодно!");
    }

    public KostyanEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
        this.setMaxUpStep(1.0f);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 2048.0)        // Практически бессмертен
                .add(Attributes.MOVEMENT_SPEED, 0.35)
                .add(Attributes.ATTACK_DAMAGE, 100.0)       // Вырубает с одного удара
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
                .add(Attributes.FOLLOW_RANGE, 64.0)
                .add(Attributes.ARMOR, 30.0);
    }

    @Override
    protected void registerGoals() {
        // Всегда следует за игроком
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2, true));
        this.goalSelector.addGoal(3, new FollowOwnerGoal());
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        // Атакует всех монстров рядом с игроком
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Monster.class, true));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        // Костян бессмертен — восстанавливает здоровье до максимума
        this.setHealth(this.getMaxHealth());

        if (!this.level().isClientSide) {
            if (source.getEntity() instanceof Player attacker) {
                attacker.sendSystemMessage(Component.literal("§cКостян: Ха! Меня не убить! Я БЕССМЕРТНЫЙ! Гав!"));
            }
        }
        return false; // Урон не проходит
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false; // Никогда не исчезает
    }

    @Override
    public boolean isPersistenceRequired() {
        return true; // Всегда сохраняется
    }

    /**
     * Привязываем Костяна к игроку при спауне
     */
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    public void setOwner(Player player) {
        this.ownerUUID = player.getUUID();
        this.ownerPlayer = player;
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    /**
     * Главный метод — ответы на вопросы игрока
     */
    public void answerQuestion(ServerPlayer player, String question) {
        String lowerQuestion = question.toLowerCase().trim();
        String answer = null;

        // Ищем ответ по ключевым словам
        for (Map.Entry<String, String> entry : KNOWLEDGE.entrySet()) {
            if (lowerQuestion.contains(entry.getKey())) {
                answer = entry.getValue();
                break;
            }
        }

        if (answer == null) {
            // Специальные ответы
            if (lowerQuestion.contains("как") && lowerQuestion.contains("убить")) {
                answer = "Чтобы убить большинство мобов — нужен хороший меч с зачарованием Острота 5! Не забывай про щит для защиты.";
            } else if (lowerQuestion.contains("где")) {
                answer = "Точное место зависит от чего! Алмазы — на уровне Y -60, крепость — далеко от спауна, деревни — в равнинах и пустынях. Спроси конкретнее!";
            } else if (lowerQuestion.contains("?")) {
                answer = "Хороший вопрос! Я знаю про: алмазы, мобов, крафт, биомы, нижний мир, зачарования, строительство. Спроси конкретнее — отвечу точнее! Гав!";
            } else {
                answer = "Хм, не совсем понял вопрос. Спроси про конкретный аспект Майнкрафта: мобов, ресурсы, крафт, биомы или механики — я всё знаю! Гав-гав!";
            }
        }

        // Отправляем ответ в чат с форматированием
        player.sendSystemMessage(Component.literal("§6[Костян] §f" + answer));
    }

    /**
     * Приветственное сообщение при спауне
     */
    public void sendGreeting(Player player) {
        if (!this.level().isClientSide && player instanceof ServerPlayer serverPlayer) {
            serverPlayer.sendSystemMessage(Component.literal(
                    "§6[Костян] §fГав! Я появился! Я Костян — твоя бессмертная собака-помощник! " +
                    "Пиши §e/kostyan <вопрос>§f чтобы спросить меня что-нибудь про Майнкрафт!"
            ));
        }
    }

    // Кастомная цель — следование за владельцем
    private class FollowOwnerGoal extends Goal {
        @Override
        public boolean canUse() {
            Player nearest = KostyanEntity.this.level().getNearestPlayer(KostyanEntity.this, 50.0);
            return nearest != null && KostyanEntity.this.distanceTo(nearest) > 4.0;
        }

        @Override
        public void tick() {
            Player nearest = KostyanEntity.this.level().getNearestPlayer(KostyanEntity.this, 50.0);
            if (nearest != null) {
                Vec3 target = nearest.position();
                KostyanEntity.this.getNavigation().moveTo(target.x, target.y, target.z, 1.3);
            }
        }
    }
}
