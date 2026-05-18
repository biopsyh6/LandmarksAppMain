package com.pavlusha.landmarksapp

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.pavlusha.data.model.remote.ARAnnotationRemoteModel
import com.pavlusha.data.model.remote.ARContentRemoteModel
import com.pavlusha.data.model.remote.ExternalInfoRemoteModel
import com.pavlusha.data.model.remote.GeoFenceRemoteModel
import com.pavlusha.data.model.remote.HistoricalPeriodRemoteModel
import com.pavlusha.data.model.remote.LandmarkCategoryRemoteModel
import com.pavlusha.data.model.remote.LandmarkRemoteModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

fun uploadInitialDataToFirestore() {
    CoroutineScope(Dispatchers.IO).launch {
        val firestore = FirebaseFirestore.getInstance()

        try {
            val mirId = "mir"

            val mirLandmark = LandmarkRemoteModel(
                id = mirId,
                name = "Мирский замок",
                description = "Ми́рский за́мок (бел. Мірскі замак), за́мково-па́рковый ко́мплекс «Мир» (бел. замкава-палацавы комплекс «Мір») – оборонительное укрепление и резиденция в городском посёлке (пгт) Мир Кореличского района Гродненской области Беларуси. Памятник архитектуры, внесён в список Всемирного наследия ЮНЕСКО (с 2000 года). Архитектурный комплекс включает в себя замок XVI-XX веков, валы XVII-XVIII веков, пруд 1896–1898 годов, часовню-усыпальницу Святополк-Мирских с домом сторожа и воротами, пейзажный и регулярный парки, дом управляющего. Находится в пгт Мир, на правом берегу реки Миранки.",
                shortDescription = "Ми́рский за́мок, за́мково-па́рковый ко́мплекс «Мир» (бел. замкава-палацавы комплекс «Мір») – оборонительное укрепление и резиденция в городском посёлке (пгт) Мир Кореличского района Гродненской области Беларуси. Памятник архитектуры, внесён в список Всемирного наследия ЮНЕСКО (с 2000 года).",
                latitude = 53.45113,
                longitude = 26.47289,
                accuracyRadius = 150,
                category = LandmarkCategoryRemoteModel(
                    id = "cat_historic",
                    name = "Исторический памятник",
                    iconName = "ic_castle",
                    colorHex = "#8B4513",
                    type = "HISTORIC_SITE",
                    rawTypeName = "historic_site"
                ),
                remoteModel3dPath = "https://biopsyh6.github.io/ar-landmarks-content/models/mir/mir_castle.glb",
                remoteMainImageUrl = "https://biopsyh6.github.io/ar-landmarks-content/images/mir/main.jpeg",
                thumbnailUrl = "https://biopsyh6.github.io/ar-landmarks-content/images/mir/thumbnailUrl.jpeg",
                galleryUrls = listOf(
                    "https://biopsyh6.github.io/ar-landmarks-content/images/mir/gallery_1.jpg",
                    "https://biopsyh6.github.io/ar-landmarks-content/images/mir/gallery_2.jpg"
                ),
                tags = listOf("замок", "юнеско", "история", "16 век"),
                sourceUrls = listOf("https://mirzamak.by/"),
                historicalPeriods = listOf(
                    HistoricalPeriodRemoteModel(
                        id = "mir_p1",
                        name = "Владение Ильиничей",
                        yearFrom = 1520,
                        yearTo = 1568,
                        description = "Строительство готического замка Юрием Ильиничем.",
                        model3dPath = null
                    )
                ),
                externalInfo = listOf(
                    ExternalInfoRemoteModel(
                        sourceName = "Wikipedia",
                        webUrl = "https://ru.wikipedia.org/wiki/%D0%9C%D0%B8%D1%80%D1%81%D0%BA%D0%B8%D0%B9_%D0%B7%D0%B0%D0%BC%D0%BE%D0%BA",
                        summary = "Оборонительное сооружение и резиденция в городском поселке Мир.",
                        rating = 4.8f
                    )
                ),
                isPromoted = true,
                source = "REMOTE_API"
            )

            val mirArConfig = ARContentRemoteModel(
                landmarkId = mirId,
                displayMode = "CURRENT",
                placementType = "CENTER_SCREEN_HIT",
                activeModel3dPath = mirLandmark.remoteModel3dPath,
                selectedPeriodId = null,
                textAnnotations = listOf(
                    ARAnnotationRemoteModel(
                        text = "Главная башня",
                        positionX = 0.0f,
                        positionY = 1.2f,
                        positionZ = 0.0f,
                        colorHex = "#FFFFFF"
                    )
                ),
                modelScale = 0.5f,
                heightOffset = 0.0f,
                rotationDegrees = 0.0f,
                showDistance = true,
                showCategory = true,
                showPeriodName = true
            )

            val mirGeoFence = GeoFenceRemoteModel(
                landmarkId = mirId,
                latitude = mirLandmark.latitude,
                longitude = mirLandmark.longitude,
                radiusMeters = 200f
            )

            // =======================================================
            // 2. НАЦИОНАЛЬНАЯ БИБЛИОТЕКА (library)
            // =======================================================
            val libraryId = "library"

            val libraryLandmark = LandmarkRemoteModel(
                id = libraryId,
                name = "Национальная библиотека Беларуси",
                description = "Национальная библиотека Беларуси (полное название – Государственное учреждение «Национальная библиотека Беларуси», бел. Дзяржаўная ўстанова «Нацыянальная бібліятэка Беларусі») – главная универсальная научная библиотека Беларуси. Генеральным директором библиотеки является Вадим Францевич Гигин. Здание представляет собой ромбокубооктаэдр (в просторечии называется «алмаз») высотой 73,7 м (23 этажа) и весом 115 000 тонн (не считая книг). Площадь застройки составляет 19,5 тыс. м²; общая площадь здания – 113,7 тыс. м², в том числе книгохранилища – 54,9 тыс. м²; строительный объём здания – 421,6 тыс. м³, в том числе фондохранилища – 200,6 тыс. м³. Новое здание оборудовано автоматизированной системой доставки литературы «Телелифт», состоящей из монорельсов и контейнеров грузоподъёмностью от 5 до 10 кг.",
                shortDescription = "Национальная библиотека Беларуси (полное название – Государственное учреждение «Национальная библиотека Беларуси», бел. Дзяржаўная ўстанова «Нацыянальная бібліятэка Беларусі») – главная универсальная научная библиотека Беларуси. Генеральным директором библиотеки является Вадим Францевич Гигин.",
                latitude = 53.9314,
                longitude = 27.6462,
                accuracyRadius = 100,
                category = LandmarkCategoryRemoteModel(
                    id = "cat_architecture",
                    name = "Современная архитектура",
                    iconName = "ic_building",
                    colorHex = "#4682B4",
                    type = "ARCHITECTURE",
                    rawTypeName = "modern_architecture"
                ),
                remoteModel3dPath = "https://biopsyh6.github.io/ar-landmarks-content/models/library/national_library.glb",
                remoteMainImageUrl = "https://biopsyh6.github.io/ar-landmarks-content/images/library/main.jpg",
                thumbnailUrl = "https://biopsyh6.github.io/ar-landmarks-content/images/library/thumbnailUrl.jpg",
                galleryUrls = listOf(
                    "https://biopsyh6.github.io/ar-landmarks-content/images/library/gallery_1.jpg",
                    "https://biopsyh6.github.io/ar-landmarks-content/images/library/gallery_2.jpg"
                ),
                tags = listOf("библиотека", "минск", "алмаз", "смотровая площадка"),
                sourceUrls = listOf("https://www.nlb.by/"),
                historicalPeriods = listOf(),
                externalInfo = listOf(
                    ExternalInfoRemoteModel(
                        sourceName = "Wikipedia",
                        webUrl = "https://ru.wikipedia.org/wiki/%D0%9D%D0%B0%D1%86%D0%B8%D0%BE%D0%BD%D0%B0%D0%BB%D1%8C%D0%BD%D0%B0%D1%8F_%D0%B1%D0%B8%D0%B1%D0%BB%D0%B8%D0%BE%D1%82%D0%B5%D0%BA%D0%B0_%D0%91%D0%B5%D0%BB%D0%B0%D1%80%D1%83%D1%81%D0%B8",
                        summary = "Национальная научная публичная библиотека.",
                        rating = 4.9f
                    )
                ),
                isPromoted = true,
                source = "REMOTE_API"
            )

            val libraryArConfig = ARContentRemoteModel(
                landmarkId = libraryId,
                displayMode = "CURRENT",
                placementType = "CENTER_SCREEN_HIT",
                activeModel3dPath = libraryLandmark.remoteModel3dPath,
                selectedPeriodId = null,
                textAnnotations = listOf(
                    ARAnnotationRemoteModel(
                        text = "Смотровая площадка",
                        positionX = 0.0f,
                        positionY = 2.0f,
                        positionZ = 0.0f,
                        colorHex = "#FFD700"
                    )
                ),
                modelScale = 0.3f,
                heightOffset = -0.5f,
                rotationDegrees = 45.0f,
                showDistance = true,
                showCategory = true,
                showPeriodName = false
            )

            val libraryGeoFence = GeoFenceRemoteModel(
                landmarkId = libraryId,
                latitude = libraryLandmark.latitude,
                longitude = libraryLandmark.longitude,
                radiusMeters = 300f
            )

            // =======================================================
            // 3. ОСТРОВ СЛЁЗ (island_of_tears)
            // =======================================================
            val islandId = "island_of_tears"

            val islandLandmark = LandmarkRemoteModel(
                id = islandId,
                name = "Остров Мужества и Скорби",
                description = "«Остров Мужества и Скорби» (бел. Востраў Мужнасці і Смутку, также «остров слёз») – мемориал, посвящённый белорусским воинам-интернационалистам, павшим в Афганистане в 1979–1989 гг. В войне принимало участие более 30 000 белорусов, из них погибло 789 человек, пропало без вести 12 человек, остались инвалидами 718 человек. Комплекс расположен на искусственном острове на реке Свислочь, в самом центре Старого Минска, рядом с Троицким предместьем. Строительство было начато ещё в 1988 году, когда Афганская война ещё не закончилась. Полностью комплекс был открыт 3 августа 1996 года. В основу очертаний Храма положен первоначальный облик Храма Ефросиньи Полоцкой, такой, каким он был в XII веке. Сам мемориальный комплекс был возведен по проекту группы архитекторов и скульпторов под руководством Юрия Павлова, чья работа была признана лучшей по результатам конкурса.",
                shortDescription = "«Остров Мужества и Скорби» (бел. Востраў Мужнасці і Смутку, также «остров слёз») – мемориал, посвящённый белорусским воинам-интернационалистам, павшим в Афганистане в 1979–1989 гг. В войне принимало участие более 30 000 белорусов, из них погибло 789 человек, пропало без вести 12 человек, остались инвалидами 718 человек.",
                latitude = 53.9098,
                longitude = 27.5546,
                accuracyRadius = 50,
                category = LandmarkCategoryRemoteModel(
                    id = "cat_monument",
                    name = "Мемориал",
                    iconName = "ic_monument",
                    colorHex = "#2F4F4F",
                    type = "MONUMENT",
                    rawTypeName = "memorial"
                ),
                remoteModel3dPath = "",
                remoteMainImageUrl = "https://biopsyh6.github.io/ar-landmarks-content/images/island_of_tears/main.jpg",
                thumbnailUrl = "https://biopsyh6.github.io/ar-landmarks-content/images/island_of_tears/thumbnailUrl.jpg",
                galleryUrls = listOf("https://biopsyh6.github.io/ar-landmarks-content/images/island_of_tears/gallery_1.jpg", "https://biopsyh6.github.io/ar-landmarks-content/images/island_of_tears/gallery_2.jpg"),
                tags = listOf("мемориал", "минск", "свислочь", "памятник"),
                sourceUrls = listOf("https://minsk.gov.by/ru/freepage/tourism/island/"),
                historicalPeriods = emptyList(),
                externalInfo = listOf(ExternalInfoRemoteModel(
                    sourceName = "Wikipedia",
                    webUrl = "https://ru.wikipedia.org/wiki/%D0%9E%D1%81%D1%82%D1%80%D0%BE%D0%B2_%D0%9C%D1%83%D0%B6%D0%B5%D1%81%D1%82%D0%B2%D0%B0_%D0%B8_%D0%A1%D0%BA%D0%BE%D1%80%D0%B1%D0%B8",
                    summary = "Остров слез.",
                    rating = 4.9f
                )),
                isPromoted = true,
                source = "REMOTE_API"
            )

            val islandArConfig = ARContentRemoteModel(
                landmarkId = islandId,
                displayMode = "CURRENT",
                placementType = "CENTER_SCREEN_HIT",
                activeModel3dPath = islandLandmark.remoteModel3dPath,
                selectedPeriodId = null,
                textAnnotations = listOf(ARAnnotationRemoteModel(
                    text = "Остров",
                    positionX = 0.0f,
                    positionY = 2.0f,
                    positionZ = 0.0f,
                    colorHex = "#FFD700"
                )),
                modelScale = 0.8f,
                heightOffset = 0.0f,
                rotationDegrees = 0.0f,
                showDistance = true,
                showCategory = true,
                showPeriodName = false
            )

            val islandGeoFence = GeoFenceRemoteModel(
                landmarkId = islandId,
                latitude = islandLandmark.latitude,
                longitude = islandLandmark.longitude,
                radiusMeters = 80f
            )

            // =======================================================
            // 4. БИГ БЕН (bigben)
            // =======================================================
            val bigbenId = "bigben"

            val bigbenLandmark = LandmarkRemoteModel(
                id = bigbenId,
                name = "Биг-Бен",
                description = "Биг-Бен (англ. Big Ben) – обиходное туристическое название часовой башни Вестминстерского дворца; один из самых узнаваемых символов Великобритании, часто используемый в популярной культуре. Официальное название объекта с 2012 года – Елизаве́тинская башня или Башня Елизаве́ты (Elizabeth Tower). Изначально «Биг-Бен» являлось названием самого большого из пяти колоколов, однако часто это название расширительно относят и к часам, и к часовой башне в целом. На момент отливки Биг-Бен, масса которого составляла 13,7 тонны, был самым большим и тяжёлым колоколом Соединённого Королевства. В 1881 году уступил первенство колоколу Большой Пол (17 тонн).",
                shortDescription = "Биг-Бен (англ. Big Ben) – обиходное туристическое название часовой башни Вестминстерского дворца; один из самых узнаваемых символов Великобритании, часто используемый в популярной культуре. Официальное название объекта с 2012 года – Елизаве́тинская башня или Башня Елизаве́ты (Elizabeth Tower).",
                latitude = 51.5007,
                longitude = -0.1248,
                accuracyRadius = 50,
                category = LandmarkCategoryRemoteModel(
                    id = "cat_architecture",
                    name = "Историческая архитектура",
                    iconName = "ic_building",
                    colorHex = "#4682B4",
                    type = "ARCHITECTURE",
                    rawTypeName = "historic_architecture"
                ),
                remoteModel3dPath = "https://biopsyh6.github.io/ar-landmarks-content/models/bigben/clock_tower_big_ben.glb",
                remoteMainImageUrl = "https://biopsyh6.github.io/ar-landmarks-content/images/bigben/main.jpg",
                thumbnailUrl = "https://biopsyh6.github.io/ar-landmarks-content/images/bigben/thumbnailUrl.jpg",
                galleryUrls = listOf("https://biopsyh6.github.io/ar-landmarks-content/images/bigben/gallery_1.jpg", "https://biopsyh6.github.io/ar-landmarks-content/images/bigben/gallery_2.jpg"),
                tags = listOf("лондон", "часовая башня", "великобритания", "биг-бен"),
                sourceUrls = listOf("https://www.parliament.uk/bigben/"),
                historicalPeriods = emptyList(),
                externalInfo = listOf(ExternalInfoRemoteModel(
                    sourceName = "Wikipedia",
                    webUrl = "https://ru.wikipedia.org/wiki/%D0%91%D0%B8%D0%B3-%D0%91%D0%B5%D0%BD",
                    summary = "Часовая башня Вестминстерского дворца в Лондоне.",
                    rating = 4.9f
                )),
                isPromoted = true,
                source = "REMOTE_API"
            )

            val bigbenArConfig = ARContentRemoteModel(
                landmarkId = bigbenId,
                displayMode = "CURRENT",
                placementType = "CENTER_SCREEN_HIT",
                activeModel3dPath = bigbenLandmark.remoteModel3dPath,
                selectedPeriodId = null,
                textAnnotations = listOf(ARAnnotationRemoteModel(
                    text = "Знаменитый циферблат",
                    positionX = 0.0f,
                    positionY = 1.5f,
                    positionZ = 0.0f,
                    colorHex = "#FFFFFF"
                )),
                modelScale = 0.5f,
                heightOffset = 0.0f,
                rotationDegrees = 0.0f,
                showDistance = true,
                showCategory = true,
                showPeriodName = false
            )

            val bigbenGeoFence = GeoFenceRemoteModel(
                landmarkId = bigbenId,
                latitude = bigbenLandmark.latitude,
                longitude = bigbenLandmark.longitude,
                radiusMeters = 150f
            )

            // =======================================================
            // 5. Свято-Духов собор (Минск) (church_nemiga)
            // =======================================================

            val nemigaChurchId = "church_nemiga"

            val nemigaChurchLandmark = LandmarkRemoteModel(
                id = nemigaChurchId,
                name = "Свято-Духов собор (Минск)",
                description = "Свято-Духов кафедральный собор – православный храм в Минске, кафедральный собор Белорусского экзархата Русской Православной церкви. Бывший костёл (католический храм) монастыря бернардинок; одна из главных достопримечательностей Верхнего города. История собора началась в 1633–1642 годах, когда было построено здание, служившее храмом католического монастыря бернардинок. Инициатором строительства выступил трокский воевода Александр Слушка. Монастырь был образован на месте церкви Косьмы и Дамиана. 31 августа 1687 года состоялось освящение костёла в честь Рождества Девы Марии. В то же время к северу от храма был построен П-образный монастырский корпус. Главный вход в монастырь первоначально находился с противоположной от храма стороны.",
                shortDescription = "Свято-Духов кафедральный собор – православный храм в Минске, кафедральный собор Белорусского экзархата Русской Православной церкви. Бывший костёл (католический храм) монастыря бернардинок; одна из главных достопримечательностей Верхнего города.",
                latitude = 53.90518,
                longitude = 27.5559,
                accuracyRadius = 50,
                category = LandmarkCategoryRemoteModel(
                    id = "cat_religious",
                    name = "Религиозный объект",
                    iconName = "ic_church",
                    colorHex = "#FFD700",
                    type = "RELIGIOUS",
                    rawTypeName = "cathedral"
                ),
                remoteModel3dPath = "https://biopsyh6.github.io/ar-landmarks-content/models/church_nemiga/%D0%A1%D0%BE%D0%B1%D0%BE%D1%80+%D0%A1%D0%BE%D1%88%D0%B5%D1%81%D1%82%D0%B2%D0%B8%D1%8F+%D0%A1%D0%B2%D1%8F%D1%82%D0%BE%D0%B3%D0%BE+%D0%94%D1%83%D1%85%D0%B0.glb",
                remoteMainImageUrl = "https://biopsyh6.github.io/ar-landmarks-content/images/church_nemiga/main.jpg",
                thumbnailUrl = "https://biopsyh6.github.io/ar-landmarks-content/images/church_nemiga/thumbnailUrl.jpg",
                galleryUrls = listOf("https://biopsyh6.github.io/ar-landmarks-content/images/church_nemiga/gallery_1.jpg", "https://biopsyh6.github.io/ar-landmarks-content/images/church_nemiga/gallery_2.jpg"),
                tags = listOf("храм", "собор", "минск", "немига", "верхний город", "православие"),
                sourceUrls = listOf("https://sobor.minsk.by/"),
                historicalPeriods = emptyList(),
                externalInfo = listOf(ExternalInfoRemoteModel(
                    sourceName = "Wikipedia",
                    webUrl = "https://ru.wikipedia.org/wiki/%D0%A1%D0%B2%D1%8F%D1%82%D0%BE-%D0%94%D1%83%D1%85%D0%BE%D0%B2_%D1%81%D0%BE%D0%B1%D0%BE%D1%80_(%D0%9C%D0%B8%D0%BD%D1%81%D0%BA)",
                    summary = "Кафедральный собор Минска, бывший костёл бернардинок.",
                    rating = 4.8f
                )),
                isPromoted = true,
                source = "REMOTE_API"
            )

            val nemigaChurchArConfig = ARContentRemoteModel(
                landmarkId = nemigaChurchId,
                displayMode = "CURRENT",
                placementType = "CENTER_SCREEN_HIT",
                activeModel3dPath = nemigaChurchLandmark.remoteModel3dPath,
                selectedPeriodId = null,
                textAnnotations = listOf(ARAnnotationRemoteModel(
                    text = "Главный фасад",
                    positionX = 0.0f,
                    positionY = 1.0f,
                    positionZ = 0.5f,
                    colorHex = "#FFFFFF"
                )),
                modelScale = 0.6f,
                heightOffset = 0.0f,
                rotationDegrees = 0.0f,
                showDistance = true,
                showCategory = true,
                showPeriodName = false
            )

            val nemigaChurchGeoFence = GeoFenceRemoteModel(
                landmarkId = nemigaChurchId,
                latitude = nemigaChurchLandmark.latitude,
                longitude = nemigaChurchLandmark.longitude,
                radiusMeters = 100f
            )

            // =======================================================
            // 6. Колизей (colosseum)
            // =======================================================
            val colosseumId = "colosseum"

            val colosseumLandmark = LandmarkRemoteModel(
                id = colosseumId,
                name = "Колизей",
                description = "Колизе́й (лат. Colosseus, colosseum – исполинский), или амфитеатр Флавиев (лат. Amphitheatrum Flavium) – амфитеатр, памятник архитектуры Древнего Рима, наиболее известное и одно из самых грандиозных сооружений Древнего мира, сохранившихся до нашего времени. Находится в Риме, в низине между Эсквилинским, Палатинским и Целиевым холмами. Строительство самого большого амфитеатра античного мира, вместимостью свыше 50 тыс. человек, велось на протяжении восьми лет как коллективное сооружение императоров династии Флавиев. Его начали строить в 72 году н. э. при императоре Веспасиане, а в 80 году н. э. амфитеатр был освящён императором Титом. Амфитеатр расположился на том месте, где был пруд, относившийся к Золотому дому Нерона.",
                shortDescription = "Колизе́й (лат. Colosseus, colosseum – исполинский), или амфитеатр Флавиев (лат. Amphitheatrum Flavium) – амфитеатр, памятник архитектуры Древнего Рима, наиболее известное и одно из самых грандиозных сооружений Древнего мира, сохранившихся до нашего времени. Находится в Риме, в низине между Эсквилинским, Палатинским и Целиевым холмами.",
                latitude = 41.8902,
                longitude = 12.4922,
                accuracyRadius = 150,
                category = LandmarkCategoryRemoteModel(
                    id = "cat_ruins",
                    name = "Руины",
                    iconName = "ic_ruins",
                    colorHex = "#A0522D",
                    type = "RUINS",
                    rawTypeName = "ancient_ruins"
                ),
                remoteModel3dPath = "https://biopsyh6.github.io/ar-landmarks-content/models/colosseum/colosseum.glb",
                remoteMainImageUrl = "https://biopsyh6.github.io/ar-landmarks-content/images/colosseum/main.jpg",
                thumbnailUrl = "https://biopsyh6.github.io/ar-landmarks-content/images/colosseum/thumbnailUrl.jpg",
                galleryUrls = listOf(
                    "https://biopsyh6.github.io/ar-landmarks-content/images/colosseum/gallery_1.jpg",
                    "https://biopsyh6.github.io/ar-landmarks-content/images/colosseum/gallery_2.jpg"
                ),
                tags = listOf("рим", "колизей", "италия", "амфитеатр", "античность", "руины"),
                sourceUrls = listOf("https://colosseo.it/en/area/the-colosseum/"),
                historicalPeriods = emptyList(),
                externalInfo = listOf(ExternalInfoRemoteModel(
                    sourceName = "Wikipedia",
                    webUrl = "https://ru.wikipedia.org/wiki/%D0%9A%D0%BE%D0%BB%D0%B8%D0%B7%D0%B5%D0%B9",
                    summary = "Грандиозный амфитеатр Древнего Рима.",
                    rating = 4.9f
                )),
                isPromoted = true,
                source = "REMOTE_API"
            )

            val colosseumArConfig = ARContentRemoteModel(
                landmarkId = colosseumId,
                displayMode = "CURRENT",
                placementType = "CENTER_SCREEN_HIT",
                activeModel3dPath = colosseumLandmark.remoteModel3dPath,
                selectedPeriodId = null,
                textAnnotations = listOf(ARAnnotationRemoteModel(
                    text = "Арена гладиаторов",
                    positionX = 0.0f,
                    positionY = 0.5f,
                    positionZ = 0.0f,
                    colorHex = "#FFA500"
                )),
                modelScale = 0.3f,
                heightOffset = 0.0f,
                rotationDegrees = 0.0f,
                showDistance = true,
                showCategory = true,
                showPeriodName = false
            )

            val colosseumGeoFence = GeoFenceRemoteModel(
                landmarkId = colosseumId,
                latitude = colosseumLandmark.latitude,
                longitude = colosseumLandmark.longitude,
                radiusMeters = 300f
            )

            // =======================================================
            // 7. Эйфелева башня (eiffel)
            // =======================================================
            val eiffelId = "eiffel"

            val eiffelLandmark = LandmarkRemoteModel(
                id = eiffelId,
                name = "Эйфелева башня",
                description = "Э́йфелева ба́шня (фр. tour Eiffel, – металлическая башня в центре Парижа, самая узнаваемая его архитектурная достопримечательность. Названа в честь главного конструктора Гюстава Эйфеля; сам Эйфель называл её просто «300-метровая башня» (tour de 300 mètres). Башня, впоследствии ставшая символом Парижа, строилась с 1887-го по 1889 год и первоначально задумывалась как временное сооружение, служившее входной аркой парижской Всемирной выставки 1889 года. Эйфелеву башню называют самой посещаемой платной и самой фотографируемой достопримечательностью мира.",
                shortDescription = "Э́йфелева ба́шня (фр. tour Eiffel, – металлическая башня в центре Парижа, самая узнаваемая его архитектурная достопримечательность. Названа в честь главного конструктора Гюстава Эйфеля; сам Эйфель называл её просто «300-метровая башня» (tour de 300 mètres).",
                latitude = 48.8580,
                longitude = 2.2944,
                accuracyRadius = 200,
                category = LandmarkCategoryRemoteModel(
                    id = "cat_monument",
                    name = "Монумент",
                    iconName = "ic_monument",
                    colorHex = "#708090",
                    type = "MONUMENT",
                    rawTypeName = "tower"
                ),
                remoteModel3dPath = "https://biopsyh6.github.io/ar-landmarks-content/models/eiffel/eiffel_tower.glb",
                remoteMainImageUrl = "https://biopsyh6.github.io/ar-landmarks-content/images/eiffel/main.jpg",
                thumbnailUrl = "https://biopsyh6.github.io/ar-landmarks-content/images/eiffel/thumbnailUrl.jpg",
                galleryUrls = listOf("https://biopsyh6.github.io/ar-landmarks-content/images/eiffel/gallery_1.jpg", "https://biopsyh6.github.io/ar-landmarks-content/images/eiffel/gallery_2.jpg"),
                tags = listOf("париж", "франция", "башня", "эйфель", "символ"),
                sourceUrls = listOf("https://www.toureiffel.paris/en"),
                historicalPeriods = emptyList(),
                externalInfo = listOf(ExternalInfoRemoteModel(
                    sourceName = "Wikipedia",
                    webUrl = "https://ru.wikipedia.org/wiki/%D0%AD%D0%B9%D1%84%D0%B5%D0%BB%D0%B5%D0%B2%D0%B0_%D0%B1%D0%B0%D1%88%D0%BD%D1%8F",
                    summary = "Металлическая башня в центре Парижа, построенная в 1889 году.",
                    rating = 4.8f
                )),
                isPromoted = true,
                source = "REMOTE_API"
            )

            val eiffelArConfig = ARContentRemoteModel(
                landmarkId = eiffelId,
                displayMode = "CURRENT",
                placementType = "CENTER_SCREEN_HIT",
                activeModel3dPath = eiffelLandmark.remoteModel3dPath,
                selectedPeriodId = null,
                textAnnotations = listOf(ARAnnotationRemoteModel(
                    text = "Смотровая площадка",
                    positionX = 0.0f,
                    positionY = 2.5f,
                    positionZ = 0.0f,
                    colorHex = "#FFFFFF"
                )),
                modelScale = 0.2f,
                heightOffset = -0.5f,
                rotationDegrees = 0.0f,
                showDistance = true,
                showCategory = true,
                showPeriodName = false
            )

            val eiffelGeoFence = GeoFenceRemoteModel(
                landmarkId = eiffelId,
                latitude = eiffelLandmark.latitude,
                longitude = eiffelLandmark.longitude,
                radiusMeters = 400f
            )

            // =======================================================
            // 8. Исаакиевский собор (isaac)
            // =======================================================
            val isaacId = "isaac"

            val isaacLandmark = LandmarkRemoteModel(
                id = isaacId,
                name = "Исаакиевский собор",
                description = "Исаа́киевский собо́р (собор преподо́бного Исаа́кия Далма́тского) – крупнейший православный храм в Санкт-Петербурге. Расположен на Исаакиевской площади. Кафедральный собор Санкт-Петербургской епархии с 1858 по 1929 год. С 1928 года имеет статус музея (Государственный музей «Исаакиевский собор»). Современное здание собора является четвёртым петербургским храмом в честь Исаакия Далматского, возведённым на месте собора, спроектированного Антонио Ринальди. Автором проекта четвёртого собора, которому предназначалось стать главной православной святыней империи, стал архитектор Огюст Монферран. Строительство курировал сам Николай I, председателем Комиссии по строительству собора был Карл Опперман.",
                shortDescription = "Исаа́киевский собо́р (собор преподо́бного Исаа́кия Далма́тского) – крупнейший православный храм в Санкт-Петербурге. Расположен на Исаакиевской площади. Кафедральный собор Санкт-Петербургской епархии с 1858 по 1929 год. С 1928 года имеет статус музея (Государственный музей «Исаакиевский собор»).",
                latitude = 59.93391,
                longitude = 30.30647,
                accuracyRadius = 150,
                category = LandmarkCategoryRemoteModel(
                    id = "cat_religious",
                    name = "Религиозный объект",
                    iconName = "ic_church",
                    colorHex = "#FFD700",
                    type = "RELIGIOUS",
                    rawTypeName = "cathedral"
                ),
                remoteModel3dPath = "https://biopsyh6.github.io/ar-landmarks-content/models/isaac/stisaaccathedral.glb",
                remoteMainImageUrl = "https://biopsyh6.github.io/ar-landmarks-content/images/isaac/main.jpg",
                thumbnailUrl = "https://biopsyh6.github.io/ar-landmarks-content/images/isaac/thumbnailUrl.jpg",
                galleryUrls = listOf(
                    "https://biopsyh6.github.io/ar-landmarks-content/images/isaac/gallery_1.JPG",
                    "https://biopsyh6.github.io/ar-landmarks-content/images/isaac/gallery_2.JPG"
                ),
                tags = listOf("санкт-петербург", "россия", "собор", "храм", "архитектура"),
                sourceUrls = listOf("https://www.cathedral.ru/ru"),
                historicalPeriods = emptyList(),
                externalInfo = listOf(ExternalInfoRemoteModel(
                    sourceName = "Wikipedia",
                    webUrl = "https://ru.wikipedia.org/wiki/%D0%98%D1%81%D0%B0%D0%B0%D0%BA%D0%B8%D0%B5%D0%B2%D1%81%D0%BA%D0%B8%D0%B9_%D1%81%D0%BE%D0%B1%D0%BE%D1%80",
                    summary = "Крупнейший православный храм Санкт-Петербурга.",
                    rating = 4.9f
                )),
                isPromoted = true,
                source = "REMOTE_API"
            )

            val isaacArConfig = ARContentRemoteModel(
                landmarkId = isaacId,
                displayMode = "CURRENT",
                placementType = "CENTER_SCREEN_HIT",
                activeModel3dPath = isaacLandmark.remoteModel3dPath,
                selectedPeriodId = null,
                textAnnotations = listOf(ARAnnotationRemoteModel(
                    text = "Золотой купол",
                    positionX = 0.0f,
                    positionY = 2.0f,
                    positionZ = 0.0f,
                    colorHex = "#FFD700"
                )),
                modelScale = 0.3f,
                heightOffset = -0.5f,
                rotationDegrees = 0.0f,
                showDistance = true,
                showCategory = true,
                showPeriodName = false
            )

            val isaacGeoFence = GeoFenceRemoteModel(
                landmarkId = isaacId,
                latitude = isaacLandmark.latitude,
                longitude = isaacLandmark.longitude,
                radiusMeters = 250f
            )

            // =======================================================
            // 9. Ворота Минска (minsk_gates)
            // =======================================================

            val minskGatesId = "minsk_gates"

            val minskGatesLandmark = LandmarkRemoteModel(
                id = minskGatesId,
                name = "Ворота Минска",
                description = "«Ворота Минска» – архитектурный комплекс на Привокзальной площади в Минске, представляющий собой два 11-этажных здания-башни по углам 5-этажных домов, расположенных симметрично относительно поперечной оси площади (здание вокзала – улица Кирова). Из-за того, что ворота стоят напротив вокзала и вдоль автомобильной магистрали их назвали \"Въездными вратами\". В конце 1940-х годов развернулись работы по реконструкции Привокзальной площади (архитектор Б. Рубаненко), в результате которых был создан архитектурный ансамбль площади в стиле сталинского ампира.",
                shortDescription = "«Ворота Минска» – архитектурный комплекс на Привокзальной площади в Минске, представляющий собой два 11-этажных здания-башни по углам 5-этажных домов, расположенных симметрично относительно поперечной оси площади (здание вокзала – улица Кирова).",
                latitude = 53.892071,
                longitude = 27.551050,
                accuracyRadius = 100,
                category = LandmarkCategoryRemoteModel(
                    id = "cat_architecture",
                    name = "Архитектура",
                    iconName = "ic_building",
                    colorHex = "#4682B4",
                    type = "ARCHITECTURE",
                    rawTypeName = "historic_architecture"
                ),
                remoteModel3dPath = "",
                remoteMainImageUrl = "https://biopsyh6.github.io/ar-landmarks-content/images/minsk_gates/main.jpg",
                thumbnailUrl = "https://biopsyh6.github.io/ar-landmarks-content/images/minsk_gates/thumbnailUrl.jpg",
                galleryUrls = listOf(
                    "https://biopsyh6.github.io/ar-landmarks-content/images/minsk_gates/gallery_1.jpg",
                    "https://biopsyh6.github.io/ar-landmarks-content/images/minsk_gates/gallery_2.jpg"
                ),
                tags = listOf("минск", "беларусь", "сталинский ампир", "башни", "вокзал", "символ"),
                sourceUrls = emptyList(),
                historicalPeriods = emptyList(),
                externalInfo = listOf(ExternalInfoRemoteModel(
                    sourceName = "Wikipedia",
                    webUrl = "https://ru.wikipedia.org/wiki/%D0%92%D0%BE%D1%80%D0%BE%D1%82%D0%B0_%D0%9C%D0%B8%D0%BD%D1%81%D0%BA%D0%B0",
                    summary = "Архитектурный комплекс на Привокзальной площади в Минске.",
                    rating = 4.7f
                )),
                isPromoted = true,
                source = "REMOTE_API"
            )

            val minskGatesArConfig = ARContentRemoteModel(
                landmarkId = minskGatesId,
                displayMode = "CURRENT",
                placementType = "CENTER_SCREEN_HIT",
                activeModel3dPath = minskGatesLandmark.remoteModel3dPath,
                selectedPeriodId = null,
                textAnnotations = listOf(
                    ARAnnotationRemoteModel(
                        text = "Трофейные часы",
                        positionX = -0.5f,
                        positionY = 1.8f,
                        positionZ = 0.0f,
                        colorHex = "#FFFFFF"
                    )
                ),
                modelScale = 0.4f,
                heightOffset = -0.2f,
                rotationDegrees = 0.0f,
                showDistance = true,
                showCategory = true,
                showPeriodName = false
            )

            val minskGatesGeoFence = GeoFenceRemoteModel(
                landmarkId = minskGatesId,
                latitude = minskGatesLandmark.latitude,
                longitude = minskGatesLandmark.longitude,
                radiusMeters = 200f
            )

            // =======================================================
            // 10. Пизанская башня (pisa)
            // =======================================================
            val pisaId = "pisa"

            val pisaLandmark = LandmarkRemoteModel(
                id = pisaId,
                name = "Пизанская башня",
                description = "Пиза́нская ба́шня (итал. Torre pendente di Pisa) – кампанила, часть ансамбля собора Санта-Мария-Ассунта в городе Пиза, получившая всемирную известность благодаря непреднамеренному наклону. Расположена позади собора, является третьей старейшей постройкой на Площади чудес Пизы после самого собора и баптистерия. Наклон башни возник в ходе её строительства, длившегося несколько десятилетий, и в последующее время медленно увеличивался, пока не был стабилизирован (и частично скорректирован) благодаря усилиям по укреплению в конце XX и начале XXI веков.",
                shortDescription = "Пиза́нская ба́шня (итал. Torre pendente di Pisa) – кампанила, часть ансамбля собора Санта-Мария-Ассунта в городе Пиза, получившая всемирную известность благодаря непреднамеренному наклону.",
                latitude = 43.722952,
                longitude = 10.396597,
                accuracyRadius = 100,
                category = LandmarkCategoryRemoteModel(
                    id = "cat_monument",
                    name = "Памятник архитектуры",
                    iconName = "ic_monument",
                    colorHex = "#A0522D",
                    type = "MONUMENT",
                    rawTypeName = "tower"
                ),
                remoteModel3dPath = "https://biopsyh6.github.io/ar-landmarks-content/models/pisa/torre_pisa.glb",
                remoteMainImageUrl = "https://biopsyh6.github.io/ar-landmarks-content/images/pisa/main.jpg",
                thumbnailUrl = "https://biopsyh6.github.io/ar-landmarks-content/images/pisa/thumbnailUrl.jpg",
                galleryUrls = listOf(
                    "https://biopsyh6.github.io/ar-landmarks-content/images/pisa/gallery_1.jpg", "https://biopsyh6.github.io/ar-landmarks-content/images/pisa/gallery_2.jpg"
                ),
                tags = listOf("пиза", "италия", "башня", "падающая башня", "архитектура"),
                sourceUrls = listOf("https://www.opapisa.it/en/square-of-miracles/tower/"),
                historicalPeriods = emptyList(),
                externalInfo = listOf(ExternalInfoRemoteModel(
                    sourceName = "Wikipedia",
                    webUrl = "https://ru.wikipedia.org/wiki/%D0%9F%D0%B8%D0%B7%D0%B0%D0%BD%D1%81%D0%BA%D0%B0%D1%8F_%D0%B1%D0%B0%D1%88%D0%BD%D1%8F",
                    summary = "Знаменитая «падающая» колокольная башня.",
                    rating = 4.8f
                )),
                isPromoted = true,
                source = "REMOTE_API"
            )

            val pisaArConfig = ARContentRemoteModel(
                landmarkId = pisaId,
                displayMode = "CURRENT",
                placementType = "CENTER_SCREEN_HIT",
                activeModel3dPath = pisaLandmark.remoteModel3dPath,
                selectedPeriodId = null,
                textAnnotations = listOf(ARAnnotationRemoteModel(
                    text = "Знаменитый наклон",
                    positionX = 0.5f,
                    positionY = 1.5f,
                    positionZ = 0.0f,
                    colorHex = "#FFFFFF"
                )),
                modelScale = 0.5f,
                heightOffset = -0.5f,
                rotationDegrees = 0.0f,
                showDistance = true,
                showCategory = true,
                showPeriodName = false
            )

            val pisaGeoFence = GeoFenceRemoteModel(
                landmarkId = pisaId,
                latitude = pisaLandmark.latitude,
                longitude = pisaLandmark.longitude,
                radiusMeters = 150f
            )

            // =======================================================
            // 11. Большой сфинкс (sphinx)
            // =======================================================
            val sphinxId = "sphinx"

            val sphinxLandmark = LandmarkRemoteModel(
                id = sphinxId,
                name = "Большой сфинкс",
                description = "Большой сфинкс на западном берегу Нила в Эль-Гизе – древнейшая сохранившаяся на Земле монументальная скульптура. Высечена из монолитной известковой скалы в форме колоссального сфинкса, лежащего на песке, лицу которого, как издавна принято считать, придано портретное сходство с фараоном Хефреном (ок. 2575–2465 гг. до н. э.), погребальная пирамида которого находится поблизости. Длина статуи – 73 метра, высота – 20 метров, что делает её самой большой однокаменной статуей в мире; между передними лапами располагалось небольшое святилище. Предположительная дата постройки – 2559 год до н. э. За время существования Сфинкс утратил бороду фараона, нос и часть головного убора.",
                shortDescription = "Большой сфинкс на западном берегу Нила в Эль-Гизе – древнейшая сохранившаяся на Земле монументальная скульптура. Высечена из монолитной известковой скалы в форме колоссального сфинкса, лежащего на песке, лицу которого, как издавна принято считать, придано портретное сходство с фараоном Хефреном (ок. 2575–2465 гг. до н. э.), погребальная пирамида которого находится поблизости.",
                latitude = 29.975279,
                longitude = 31.137564,
                accuracyRadius = 300,
                category = LandmarkCategoryRemoteModel(
                    id = "cat_ruins",
                    name = "Древность",
                    iconName = "ic_ruins",
                    colorHex = "#D2B48C",
                    type = "RUINS",
                    rawTypeName = "ancient_ruins"
                ),
                remoteModel3dPath = "https://biopsyh6.github.io/ar-landmarks-content/models/sphinx/the_great_sphinx_of_giza_-_egypt.glb",
                remoteMainImageUrl = "https://biopsyh6.github.io/ar-landmarks-content/images/sphinx/main.jpg",
                thumbnailUrl = "https://biopsyh6.github.io/ar-landmarks-content/images/sphinx/thumbnailUrl.JPG",
                galleryUrls = listOf(
                    "https://biopsyh6.github.io/ar-landmarks-content/images/sphinx/gallery_1.jpg", "https://biopsyh6.github.io/ar-landmarks-content/images/sphinx/gallery_2.jpg"
                ),
                tags = listOf("египет", "гиза", "сфинкс", "древность", "скульптура", "пирамиды"),
                sourceUrls = listOf("https://egymonuments.gov.eg/ar/monuments/the-great-sphinx/"),
                historicalPeriods = emptyList(),
                externalInfo = listOf(ExternalInfoRemoteModel(
                    sourceName = "Wikipedia",
                    webUrl = "https://ru.wikipedia.org/wiki/%D0%91%D0%BE%D0%BB%D1%8C%D1%88%D0%BE%D0%B9_%D1%81%D1%84%D0%B8%D0%BD%D0%BA%D1%81",
                    summary = "Древнейшая монументальная скульптура на плато Гиза в Египте.",
                    rating = 4.9f
                )),
                isPromoted = true,
                source = "REMOTE_API"
            )

            val sphinxArConfig = ARContentRemoteModel(
                landmarkId = sphinxId,
                displayMode = "CURRENT",
                placementType = "CENTER_SCREEN_HIT",
                activeModel3dPath = sphinxLandmark.remoteModel3dPath,
                selectedPeriodId = null,
                textAnnotations = listOf(ARAnnotationRemoteModel(
                    text = "Лицо фараона",
                    positionX = 0.0f,
                    positionY = 1.2f,
                    positionZ = 0.0f,
                    colorHex = "#FFFFFF"
                )),
                modelScale = 0.4f,
                heightOffset = -0.3f,
                rotationDegrees = -90.0f,
                showDistance = true,
                showCategory = true,
                showPeriodName = false
            )

            val sphinxGeoFence = GeoFenceRemoteModel(
                landmarkId = sphinxId,
                latitude = sphinxLandmark.latitude,
                longitude = sphinxLandmark.longitude,
                radiusMeters = 500f
            )

            // =======================================================
            // 12. Тадж-Махал (taj_mahal)
            // =======================================================
            val tajMahalId = "taj_mahal"

            val tajMahalLandmark = LandmarkRemoteModel(
                id = tajMahalId,
                name = "Тадж-Махал",
                description = "Тадж-Маха́л (англ. Taj Mahal) – мавзолей-мечеть, находящийся в Агре, Индия, на берегу реки Джамна (архитекторы, вероятно, Устад-Иса и др.) Построен по приказу падишаха империи Великих Моголов Шах Джахана I, в память о жене Мумтаз-Махал, умершей при родах четырнадцатого ребёнка. Позже в мавзолее был похоронен и сам Шах Джахан I. Тадж-Махал (также «Тадж») считается лучшим примером архитектуры стиля моголов, который сочетает в себе элементы индийского, персидского и арабского архитектурных стилей. В 1983 году Тадж-Махал был назван объектом Всемирного наследия ЮНЕСКО: «жемчужиной мусульманского искусства в Индии и одним из всеми признанных шедевров всемирного наследия».",
                shortDescription = "Тадж-Маха́л (англ. Taj Mahal) – мавзолей-мечеть, находящийся в Агре, Индия, на берегу реки Джамна (архитекторы, вероятно, Устад-Иса и др.) Построен по приказу падишаха империи Великих Моголов Шах Джахана I, в память о жене Мумтаз-Махал, умершей при родах четырнадцатого ребёнка. Позже в мавзолее был похоронен и сам Шах Джахан I.",
                latitude = 27.175015,
                longitude = 78.042155,
                accuracyRadius = 250,
                category = LandmarkCategoryRemoteModel(
                    id = "cat_monument",
                    name = "Памятник архитектуры",
                    iconName = "ic_monument",
                    colorHex = "#F5F5DC",
                    type = "MONUMENT",
                    rawTypeName = "mausoleum"
                ),
                remoteModel3dPath = "https://biopsyh6.github.io/ar-landmarks-content/models/taj_mahal/taj_mahal.glb",
                remoteMainImageUrl = "https://biopsyh6.github.io/ar-landmarks-content/images/taj_mahal/main.jpg",
                thumbnailUrl = "https://biopsyh6.github.io/ar-landmarks-content/images/taj_mahal/thumbnailUrl.jpg",
                galleryUrls = listOf(
                    "https://biopsyh6.github.io/ar-landmarks-content/images/taj_mahal/gallery_1.jpg",
                    "https://biopsyh6.github.io/ar-landmarks-content/images/taj_mahal/gallery_2.jpg"
                ),
                tags = listOf("индия", "агра", "тадж-махал", "мавзолей", "архитектура"),
                sourceUrls = listOf("https://www.tajmahal.gov.in/"),
                historicalPeriods = emptyList(),
                externalInfo = listOf(ExternalInfoRemoteModel(
                    sourceName = "Wikipedia",
                    webUrl = "https://ru.wikipedia.org/wiki/%D0%A2%D0%B0%D0%B4%D0%B6-%D0%9C%D0%B0%D1%85%D0%B0%D0%BB",
                    summary = "Мавзолей-мечеть в Агре, построенный Шах-Джаханом.",
                    rating = 4.9f
                )),
                isPromoted = true,
                source = "REMOTE_API"
            )

            val tajMahalArConfig = ARContentRemoteModel(
                landmarkId = tajMahalId,
                displayMode = "CURRENT",
                placementType = "CENTER_SCREEN_HIT",
                activeModel3dPath = tajMahalLandmark.remoteModel3dPath,
                selectedPeriodId = null,
                textAnnotations = listOf(ARAnnotationRemoteModel(
                    text = "Главный купол",
                    positionX = 0.0f,
                    positionY = 1.5f,
                    positionZ = 0.0f,
                    colorHex = "#FFFFFF"
                )),
                modelScale = 0.25f,
                heightOffset = -0.5f,
                rotationDegrees = 0.0f,
                showDistance = true,
                showCategory = true,
                showPeriodName = false
            )

            val tajMahalGeoFence = GeoFenceRemoteModel(
                landmarkId = tajMahalId,
                latitude = tajMahalLandmark.latitude,
                longitude = tajMahalLandmark.longitude,
                radiusMeters = 400f
            )

            // =======================================================
            // 13. Тауэрский мост (tower_bridge)
            // =======================================================
            val towerBridgeId = "tower_bridge"

            val towerBridgeLandmark = LandmarkRemoteModel(
                id = towerBridgeId,
                name = "Тауэрский мост",
                description = "Та́уэрский мост, или Тауэр-бридж (англ. Tower Bridge, досл.: «Башенный мост») – комбинированный подвесной (висячий) и разводной (раскрывающийся) мост в центре Лондона над рекой Темзой, недалеко от Лондонского Тауэра. Иногда путают с Лондонским мостом, расположенным примерно в 0,8 км выше по течению. Строительство началось в 1886 году; открыт в 1894 году. Также является одним из символов Лондона и Британии. Тауэрский мост – один из пяти лондонских мостов, принадлежащих и обслуживаемых благотворительным фондом Bridge House Estates, контролируемым Корпорацией лондонского Сити. Мост состоит из двух мостовых башен, соединённых на верхнем уровне двумя горизонтальными проходами, предназначенных для противостояния силе горизонтального натяжения, создаваемой подвесными секциями моста на боковых сторонах башен. С 1977 года до реставрации 2010-х годов мост был окрашен в красный, белый и синий цвета. Впоследствии его цвета были восстановлены в синий и белый.",
                shortDescription = "Та́уэрский мост, или Тауэр-бридж (англ. Tower Bridge, досл.: «Башенный мост») – комбинированный подвесной (висячий) и разводной (раскрывающийся) мост в центре Лондона над рекой Темзой, недалеко от Лондонского Тауэра. Иногда путают с Лондонским мостом, расположенным примерно в 0,8 км выше по течению. Строительство началось в 1886 году; открыт в 1894 году. Также является одним из символов Лондона и Британии.",
                latitude = 51.5056,
                longitude = -0.0754,
                accuracyRadius = 150,
                category = LandmarkCategoryRemoteModel(
                    id = "cat_architecture",
                    name = "Историческая архитектура",
                    iconName = "ic_building",
                    colorHex = "#4682B4",
                    type = "ARCHITECTURE",
                    rawTypeName = "bridge"
                ),
                remoteModel3dPath = "https://biopsyh6.github.io/ar-landmarks-content/models/tower_bridge/tower_bridge.glb",
                remoteMainImageUrl = "https://biopsyh6.github.io/ar-landmarks-content/images/tower_bridge/main.jpg",
                thumbnailUrl = "https://biopsyh6.github.io/ar-landmarks-content/images/tower_bridge/thumbnailUrl.jpg",
                galleryUrls = listOf(
                    "https://biopsyh6.github.io/ar-landmarks-content/images/tower_bridge/gallery_1.jpg",
                    "https://biopsyh6.github.io/ar-landmarks-content/images/tower_bridge/gallery_2.jpg"
                ),
                tags = listOf("лондон", "великобритания", "мост", "темза", "тауэр", "архитектура"),
                sourceUrls = listOf("https://www.towerbridge.org.uk/"),
                historicalPeriods = emptyList(),
                externalInfo = listOf(ExternalInfoRemoteModel(
                    sourceName = "Wikipedia",
                    webUrl = "https://ru.wikipedia.org/wiki/%D0%A2%D0%B0%D1%83%D1%8D%D1%80%D1%81%D0%BA%D0%B8%D0%B9_%D0%BC%D0%BE%D1%81%D1%82",
                    summary = "Разводной мост над Темзой, один из символов Лондона.",
                    rating = 4.8f
                )),
                isPromoted = true,
                source = "REMOTE_API"
            )

            val towerBridgeArConfig = ARContentRemoteModel(
                landmarkId = towerBridgeId,
                displayMode = "CURRENT",
                placementType = "CENTER_SCREEN_HIT",
                activeModel3dPath = towerBridgeLandmark.remoteModel3dPath,
                selectedPeriodId = null,
                textAnnotations = listOf(ARAnnotationRemoteModel(
                    text = "Верхние галереи",
                    positionX = 0.0f,
                    positionY = 1.8f,
                    positionZ = 0.0f,
                    colorHex = "#FFFFFF"
                )),
                modelScale = 0.35f,
                heightOffset = 0.0f,
                rotationDegrees = 0.0f,
                showDistance = true,
                showCategory = true,
                showPeriodName = false
            )

            val towerBridgeGeoFence = GeoFenceRemoteModel(
                landmarkId = towerBridgeId,
                latitude = towerBridgeLandmark.latitude,
                longitude = towerBridgeLandmark.longitude,
                radiusMeters = 300f
            )

            // =======================================================
            // 14. Минская ратуша (townhall)
            // =======================================================
            val townhallId = "townhall"

            val townhallLandmark = LandmarkRemoteModel(
                id = townhallId,
                name = "Минская ратуша",
                description = "Минская ратуша (бел. Мінская ратуша) – административное здание (ратуша) в центральной части Минска, на Высоком Рынке; памятник архитектуры классицизма. Снесено властями в 1857 году; воссоздано в 2002-2004 гг. по сохранившимся изображениям. Первая минская ратуша была построена в 1600 году и обладала единственными в городе часами. В конце XVIII в. здание перестроено в стиле классицизма (арх. Ф. Крамер). Прямоугольное в плане здание было накрыто пологой вальмовой крышей. На главном вытянутом фасаде значительно выступала центральная часть с трёхмаршевой лестницей, которая завершалась четвериковой башней, накрытой полусферическим куполом со шпилем. Центральная часть и торцы здания были оформлены 4-колонными ионическими портиками, имевшими по две дополнительные колонны. Декоративное решение здания дополнялось коваными оградами балконов в портиках и на башне.",
                shortDescription = "Минская ратуша (бел. Мінская ратуша) – административное здание (ратуша) в центральной части Минска, на Высоком Рынке; памятник архитектуры классицизма. Снесено властями в 1857 году; воссоздано в 2002-2004 гг. по сохранившимся изображениям.",
                latitude = 53.903559,
                longitude = 27.556145,
                accuracyRadius = 50,
                category = LandmarkCategoryRemoteModel(
                    id = "cat_historic",
                    name = "Историческое здание",
                    iconName = "ic_building",
                    colorHex = "#FFFAF0",
                    type = "HISTORIC_SITE",
                    rawTypeName = "townhall"
                ),
                remoteModel3dPath = "https://biopsyh6.github.io/ar-landmarks-content/models/townhall/ratusha.glb",
                remoteMainImageUrl = "https://biopsyh6.github.io/ar-landmarks-content/images/townhall/main.jpg",
                thumbnailUrl = "https://biopsyh6.github.io/ar-landmarks-content/images/townhall/thumbnailUrl.jpg",
                galleryUrls = listOf(
                    "https://biopsyh6.github.io/ar-landmarks-content/images/townhall/gallery_1.jpg", "https://biopsyh6.github.io/ar-landmarks-content/images/townhall/gallery_2.jpg"
                ),
                tags = listOf("минск", "беларусь", "ратуша", "верхний город", "история", "магдебургское право"),
                sourceUrls = listOf("https://www.minskmuseum.by/minskaya-gorodskaya-ratusha"),
                historicalPeriods = emptyList(),
                externalInfo = listOf(ExternalInfoRemoteModel(
                    sourceName = "Wikipedia",
                    webUrl = "https://ru.wikipedia.org/wiki/%D0%9C%D0%B8%D0%BD%D1%81%D0%BA%D0%B0%D1%8F_%D1%80%D0%B0%D1%82%D1%83%D1%88%D0%B0",
                    summary = "Восстановленное здание ратуши в историческом центре Минска.",
                    rating = 4.7f
                )),
                isPromoted = true,
                source = "REMOTE_API"
            )

            val townhallArConfig = ARContentRemoteModel(
                landmarkId = townhallId,
                displayMode = "CURRENT",
                placementType = "CENTER_SCREEN_HIT",
                activeModel3dPath = townhallLandmark.remoteModel3dPath,
                selectedPeriodId = null,
                textAnnotations = listOf(ARAnnotationRemoteModel(
                    text = "Башенные часы",
                    positionX = 0.0f,
                    positionY = 2.0f,
                    positionZ = 0.5f,
                    colorHex = "#FFD700"
                )),
                modelScale = 0.6f,
                heightOffset = -0.2f,
                rotationDegrees = 0.0f,
                showDistance = true,
                showCategory = true,
                showPeriodName = false
            )

            val townhallGeoFence = GeoFenceRemoteModel(
                landmarkId = townhallId,
                latitude = townhallLandmark.latitude,
                longitude = townhallLandmark.longitude,
                radiusMeters = 100f
            )

            // =======================================================
            // ОТПРАВЛЯЕМ ВСЁ В FIREBASE
            // =======================================================

            val landmarks = listOf(mirLandmark, libraryLandmark, islandLandmark, bigbenLandmark, nemigaChurchLandmark, colosseumLandmark, eiffelLandmark, isaacLandmark, minskGatesLandmark, pisaLandmark, sphinxLandmark, tajMahalLandmark, towerBridgeLandmark, townhallLandmark)
            val arConfigs = listOf(mirArConfig, libraryArConfig, islandArConfig, bigbenArConfig, nemigaChurchArConfig, colosseumArConfig, eiffelArConfig, isaacArConfig, minskGatesArConfig, pisaArConfig, sphinxArConfig, tajMahalArConfig, towerBridgeArConfig, townhallArConfig)
            val geoFences = listOf(mirGeoFence, libraryGeoFence, islandGeoFence, bigbenGeoFence, nemigaChurchGeoFence, colosseumGeoFence, eiffelGeoFence, isaacGeoFence, minskGatesGeoFence, pisaGeoFence, sphinxGeoFence, tajMahalGeoFence, towerBridgeGeoFence, townhallGeoFence)

            for (landmark in landmarks) {
                firestore.collection("landmarks").document(landmark.id).set(landmark).await()
            }
            Log.d("FirebaseSync", "Landmarks uploaded")

            for (arConfig in arConfigs) {
                firestore.collection("ar_configs").document(arConfig.landmarkId).set(arConfig)
                    .await()
            }
            Log.d("FirebaseSync", "AR Configs uploaded")

            for (geoFence in geoFences) {
                firestore.collection("geo_fences").document(geoFence.landmarkId).set(geoFence)
                    .await()
            }
            Log.d("FirebaseSync", "Geo Fences uploaded")

            Log.d("FirebaseSync", "All initial data successfully uploaded to Firestore!")

        } catch (e: Exception) {
            Log.e("FirebaseSync", "Error uploading data", e)
        }
    }
}