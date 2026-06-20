# 📤 Как загрузить мод на GitHub

## Шаг 1: Создай репозиторий на GitHub

1. Зайди на [github.com](https://github.com) и войди в аккаунт
2. Нажми зелёную кнопку **"New"** или **"+"** → **"New repository"**
3. Заполни:
   - **Repository name**: `kostyan-mod`
   - **Description**: `Kostyan - виртуальный помощник-собака для Minecraft 1.21.1 Forge`
   - Выбери **Public** (чтобы другие могли скачать)
   - Поставь галочку **"Add a README file"** — НЕ НАДО, README уже есть
4. Нажми **"Create repository"**

---

## Шаг 2: Загрузи файлы

### Способ 1: Через браузер (самый простой)

1. На странице репозитория нажми **"uploading an existing file"**
2. Перетащи ВСЮ папку `kostyan-mod` или выбери все файлы
3. Внизу страницы напиши **"Initial commit"**
4. Нажми **"Commit changes"**

### Способ 2: Через Git (если установлен)

```bash
# В папке kostyan-mod выполни:
git init
git add .
git commit -m "Initial commit: Kostyan Mod v1.0.0"
git branch -M main
git remote add origin https://github.com/ВАШ_ЛОГИН/kostyan-mod.git
git push -u origin main
```

---

## Шаг 3: Структура на GitHub

После загрузки твой репозиторий будет выглядеть так:

```
kostyan-mod/
├── 📁 gradle/wrapper/
├── 📁 src/main/java/com/kostyanmod/
├── 📁 src/main/resources/
├── 📄 build.gradle
├── 📄 settings.gradle
├── 📄 gradle.properties
├── 📄 .gitignore
└── 📄 README.md
```

---

## Шаг 4: Добавь текстуру Костяна

**ВАЖНО:** Перед загрузкой добавь файл текстуры!

1. Скачай [Blockbench](https://www.blockbench.net/) — бесплатный редактор
2. Создай текстуру 64x32 пикселей для собаки
3. Сохрани как `kostyan.png`
4. Положи в `src/main/resources/assets/kostyanmod/textures/entity/kostyan.png`

Или временно используй стандартную текстуру волка из Minecraft (для теста).

---

## ⚠️ Что нужно для компиляции мода

1. **Java JDK 21** — [скачать](https://adoptium.net/)
2. **Forge MDK 1.21.1** — [скачать](https://files.minecraftforge.net/net/minecraftforge/forge/index_1.21.1.html)
3. Команда для сборки: `./gradlew build`

---

## 🎯 Итог

После загрузки у тебя будет:
- ✅ Полный исходный код мода
- ✅ README с инструкцией
- ✅ Готовая структура для компиляции
- ✅ .gitignore (игнорирует build файлы)

Ссылку на репозиторий можно отправить друзьям — они смогут скачать и собрать мод!
