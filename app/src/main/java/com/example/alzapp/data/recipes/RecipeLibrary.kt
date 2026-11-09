package com.example.alzapp.data.recipes

import com.example.alzapp.R
import com.example.alzapp.data.recipes.local.RecipeEntity

// all recipes available in the app
// recipes are downloaded with the app itself
object RecipeLibrary {
    fun getInitialRecipes(): List<RecipeEntity> {
        return listOf(
            RecipeEntity(
                name = "Sałatka z komosą",
                imageRes = R.drawable.quinoa_salad,
                category = MealCategory.SALADS_AND_SNACKS,
                ingredients = listOf(
                    "Komosa ryżowa - pół szklanki",
                    "Awokado - pół sztuki",
                    "Pomidorki koktajlowe - 7 sztuk",
                    "Szpinak świeży - 1 duża garść",
                    "Oliwa z oliwek - 1 łyżka",
                    "Sok z cytryny - 1 łyżeczka",
                    "Sól i pieprz - szczypta",
                ),
                steps = listOf(
                    "Wsyp komosę do garnka, zalej szklanką wody i gotuj pod przykryciem przez około 12-15 minut.",
                    "Wyłącz palnik i zostaw komosę do lekkiego przestygnięcia.",
                    "Umyj szpinak i pomidorki. Przekrój pomidorki na pół.",
                    "Awokado przekrój na pół, wyjmij pestkę, a miąższ wyjmij łyżką i pokrój.",
                    "Do miski wsyp ugotowaną komosę.",
                    "Do miski dodaj szpinak, pomidorki oraz awokado.",
                    "Skrop wszystko oliwą z oliwek i sokiem z cytryny.",
                    "Wymieszaj wszystko łyżką i dopraw szczyptą soli i pieprzu",
                    "Sprawdź, czy palnik jest wyłączony."
                ),
                cookingTime = "15 min",
                difficulty = "Łatwe",
                tools = listOf(
                    "Mały garnek z pokrywką",
                    "Miska",
                    "Nóż",
                    "Łyżka",
                    "Łyżeczka",
                    "Szklanka"
                )
            ),
            RecipeEntity(
                name = "Sałatka z brokułów",
                imageRes = R.drawable.broccoli_salad,
                category = MealCategory.SALADS_AND_SNACKS,
                ingredients = listOf(
                    "Brokuł - 1 duży (same różyczki)",
                    "Marchew - 2 sztuki",
                    "Seler naciowy - 2 łodygi",
                    "Jabłko - 1 sztuka",
                    "Czerwona cebula - 1/4 sztuki",
                    "Pestki dyni lub orzechy włoskie - 1/4 szklanki",
                    "Oliwa z oliwek - 1/3 szklanki",
                    "Ocet winny lub jabłkowy - 2-3 łyżki",
                    "Musztarda Dijon - 1 łyżka",
                    "Miód - 2 łyżeczki",
                    "Sól - 3/4 łyżeczki"
                ),
                steps = listOf(
                    "Posiekaj brokuła na małe różyczki i wrzuć do dużej miski.",
                    "Zetrzyj marchewki na tarce.",
                    "Pokrój seler naciowy w drobną kostkę.",
                    "Obierz jabłko i pokrój je w cienkie plasterki.",
                    "Pokrój cebulę w bardzo drobną kostkę.",
                    "Dodaj marchew, seler, jabłko, cebulę oraz pestki dyni do miski z brokułem.",
                    "W małej miseczce wymieszaj oliwę, ocet, musztardę, miód i sól.",
                    "Mieszaj sos energicznie widelcem, aż składniki się połączą.",
                    "Wlej sos do sałatki i dokładnie wymieszaj.",
                    "Wstaw sałatkę do lodówki na godzinę",
                ),
                cookingTime = "20 min + chłodzenie",
                difficulty = "Łatwe",
                tools = listOf(
                    "Duża miska",
                    "Mała miseczka",
                    "Nóż i deska do krojenia",
                    "Widelec",
                    "Obieraczka do warzyw",
                    "Szklanka",
                    "Łyżka",
                    "Łyżeczka",
                    "Tarka"
                ),
            ),
            RecipeEntity(
                name = "Koktajl jagodowy",
                imageRes = R.drawable.huckleberry_smoothie,
                category = MealCategory.DESSERTS,
                ingredients = listOf(
                    "Szpinak - 2 szklanki",
                    "Truskawki - pół szklanki",
                    "Jagody - pół szklanki",
                    "Banan - 1 sztuka",
                    "Mleko - półtorej szklanki",
                    "Serek wiejski - pół szklanki",
                    "Miód - 1 łyżka",
                    "Cynamon - szczypta"
                ),
                steps = listOf(
                    "Umyj wodą szpinak i owoce.",
                    "Wyciągnij bazę blendera i postaw na blacie.",
                    "Wlej półtorej szklanki mleka do kielicha blendera lub do miski.",
                    "Dodaj dwie szklanki szpinaku.",
                    "Wsyp truskawki oraz jagody.",
                    "Obierz banana, przełam go na pół i wrzuć do środka.",
                    "Dodaj pół szklanki serka wiejskiego oraz łyżkę miodu i szczyptę cynamonu.",
                    "Zamknij szczelnie pokrywkę blendera.",
                    "Miksuj, aż masa będzie gładka",
                    "Przelej do ulubionej szklanki.",
                ),
                cookingTime = "5 min",
                difficulty = "Bardzo łatwe",
                tools = listOf(
                    "Blender kielichowy lub ręczny",
                    "Miska",
                    "Szklanka",
                    "Łyżka"
                )
            ),
            RecipeEntity(
                name = "Pieczone udka z warzywami",
                imageRes = R.drawable.chicken_legs_veggies,
                category = MealCategory.MEAT,
                ingredients = listOf(
                    "Udka z kurczaka (bez kości i skóry) - 4 sztuki",
                    "Sól, pieprz, czosnek granulowany, papryka słodka, tymianek - po 1/2 łyżeczki",
                    "Oliwa z oliwek - 2 łyżki",
                    "Ocet winny biały - 1 łyżka",
                    "Pomidorki koktajlowe - 10 sztuk)",
                    "Szparagi - 4 sztuki",
                    "Czerwona papryka - 1 sztuka",
                    "Czerwona cebula - pół sztuki"
                ),
                steps = listOf(
                    "Włącz piekarnik i ustaw temperaturę na 200°C (niech się nagrzewa).",
                    "Oczyść kurczaka z tłuszczu, posyp solą, pieprzem, czosnkiem, papryką i tymiankiem z obu stron.",
                    "Umyj warzywa: pomidorki, szparagi i paprykę.",
                    "Pokrój szparagi i paprykę w drobne kawałki",
                    "Obierz cebulę i pokrój ją w grubszą kostkę.",
                    "Na patelni rozgrzej 2 łyżki oliwy na średnim ogniu.",
                    "Połóż kurczaka na patelni. Smaż przez 4 minuty z każdej strony, aż będzie brązowy.",
                    "Zdejmij kurczaka z patelni na czysty talerz (jeszcze nie jest gotowy do spożycia!!!).",
                    "Wlej na patelnię łyżkę octu i zamieszaj drewnianą łyżką.",
                    "Wrzuć na patelnię cebulę, paprykę i szparagi. Smaż kilka minut, aż zmiękną.",
                    "Dodaj pomidorki i zamieszaj wszystko.",
                    "Przełóż składniki z patelni do naczynia żaroodpornego i połóż na nich kurczaka.",
                    "Wstaw naczynie ostrożnie do nagrzanego piekarnika.",
                    "Piecz przez 20 minut.",
                    "Sprawdź, czy piekarnik jest wyłączony."
                ),
                cookingTime = "45 min",
                difficulty = "Trudne",
                tools = listOf(
                    "Piekarnik",
                    "Patelnia",
                    "Naczynie, które można włożyć do piekarnika",
                    "Drewniana łyżka",
                    "Nóż i deska do krojenia",
                    "Talerz",
                    "Łyżka",
                    "Łyżeczka",
                ),
            ),
            RecipeEntity(
                name = "Chrupiąca kanapka z tuńczykiem",
                imageRes = R.drawable.tuna_sandwich,
                category = MealCategory.SEAFOOD,
                ingredients = listOf(
                    "Bagietka pełnoziarnista - pół sztuki",
                    "Tuńczyk w oliwie - 1 puszka (170g)",
                    "Serek kanapkowy o dowolnym smaku - 1 płaska łyżka",
                    "Sok z cytryny - 2 łyżki",
                    "Czerwona cebula - 1/2 małej sztuki",
                    "Słodkie ogórki konserwowe - 3 małe sztuki",
                    "Papryka marynowana łagodna - 1/4 szklanki",
                    "Oliwa z oliwek - 2 łyżki",
                    "Rukola - 2 szklanki"
                ),
                steps = listOf(
                    "Przekrój bagietkę na pół (wzdłuż).",
                    "Połóż pieczywo na patelni i opiekaj na bardzo małym ogniu, aż będzie lekko zarumienione.",
                    "Sprawdź, czy palnik pod patelnią został wyłączony.",
                    "Otwórz puszkę tuńczyka i starannie odlej tłuszcz do śmieci.",
                    "Przełóż tuńczyka z puszki do miski.",
                    "Dodaj do miski serek kanapkowy, sok z cytryny, cebulę, ogórki oraz paprykę.",
                    "Wszystko dokładnie wymieszaj łyżką.",
                    "Połóż podpieczone pieczywo na desce i skrop je oliwą z oliwek.",
                    "Nałóż przygotowanego tuńczyka na pieczywo.",
                    "Na wierzchu każdej kanapki połóż garść rukoli.",
                ),
                cookingTime = "10 min",
                difficulty = "Łatwe",
                tools = listOf(
                    "Patelnia",
                    "Miska",
                    "Nóż do chleba i deska",
                    "Łyżka"
                ),
            ),
            RecipeEntity(
                name = "Chrupiąca pieczona ciecierzyca",
                imageRes = R.drawable.baked_chickpeas,
                category = MealCategory.SALADS_AND_SNACKS,
                ingredients = listOf(
                    "Ciecierzyca z puszki - 1 sztuka (400g)",
                    "Oliwa z oliwek - 2 łyżki",
                    "Sól - pół łyżeczki",
                    "Pieprz czarny - 1/4 łyżeczki",
                    "Ulubione przyprawy (np. papryka słodka, czosnek) - pół łyżeczki"
                ),
                steps = listOf(
                    "Włącz piekarnik i ustaw temperaturę na 200°C.",
                    "Otwórz puszkę ciecierzycy i wysyp ją na sitko (durszlak).",
                    "Płucz ciecierzycę pod bieżącą wodą.",
                    "Wysyp ciecierzycę na ręcznik papierowy i bardzo dokładnie ją osusz.",
                    "W małej misce wymieszaj osuszoną ciecierzycę z oliwą, solą, pieprzem i przyprawami.",
                    "Wyłóż blachę papierem do pieczenia.",
                    "Wysyp ciecierzycę na blachę i rozłóż ją tak, aby ziarna nie leżały jedno na drugim.",
                    "Ostrożnie wstaw blachę do nagrzanego piekarnika.",
                    "Ustaw minutnik na 25 minut.",
                    "Ostrożnie wyjmij blachę.",
                    "Przesyp do miseczki i sprawdź, czy piekarnik został wyłączony."
                ),
                cookingTime = "50 min",
                difficulty = "Łatwe",
                tools = listOf(
                    "Piekarnik",
                    "Blacha do pieczenia",
                    "Papier do pieczenia lub folia aluminiowa",
                    "Durszlak (sitko)",
                    "Ręcznik papierowy",
                    "Mała miska",
                    "Łyżka",
                    "Łyżeczka"
                ),
            ),
            RecipeEntity(
                name = "Chrupiące grzanki z tuńczykiem",
                imageRes = R.drawable.tuna_toasts,
                category = MealCategory.SEAFOOD,
                ingredients = listOf(
                    "Chleb żytni - 3 kromki",
                    "Oliwa z oliwek - 2 łyżeczki",
                    "Pomidor - 1 duża sztuka",
                    "Tuńczyk w sosie własnym - 1 puszka (120g)",
                    "Cebula dymka - 1 sztuka",
                    "Natka pietruszki - 2 łyżeczki (posiekanej)",
                    "Sok z cytryny - 1 łyżka",
                    "Sól i pieprz - szczypta"
                ),
                steps = listOf(
                    "Otwórz puszkę tuńczyka i przełóż go do miski.",
                    "Umyj pomidora i pokrój go w małą kostkę.",
                    "Posiekaj drobno cebulę dymkę oraz natkę pietruszki.",
                    "Do miski z tuńczykiem dodaj: pomidora, cebulę oraz pietruszkę.",
                    "Do tej samej miski dodaj sok z cytryny, szczyptę soli oraz pieprzu. Wymieszaj i odstaw.",
                    "Połóż kromki chleba na desce i skrop każdą z nich oliwą z obu stron.",
                    "Rozgrzej patelnię na małym ogniu (bez dodatkowego tłuszczu).",
                    "Połóż chleb na patelni. Zapiekaj przez około 2 minuty z każdej strony, aż będzie chrupiący.",
                    "Zdejmij grzanki z patelni i połóż na talerzu.",
                    "Wyłącz od razu palnik pod patelnią.",
                    "Nałóż przygotowaną sałatkę z tuńczyka na każdą grzankę.",
                    "Zjedz od razu, póki chleb jest chrupiący."
                ),
                cookingTime = "15 min",
                difficulty = "Łatwe",
                tools = listOf(
                    "Patelnia",
                    "Mała miska",
                    "Nóż i deska do krojenia",
                    "Otwieracz do puszek (jeśli potrzebny)",
                    "Łyżka",
                    "Łyżeczka"
                ),
            ),
            RecipeEntity(
                name = "Kurczak z pieczonymi batatami",
                imageRes = R.drawable.chicken_batata,
                category = MealCategory.MEAT,
                ingredients = listOf(
                    "Batat - 1 średnia sztuka",
                    "Pierś z kurczaka - 1 sztuka",
                    "Olej rzepakowy lub oliwa - 3 łyżeczki",
                    "Przyprawy do kurczaka: papryka słodka, zioła prowansalskie, sól",
                    "Przyprawy do batatów: tymianek, sól",
                    "Miks sałat - 1 duża garść",
                    "Ogórek zielony - 1 średnia sztuka",
                    "Sos: 1 łyżeczka miodu, 1 łyżka soku z cytryny, 1 łyżka octu winnego, 1 łyżeczka musztardy"
                ),
                steps = listOf(
                    "Włącz piekarnik i ustaw temperaturę na 200°C (grzanie góra-dół).",
                    "Umyj i obierz batata. Pokrój go w cienkie słupki (jak frytki).",
                    "W miseczce wymieszaj słupki batata z 1 łyżeczką oleju, solą i tymiankiem.",
                    "Pokrój kurczaka w paski.",
                    "Przygotuj arkusz folii aluminiowej. Połóż na nim umyte mięso z kurczaka.",
                    "Posyp kurczaka papryką słodką, solą oraz ziołami prowansalskimi.",
                    "Wyłóż blachę papierem do pieczenia. Połóż na niej frytki z batata i przyprawionego kurczaka.",
                    "Wstaw blachę do piekarnika i ustaw minutnik na 35 minut.",
                    "W międzyczasie przygotuj sałatkę: umyj sałatę i przełóż ją do miski.",
                    "Umyj i pokrój ogórka, wrzuć go do miski z sałatą.",
                    "W małym kubeczku wymieszaj miód, sok z cytryny, ocet, musztardę i resztę oliwy. Polej sałatkę.",
                    "Wyjmij blachę z piekarnika. Sprawdź, czy na pewno wyłączyłeś piekarnik.",
                    "Przełóż wszystko na talerz. Smacznego!"
                ),
                cookingTime = "50 min",
                difficulty = "Trudne",
                tools = listOf(
                    "Piekarnik",
                    "Blacha do pieczenia",
                    "Papier do pieczenia",
                    "Folia aluminiowa (sreberko)",
                    "Dwie miski",
                    "Nóż i obieraczka do warzyw",
                    "Łyżka",
                    "Łyżeczka"
                ),
            ),
            RecipeEntity(
                name = "Gofry kakaowe z owocami",
                imageRes = R.drawable.chocolate_waffles,
                category = MealCategory.DESSERTS,
                ingredients = listOf(
                    "Jajka - 2 sztuki",
                    "Mleko - półtorej szklanki",
                    "Mąka (pełnoziarnista lub owsiana) - 1 szklanka",
                    "Olej - 2 łyżeczki",
                    "Proszek do pieczenia - pół łyżeczki",
                    "Erytrytol lub cukier - 1.5 łyżki",
                    "Kakao - 1 łyżka",
                    "Ekstrakt waniliowy - 1/2 łyżki",
                    "Sól - szczypta",
                    "Czekolada gorzka 80% - 1 kostka",
                    "Owoce: truskawki i kiwi"
                ),
                steps = listOf(
                    "Podłącz gofrownicę do prądu, zamknij ją i poczekaj, aż się nagrzeje.",
                    "Do dużej miski wbij 2 jajka, wlej mleko, olej oraz ekstrakt waniliowy.",
                    "Wymieszaj mokre składniki trzepaczką.",
                    "Wsyp mąkę, kakao, proszek do pieczenia, erytrytol lub cukier oraz szczyptę soli.",
                    "Wymieszaj wszystko, aż powstanie gładka masa bez grudek.",
                    "Ostrożnie otwórz gorącą gofrownicę.",
                    "Nalej chochlą porcję ciasta w wyznaczone miejsce gofrownicy i zamknij pokrywę.",
                    "Ustaw minutnik na 5 minut. Nie otwieraj gofrownicy w tym czasie.",
                    "W międzyczasie umyj truskawki, obierz kiwi i pokrój je w plasterki.",
                    "Gdy minutnik zadzwoni, wyjmij gofry za pomocą drewnianej łopatki na talerz.",
                    "Odłącz gofrownicę od prądu i zostaw ją otwartą do wystygnięcia.",
                    "Ułóż owoce na gofrach, dodaj kostkę czekolady i gotowe!"
                ),
                cookingTime = "20 min",
                difficulty = "Średnio trudne",
                tools = listOf(
                    "Gofrownica",
                    "Duża miska",
                    "Trzepaczka",
                    "Chochla",
                    "Nóż i deska do krojenia",
                    "Talerz",
                    "Szklanka",
                    "Łyżka",
                    "Łyżeczka",
                    "Łyżka",
                    "Drewniana łopatka"
                ),
            ),
            RecipeEntity(
                name = "Sałatka z ciecierzycą i awokado",
                imageRes = R.drawable.chickpeas_avocado_salad,
                category = MealCategory.SALADS_AND_SNACKS,
                ingredients = listOf(
                    "Ciecierzyca z puszki - 1 opakowanie (240g)",
                    "Awokado - 1 dojrzała sztuka",
                    "Ser Feta - 1 opakowanie (200g)",
                    "Pomidorki koktajlowe - 10 sztuk",
                    "Miks sałat - 2 garści",
                    "Jogurt typu Skyr - 3 łyżki",
                    "Sok z cytryny - 2 łyżki",
                    "Czosnek - 1 ząbek",
                    "Miód - 1 łyżeczka",
                    "Sól i pieprz - szczypta"
                ),
                steps = listOf(
                    "Otwórz puszkę ciecierzycy, wysyp ją na sitko i przepłucz zimną wodą.",
                    "Przygotuj sos: do małej miseczki włóż jogurt, miód oraz sok z cytryny.",
                    "Obierz ząbek czosnku i przeciśnij go przez praskę prosto do miseczki z sosem.",
                    "Dodaj szczyptę soli oraz pieprzu do sosu i wymieszaj wszystko dokładnie łyżeczką.",
                    "Umyj pomidorki i przekrój każdy z nich na pół.",
                    "Przekrój awokado na pół, wyjmij pestkę, a następnie wyjmij miąższ łyżką i pokrój w kostkę.",
                    "Wyciągnij ser feta z opakowania i pokrój go w kostkę.",
                    "Do dużej miski wrzuć miks sałat.",
                    "Dodaj do sałaty ciecierzycę, pomidorki, awokado oraz ser feta.",
                    "Polej sałatkę przygotowanym wcześniej sosem.",
                    "Wymieszaj wszystkie składniki",
                    "Gotowe!"
                ),
                cookingTime = "15 min",
                difficulty = "Łatwe",
                tools = listOf(
                    "Duża miska",
                    "Mała miseczka",
                    "Nóż i deska do krojenia",
                    "Praska do czosnku (lub mała tarka)",
                    "Sitko (do ciecierzycy)",
                    "Łyżka",
                    "Łyżeczka"
                ),
            ),
            RecipeEntity(
                name = "Orzechy włoskie w czekoladzie",
                imageRes = R.drawable.chocolate_walnuts,
                category = MealCategory.DESSERTS,
                ingredients = listOf(
                    "Orzechy włoskie (obrane) - 100g",
                    "Czekolada gorzka (minimum 70%) - 1 tabliczka (100g)"
                ),
                steps = listOf(
                    "Pokryj talerz lub tackę papierem do pieczenia, aby przyszłe orzechy w czekoladzie nie przywarły.",
                    "Weź patelnię i postaw ją na małym ogniu. Wsyp orzechy na suchą patelnię.",
                    "Praż orzechy przez 2-3 minuty, cały czas mieszając drewnianą łyżką. Pilnuj, żeby się nie przypaliły!",
                    "Przesyp orzechy na talerz.",
                    "Przygotuj kąpiel wodną: do garnka wlej pół szklanki wody i zagotuj ją.",
                    "Postaw miseczkę na garnku i zmniejsz ogień (musi być bardzo mały)",
                    "Połam czekoladę na małe kawałki i wrzuć do miseczki.",
                    "Mieszaj czekoladę, aż całkowicie się rozpuści, a następnie wyłącz palnik.",
                    "Wrzucaj po jednym orzechu do płynnej czekolady, obtocz go i wyjmij widelcem.",
                    "Kładź czekoladowe orzechy na przygotowany wcześniej papier do pieczenia.",
                    "Wstaw talerz do zamrażalnika na 10 minut, aby czekolada zastygła.",
                    "Sprawdź, czy na pewno wyłączyłeś palnik pod garnkiem i patelnią."
                ),
                cookingTime = "15 min + chłodzenie",
                difficulty = "Średnio trudne",
                tools = listOf(
                    "Sucha patelnia",
                    "Szklanka",
                    "Mały garnek i miseczka (pasująca na garnek)",
                    "Talerz lub tacka",
                    "Papier do pieczenia",
                    "Widelec lub wykałaczka",
                    "Drewniana łyżka"
                ),
            ),
            RecipeEntity(
                name = "Zupa krem z dyni",
                imageRes = R.drawable.pumpkin_soup,
                category = MealCategory.VEGETARIAN,
                ingredients = listOf(
                    "Dynia (np. odmiana Hokkaido) - pół sztuki (ok. 500g)",
                    "Ziemniaki - 2 średnie sztuki",
                    "Marchew - 1 sztuka",
                    "Cebula - 1 mała sztuka",
                    "Bulion warzywny lub woda - 3 szklanki (750ml)",
                    "Oliwa z oliwek - 1 łyżka",
                    "Imbir mielony lub świeży - szczypta",
                    "Sól i pieprz - szczypta",
                    "Pestki dyni - 2 łyżki (do posypania)"
                ),
                steps = listOf(
                    "Umyj dynię. Jeśli to dynia Hokkaido, nie musisz jej obierać. Jeśli inna - obierz ją ostrożnie ze skóry.",
                    "Usuń nasiona łyżką i pokrój miąższ dyni w kostkę.",
                    "Obierz ziemniaki oraz marchew, umyj i pokrój je w kostkę.",
                    "Posiekaj cebulę w drobną kostkę.",
                    "W dużym garnku rozgrzej łyżkę oliwy i wrzuć cebulę. Smaż przez 2 minuty, aż zmięknie.",
                    "Dodaj do garnka pokrojoną dynię, ziemniaki i marchew. Smaż razem przez 5 minut, mieszając.",
                    "Wlej bulion (lub wodę) tak, aby przykrył warzywa. Dodaj szczyptę soli, pieprzu i imbiru.",
                    "Gotuj zupę na średnim ogniu pod przykryciem przez około 20-25 minut (aż warzywa będą miękkie).",
                    "Wyłącz palnik i poczekaj 5 minut, aby zupa lekko przestygła przed blendowaniem.",
                    "Zblenduj zupę na gładki krem. Uważaj, aby nie rozpryskiwać gorącego płynu!",
                    "Sprawdź, czy wyłączyłeś palnik.",
                    "Nalej zupę na talerz i posyp pestkami dyni."
                ),
                cookingTime = "40 min",
                difficulty = "Trudne",
                tools = listOf(
                    "Duży garnek",
                    "Blender (kielichowy lub ręczny żyrafa)",
                    "Nóż i deska do krojenia",
                    "Obieraczka do warzyw",
                    "Łyżka drewniana",
                    "Łyżka",
                    "Głęboki talerz do zupy"
                ),
            ),
            RecipeEntity(
                name = "Łosoś pieczony z koperkiem",
                imageRes = R.drawable.baked_salmon,
                category = MealCategory.SEAFOOD,
                ingredients = listOf(
                    "1 Filet z łososia - 150 g",
                    "Sok z cytryny - 1 łyżka",
                    "Koper świeży - 1 łyżeczka (posiekany)",
                    "Oliwa z oliwek - 1 łyżka",
                    "Sól i pieprz - szczypta"
                ),
                steps = listOf(
                    "Włącz piekarnik i ustaw temperaturę na 200°C.",
                    "Wyłóż blachę papierem do pieczenia.",
                    "Opłucz łososia pod zimną wodą i osusz go delikatnie ręcznikiem papierowym.",
                    "Połóż łososia na blaszce.",
                    "Polej rybę sokiem z cytryny oraz oliwą.",
                    "Posyp rybę solą, pieprzem oraz koperkiem.",
                    "Wstaw blachę do nagrzanego piekarnika na 12-15 minut.",
                    "Ostrożnie wyjmij blachę używając rękawic kuchennych.",
                    "Wyłącz piekarnik."
                ),
                cookingTime = "20 min",
                difficulty = "Średnio trudne",
                tools = listOf(
                    "Piekarnik",
                    "Blacha do pieczenia",
                    "Łyżka",
                    "Ręcznik papierowy",
                    "Papier do pieczenia",
                    "Rękawice kuchenne"
                )
            ),
            RecipeEntity(
                name = "Owsianka z borówkami",
                imageRes = R.drawable.blueberry_oats,
                category = MealCategory.DESSERTS,
                ingredients = listOf(
                    "Płatki owsiane - 4 łyżki (40 g)",
                    "Mleko lub napój roślinny - 1 szklanka (250 ml)",
                    "Borówki świeże - 1 garść",
                    "Orzechy włoskie - 3-4 sztuki",
                    "Miód - 1 łyżeczka (opcjonalnie)",
                    "Banan - 1 sztuka (opcjonalnie)"
                ),
                steps = listOf(
                    "Wlej mleko do małego rondelka i postaw na średnim ogniu.",
                    "Gdy mleko zacznie wrzeć, wsyp płatki owsiane.",
                    "Zmniejsz ogień na bardzo mały.",
                    "Gotuj przez 5 minut, często mieszając łyżką, aby płatki nie przywarły do dna.",
                    "Wyłącz palnik i przelej owsiankę do miseczki.",
                    "Sprawdź, czy palnik został wyłączony.",
                    "Posiekaj orzechy na mniejsze kawałki.",
                    "Na wierzchu owsianki ułóż umyte borówki, orzechy oraz jeśli chcesz - banana.",
                ),
                cookingTime = "10 min",
                difficulty = "Bardzo łatwe",
                tools = listOf("Mały rondelek", "Miseczka", "Łyżka", "Nóż")
            ),
            RecipeEntity(
                name = "Gulasz z soczewicy",
                imageRes = R.drawable.lentil_soup,
                category = MealCategory.VEGETARIAN,
                ingredients = listOf(
                    "Czerwona soczewica - pół szklanki (100 g)",
                    "Pomidory w puszce - pół opakowania (125g)",
                    "Marchew - 1 sztuka",
                    "Cebula - pół sztuki",
                    "Woda - 1 szklanka",
                    "Oliwa z oliwek - 1 łyżka"
                ),
                steps = listOf(
                    "Umyj i obierz cebulę i marchewkę.",
                    "Pokrój cebulę w drobną kostkę, a marchewkę w cienkie plasterki.",
                    "W garnku rozgrzej oliwę i wrzuć pokrojone warzywa.",
                    "Smaż przez 3 minuty na średnim ogniu, aż cebula się przyrumieni.",
                    "Wsyp soczewicę do garnka i dodaj pomidory z puszki.",
                    "Wlej szklankę wody i wymieszaj.",
                    "Przykryj garnek pokrywką i gotuj na małym ogniu przez 15 minut.",
                    "Sprawdź, czy soczewica jest miękka. Jeśli tak, wyłącz palnik.",
                    "Odstaw na 2 minuty przed nałożeniem na talerz."
                ),
                cookingTime = "30 min",
                difficulty = "Średnio trudne",
                tools = listOf(
                    "Garnek z pokrywką",
                    "Nóż",
                    "Szklanka",
                    "Łyżka",
                    "Deska do krojenia",
                    "Drewniana łyżka"
                )
            ),
            RecipeEntity(
                name = "Sałatka ze szpinakiem",
                imageRes = R.drawable.spinach_feta_salad,
                category = MealCategory.SALADS_AND_SNACKS,
                ingredients = listOf(
                    "Szpinak świeży - 2 duże garści",
                    "Orzechy włoskie - 1 garść (30 g)",
                    "Ser feta - 1 grubszy plaster (50 g)",
                    "Oliwa z oliwek - 1 łyżka",
                    "Sok z cytryny - 1 łyżeczka"
                ),
                steps = listOf(
                    "Umyj szpinak i dokładnie go osusz (możesz użyć ręcznika papierowego lub czystej ściereczki).",
                    "Postaw patelnię na małym ogniu i wsyp orzechy.",
                    "Praż orzechy przez 2 minuty, mieszając.",
                    "Wyłącz palnik. Przesyp orzechy na talerz i poczekaj aż wystygną.",
                    "Do miski wrzuć szpinak i ser feta.",
                    "Dodaj uprażone orzechy.",
                    "W małym kubeczku wymieszaj oliwę z sokiem z cytryny, a następnie polej tym sosem sałatkę.",
                    "Wymieszaj delikatnie wszystkie składniki łyżką."
                ),
                cookingTime = "10 min",
                difficulty = "Łatwe",
                tools = listOf(
                    "Miska",
                    "Ręcznik papierowy lub czysta ściereczka",
                    "Mały kubek",
                    "Patelnia",
                    "Talerz",
                    "Łyżka"
                )
            ),
            RecipeEntity(
                name = "Kasza jaglana z indykiem i warzywami",
                imageRes = R.drawable.poultry_millet,
                category = MealCategory.MEAT,
                ingredients = listOf(
                    "Filet z indyka - 150 g",
                    "Cukinia - pół małej sztuki",
                    "Papryka czerwona - pół sztuki",
                    "Kasza jaglana - 1/3 szklanki (50 g)",
                    "Oliwa - 1 łyżka"
                ),
                steps = listOf(
                    "Wsyp kaszę do garnka, zalej szklanką wody i gotuj pod przykryciem ok. 10-12 minut.",
                    "W międzyczasie pokrój mięso z indyka w małą kostkę.",
                    "Umyj cukinię i paprykę.",
                    "Pokrój cukinię i paprykę w kostkę.",
                    "Na patelni rozgrzej oliwę i wrzuć mięso. Smaż przez 5 minut na średnim ogniu.",
                    "Dodaj pokrojoną paprykę i cukinię do mięsa.",
                    "Zmniejsz ogień, przykryj patelnię i smaż wszystko przez 10 minut.",
                    "Wyłącz oba palniki (pod garnkiem i pod patelnią).",
                    "Nałóż na talerz kaszę i indyka z warzywami."
                ),
                cookingTime = "25 min",
                difficulty = "Średnio trudne",
                tools = listOf(
                    "Mały garnek",
                    "Szklanka",
                    "Patelnia z pokrywką",
                    "Talerz",
                    "Nóż",
                    "Deska do krojenia"
                )
            ),
            RecipeEntity(
                name = "Dorsz z koperkiem",
                imageRes = R.drawable.cod_dill,
                category = MealCategory.SEAFOOD,
                ingredients = listOf(
                    "1 Polędwica z dorsza - około 150 g",
                    "Świeży koperek - 2 gałązki",
                    "Masło - 1 mały plaster (10 g)",
                    "Cytryna - 2 plastry",
                    "Sól i pieprz - szczypta"
                ),
                steps = listOf(
                    "Włącz piekarnik i ustaw temperaturę na 180°C.",
                    "Przygotuj arkusz folii aluminiowej i połóż na nim dorsza.",
                    "Posyp rybę solą i pieprzem.",
                    "Na wierzchu ryby połóż plasterek masła, gałązki koperku i plastry cytryny.",
                    "Zawiń folię szczelnie wokół ryby.",
                    "Połóż owiniętą rybę na blaszce i wstaw do piekarnika na 20 minut.",
                    "Po 20 minutach wyłącz piekarnik, załóż rękawice kuchenne i ostrożnie wyjmij blachę.",
                ),
                cookingTime = "25 min",
                difficulty = "Średnio trudne",
                tools = listOf(
                    "Piekarnik",
                    "Blacha do pieczenia",
                    "Folia aluminiowa",
                    "Rękawice kuchenne"
                )
            ),
            RecipeEntity(
                name = "Pudding chia",
                imageRes = R.drawable.chia_pudding,
                category = MealCategory.DESSERTS,
                ingredients = listOf(
                    "Nasiona chia - 3 łyżki",
                    "Napój migdałowy lub mleko - 1 szklanka (200 ml)",
                    "Maliny - 1 garść",
                    "Miód - 1 łyżeczka"
                ),
                steps = listOf(
                    "Wlej napój migdałowy do słoika lub wysokiej szklanki.",
                    "Dodaj miód i wymieszaj łyżeczką.",
                    "Wsyp nasiona chia i mieszaj tak długo, by nie było grudek.",
                    "Wstaw do lodówki na całą noc (lub minimum 3 godziny).",
                    "Wyjmij z lodówki przed jedzeniem i na wierzchu ułóż umyte maliny."
                ),
                cookingTime = "5 min + chłodzenie",
                difficulty = "Bardzo łatwe",
                tools = listOf("Słoik lub wysoka szklanka", "Łyżeczka")
            ),
            RecipeEntity(
                name = "Hummus klasyczny",
                imageRes = R.drawable.hummus,
                category = MealCategory.SALADS_AND_SNACKS,
                ingredients = listOf(
                    "Ciecierzyca z puszki - 1 opakowanie (400 g)",
                    "Pasta tahini - 2 łyżki",
                    "Sok z cytryny - 2 łyżki",
                    "Czosnek - 1 mały ząbek",
                    "Woda zimna - 2-3 łyżki"
                ),
                steps = listOf(
                    "Otwórz puszkę ciecierzycy i odcedź ją na sitku, płucząc zimną wodą.",
                    "Wrzuć ciecierzycę do miski i użyj blendera.",
                    "Obierz czosnek i dodaj do ciecierzycy.",
                    "Dodaj pastę tahini oraz sok z cytryny.",
                    "Zblenduj wszystko na gładką masę.",
                    "Przełóż gotowy hummus do miseczki."
                ),
                cookingTime = "10 min",
                difficulty = "Łatwe",
                tools = listOf("Blender", "Sitko", "Łyżka", "Miseczka")
            )
        )
    }
}
