package com.example.data

import com.example.model.DailyHadith
import com.example.model.DailyVerse
import com.example.model.HijriDate
import com.example.model.ReligiousOccasion
import java.util.Calendar

object DailyContentProvider {

    private val DAILY_VERSES = listOf(
        DailyVerse(
            ayahText = "أَلَا بِذِكْرِ اللَّهِ تَطْمَئِنُّ الْقُلُوبُ",
            surahName = "سورة الرعد",
            ayahNumber = 28,
            tafseerSnippet = "أي تسكن قلوب المؤمنين وتأنس بذكره وتستقر بالتوحيد والرضا بقضائه."
        ),
        DailyVerse(
            ayahText = "وَإِذَا سَأَلَكَ عِبَادِي عَنِّي فَإِنِّي قَرِيبٌ ۖ أُجِيبُ دَعْوَةَ الدَّاعِ إِذَا دَعَانِ",
            surahName = "سورة البقرة",
            ayahNumber = 186,
            tafseerSnippet = "بشارة عظيمة للمؤمن بقرب الله منه وسماعه لندائه وإجابته لدعائه الصادق."
        ),
        DailyVerse(
            ayahText = "إِنَّ مَعَ الْعُسْرِ يُسْرًا",
            surahName = "سورة الشرح",
            ayahNumber = 6,
            tafseerSnippet = "وعد رباني قاطع بأن بعد كل ضيق ومحنة فرجاً وتيسيراً قريباً."
        ),
        DailyVerse(
            ayahText = "وَمَن يَتَّقِ اللَّهَ يَجْعَل لَّهُ مَخْرَجًا وَيَرْزُقْهُ مِنْ حَيْثُ لَا يَحْتَسِبُ",
            surahName = "سورة الطلاق",
            ayahNumber = 2,
            tafseerSnippet = "من اتقى الله في أمره كفاه همه وفتح له أبواب الخير والرزق المبارك."
        ),
        DailyVerse(
            ayahText = "وَتَوَكَّلْ عَلَى الْحَيِّ الَّذِي لَا يَمُوتُ وَسَبِّحْ بِحَمْدِهِ",
            surahName = "سورة الفرقان",
            ayahNumber = 58,
            tafseerSnippet = "فوض أمرك كله لله الواحد الدائم الذي لا تأخذه سنة ولا موت."
        )
    )

    private val DAILY_HADITHS = listOf(
        DailyHadith(
            text = "مَنْ سَلَكَ طَرِيقاً يَلْتَمِسُ فِيهِ عِلْماً، سَهَّلَ اللَّهُ لَهُ بِهِ طَرِيقاً إِلَى الْجَنَّةِ",
            narrator = "عن أبي هريرة رضي الله عنه",
            source = "صحيح مسلم",
            explanation = "حث عظيم على طلب العلم النافع وبيان منزلته وثوابه الرفيع عند الله تعالى."
        ),
        DailyHadith(
            text = "خَيْرُكُمْ مَنْ تَعَلَّمَ القُرْآنَ وَعَلَّمَهُ",
            narrator = "عن عثمان بن عفان رضي الله عنه",
            source = "صحيح البخاري",
            explanation = "أشرف العلوم وأعظم الرتب هي صحبة القرآن الكريم حفظاً وفهماً وتعليماً للناس."
        ),
        DailyHadith(
            text = "عَجَباً لأَمْرِ الْمُؤْمِنِ إِنَّ أَمْرَهُ كُلَّهُ خَيْرٌ، وَلَيْسَ ذَاكَ لأَحَدٍ إِلاَّ لِلْمُؤْمِنِ: إِنْ أَصَابَتْهُ سَرَّاءُ شَكَرَ فَكَانَ خَيْراً لَهُ، وَإِنْ أَصَابَتْهُ ضَرَّاءُ صَبَرَ فَكَانَ خَيْراً لَهُ",
            narrator = "عن صهيب الرومي رضي الله عنه",
            source = "صحيح مسلم",
            explanation = "المؤمن رابح في جميع أحواله بين الشكر في الرخاء والصبر في البلاء."
        ),
        DailyHadith(
            text = "كَلِمَتَانِ خَفِيفَتَانِ عَلَى اللِّسَانِ، ثَقِيلَتَانِ فِي الْمِيزَانِ، حَبِيبَتَانِ إِلَى الرَّحْمَنِ: سُبْحَانَ اللَّهِ وَبِحَمْدِهِ، سُبْحَانَ اللَّهِ الْعَظِيمِ",
            narrator = "عن أبي هريرة رضي الله عنه",
            source = "صحيح البخاري ومسلم",
            explanation = "فضل هاتين الكلمتين العظيمتين في ميزان الحسنات يوم القيامة."
        )
    )

    fun getTodayVerse(): DailyVerse {
        val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        return DAILY_VERSES[dayOfYear % DAILY_VERSES.size]
    }

    fun getTodayHadith(): DailyHadith {
        val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        return DAILY_HADITHS[dayOfYear % DAILY_HADITHS.size]
    }

    fun getHijriDate(): HijriDate {
        // Approximate astronomical Hijri calculation
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH) + 1
        val day = cal.get(Calendar.DAY_OF_MONTH)

        // Julian Day
        var y = year
        var m = month
        if (m <= 2) {
            y -= 1
            m += 12
        }
        val a = Math.floor(y / 100.0)
        val b = 2 - a + Math.floor(a / 4.0)
        val jd = Math.floor(365.25 * (y + 4716)) + Math.floor(30.6001 * (m + 1)) + day + b - 1524.5

        val l = jd - 1948440 + 10632
        val n = Math.floor((l - 1) / 10631.0)
        val l1 = l - 10631 * n + 354
        val j = (Math.floor((10985 - l1) / 5316.0)) * (Math.floor((50 * l1) / 17719.0)) +
                (Math.floor(l1 / 5670.0)) * (Math.floor((43 * l1) / 15238.0))
        val l2 = l1 - (Math.floor((30 - j) / 15.0)) * (Math.floor((17719 * j) / 50.0)) -
                (Math.floor(j / 16.0)) * (Math.floor((15238 * j) / 43.0)) + 29
        val hijriMonth = Math.floor((24 * l2) / 709.0).toInt()
        val hijriDay = (l2 - Math.floor((709 * hijriMonth) / 24.0)).toInt()
        val hijriYear = (30 * n + j - 30).toInt()

        val monthNames = listOf(
            "محرم", "صفر", "ربيع الأول", "ربيع الآخر",
            "جمادى الأولى", "جمادى الآخرة", "رجب", "شعبان",
            "رمضان المبارك", "شوال", "ذو القعدة", "ذو الحجة"
        )
        val safeMonth = (hijriMonth - 1).coerceIn(0, 11)
        val nameAr = monthNames[safeMonth]
        val formatted = "$hijriDay $nameAr $hijriYear هـ"

        return HijriDate(
            day = hijriDay,
            monthNumber = safeMonth + 1,
            monthNameAr = nameAr,
            year = hijriYear,
            formatted = formatted
        )
    }

    fun getUpcomingOccasions(): List<ReligiousOccasion> {
        return listOf(
            ReligiousOccasion("بداية شهر رمضان المبارك", "1 رمضان", 112, "شهر الصيام والقرآن والرحمات ومضاعفة الأجور"),
            ReligiousOccasion("عيد الفطر المبارك", "1 شوال", 142, "فرحة الصائمين بإتمام فريضة الصيام وصلة الأرحام"),
            ReligiousOccasion("يوم عرفة", "9 ذو الحجة", 210, "أعظم أيام العام وصيامه يكفر سنة ماضية وسنة باقية"),
            ReligiousOccasion("عيد الأضحى المبارك", "10 ذو الحجة", 211, "يوم النحر وأيام التشريق وذكر الله وذبح الأضاحي"),
            ReligiousOccasion("رأس السنة الهجرية", "1 محرم", 240, "ذكرى هجرة المصطفى صلى الله عليه وسلم وبداية العام الهجري"),
            ReligiousOccasion("يوم عاشوراء", "10 محرم", 250, "اليوم الذي نجى الله فيه موسى وقومه وصيامه يكفر سنة ماضية")
        )
    }
}
