package ru.gpustore.app.data

/** Начальный набор видеокарт, которым заполняется база при первом запуске */
object SeedData {

    val cards: List<GraphicsCard> = listOf(
        GraphicsCard(
            name = "GeForce RTX 4090", brand = "NVIDIA", memoryGb = 24, memoryType = "GDDR6X",
            busBits = 384, powerWatts = 450, price = 189_990,
            description = "Флагман для 4K гейминга, рендеринга и работы с нейросетями. Три вентилятора и усиленная система охлаждения."
        ),
        GraphicsCard(
            name = "GeForce RTX 4080 SUPER", brand = "NVIDIA", memoryGb = 16, memoryType = "GDDR6X",
            busBits = 256, powerWatts = 320, price = 109_990,
            description = "Мощная карта для игр в 4K с трассировкой лучей и технологией DLSS 3."
        ),
        GraphicsCard(
            name = "GeForce RTX 4070 Ti SUPER", brand = "NVIDIA", memoryGb = 16, memoryType = "GDDR6X",
            busBits = 256, powerWatts = 285, price = 84_990,
            description = "Оптимальный выбор для игр в 1440p и 4K с высокими настройками графики."
        ),
        GraphicsCard(
            name = "GeForce RTX 4060", brand = "NVIDIA", memoryGb = 8, memoryType = "GDDR6",
            busBits = 128, powerWatts = 115, price = 29_990,
            description = "Экономичная видеокарта для комфортной игры в Full HD с поддержкой DLSS 3."
        ),
        GraphicsCard(
            name = "GeForce RTX 3060", brand = "NVIDIA", memoryGb = 12, memoryType = "GDDR6",
            busBits = 192, powerWatts = 170, price = 28_990,
            description = "Проверенная временем модель с большим объемом памяти для игр и монтажа видео."
        ),
        GraphicsCard(
            name = "GeForce GTX 1660 SUPER", brand = "NVIDIA", memoryGb = 6, memoryType = "GDDR6",
            busBits = 192, powerWatts = 125, price = 17_990,
            description = "Недорогая карта для киберспортивных игр и офисных задач без трассировки лучей."
        ),
        GraphicsCard(
            name = "Radeon RX 7900 XTX", brand = "AMD", memoryGb = 24, memoryType = "GDDR6",
            busBits = 384, powerWatts = 355, price = 94_990,
            description = "Топовая карта AMD на архитектуре RDNA 3 с огромным запасом видеопамяти."
        ),
        GraphicsCard(
            name = "Radeon RX 7800 XT", brand = "AMD", memoryGb = 16, memoryType = "GDDR6",
            busBits = 256, powerWatts = 263, price = 54_990,
            description = "Отличный баланс цены и производительности для игр в 1440p."
        ),
        GraphicsCard(
            name = "Radeon RX 7600", brand = "AMD", memoryGb = 8, memoryType = "GDDR6",
            busBits = 128, powerWatts = 165, price = 27_990,
            description = "Современная карта для игр в Full HD с низким уровнем шума."
        ),
        GraphicsCard(
            name = "Radeon RX 6600", brand = "AMD", memoryGb = 8, memoryType = "GDDR6",
            busBits = 128, powerWatts = 132, price = 21_990,
            description = "Доступная и энергоэффективная карта для Full HD гейминга."
        ),
        GraphicsCard(
            name = "Arc A770", brand = "Intel", memoryGb = 16, memoryType = "GDDR6",
            busBits = 256, powerWatts = 225, price = 32_990,
            description = "Видеокарта Intel с большим объемом памяти и аппаратным ускорением AV1."
        ),
        GraphicsCard(
            name = "Arc A580", brand = "Intel", memoryGb = 8, memoryType = "GDDR6",
            busBits = 256, powerWatts = 185, price = 19_990,
            description = "Бюджетная карта Intel для игр в Full HD и работы с видео."
        )
    )
}
