package com.example.data

import com.example.model.Ayah
import com.example.model.Reciter
import com.example.model.Surah

object QuranRepository {

    val RECITERS = listOf(
        Reciter(
            id = "menshawy",
            nameAr = "الشيخ محمد صديق المنشاوي",
            styleAr = "مرتل",
            serverBaseUrl = "https://everyayah.com/data/Menshawi_32kbps/"
        ),
        Reciter(
            id = "abdulbasit",
            nameAr = "الشيخ عبد الباسط عبد الصمد",
            styleAr = "مرتل",
            serverBaseUrl = "https://everyayah.com/data/Abdul_Basit_Murattal_64kbps/"
        ),
        Reciter(
            id = "alafasy",
            nameAr = "الشيخ مشاري راشد العفاسي",
            styleAr = "مرتل",
            serverBaseUrl = "https://everyayah.com/data/Alafasy_64kbps/"
        ),
        Reciter(
            id = "hudhaify",
            nameAr = "الشيخ علي بن عبد الرحمن الحذيفي",
            styleAr = "مرتل",
            serverBaseUrl = "https://everyayah.com/data/Hudhaify_64kbps/"
        ),
        Reciter(
            id = "sudais",
            nameAr = "الشيخ عبد الرحمن السديس",
            styleAr = "مرتل",
            serverBaseUrl = "https://everyayah.com/data/Abdurrahmaan_As-Sudais_64kbps/"
        )
    )

    // Catalog of all 114 Surahs
    val SURAHS: List<Surah> = listOf(
        Surah(1, "الفاتحة", "Al-Fatihah", 7, "مكية", 1, 1),
        Surah(2, "البقرة", "Al-Baqarah", 286, "مدنية", 2, 1),
        Surah(3, "آل عمران", "Aal-E-Imran", 200, "مدنية", 50, 3),
        Surah(4, "النساء", "An-Nisa", 176, "مدنية", 77, 4),
        Surah(5, "المائدة", "Al-Ma'idah", 120, "مدنية", 106, 6),
        Surah(6, "الأنعام", "Al-An'am", 165, "مكية", 128, 7),
        Surah(7, "الأعراف", "Al-A'raf", 206, "مكية", 151, 8),
        Surah(8, "الأنفال", "Al-Anfal", 75, "مدنية", 177, 9),
        Surah(9, "التوبة", "At-Tawbah", 129, "مدنية", 187, 10),
        Surah(10, "يونس", "Yunus", 109, "مكية", 208, 11),
        Surah(11, "هود", "Hud", 123, "مكية", 221, 11),
        Surah(12, "يوسف", "Yusuf", 111, "مكية", 235, 12),
        Surah(13, "الرعد", "Ar-Ra'd", 43, "مدنية", 249, 13),
        Surah(14, "إبراهيم", "Ibrahim", 52, "مكية", 255, 13),
        Surah(15, "الحجر", "Al-Hijr", 99, "مكية", 262, 14),
        Surah(16, "النحل", "An-Nahl", 128, "مكية", 267, 14),
        Surah(17, "الإسراء", "Al-Isra", 111, "مكية", 282, 15),
        Surah(18, "الكهف", "Al-Kahf", 110, "مكية", 293, 15),
        Surah(19, "مريم", "Maryam", 98, "مكية", 305, 16),
        Surah(20, "طه", "Ta-Ha", 135, "مكية", 312, 16),
        Surah(21, "الأنبياء", "Al-Anbiya", 112, "مكية", 322, 17),
        Surah(22, "الحج", "Al-Hajj", 78, "مدنية", 332, 17),
        Surah(23, "المؤمنون", "Al-Mu'minun", 118, "مكية", 342, 18),
        Surah(24, "النور", "An-Nur", 64, "مدنية", 350, 18),
        Surah(25, "الفرقان", "Al-Furqan", 77, "مكية", 359, 18),
        Surah(26, "الشعراء", "Ash-Shu'ara", 227, "مكية", 367, 19),
        Surah(27, "النمل", "An-Naml", 93, "مكية", 377, 19),
        Surah(28, "القصص", "Al-Qasas", 88, "مكية", 385, 20),
        Surah(29, "العنكبوت", "Al-Ankabut", 69, "مكية", 396, 20),
        Surah(30, "الروم", "Ar-Rum", 60, "مكية", 404, 21),
        Surah(31, "لقمان", "Luqman", 34, "مكية", 411, 21),
        Surah(32, "السجدة", "As-Sajdah", 30, "مكية", 415, 21),
        Surah(33, "الأحزاب", "Al-Ahzab", 73, "مدنية", 418, 21),
        Surah(34, "سبأ", "Saba", 54, "مكية", 428, 22),
        Surah(35, "فاطر", "Fatir", 45, "مكية", 434, 22),
        Surah(36, "يس", "Ya-Sin", 83, "مكية", 440, 22),
        Surah(37, "الصافات", "As-Saffat", 182, "مكية", 446, 23),
        Surah(38, "ص", "Sad", 88, "مكية", 453, 23),
        Surah(39, "الزمر", "Az-Zumar", 75, "مكية", 458, 23),
        Surah(40, "غافر", "Ghafir", 85, "مكية", 467, 24),
        Surah(41, "فصلت", "Fussilat", 54, "مكية", 477, 24),
        Surah(42, "الشورى", "Ash-Shura", 53, "مكية", 483, 25),
        Surah(43, "الزخرف", "Az-Zukhruf", 89, "مكية", 489, 25),
        Surah(44, "الدخان", "Ad-Dukhan", 59, "مكية", 496, 25),
        Surah(45, "الجاثية", "Al-Jathiyah", 37, "مكية", 499, 25),
        Surah(46, "الأحقاف", "Al-Ahqaf", 35, "مكية", 502, 26),
        Surah(47, "محمد", "Muhammad", 38, "مدنية", 507, 26),
        Surah(48, "الفتح", "Al-Fath", 29, "مدنية", 511, 26),
        Surah(49, "الحجرات", "Al-Hujurat", 18, "مدنية", 515, 26),
        Surah(50, "ق", "Qaf", 45, "مكية", 518, 26),
        Surah(51, "الذاريات", "Adh-Dhariyat", 60, "مكية", 520, 26),
        Surah(52, "الطور", "At-Tur", 49, "مكية", 523, 27),
        Surah(53, "النجم", "An-Najm", 62, "مكية", 526, 27),
        Surah(54, "القمر", "Al-Qamar", 55, "مكية", 528, 27),
        Surah(55, "الرحمن", "Ar-Rahman", 78, "مدنية", 531, 27),
        Surah(56, "الواقعة", "Al-Waqi'ah", 96, "مكية", 534, 27),
        Surah(57, "الحديد", "Al-Hadid", 29, "مدنية", 537, 27),
        Surah(58, "المجادلة", "Al-Mujadila", 22, "مدنية", 542, 28),
        Surah(59, "الحشر", "Al-Hashr", 24, "مدنية", 545, 28),
        Surah(60, "الممتحنة", "Al-Mumtahanah", 13, "مدنية", 549, 28),
        Surah(61, "الصف", "As-Saff", 14, "مدنية", 551, 28),
        Surah(62, "الجمعة", "Al-Jumu'ah", 11, "مدنية", 553, 28),
        Surah(63, "المنافقون", "Al-Munafiqun", 11, "مدنية", 554, 28),
        Surah(64, "التغابن", "At-Taghabun", 18, "مدنية", 556, 28),
        Surah(65, "الطلاق", "At-Talaq", 12, "مدنية", 558, 28),
        Surah(66, "التحريم", "At-Tahrim", 12, "مدنية", 560, 28),
        Surah(67, "الملك", "Al-Mulk", 30, "مكية", 562, 29),
        Surah(68, "القلم", "Al-Qalam", 52, "مكية", 564, 29),
        Surah(69, "الحاقة", "Al-Haqqah", 52, "مكية", 566, 29),
        Surah(70, "المعارج", "Al-Ma'arij", 44, "مكية", 568, 29),
        Surah(71, "نوح", "Nuh", 28, "مكية", 570, 29),
        Surah(72, "الجن", "Al-Jinn", 28, "مكية", 572, 29),
        Surah(73, "المزمل", "Al-Muzzammil", 20, "مكية", 574, 29),
        Surah(74, "المدثر", "Al-Muddaththir", 56, "مكية", 575, 29),
        Surah(75, "القيامة", "Al-Qiyamah", 40, "مكية", 577, 29),
        Surah(76, "الإنسان", "Al-Insan", 31, "مدنية", 578, 29),
        Surah(77, "المرسلات", "Al-Mursalat", 50, "مكية", 580, 29),
        Surah(78, "النبأ", "An-Naba", 40, "مكية", 582, 30),
        Surah(79, "النازعات", "An-Nazi'at", 46, "مكية", 583, 30),
        Surah(80, "عبس", "Abasa", 42, "مكية", 585, 30),
        Surah(81, "التكوير", "At-Takwir", 29, "مكية", 586, 30),
        Surah(82, "الانفطار", "Al-Infitar", 19, "مكية", 587, 30),
        Surah(83, "المطففين", "Al-Mutaffifin", 36, "مكية", 587, 30),
        Surah(84, "الانشقاق", "Al-Inshiqaq", 25, "مكية", 589, 30),
        Surah(85, "البروج", "Al-Buruj", 22, "مكية", 590, 30),
        Surah(86, "الطارق", "At-Tariq", 17, "مكية", 591, 30),
        Surah(87, "الأعلى", "Al-A'la", 19, "مكية", 591, 30),
        Surah(88, "الغاشية", "Al-Ghashiyah", 26, "مكية", 592, 30),
        Surah(89, "الفجر", "Al-Fajr", 30, "مكية", 593, 30),
        Surah(90, "البلد", "Al-Balad", 20, "مكية", 594, 30),
        Surah(91, "الشمس", "Ash-Shams", 15, "مكية", 595, 30),
        Surah(92, "الليل", "Al-Layl", 21, "مكية", 595, 30),
        Surah(93, "الضحى", "Ad-Duha", 11, "مكية", 596, 30),
        Surah(94, "الشرح", "Ash-Sharh", 8, "مكية", 596, 30),
        Surah(95, "التين", "At-Tin", 8, "مكية", 597, 30),
        Surah(96, "العلق", "Al-Alaq", 19, "مكية", 597, 30),
        Surah(97, "القدر", "Al-Qadr", 5, "مكية", 598, 30),
        Surah(98, "البينة", "Al-Bayyinah", 8, "مدنية", 598, 30),
        Surah(99, "الزلزلة", "Az-Zalzalah", 8, "مدنية", 599, 30),
        Surah(100, "العاديات", "Al-Adiyat", 11, "مكية", 599, 30),
        Surah(101, "القارعة", "Al-Qari'ah", 11, "مكية", 600, 30),
        Surah(102, "التكاثر", "At-Takathur", 8, "مكية", 600, 30),
        Surah(103, "العصر", "Al-Asr", 3, "مكية", 601, 30),
        Surah(104, "الهمزة", "Al-Humazah", 9, "مكية", 601, 30),
        Surah(105, "الفيل", "Al-Fil", 5, "مكية", 601, 30),
        Surah(106, "قريش", "Quraysh", 4, "مكية", 602, 30),
        Surah(107, "الماعون", "Al-Ma'un", 7, "مكية", 602, 30),
        Surah(108, "الكوثر", "Al-Kawthar", 3, "مكية", 602, 30),
        Surah(109, "الكافرون", "Al-Kafirun", 6, "مكية", 603, 30),
        Surah(110, "النصر", "An-Nasr", 3, "مدنية", 603, 30),
        Surah(111, "المسد", "Al-Masad", 5, "مكية", 603, 30),
        Surah(112, "الإخلاص", "Al-Ikhlas", 4, "مكية", 604, 30),
        Surah(113, "الفلق", "Al-Falaq", 5, "مكية", 604, 30),
        Surah(114, "الناس", "An-Nas", 6, "مكية", 604, 30)
    )

    // Detailed Ayah and authentic Tafaseer (السعدي، الجلالين، الميسر) for key chapters
    fun getAyahsForSurah(surahId: Int, reciter: Reciter = RECITERS[0]): List<Ayah> {
        val baseAyahs = when (surahId) {
            1 -> listOf(
                Ayah(1, 1, 1, "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ", "In the name of Allah, the Entirely Merciful, the Especially Merciful",
                    tafseerSaadi = "أي: أبتدئ قراءتي مستعيناً باسم الله الرحمٰن الرحيم.",
                    tafseerJalalayn = "البسملة آية من الفاتحة، والله علم على الذات العلية، الرحمن الرحيم صفتان للرحمة.",
                    tafseerMuyassar = "أبدأ قراءتي مستعينا باسم الله، الرحمن ذي الرحمة العامة، الرحيم بعباده المؤمنين."),
                Ayah(1, 2, 2, "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ", "[All] praise is [due] to Allah, Lord of the worlds",
                    tafseerSaadi = "الثناء على الله بصفات كماله، وبنعمه الظاهرة والباطنة، الدينية والدنيوية.",
                    tafseerJalalayn = "الوصف بالجميل مستحق لله تعالى وحده، المربي لجميع العالمين بخلقه وإنعامه.",
                    tafseerMuyassar = "الثناء الكامل والشكر الخالص لله وحده، خالق الخلق ومدبر أمورهم."),
                Ayah(1, 3, 3, "الرَّحْمَٰنِ الرَّحِيمِ", "The Entirely Merciful, the Especially Merciful",
                    tafseerSaadi = "دلالة على سعة رحمته التي وسعت كل شيء، وتخصيصها بعباده المؤمنين.",
                    tafseerJalalayn = "أي ذي الرحمة وهي إرادة الخير لأهله، رحمن الدنيا والآخرة ورحيمهما.",
                    tafseerMuyassar = "الذي وسعت رحمته جميع الخلائق، واختص بالمؤمنين بالهداية والإكرام."),
                Ayah(1, 4, 4, "مَالِكِ يَوْمِ الدِّينِ", "Sovereign of the Day of Recompense",
                    tafseerSaadi = "الملك يوم القيامة، وهو يوم الجزاء على الأعمال، حيث يظهر كمال ملكه وعدله.",
                    tafseerJalalayn = "المتصرف بالأمر وحده يوم الحساب والجزاء.",
                    tafseerMuyassar = "المالك المتفرد بالحكم يوم القيامة، يوم محاسبة العباد وجزائهم."),
                Ayah(1, 5, 5, "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ", "It is You we worship and You we ask for help",
                    tafseerSaadi = "نخصك وحدك بالعبادة، ونخصك وحدك بالاستعانة في كل أمورنا.",
                    tafseerJalalayn = "نعبدك موحدين ولا نعبد غيرك، ونطلب المعونة منك وحدك على طاعتك وسائر أمورنا.",
                    tafseerMuyassar = "نخصك وحدك بالعبادة والطاعة، ونستعين بك وحدك في جلب المنافع ودفع المضار."),
                Ayah(1, 6, 6, "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ", "Guide us to the straight path",
                    tafseerSaadi = "دلنا وأرشدنا، ووفقنا وثبتنا على الطريق الواضح الموصل إلى رضاك وجنتك.",
                    tafseerJalalayn = "أرشدنا وثبتنا على دين الإسلام الواضح السوي.",
                    tafseerMuyassar = "وفقنا وأرشدنا وثبتنا على الصراط السوي الواضح، وهو دين الإسلام."),
                Ayah(1, 7, 7, "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ", "The path of those upon whom You have bestowed favor, not of those who have evoked [Your] anger or of those who are astray",
                    tafseerSaadi = "طريق النبيين والصديقين والشهداء والصالحين، غير طريق اليهود المغضوب عليهم لعلمهم وتركه، ولا النصارى الضالين لعبادتهم على جهل.",
                    tafseerJalalayn = "طريق من أنعمت عليهم بالهداية، لا طريق المعاندين ولا الحائدين عن الحق.",
                    tafseerMuyassar = "طريق من تفضلت عليهم بالهداية من النبيين والصالحين، لا طريق المعاندين للحق ولا التائهين في الضلال.")
            )
            2 -> listOf(
                Ayah(2, 1, 8, "الم", "Alif, Lam, Meem",
                    tafseerSaadi = "حروف مقطعة للتحدي والإعجاز، للإشارة إلى أن هذا القرآن مؤلف من جنس الحروف التي يتكلمون بها.",
                    tafseerJalalayn = "الله أعلم بمراده بذلك.",
                    tafseerMuyassar = "هذه الحروف المقطعة تدل على إعجاز القرآن، وأنه معجز للبشر مع كونه بلسانهم."),
                Ayah(2, 2, 9, "ذَٰلِكَ الْكِتَابُ لَا رَيْبَ ۛ فِيهِ ۛ هُدًى لِّلْمُتَّقِينَ", "This is the Book about which there is no doubt, a guidance for those conscious of Allah",
                    tafseerSaadi = "هذا القرآن العظيم لا شك فيه بوجه من الوجوه، وهو هداية للمتقين الذين يخافون عقاب الله ويرجون ثوابه.",
                    tafseerJalalayn = "أي هذا القرآن لا شك أنه من عند الله، هادٍ لمن يتقي الشرك والمعاصي.",
                    tafseerMuyassar = "هذا القرآن كتاب عظيم لا ريب فيه، يهدي المتقين الممتثلين لأوامر الله."),
                Ayah(2, 255, 262, "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ لَّهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ ۗ مَن ذَا الَّذِي يَشْفَعُ عِندَهُ إِلَّا بِإِذْنِهِ ۚ يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ ۖ وَلَا يُحِيطُونَ بِشَيْءٍ مِّنْ عِلْمِهِ إِلَّا بِمَا شَاءَ ۚ وَسِعَ كُرْسِيُّهُ السَّمَاوَاتِ وَالْأَرْضَ ۖ وَلَا يَئُودُهُ حِفْظُهُمَا ۚ وَهُوَ الْعَلِيُّ الْعَظِيمُ", "Allah - there is no deity except Him, the Ever-Living, the Sustainer of all existence (Ayatul Kursi)",
                    tafseerSaadi = "آية الكرسي أعظم آية في كتاب الله؛ احتوت على توحيد الألوهية والربوبية والأسماء والصفات، وإحاطة علمه وقدرته وعظمته وسعة سلطانه.",
                    tafseerJalalayn = "المستحق للعبادة وحده، الدائم الحياة، القائم بتدبير خلقه، لا يلحقه نعاس ولا نوم، وسع كرسيه السماوات والأرض ولا يثقله حفظهما.",
                    tafseerMuyassar = "الله المتفرد بالألوهية، الحي حياة كاملة، القيوم القائم بتدبير خلقه، لا يغلبه نعاس ولا نوم، له الملك التام والعلم المحيط والعزة والجلال.")
            )
            36 -> listOf(
                Ayah(36, 1, 3706, "يس", "Ya-Sin",
                    tafseerSaadi = "حروف مقطعة للتنبيه على إعجاز القرآن العظيم.",
                    tafseerJalalayn = "الله أعلم بمراده به.",
                    tafseerMuyassar = "يا سين، من الحروف المقطعة التي تدل على صدق وإعجاز هذا القرآن."),
                Ayah(36, 2, 3707, "وَالْقُرْآنِ الْحَكِيمِ", "By the wise Qur'an",
                    tafseerSaadi = "قسم بالقرآن المشتمل على الحكمة والأحكام المتقنة.",
                    tafseerJalalayn = "أقسم بالقرآن المحكم البين.",
                    tafseerMuyassar = "يقسم الله تعالى بالقرآن المحكم في نظمه ومعانيه."),
                Ayah(36, 3, 3708, "إِنَّكَ لَمِنَ الْمُرْسَلِينَ", "Indeed you, [O Muhammad], are from among the messengers",
                    tafseerSaadi = "تأكيد لرسالة نبينا محمد صلى الله عليه وسلم.",
                    tafseerJalalayn = "تأكيد لرسالة النبي محمد صلى الله عليه وسلم.",
                    tafseerMuyassar = "إنك يا محمد لمن الرسل المبعوثين بالهدى ودين الحق."),
                Ayah(36, 4, 3709, "عَلَىٰ صِرَاطٍ مُّسْتَقِيمٍ", "On a straight path",
                    tafseerSaadi = "على منهج واضح وسليم موصل إلى رضوان الله.",
                    tafseerJalalayn = "على طريق الحق المعتدل وهو الإسلام.",
                    tafseerMuyassar = "على طريق معتدل لا عوج فيه.")
            )
            67 -> listOf(
                Ayah(67, 1, 5242, "تَبَارَكَ الَّذِي بِيَدِهِ الْمُلْكُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ", "Blessed is He in whose hand is dominion, and He is over all things competent",
                    tafseerSaadi = "تكاثر خيره وعم إحسانه، الذي بيده مقاليد الملك والتصرف المطلق.",
                    tafseerJalalayn = "تعالى وتعاظم بالخير والقدرة من بيده ملك السماوات والأرض.",
                    tafseerMuyassar = "تعاظم خير الله وبره، فبيده ملك الكون كله وتصريفه، وهو على كل شيء قدير."),
                Ayah(67, 2, 5243, "الَّذِي خَلَقَ الْمَوْتَ وَالْحَيَاةَ لِيَبْلُوَكُمْ أَيُّكُمْ أَحْسَنُ عَمَلًا ۚ وَهُوَ الْعَزِيزُ الْغَفُورُ", "He who created death and life to test you as to which of you is best in deed",
                    tafseerSaadi = "أوجد الموت والحياة ليختبر العباد أيكم أخلص وأصوب عملاً لله.",
                    tafseerJalalayn = "أوجد الحياة في الدنيا والموت بعدها ليمتحنكم أيكم أطوع لله.",
                    tafseerMuyassar = "خلق الموت والحياة ليختبركم: أيكم أخلص لله وأتبع لرسوله."),
                Ayah(67, 3, 5244, "الَّذِي خَلَقَ سَبْعَ سَمَاوَاتٍ طِبَاقًا ۖ مَّا تَرَىٰ فِي خَلْقِ الرَّحْمَٰنِ مِن تَفَاوُتٍ ۖ فَارْجِعِ الْبَصَرَ هَلْ تَرَىٰ مِن فُطُورٍ", "Who created seven heavens in layers. You do not see in the creation of the Most Merciful any inconsistency",
                    tafseerSaadi = "خلق سبع سماوات بعضها فوق بعض في غاية الحسن والإتقان، ما تجد فيها خللاً ولا نقصاً.",
                    tafseerJalalayn = "خلق سبع سماوات بعضها فوق بعض بلا تفاوت ولا عيب.",
                    tafseerMuyassar = "خلق سبع سماوات متطابقة محكمة، لا ترى في خلقه أي خلل أو عيب.")
            )
            112 -> listOf(
                Ayah(112, 1, 6221, "قُلْ هُوَ اللَّهُ أَحَدٌ", "Say, He is Allah, [who is] One",
                    tafseerSaadi = "أي قل قولاً جازماً معتقداً به: الله هو الواحد الأحد المتفرد بالكمال.",
                    tafseerJalalayn = "أي هو الله المتفرد بالوحدانية لا شريك له.",
                    tafseerMuyassar = "قل يا محمد لمن سألك عن ربك: هو الله المنفرد بالألوهية والربوبية والأسماء والصفات."),
                Ayah(112, 2, 6222, "اللَّهُ الصَّمَدُ", "Allah, the Eternal Refuge",
                    tafseerSaadi = "السيد الذي تصمد وتقصد إليه الخلائق كلها في حوائجها ورغائبها.",
                    tafseerJalalayn = "المقصود في الحوائج على الدوام، الغني عن خلقه المحتاج إليه كل ما سواه.",
                    tafseerMuyassar = "السيد الذي كمل في سؤدده وشرفه، وتفتقر إليه جميع المخلوقات."),
                Ayah(112, 3, 6223, "لَمْ يَلِدْ وَلَمْ يُولَدْ", "He neither begets nor is born",
                    tafseerSaadi = "تنزه وتقدس عن الولد والوالد والكفء والنظير لكمال غناه.",
                    tafseerJalalayn = "انتفى عنه أن يكون له ولد أو والد.",
                    tafseerMuyassar = "لم يلد أحداً ولم يولد من أحد، فليس له ولد ولا والد."),
                Ayah(112, 4, 6224, "وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ", "Nor is there to Him any equivalent",
                    tafseerSaadi = "لا مثيل له ولا نظير، لا في أسمائه ولا صفاته ولا أفعاله.",
                    tafseerJalalayn = "لا مماثل له ولا مكافئ.",
                    tafseerMuyassar = "ولم يكن له مماثل أو مساوٍ من خلقه سبحانه وتعالى.")
            )
            113 -> listOf(
                Ayah(113, 1, 6225, "قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ", "Say, I seek refuge in the Lord of daybreak",
                    tafseerSaadi = "أي أعتصم وألتجئ برب الصبح الذي يفلق الظلام.",
                    tafseerJalalayn = "ألتجئ برب الصبح وفالقه.",
                    tafseerMuyassar = "قل يا محمد: أعتصم وأتحصن برب الصبح."),
                Ayah(113, 2, 6226, "مِن شَرِّ مَا خَلَقَ", "From the evil of that which He created",
                    tafseerSaadi = "من شر كل مخلوق فيه شر من إنس وجن ودواب وهوام.",
                    tafseerJalalayn = "من شر جميع ما خلق من المكلفين وغيرهم.",
                    tafseerMuyassar = "من شر جميع المخلوقات المؤذية."),
                Ayah(113, 3, 6227, "وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ", "And from the evil of darkness when it settles",
                    tafseerSaadi = "ومن شر الليل وما ينتشر فيه من الشرور وأهل المعاصي.",
                    tafseerJalalayn = "من شر الليل إذا أظلم ودخل في كل شيء.",
                    tafseerMuyassar = "ومن شر الليل إذا اشتدت ظلمته وما ينتشر فيه."),
                Ayah(113, 4, 6228, "وَمِن شَرِّ النَّفَّاثَاتِ فِي الْعُقَدِ", "And from the evil of the blowers in knots",
                    tafseerSaadi = "ومن شر الساحرات اللاتي ينفثن في عقد السحر للإضرار.",
                    tafseerJalalayn = "من شر السواحر اللاتي يعقدن الخيوط وينفثن فيها بالسحر.",
                    tafseerMuyassar = "ومن شر السواحر اللاتي يعقدن وينفخن في العقد للإضرار بالناس."),
                Ayah(113, 5, 6229, "وَمِن شَرِّ حَاسِدٍ إِذَا حَسَدَ", "And from the evil of an envier when he envies",
                    tafseerSaadi = "ومن شر من يتمنى زوال النعمة عن غيره ويسعى في إيذائه.",
                    tafseerJalalayn = "من شر من يتمنى زوال نعمتك ويظهر حسده بالعمل.",
                    tafseerMuyassar = "ومن شر حاسد مبغض يتمنى زوال النعمة عن غيره.")
            )
            114 -> listOf(
                Ayah(114, 1, 6230, "قُلْ أَعُوذُ بِرَبِّ النَّاسِ", "Say, I seek refuge in the Lord of mankind",
                    tafseerSaadi = "ألتجئ وأعتصم بخالق الناس ومالكهم ومربيهم بنعمه.",
                    tafseerJalalayn = "ألتجئ بخالق الناس ومدبر أمورهم.",
                    tafseerMuyassar = "قل: أعتصم وأتحصن برب البشر وخالقهم."),
                Ayah(114, 2, 6231, "مَلِكِ النَّاسِ", "The Sovereign of mankind",
                    tafseerSaadi = "المتصرف فيهم بالأمر والنهي والجزاء.",
                    tafseerJalalayn = "المتصرف فيهم وحده بما يشاء.",
                    tafseerMuyassar = "الملك المتصرف في شؤونهم بتمام الحكمة والعدل."),
                Ayah(114, 3, 6232, "إِلَٰهِ النَّاسِ", "The God of mankind",
                    tafseerSaadi = "معبودهم الحق الذي لا معبود سواه.",
                    tafseerJalalayn = "معبودهم الحق الذي لا شريك له.",
                    tafseerMuyassar = "معبودهم الحق الذي لا تنبغي العبادة إلا له."),
                Ayah(114, 4, 6233, "مِن شَرِّ الْوَسْوَاسِ الْخَنَّاسِ", "From the evil of the retreating whisperer",
                    tafseerSaadi = "من شر الشيطان الملازم الذي يوسوس عند الغفلة، ويخنس عند ذكر الله.",
                    tafseerJalalayn = "الشيطان يوسوس في الصدور فإذا ذُكر الله خنس وتوارى.",
                    tafseerMuyassar = "من شر الشيطان الذي يلقي وساوسه في النفس ويختفي عند ذكر الله."),
                Ayah(114, 5, 6234, "الَّذِي يُوَسْوِسُ فِي صُدُورِ النَّاسِ", "Who whispers into the breasts of mankind",
                    tafseerSaadi = "يلقي الشبه والشهوات ويزين الباطل في قلوب الخلق.",
                    tafseerJalalayn = "يلقي الوسوسة في قلوبهم إذا غفلوا عن ذكر ربهم.",
                    tafseerMuyassar = "الذي يبث الشكوك والخواطر الرديئة في قلوب الناس."),
                Ayah(114, 6, 6235, "مِنَ الْجِنَّةِ وَالنَّاسِ", "From among the jinn and mankind",
                    tafseerSaadi = "بيان لأن الوسواس قد يكون من شياطين الإنس أو شياطين الجن.",
                    tafseerJalalayn = "بيان للوسواس أنه يكون من شياطين الإنس والجن.",
                    tafseerMuyassar = "من شياطين الجن وشياطين الإنس.")
            )
            else -> {
                // Synthesize Surah placeholder verses with authentic Bismillah and Quranic verse template
                val surahInfo = SURAHS.firstOrNull { it.id == surahId } ?: SURAHS[0]
                val list = mutableListOf<Ayah>()
                if (surahId != 9) { // At-Tawbah does not begin with Basmala
                    list.add(
                        Ayah(
                            surahId, 1, 1,
                            "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                            "In the name of Allah, the Entirely Merciful, the Especially Merciful",
                            tafseerSaadi = "أبتدئ قراءتي مستعيناً باسم الله الرحمن الرحيم.",
                            tafseerJalalayn = "البسملة آية مباركة تفتتح بها سور القرآن.",
                            tafseerMuyassar = "أبدأ قراءتي مستعينا باسم الله تبارك وتعالى."
                        )
                    )
                }
                val startIdx = if (surahId != 9) 2 else 1
                val demoCount = minOf(surahInfo.versesCount, 10)
                for (i in startIdx..demoCount) {
                    list.add(
                        Ayah(
                            surahId, i, i,
                            "آية رقم $i من سورة ${surahInfo.nameAr} المباركة — هدى ونور وشفاء لما في الصدور",
                            "Verse $i of Surah ${surahInfo.nameEn}",
                            tafseerSaadi = "تفسير الآية الكريمة: بيان الهداية والرحمة والحكمة الإلهية.",
                            tafseerJalalayn = "تفسير الجلالين: هداية وتوجيه وبيان لأحكام الشريعة.",
                            tafseerMuyassar = "التفسير الميسر: ترغيب في الطاعة والعمل الصالح والتدبر."
                        )
                    )
                }
                list
            }
        }

        // Attach audio streaming URL for each Ayah according to reciter pattern
        return baseAyahs.map { ayah ->
            val surahFormatted = String.format("%03d", ayah.surahId)
            val ayahFormatted = String.format("%03d", ayah.numberInSurah)
            val audioUrl = "${reciter.serverBaseUrl}$surahFormatted$ayahFormatted.mp3"
            ayah.copy(audioUrl = audioUrl)
        }
    }

    fun searchQuran(query: String): List<Pair<Surah, List<Ayah>>> {
        val cleanQuery = query.trim()
        if (cleanQuery.isEmpty()) return emptyList()

        val results = mutableListOf<Pair<Surah, List<Ayah>>>()
        for (surah in SURAHS) {
            val ayahs = getAyahsForSurah(surah.id)
            val matchedAyahs = ayahs.filter {
                it.textAr.contains(cleanQuery, ignoreCase = true) ||
                it.textEn.contains(cleanQuery, ignoreCase = true) ||
                surah.nameAr.contains(cleanQuery, ignoreCase = true) ||
                surah.nameEn.contains(cleanQuery, ignoreCase = true)
            }
            if (matchedAyahs.isNotEmpty() || surah.nameAr.contains(cleanQuery, ignoreCase = true)) {
                results.add(Pair(surah, matchedAyahs))
            }
        }
        return results
    }
}
