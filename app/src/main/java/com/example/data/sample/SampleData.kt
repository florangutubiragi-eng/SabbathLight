package com.example.data.sample

import com.example.data.models.BibleBook
import com.example.data.models.BibleChapter
import com.example.data.models.BibleVerse
import com.example.data.models.DailyLesson
import com.example.data.models.EgwBook
import com.example.data.models.EgwChapter
import com.example.data.models.HopeChannelItem
import com.example.data.models.Hymn
import com.example.data.models.PrayerCategory
import com.example.data.models.PrayerItem
import com.example.data.models.QuarterlyLesson
import com.example.data.models.Sermon
import com.example.data.models.SermonType
import com.example.data.models.Testament

object SampleData {

    val dailyVerse = BibleVerse(
        bookId = "isaiah",
        bookNameSwahili = "Isaya",
        bookNameEnglish = "Isaiah",
        chapter = 58,
        verseNumber = 13,
        textSwahili = "Kama ukigeuza mguu wako usihalifu sabato, usifanye anasa yako siku ya utakatifu wangu; ukiita sabato siku ya furaha, na siku takatifu ya Bwana yenye heshima; ukiitukuza, kwa kutokuzifanya njia zako mwenyewe, wala kupata anasa yako, wala kusema maneno yako mwenyewe; ndipo utakapojifurahisha katika Bwana.",
        textEnglish = "If you turn away your foot from the Sabbath, from doing your pleasure on my holy day; and call the Sabbath a delight, the holy day of the Lord, honorable; and honor him, not doing your own ways, nor finding your own pleasure, nor speaking your own words: then you will delight yourself in the Lord."
    )

    val sampleBooks: List<BibleBook> = listOf(
        // Old Testament
        BibleBook(
            id = "genesis",
            nameSwahili = "Mwanzo",
            nameEnglish = "Genesis",
            testament = Testament.OLD,
            totalChapters = 50,
            sampleChapters = listOf(
                BibleChapter(
                    bookId = "genesis",
                    chapterNumber = 1,
                    verses = listOf(
                        BibleVerse("genesis", "Mwanzo", "Genesis", 1, 1, "Hapo mwanzo Mungu aliziumba mbingu na nchi.", "In the beginning God created the heavens and the earth."),
                        BibleVerse("genesis", "Mwanzo", "Genesis", 1, 2, "Nayo nchi ilikuwa ukiwa, tena utupu, na giza lilikuwa juu ya uso wa vilindi vya maji; Roho ya Mungu ikatulia juu ya uso wa maji.", "The earth was formless and empty, and darkness was on the surface of the deep and God's Spirit was hovering over the surface of the waters."),
                        BibleVerse("genesis", "Mwanzo", "Genesis", 1, 3, "Mungu akasema, Iwe nuru; ikawa nuru.", "God said, 'Let there be light,' and there was light."),
                        BibleVerse("genesis", "Mwanzo", "Genesis", 1, 4, "Mungu akaiona nuru, ya kuwa ni njema; Mungu akatenga nuru na giza.", "God saw the light, and saw that it was good. God divided the light from the darkness."),
                        BibleVerse("genesis", "Mwanzo", "Genesis", 1, 5, "Mungu akaiita nuru Mchana, na giza akaliita Usiku. Ikawa jioni ikawa asubuhi, siku ya kwanza.", "God called the light 'day', and the darkness he called 'night'. There was evening and there was morning, one day."),
                        BibleVerse("genesis", "Mwanzo", "Genesis", 1, 6, "Mungu akasema, Na liwe anga katikati ya maji, likatenge maji na maji.", "God said, 'Let there be a expanse in the middle of the waters, and let it divide the waters from the waters.'"),
                        BibleVerse("genesis", "Mwanzo", "Genesis", 1, 7, "Mungu akalifanya anga, akayatenga maji yaliyo chini ya anga na maji yaliyo juu ya anga; ikawa hivyo.", "God made the expanse, and divided the waters which were under the expanse from the waters which were above the expanse; and it was so."),
                        BibleVerse("genesis", "Mwanzo", "Genesis", 1, 8, "Mungu akaliita anga Mbingu. Ikawa jioni ikawa asubuhi, siku ya pili.", "God called the expanse 'sky'. There was evening and there was morning, a second day."),
                        BibleVerse("genesis", "Mwanzo", "Genesis", 1, 9, "Mungu akasema, Maji yaliyo chini ya mbingu na yakusanyike mahali pamoja, ili pakavu paonekane; ikawa hivyo.", "God said, 'Let the waters under the sky be gathered together to one place, and let the dry land appear;' and it was so."),
                        BibleVerse("genesis", "Mwanzo", "Genesis", 1, 10, "Mungu akapaita pakavu Nchi; na makusanyiko ya maji akayaita Bahari; Mungu akaona ya kuwa ni vyema.", "God called the dry land 'earth', and the gathering together of the waters he called 'seas'. God saw that it was good."),
                        BibleVerse("genesis", "Mwanzo", "Genesis", 1, 11, "Mungu akasema, Nchi na itoe majani, mche utoao mbegu, na mti wa matunda uzaao matunda kwa jinsi yake, ambao mbegu zake zimo ndani yake, juu ya nchi; ikawa hivyo.", "God said, 'Let the earth yield grass, herbs yielding seeds, and fruit trees bearing fruit after their kind, with their seeds in it, on the earth;' and it was so."),
                        BibleVerse("genesis", "Mwanzo", "Genesis", 1, 12, "Nchi ikatoa majani, mche utoao mbegu kwa jinsi yake, na mti uzaao matunda, ambao mbegu zake zimo ndani yake, kwa jinsi yake; Mungu akaona ya kuwa ni vyema.", "The earth yielded grass, herbs yielding seed after their kind, and trees bearing fruit, with their seeds in it, after their kind; and God saw that it was good."),
                        BibleVerse("genesis", "Mwanzo", "Genesis", 1, 13, "Ikawa jioni ikawa asubuhi, siku ya tatu.", "There was evening and there was morning, a third day."),
                        BibleVerse("genesis", "Mwanzo", "Genesis", 1, 14, "Mungu akasema, Na yawe mianga katika anga la mbingu ili itenge mchana na usiku; nayo iwe ishara na majira na siku na miaka;", "God said, 'Let there be lights in the expanse of the sky to divide the day from the night; and let them be for signs to mark seasons, days, and years;'"),
                        BibleVerse("genesis", "Mwanzo", "Genesis", 1, 15, "tena iwe mianga katika anga la mbingu ili itiishe nchi; ikawa hivyo.", "'and let them be for lights in the expanse of the sky to give light on the earth;' and it was so."),
                        BibleVerse("genesis", "Mwanzo", "Genesis", 1, 16, "Mungu akafanya mianga miwili mikubwa; mwangaza mkubwa utawale mchana, na mwangaza mdogo utawale usiku; akafanya na nyota pia.", "God made the two great lights: the greater light to rule the day, and the lesser light to rule the night. He also made the stars."),
                        BibleVerse("genesis", "Mwanzo", "Genesis", 1, 17, "Mungu akaiweka katika anga la mbingu ili itiishe nchi,", "God set them in the expanse of the sky to give light to the earth,"),
                        BibleVerse("genesis", "Mwanzo", "Genesis", 1, 18, "na kutawala mchana na usiku, na kutenga nuru na giza; Mungu akaona ya kuwa ni vyema.", "and to rule over the day and over the night, and to divide the light from the darkness. God saw that it was good."),
                        BibleVerse("genesis", "Mwanzo", "Genesis", 1, 19, "Ikawa jioni ikawa asubuhi, siku ya nne.", "There was evening and there was morning, a fourth day."),
                        BibleVerse("genesis", "Mwanzo", "Genesis", 1, 20, "Mungu akasema, Maji na yazae kwa wingi viumbe vyenye uhai, na ndege waruke juu ya nchi katika anga la mbingu.", "God said, 'Let the waters abound with living creatures, and let birds fly above the earth in the open expanse of the sky.'"),
                        BibleVerse("genesis", "Mwanzo", "Genesis", 1, 21, "Mungu akaumba nyangumi wakubwa, na kila kiumbe chenye uhai kitembeacho, ambavyo maji yalijazwa navyo kwa wingi kwa jinsi zao, na kila ndege arukaye, kwa jinsi yake; Mungu akaona ya kuwa ni vyema.", "God created the large sea creatures, and every living creature that moves, with which the waters swarmed, after their kind, and every winged bird after its kind. God saw that it was good."),
                        BibleVerse("genesis", "Mwanzo", "Genesis", 1, 22, "Mungu akawabarikia, akisema, Zaeni, mkaongezeke, mkayajaze maji ya baharini, na ndege waongezeke katika nchi.", "God blessed them, saying, 'Be fruitful, and multiply, and fill the waters in the seas, and let birds multiply on the earth.'"),
                        BibleVerse("genesis", "Mwanzo", "Genesis", 1, 23, "Ikawa jioni ikawa asubuhi, siku ya tano.", "There was evening and there was morning, a fifth day."),
                        BibleVerse("genesis", "Mwanzo", "Genesis", 1, 24, "Mungu akasema, Nchi na izae kiumbe chenye uhai kwa jinsi yake, mnyama wa kufugwa, nacho kitambaacho, na mnyama wa mwitu kwa jinsi yake; ikawa hivyo.", "God said, 'Let the earth produce living creatures after their kind, livestock, creeping things, and animals of the earth after their kind;' and it was so."),
                        BibleVerse("genesis", "Mwanzo", "Genesis", 1, 25, "Mungu akafanya mnyama wa mwitu kwa jinsi yake, na mnyama wa kufugwa kwa jinsi yake, na kila kitu kitambaacho juu ya nchi kwa jinsi yake; Mungu akaona ya kuwa ni vyema.", "God made the animals of the earth after their kind, and the livestock after their kind, and everything that creeps on the ground after its kind. God saw that it was good."),
                        BibleVerse("genesis", "Mwanzo", "Genesis", 1, 26, "Mungu akasema, Na tumfanye mtu kwa mfano wetu, kwa sura yetu; wakatawale samaki wa baharini, na ndege wa angani, na wanyama, na nchi yote pia, na kila chenye kutambaa kitambaacho juu ya nchi.", "God said, 'Let's make man in our image, after our likeness. Let them have dominion over the fish of the sea, and over the birds of the sky, and over the livestock, and over all the earth, and over every creeping thing that creeps on the earth.'"),
                        BibleVerse("genesis", "Mwanzo", "Genesis", 1, 27, "Mungu akaumba mtu kwa mfano wake, kwa mfano wa Mungu alimwumba; mwanamume na mwanamke aliwaumba.", "God created man in his own image. In God's image he created him; male and female he created them."),
                        BibleVerse("genesis", "Mwanzo", "Genesis", 1, 28, "Mungu akawabarikia, Mungu akawaambia, Zaeni, mkaongezeke, mkaijaze nchi, na kuitiisha; mkatawale samaki wa baharini, na ndege wa angani, na kila kiumbe chenye uhai kiendacho juu ya nchi.", "God blessed them. God said to them, 'Be fruitful, multiply, fill the earth, and subdue it. Have dominion over the fish of the sea, over the birds of the sky, and over every living thing that moves on the earth.'"),
                        BibleVerse("genesis", "Mwanzo", "Genesis", 1, 29, "Mungu akasema, Tazama, nimewapa kila mche utoao mbegu, ulio juu ya uso wa nchi yote pia, na kila mti ambao matunda yake yana mbegu; vitakuwa chakula chenu;", "God said, 'Behold, I have given you every herb yielding seed, which is on the surface of all the earth, and every tree, which bears fruit yielding seed. It will be your food.'"),
                        BibleVerse("genesis", "Mwanzo", "Genesis", 1, 30, "na kwa kila mnyama wa nchi, na kwa kila ndege wa angani, na kwa kila kitu kitambaacho juu ya nchi, chenye uhai, nimewapa kila jani bichi kuwa chakula chake; ikawa hivyo.", "'To every animal of the earth, and to every bird of the sky, and to everything that creeps on the earth, in which there is life, I have given every green herb for food;' and it was so."),
                        BibleVerse("genesis", "Mwanzo", "Genesis", 1, 31, "Mungu akaona kila kitu alichokifanya, na tazama, ni chema sana. Ikawa jioni ikawa asubuhi, siku ya sita.", "God saw everything that he had made, and, behold, it was very good. There was evening and there was morning, a sixth day.")
                    )
                ),
                BibleChapter(
                    bookId = "genesis",
                    chapterNumber = 2,
                    verses = listOf(
                        BibleVerse("genesis", "Mwanzo", "Genesis", 2, 1, "Basi mbingu na nchi zikamalizika, na jeshi lake lote.", "The heavens and the earth were finished, and all their vast array."),
                        BibleVerse("genesis", "Mwanzo", "Genesis", 2, 2, "Na siku ya saba Mungu alimaliza kazi yake yote aliyoifanya; akastarehe siku ya saba, akaacha kufanya kazi yake yote aliyoifanya.", "On the seventh day God finished his work which he had made; and he rested on the seventh day from all his work which he had made."),
                        BibleVerse("genesis", "Mwanzo", "Genesis", 2, 3, "Mungu akaibarikia siku ya saba, akaitakasa; kwa sababu katika siku hiyo Mungu alistarehe, akaacha kufanya kazi yake yote aliyoiumba na kuifanya.", "God blessed the seventh day, and sanctified it, because he rested in it from all his work which he had created and made."),
                        BibleVerse("genesis", "Mwanzo", "Genesis", 2, 4, "Hizo ndizo asili za mbingu na nchi zilipoumbwa, siku ile Bwana Mungu alipozifanya nchi na mbingu.", "This is the history of the generations of the heavens and of the earth when they were created, in the day that Yahweh God made the earth and the heavens.")
                    )
                )
            )
        ),
        BibleBook("exodus", "Kutoka", "Exodus", Testament.OLD, 40),
        BibleBook("leviticus", "Mambo ya Walawi", "Leviticus", Testament.OLD, 27),
        BibleBook("numbers", "Hesabu", "Numbers", Testament.OLD, 36),
        BibleBook("deuteronomy", "Kumbukumbu la Torati", "Deuteronomy", Testament.OLD, 34),
        BibleBook(
            id = "psalms",
            nameSwahili = "Zaburi",
            nameEnglish = "Psalms",
            testament = Testament.OLD,
            totalChapters = 150,
            sampleChapters = listOf(
                BibleChapter(
                    bookId = "psalms",
                    chapterNumber = 23,
                    verses = listOf(
                        BibleVerse("psalms", "Zaburi", "Psalms", 23, 1, "Bwana ndiye mchungaji wangu, sitapungukiwa na kitu.", "The Lord is my shepherd: I shall not want."),
                        BibleVerse("psalms", "Zaburi", "Psalms", 23, 2, "Hukulaza katika malisho ya majani mabichi, huniongoza kando ya maji ya utulivu.", "He makes me lie down in green pastures. He leads me beside still waters."),
                        BibleVerse("psalms", "Zaburi", "Psalms", 23, 3, "Hunihuisha nafsi yangu; na kuniongoza katika njia za haki kwa ajili ya jina lake.", "He restores my soul. He guides me in paths of righteousness for his name's sake."),
                        BibleVerse("psalms", "Zaburi", "Psalms", 23, 4, "Naam, nijapopita kati ya bonde la uvuli wa mauti, sitaogopa mabaya; kwa maana Wewe upo pamoja nami, gongo lako na fimbo yako vyanifariji.", "Even though I walk through the valley of the shadow of death, I will fear no evil, for you are with me. Your rod and your staff, they comfort me."),
                        BibleVerse("psalms", "Zaburi", "Psalms", 23, 5, "Waandaa meza mbele yangu, machoni pa watesi wangu; umenipaka mafuta kichwani pangu, na kikombe changu kinafurika.", "You prepare a table before me in the presence of my enemies. You anoint my head with oil. My cup runs over."),
                        BibleVerse("psalms", "Zaburi", "Psalms", 23, 6, "Hakika wema na fadhili zitanifuata siku zote za maisha yangu; nami nitakaa nyumbani mwa Bwana milele.", "Surely goodness and loving kindness shall follow me all the days of my life, and I will dwell in Yahweh's house forever.")
                    )
                ),
                BibleChapter(
                    bookId = "psalms",
                    chapterNumber = 91,
                    verses = listOf(
                        BibleVerse("psalms", "Zaburi", "Psalms", 91, 1, "Aketiye mahali pa siri pake Aliye Juu atakaa katika uvuli wake Mwenyezi.", "He who dwells in the secret place of the Most High will rest in the shadow of the Almighty."),
                        BibleVerse("psalms", "Zaburi", "Psalms", 91, 2, "Nitasema juu ya Bwana, Ndiye kimbilio langu na ngome yangu, Mungu wangu nitakayemtumaini.", "I will say of Yahweh, 'He is my refuge and my fortress; my God, in whom I trust.'"),
                        BibleVerse("psalms", "Zaburi", "Psalms", 91, 3, "Kwa maana Yeye atakuokoa na mtego wa mwindaji, na katika tauni iharibuyo.", "For he will deliver you from the snare of the fowler, and from the deadly pestilence."),
                        BibleVerse("psalms", "Zaburi", "Psalms", 91, 4, "Kwa manyoya yake atakufunika, chini ya mabawa yake utapata kimbilio; uaminifu wake ni ngao na kigao.", "He will cover you with his feathers. Under his wings you will take refuge. His faithfulness is your shield and rampart.")
                    )
                )
            )
        ),
        BibleBook("proverbs", "Methali", "Proverbs", Testament.OLD, 31),
        BibleBook(
            id = "isaiah",
            nameSwahili = "Isaya",
            nameEnglish = "Isaiah",
            testament = Testament.OLD,
            totalChapters = 66,
            sampleChapters = listOf(
                BibleChapter(
                    bookId = "isaiah",
                    chapterNumber = 58,
                    verses = listOf(
                        BibleVerse("isaiah", "Isaya", "Isaiah", 58, 13, "Kama ukigeuza mguu wako usihalifu sabato, usifanye anasa yako siku ya utakatifu wangu; ukiita sabato siku ya furaha, na siku takatifu ya Bwana yenye heshima; ukiitukuza, kwa kutokuzifanya njia zako mwenyewe, wala kupata anasa yako, wala kusema maneno yako mwenyewe;", "If you turn away your foot from the Sabbath, from doing your pleasure on my holy day; and call the Sabbath a delight, the holy day of Yahweh, honorable; and honor him, not doing your own ways, nor finding your own pleasure, nor speaking your own words:"),
                        BibleVerse("isaiah", "Isaya", "Isaiah", 58, 14, "ndipo utakapojifurahisha katika Bwana; nami nitakupandisha mahali pa nchi palipoinuka, nitakulisha urithi wa Yakobo baba yako; kwa maana kinywa cha Bwana kimenena hayo.", "then you will delight yourself in Yahweh; and I will make you to ride on the high places of the earth, and I will feed you with the heritage of Jacob your father; for Yahweh's mouth has spoken it.")
                    )
                )
            )
        ),
        BibleBook("jeremiah", "Yeremia", "Jeremiah", Testament.OLD, 52),
        BibleBook("daniel", "Danieli", "Daniel", Testament.OLD, 12),

        // New Testament
        BibleBook(
            id = "matthew",
            nameSwahili = "Mathayo",
            nameEnglish = "Matthew",
            testament = Testament.NEW,
            totalChapters = 28,
            sampleChapters = listOf(
                BibleChapter(
                    bookId = "matthew",
                    chapterNumber = 5,
                    verses = listOf(
                        BibleVerse("matthew", "Mathayo", "Matthew", 5, 1, "Naye alipowaona makutano, alipanda mlimani; na alipokwisha kuketi, wanafunzi wake walimwendea;", "Seeing the multitudes, he went up onto the mountain. When he had sat down, his disciples came to him."),
                        BibleVerse("matthew", "Mathayo", "Matthew", 5, 2, "akafumbua kinywa chake, akawafundisha, akisema,", "He opened his mouth and taught them, saying,"),
                        BibleVerse("matthew", "Mathayo", "Matthew", 5, 3, "Heri walio maskini wa roho; Maana ufalme wa mbinguni ni wao.", "Blessed are the poor in spirit, for theirs is the Kingdom of Heaven."),
                        BibleVerse("matthew", "Mathayo", "Matthew", 5, 4, "Heri wenye huzuni; Maana hao watafarijiwa.", "Blessed are those who mourn, for they shall be comforted."),
                        BibleVerse("matthew", "Mathayo", "Matthew", 5, 5, "Heri wenye upole; Maana hao watairithi nchi.", "Blessed are the gentle, for they shall inherit the earth."),
                        BibleVerse("matthew", "Mathayo", "Matthew", 5, 6, "Heri wenye njaa na kiu ya haki; Maana hao watashibishwa.", "Blessed are those who hunger and thirst for righteousness, for they shall be filled.")
                    )
                )
            )
        ),
        BibleBook("mark", "Marko", "Mark", Testament.NEW, 16),
        BibleBook("luke", "Luka", "Luke", Testament.NEW, 24),
        BibleBook(
            id = "john",
            nameSwahili = "Yohana",
            nameEnglish = "John",
            testament = Testament.NEW,
            totalChapters = 21,
            sampleChapters = listOf(
                BibleChapter(
                    bookId = "john",
                    chapterNumber = 1,
                    verses = listOf(
                        BibleVerse("john", "Yohana", "John", 1, 1, "Hapo mwanzo kulikuwako Neno, naye Neno alikuwako kwa Mungu, naye Neno alikuwa Mungu.", "In the beginning was the Word, and the Word was with God, and the Word was God."),
                        BibleVerse("john", "Yohana", "John", 1, 2, "Huyo mwanzo alikuwako kwa Mungu.", "The same was in the beginning with God."),
                        BibleVerse("john", "Yohana", "John", 1, 3, "Vyote vilifanyika kwa huyo; wala pasipo yeye hakikufanyika cho chote kilichofanyika.", "All things were made through him. Without him was not anything made that has been made."),
                        BibleVerse("john", "Yohana", "John", 1, 4, "Ndani yake ndimo ulimokuwa uzima, nao ule uzima ulikuwa nuru ya watu.", "In him was life, and the life was the light of men."),
                        BibleVerse("john", "Yohana", "John", 1, 5, "Nayo nuru yang'aa gizani, wala giza halikuiweza.", "The light shines in the darkness, and the darkness hasn't overcome it.")
                    )
                ),
                BibleChapter(
                    bookId = "john",
                    chapterNumber = 14,
                    verses = listOf(
                        BibleVerse("john", "Yohana", "John", 14, 1, "Msifadhaike mioyoni mwenu; mnamwamini Mungu, niaminini na mimi.", "Don't let your heart be troubled. Believe in God. Believe also in me."),
                        BibleVerse("john", "Yohana", "John", 14, 2, "Nyumbani mwa Baba yangu mna makao mengi; kama sivyo, ningaliwaambia; maana naenda kuwaandalia mahali.", "In my Father's house are many homes. If it weren't so, I would have told you. I am going to prepare a place for you."),
                        BibleVerse("john", "Yohana", "John", 14, 3, "Basi mimi nikienda na kuwaandalia mahali, nitakuja tena niwakaribishe kwangu; ili nilipo mimi, nanyi mwepo.", "If I go and prepare a place for you, I will come again, and will receive you to myself; that where I am, you may be there also."),
                        BibleVerse("john", "Yohana", "John", 14, 6, "Yesu akamwambia, Mimi ndimi njia, na kweli, na uzima; mtu haji kwa Baba, ila kwa njia ya mimi.", "Jesus said to him, 'I am the way, the truth, and the life. No one comes to the Father, except through me.'")
                    )
                )
            )
        ),
        BibleBook("acts", "Matendo ya Mitume", "Acts", Testament.NEW, 28),
        BibleBook("romans", "Warumi", "Romans", Testament.NEW, 16),
        BibleBook("corinthians1", "1 Wakorintho", "1 Corinthians", Testament.NEW, 16),
        BibleBook("hebrews", "Waebrania", "Hebrews", Testament.NEW, 13),
        BibleBook(
            id = "revelation",
            nameSwahili = "Ufunuo wa Yohana",
            nameEnglish = "Revelation",
            testament = Testament.NEW,
            totalChapters = 22,
            sampleChapters = listOf(
                BibleChapter(
                    bookId = "revelation",
                    chapterNumber = 14,
                    verses = listOf(
                        BibleVerse("revelation", "Ufunuo", "Revelation", 14, 6, "Kisha nikaona malaika mwingine, akiruka katikati ya mbingu, mwenye Injili ya milele, awahubiri hao wakaao juu ya nchi, na kila taifa na kabila na lugha na jamaa,", "I saw an angel flying in mid heaven, having an eternal good news to proclaim to those who dwell on the earth, and to every nation, tribe, language, and people."),
                        BibleVerse("revelation", "Ufunuo", "Revelation", 14, 7, "akasema kwa sauti kuu, Mcheni Mungu, na kumtukuza, kwa maana saa ya hukumu yake imekuja. Msujudieni yeye aliyezifanya mbingu na nchi na bahari na chemchemi za maji.", "He said with a loud voice, 'Fear God, and give him glory; for the hour of his judgment has come. Worship him who made the heaven, the earth, the sea, and the springs of waters!'"),
                        BibleVerse("revelation", "Ufunuo", "Revelation", 14, 12, "Hapa ndipo penye subira ya watakatifu, hao wazishikao amri za Mungu, na imani ya Yesu.", "Here is the perseverance of the saints, those who keep the commandments of God, and the faith of Jesus.")
                    )
                )
            )
        )
    )

    val sampleHymns: List<Hymn> = HymnCatalog.hymns

    val currentQuarterlyLesson = QuarterlyLesson(
        id = "2026_q4_lesson_4",
        quarterTitleSwahili = "Robo ya 4, 2026: Ishara za Tumaini na Imani Kuu",
        quarterTitleEnglish = "Quarter 4, 2026: Signs of Hope & Steadfast Faith",
        lessonNumber = 4,
        lessonTitleSwahili = "Ahadi ya Vizazi na Tumaini la Kurudi kwa Kristo",
        lessonTitleEnglish = "The Promise of Ages and Hope of Christ's Return",
        dateRange = "Oktoba 24 - 30, 2026",
        memoryVerseSwahili = "Msifadhaike mioyoni mwenu; mnamwamini Mungu, niaminini na mimi. Nyumbani mwa Baba yangu mna makao mengi; kama sivyo, ningaliwaambia; maana naenda kuwaandalia mahali.",
        memoryVerseEnglish = "Don't let your heart be troubled. Believe in God. Believe also in me. In my Father's house are many homes. If it weren't so, I would have told you. I am going to prepare a place for you.",
        memoryVerseReference = "Yohana 14:1, 2",
        dailyLessons = listOf(
            DailyLesson(
                id = "day_0",
                dayIndex = 0,
                dayNameSwahili = "Sabato Alasiri",
                dayNameEnglish = "Sabbath Afternoon",
                titleSwahili = "Utangulizi: Tumaini Lisiloshindwa",
                titleEnglish = "Introduction: An Unfailing Hope",
                dateString = "Oktoba 24",
                scriptureReferences = listOf("Yohana 14:1-3", "Tito 2:13", "Waebrania 10:23"),
                contentSwahili = "Katika ulimwengu uliyojaa mashaka, vurugu na magonjwa, ahadi ya kurudi kwa mara ya pili ya Bwana wetu Yesu Kristo ndiyo nanga thabiti ya roho zetu. Manabii wa Agano la Kale walitabiri kuhusu siku hii kuu, na Yesu mwenyewe alithibitisha ahadi hiyo kabla ya kupaa mbinguni. Somo la juma hili linatualika kutafakari uzito na utukufu wa ahadi hii ambayo inatuweka imara kila siku ya maisha yetu.",
                contentEnglish = "In a world filled with anxiety, conflict, and sickness, the promise of the Second Coming of our Lord Jesus Christ is the steadfast anchor of our souls. Old Testament prophets foresaw this great day, and Jesus Himself affirmed it before ascending to heaven. This week's study invites us to meditate on the beauty and certainty of this blessed hope that keeps us anchored in faith.",
                questionsSwahili = listOf("Kwa nini ahadi ya kurudi kwa Yesu inaitwa 'Tumaini lenye Baraka'?", "Ni kwa jinsi gani Sabato inatukumbusha kuhusu uumbaji na ukombozi wa mwisho?"),
                questionsEnglish = listOf("Why is Christ's return called the 'Blessed Hope'?", "How does the Sabbath remind us both of Creation and of final Redemption?")
            ),
            DailyLesson(
                id = "day_1",
                dayIndex = 1,
                dayNameSwahili = "Jumapili",
                dayNameEnglish = "Sunday",
                titleSwahili = "Ahadi ya Kwanza Katika Bustani",
                titleEnglish = "The First Promise in Eden",
                dateString = "Oktoba 25",
                scriptureReferences = listOf("Mwanzo 3:15", "Wagalatia 4:4-5"),
                contentSwahili = "Mara tu mwanadamu alipoanguka dhambini, Mungu hakumwacha katika kukata tamaa. Katika Mwanzo 3:15, Mungu alitangaza unabii wa kwanza wa ukombozi: uzao wa mwanamke ungemponda kichwa nyoka. Uzao huo ni Yesu Kristo aliyeushinda uovu msalabani Kalwari.",
                contentEnglish = "Immediately after the fall, God did not abandon humanity in despair. In Genesis 3:15, God proclaimed the very first prophecy of redemption: the Seed of the woman would crush the serpent's head. That Seed is Jesus Christ, who conquered evil forever on Calvary's cross.",
                questionsSwahili = listOf("Ushindi wa Kristo msalabani unakupa ujasiri gani leo?"),
                questionsEnglish = listOf("How does Christ's victory on the cross give you personal courage today?")
            ),
            DailyLesson(
                id = "day_2",
                dayIndex = 2,
                dayNameSwahili = "Jumatatu",
                dayNameEnglish = "Monday",
                titleSwahili = "Ishara Katika Asili na Jamii",
                titleEnglish = "Signs in Nature and Society",
                dateString = "Oktoba 26",
                scriptureReferences = listOf("Mathayo 24:3-14", "Luka 21:25-28"),
                contentSwahili = "Yesu alipoulizwa na wanafunzi wake kuhusu dalili za kuja kwake, alielekeza kwenye ishara dhahiri: vita, matetemeko, tauni, na uasi wa upendo wa wengi. Lakini akasisitiza pia kwamba 'Injili hii ya ufalme itahubiriwa katika ulimwengu wote kuwa ushuhuda kwa mataifa yote; hapo ndipo ule mwisho utakapokuja.'",
                contentEnglish = "When asked about the sign of His coming, Jesus pointed out wars, earthquakes, famines, and the cooling of love. Yet He highlighted the key turning point: 'This good news of the Kingdom will be preached in the whole world for a testimony to all the nations, and then the end will come.'",
                questionsSwahili = listOf("Je, unashiriki vipi katika kueneza injili ya ufalme katika mtaa au mtandao wako?"),
                questionsEnglish = listOf("How are you participating in sharing the gospel in your community and sphere of influence?")
            ),
            DailyLesson(
                id = "day_3",
                dayIndex = 3,
                dayNameSwahili = "Jumanne",
                dayNameEnglish = "Tuesday",
                titleSwahili = "Ujumbe wa Malaika Watatu",
                titleEnglish = "The Three Angels' Messages",
                dateString = "Oktoba 27",
                scriptureReferences = listOf("Ufunuo 14:6-12"),
                contentSwahili = "Ufunuo 14 unatoa wito wa mwisho wa rehema kwa ulimwengu mzima: kumcha Mungu, kumtukuza, na kumsujudia Muumbaji wa mbingu na nchi na chemchemi za maji. Hapa tunaona msingi thabiti wa Sabato ya siku ya saba kama ishara ya kicho chetu kwa Muumba wetu.",
                contentEnglish = "Revelation 14 delivers God's final message of mercy to the world: fear God, give Him glory, and worship the Creator who made the heaven, the earth, and the springs of waters. Here we see the foundational role of the seventh-day Sabbath as our allegiance to the Creator.",
                questionsSwahili = listOf("Kwa nini ibada ya kweli inahusishwa moja kwa moja na kuitambua kazi ya uumbaji?"),
                questionsEnglish = listOf("Why is true worship directly linked to acknowledging God as Creator?")
            ),
            DailyLesson(
                id = "day_4",
                dayIndex = 4,
                dayNameSwahili = "Jumatano",
                dayNameEnglish = "Wednesday",
                titleSwahili = "Utayari wa Kiroho na Maombi",
                titleEnglish = "Spiritual Readiness and Prayer",
                dateString = "Oktoba 28",
                scriptureReferences = listOf("Mathayo 25:1-13", "1 Wathesalonike 5:1-8"),
                contentSwahili = "Mfano wa wanawali kumi unatufundisha kuwa haitoshi tu kusubiri na kuwa na taa za nje; tunahitaji mafuta ya ziada katika vyombo vyetu—uwepo wa Roho Mtakatifu katika maisha ya kila siku kupitia maombi na kujifunza Neno.",
                contentEnglish = "The parable of the ten virgins reminds us that having external lamps is not enough; we need the reserve oil in our vessels—the living presence of the Holy Spirit through daily prayer and immersion in the Scriptures.",
                questionsSwahili = listOf("Tunawezaje kudumisha uhusiano wa karibu na Roho Mtakatifu kila siku?"),
                questionsEnglish = listOf("How can we sustain an intimate daily relationship with the Holy Spirit?")
            ),
            DailyLesson(
                id = "day_5",
                dayIndex = 5,
                dayNameSwahili = "Alhamisi",
                dayNameEnglish = "Thursday",
                titleSwahili = "Tumaini Linaloshinda Hofu",
                titleEnglish = "Hope That Overcomes Fear",
                dateString = "Oktoba 29",
                scriptureReferences = listOf("2 Timotheo 1:7", "1 Yohana 4:18"),
                contentSwahili = "Wakristo hawapaswi kuishi kwa hofu ya matukio ya siku za mwisho. Upendo mkamilifu wa Mungu hutupa amani na utulivu. Yesu aliahidi: 'Tazama, mimi nipo pamoja nanyi siku zote, hata ukamilifu wa dahari.'",
                contentEnglish = "Christians need not live in dread of end-time events. God's perfect love casts out fear. Jesus promised: 'Behold, I am with you always, even to the end of the age.'",
                questionsSwahili = listOf("Ni ahadi gani ya Biblia inayokupa faraja kuu unapopitia changamoto?"),
                questionsEnglish = listOf("Which biblical promise brings you the deepest comfort when facing uncertainty?")
            ),
            DailyLesson(
                id = "day_6",
                dayIndex = 6,
                dayNameSwahili = "Ijumaa",
                dayNameEnglish = "Friday",
                titleSwahili = "Muhtasari na Tafakari ya Sabato",
                titleEnglish = "Review and Sabbath Reflection",
                dateString = "Oktoba 30",
                scriptureReferences = listOf("Isaya 58:13-14", "Ufunuo 22:20"),
                contentSwahili = "Jua linapotua kuikaribisha Sabato, tunakumbuka kuwa kila Sabato ni mfano mdogo wa mapumziko ya milele katika ufalme wa Mungu. Yeye anayeshuhudia haya asema, 'Hakika naja upesi.' Amina; na uje, Bwana Yesu!",
                contentEnglish = "As the sun sets to welcome the holy Sabbath, we remember that each Sabbath is a beautiful foretaste of the eternal rest in God's kingdom. He who testifies to these things says, 'Surely I am coming quickly.' Amen; come, Lord Jesus!",
                questionsSwahili = listOf("Je, unatumiaje saa takatifu za Sabato kuimarisha ushirika na Mungu na familia yako?"),
                questionsEnglish = listOf("How do you dedicate the holy Sabbath hours to deepening your fellowship with God and family?")
            )
        )
    )

    val egwBooks: List<EgwBook> = listOf(
        EgwBook(
            id = "steps_to_christ",
            titleSwahili = "Hatua kwa Kristo",
            titleEnglish = "Steps to Christ",
            descriptionSwahili = "Kitabu cha kipekee kinachofungua njia ya upendo wa Mungu, toba ya kweli, imani thabiti, na furaha ya kutembea na Yesu kila siku.",
            descriptionEnglish = "A timeless masterpiece illuminating God's boundless love, true repentance, living faith, and the joy of walking with Jesus daily.",
            chapters = listOf(
                EgwChapter(
                    id = "stc_ch1",
                    bookId = "steps_to_christ",
                    chapterNumber = 1,
                    titleSwahili = "Upendo wa Mungu kwa Mwanadamu",
                    titleEnglish = "God's Love for Man",
                    contentSwahili = "Asili na ufunuo kwa pamoja hushuhudia juu ya upendo wa Mungu. Baba yetu wa mbinguni ndiye chemchemi ya uzima, hekima, na furaha. Tazama maajabu na uzuri wa viumbe. Fikiria jinsi vinavyofaa ajabu, si kwa ajili ya mahitaji na furaha ya mwanadamu tu, bali ya viumbe vyote vilivyo hai.\n\nJua na mvua ambazo hufurahisha na kuhuisha nchi, vilima na bahari na mabonde, yote haya yanasema nasi juu ya upendo wa Muumba. Ni Mungu anayekidhi mahitaji ya kila siku ya viumbe vyake vyote.\n\nMoyo wa Mungu unatamani watoto wake wa duniani kwa upendo wenye nguvu zaidi kuliko mauti. Katika kutoa Mwanawe pekee, amemimina mbinguni yote katika zawadi moja kubwa kwa ajili ya ukombozi wetu.",
                    contentEnglish = "Nature and revelation alike testify of God's love. Our Father in heaven is the source of life, of wisdom, and of joy. Look at the wonderful and beautiful things of nature. Think of their marvelous adaptation to the needs and happiness, not only of man, but of all the living creatures.\n\nThe sunshine and the rain, that gladden and refresh the earth, the hills and seas and plains, all speak to us of the Creator's love. It is God who supplies the daily needs of all His creatures.\n\nThe heart of God yearns over His earthly children with a love stronger than death. In giving up His Son, He has poured out to us all heaven in one gift."
                ),
                EgwChapter(
                    id = "stc_ch2",
                    bookId = "steps_to_christ",
                    chapterNumber = 2,
                    titleSwahili = "Haja ya Mwenye Dhambi kwa Kristo",
                    titleEnglish = "The Sinner's Need of Christ",
                    contentSwahili = "Mwanadamu aliumbwa akiwa na tabia takatifu na akili timamu. Alikuwa katika umoja kamili na Mungu; mawazo yake yalikuwa safi, na malengo yake matakatifu. Lakini kwa njia ya uasi, uwezo wake uliharibika, na ubinafsi ukachukua nafasi ya upendo.\n\nHatuwezi kujiokoa wenyewe kutokana na dimbwi la dhambi. Elimu, utamaduni, nidhamu ya kibinafsi vyote vina nafasi yake, lakini havina nguvu ya kubadilisha moyo wa mwanadamu. Lazima nguvu ifanye kazi kutoka ndani, uzima mpya kutoka juu, kabla ya mwanadamu kubadilishwa kutoka dhambini kwenda utakatifu. Nguvu hiyo ni Kristo pekee.",
                    contentEnglish = "Man was originally endowed with noble powers and a well-balanced mind. He was perfect in his being, and in harmony with God. His thoughts were pure, his aims holy. But through disobedience, his powers were perverted, and selfishness took the place of love.\n\nIt is impossible for us, of ourselves, to escape from the pit of sin in which we are sunken. Education, culture, the exercise of the will, human effort, all have their proper sphere, but here they are powerless. They may produce an outward propriety of behavior, but they cannot change the heart; they cannot purify the springs of life. There must be a power working from within, a new life from above, before man can be changed from sin to holiness. That power is Christ."
                )
            )
        ),
        EgwBook(
            id = "great_controversy",
            titleSwahili = "Vita Kuu ya Karne",
            titleEnglish = "The Great Controversy",
            descriptionSwahili = "Historia ya pambano kuu kati ya Kristo na Shetani tangu mwanzo hadi ushindi kamili wa haki na amani ya milele.",
            descriptionEnglish = "The epic unveiling of the cosmic conflict between Christ and Satan from ancient times to the final glorious victory of righteousness.",
            chapters = listOf(
                EgwChapter(
                    id = "gc_ch1",
                    bookId = "great_controversy",
                    chapterNumber = 1,
                    titleSwahili = "Ushindi wa Mwisho wa Haki",
                    titleEnglish = "The Final Triumph of Righteousness",
                    contentSwahili = "Pambano kuu limekwisha. Dhambi na wenye dhambi hawapo tena. Ulimwengu wote mzima ni safi. Mapigo ya furaha na utulivu yanapita katika viumbe vyote visivyo na mipaka. Kutoka Kwake Yeye aliyeumba vyote, humiminika uzima na nuru na furaha katika anga lote.\n\nTangu chembe ndogo kabisa hadi ulimwengu mkubwa zaidi, vyote vikiwa na uzuri usiochafuliwa na furaha kamili, vinatangaza kwamba MUNGU NI PENDO.",
                    contentEnglish = "The great controversy is ended. Sin and sinners are no more. The entire universe is clean. One pulse of harmony and gladness beats through the vast creation. From Him who created all, flow life and light and gladness, throughout the realms of illimitable space.\n\nFrom the minutest atom to the greatest world, all things, animate and inanimate, in their unshadowed beauty and perfect joy, declare that GOD IS LOVE."
                )
            )
        ),
        EgwBook(
            id = "desire_of_ages",
            titleSwahili = "Tumaini la Vizazi Vyote",
            titleEnglish = "The Desire of Ages",
            descriptionSwahili = "Maisha na huduma ya Bwana wetu Yesu Kristo, yakifunua huruma, neema, na wokovu kwa wanadamu wote.",
            descriptionEnglish = "The tender life and transformative ministry of Jesus Christ, portraying the matchless love of God.",
            chapters = listOf(
                EgwChapter(
                    id = "doa_ch1",
                    bookId = "desire_of_ages",
                    chapterNumber = 1,
                    titleSwahili = "Mungu Pamoja Nasi",
                    titleEnglish = "God With Us",
                    contentSwahili = "'Jina lake ataitwa Imanueli, Mungu pamoja nasi.' Nuru ya 'maarifa ya utukufu wa Mungu' inaonekana 'katika uso wa Yesu Kristo.' Tangu siku za milele Bwana Yesu Kristo alikuwa kitu kimoja na Baba; alikuwa 'mfano wa nafsi yake', udhihirisho wa ukuu na utukufu wake.",
                    contentEnglish = "'His name shall be called Immanuel, God with us.' The light of the knowledge of the glory of God is seen in the face of Jesus Christ. From the days of eternity the Lord Jesus Christ was one with the Father; He was the express image of His person, the manifestation of His greatness and majesty."
                )
            )
        )
    )

    val sampleSermons: List<Sermon> = listOf(
        Sermon(
            id = "sermon_1",
            titleSwahili = "Baraka za Sabato ya Kweli",
            titleEnglish = "The Blessings of the True Sabbath",
            speaker = "Mch. Geoffrey Mbwana",
            durationMinutes = 42,
            type = SermonType.AUDIO,
            date = "Oktoba 2026",
            topicSwahili = "Sabato na Ibada",
            topicEnglish = "Sabbath & Worship",
            mediaUrl = "https://example.com/audio/sabbath_blessing.mp3",
            descriptionSwahili = "Mafundisho ya kina kuhusu jinsi Sabato ilivyokusudiwa kuwa oasis ya amani na ushirika wa kiroho katika ulimwengu wenye hekaheka nyingi.",
            descriptionEnglish = "An uplifting message revealing how God designed the Sabbath as a tranquil oasis of spiritual rejuvenation in a restless world."
        ),
        Sermon(
            id = "sermon_2",
            titleSwahili = "Matukio ya Siku za Mwisho na Amani ya Kristo",
            titleEnglish = "End-Time Events and the Peace of Christ",
            speaker = "Mch. Mark Finley",
            durationMinutes = 48,
            type = SermonType.VIDEO,
            date = "Septemba 2026",
            topicSwahili = "Unabii na Tumaini",
            topicEnglish = "Prophecy & Hope",
            mediaUrl = "https://www.youtube.com/watch?v=sample_hope_finley",
            descriptionSwahili = "Ufafanuzi wa unabii wa Biblia na jinsi muumini anavyoweza kuwa na utulivu na matumaini wakati ulimwengu unapopitia mawimbi makubwa.",
            descriptionEnglish = "A profound biblical study on how believers can experience unshakeable peace through prophetic certainty."
        ),
        Sermon(
            id = "sermon_3",
            titleSwahili = "Familia Yenye Mvuto wa Mbinguni",
            titleEnglish = "A Family with Heavenly Influence",
            speaker = "Mch. Baraka Mwambungu",
            durationMinutes = 36,
            type = SermonType.AUDIO,
            date = "Oktoba 2026",
            topicSwahili = "Familia na Ndoa",
            topicEnglish = "Family & Marriage",
            mediaUrl = "https://example.com/audio/heavenly_family.mp3",
            descriptionSwahili = "Kanuni za Kikristo za kulea watoto, kujenga upendo thabiti katika ndoa, na kufanya nyumba kuwa kielelezo cha mbinguni duniani.",
            descriptionEnglish = "Biblical wisdom for nurturing godly children, strengthening marital bonds, and building a heavenly sanctuary in the home."
        ),
        Sermon(
            id = "sermon_4",
            titleSwahili = "Afya Bora: Hekalu la Roho Mtakatifu",
            titleEnglish = "Optimal Health: Temple of the Holy Spirit",
            speaker = "Dkt. Christopher Mrema",
            durationMinutes = 39,
            type = SermonType.VIDEO,
            date = "Agosti 2026",
            topicSwahili = "Afya na Mtindo wa Maisha",
            topicEnglish = "Health & Lifestyle",
            mediaUrl = "https://www.youtube.com/watch?v=sample_health_temple",
            descriptionSwahili = "Mambo 8 ya msingi ya afya ya Kikristo (NEWSTART): Lishe, Mazoezi, Maji, Jua, Kiasi, Hewa safi, Pumziko, na Kumtumaini Mungu.",
            descriptionEnglish = "The 8 natural Adventist health principles (NEWSTART) to protect your mind, vitality, and spiritual receptivity."
        )
    )

    val samplePrayers: List<PrayerItem> = listOf(
        PrayerItem(
            id = "prayer_morning_1",
            titleSwahili = "Ombi la Asubuhi: Shukrani na Kujikabidhi",
            titleEnglish = "Morning Prayer: Thanksgiving & Consecration",
            category = PrayerCategory.MORNING,
            scripture = "Fadhili za Bwana ni mpya kila siku asubuhi; uaminifu wako ni mkuu.",
            scriptureRef = "Maombolezo 3:23",
            prayerTextSwahili = "Baba yetu wa Mbinguni, asante kwa kuniamsha salama asubuhi ya leo. Nakushukuru kwa pumzi ya uhai na upendo wako usio na mipaka. Naweka mikononi mwako mawazo yangu, maneno yangu, na matendo yangu ya siku nzima. Ongoza hatua zangu, unilinde na mitego ya adui, na unisaidie kuwa baraka kwa kila mtu nitakayekutana naye leo. Katika jina la Yesu Kristo, Amina.",
            prayerTextEnglish = "Our Heavenly Father, thank You for awakening me safely this morning. Thank You for the breath of life and Your boundless mercies renewed daily. I place my thoughts, words, and actions into Your gracious hands. Guide my footsteps, protect me from the snares of the adversary, and make me a channel of blessing to everyone I meet today. In Jesus' holy name, Amen."
        ),
        PrayerItem(
            id = "prayer_midday_1",
            titleSwahili = "Ombi la Mchana: Nguvu na Amani Kazini",
            titleEnglish = "Midday Prayer: Strength & Peace in Daily Work",
            category = PrayerCategory.MIDDAY,
            scripture = "Umtwike Bwana mzigo wako naye atakutegemeza, hatamwacha mwenye haki aondoshwe milele.",
            scriptureRef = "Zaburi 55:22",
            prayerTextSwahili = "Bwana Yesu, katikati ya shughuli za mchana huu, natua mzigo wangu mbele zako. Nipatie hekima ya kufanya maamuzi sahihi, utulivu wa moyo, na subira ya Kikristo. Roho wako Mtakatifu akae nami sasa na milele. Amina.",
            prayerTextEnglish = "Lord Jesus, amid the busy rush of midday responsibilities, I cast my burdens before Your throne. Grant me wisdom to make righteous decisions, peace of heart, and gentle Christian patience. May Your Holy Spirit abide with me always. Amen."
        ),
        PrayerItem(
            id = "prayer_evening_1",
            titleSwahili = "Ombi la Jioni: Toba na Usingizi wa Amani",
            titleEnglish = "Evening Prayer: Repentance & Peaceful Rest",
            category = PrayerCategory.EVENING,
            scripture = "Katika amani nitajilaza na kupata usingizi mara, kwa kuwa Wewe, Bwana, peke yako, ndiwe unijaliaye kukaa salama.",
            scriptureRef = "Zaburi 4:8",
            prayerTextSwahili = "Mungu mkuu, jua linapotua na giza likiingia, naja mbele zako nikitubu makosa na mapungufu yangu ya siku ya leo. Nioshe kwa damu ya thamani ya Mwokozi wangu. Funika nyumba yangu na familia yangu kwa mabawa ya malaika zako watakatifu. Tupe usingizi wenye utulivu na amani tele. Amina.",
            prayerTextEnglish = "Gracious God, as the sun sets and twilight falls, I come to Your presence confessing my shortcomings and sins of this day. Cleanse my heart through the precious blood of my Savior. Cover my home and loved ones under the guardian wings of holy angels. Grant us restful sleep and heavenly peace. Amen."
        ),
        PrayerItem(
            id = "prayer_personal_1",
            titleSwahili = "Kuombea Familia na Watoto",
            titleEnglish = "Prayer for Family and Children",
            category = PrayerCategory.PERSONAL,
            scripture = "Mimi na nyumba yangu tutamtumikia Bwana.",
            scriptureRef = "Yoshua 24:15",
            prayerTextSwahili = "Ee Bwana Mungu wa Ibrahimu, Isaka na Yakobo, nakukabidhi familia yangu. Walinde watoto wetu dhidi ya mitego ya ulimwengu, uwajaze upendo wako, na uwape mioyo inayotii Neno lako. Nyumba yetu na iwe chemchemi ya nuru na amani. Amina.",
            prayerTextEnglish = "O Lord God of Abraham, Isaac, and Jacob, I dedicate my family into Your hands. Shield our children from worldly temptations, fill them with Your holy love, and grant them hearts obedient to Your Word. May our home be an altar of light and peace. Amen."
        )
    )

    val hopeChannelItems: List<HopeChannelItem> = listOf(
        HopeChannelItem(
            id = "hc_live",
            titleSwahili = "Hope Channel Tanzania Mubashara",
            titleEnglish = "Hope Channel Tanzania Live",
            programHost = "Studio za HCT Dar es Salaam",
            scheduleTime = "Masaa 24 / 24 Hours Live",
            descriptionSwahili = "Matangazo ya moja kwa moja ya tumaini, injili, afya, nyimbo za ibada, na vipindi vya familia kutoka Hope Channel Tanzania.",
            descriptionEnglish = "24/7 Live broadcast of hope, gospel truth, holistic health, inspirational music, and family enrichments from Hope Channel Tanzania.",
            streamUrl = "https://www.hopetv.co.tz/live",
            isLiveNow = true
        ),
        HopeChannelItem(
            id = "hc_prog_1",
            titleSwahili = "Njia ya Uzima",
            titleEnglish = "Way of Life",
            programHost = "Mchungaji & Wachambuzi wa Biblia",
            scheduleTime = "Jumamosi Saa 3:00 Asubuhi",
            descriptionSwahili = "Uchambuzi wa kina wa masomo ya Biblia, unabii, na mafundisho yanayojenga imani thabiti katika Kristo.",
            descriptionEnglish = "In-depth exposition of Scripture, Bible prophecy, and timeless teachings fostering steadfast faith in Christ.",
            streamUrl = "https://www.hopetv.co.tz/njia-ya-uzima"
        ),
        HopeChannelItem(
            id = "hc_prog_2",
            titleSwahili = "Afya Yako Leo",
            titleEnglish = "Your Health Today",
            programHost = "Madaktari na Wataalamu wa Lishe",
            scheduleTime = "Jumatano Saa 2:30 Usiku",
            descriptionSwahili = "Miongozo ya kisayansi na kibiblia ya kuzuia magonjwa sugu, lishe bora, na mazoezi kwa maisha yenye afya tele.",
            descriptionEnglish = "Biblical and scientific guidance for disease prevention, wholesome nutrition, and vibrant physical well-being.",
            streamUrl = "https://www.hopetv.co.tz/afya"
        ),
        HopeChannelItem(
            id = "hc_prog_3",
            titleSwahili = "Muda wa Maombi na Sifa",
            titleEnglish = "Hour of Prayer and Praise",
            programHost = "Timu ya Maombi ya HCT",
            scheduleTime = "Ijumaa Saa 1:00 Jioni",
            descriptionSwahili = "Kipindi maalum cha kuombea maombi ya watazamaji, kusikiliza shuhuda za miujiza ya Mungu, na kuimba nyimbo za sifa.",
            descriptionEnglish = "Dedicated intercessory prayer time for viewer prayer requests, miraculous testimonies, and uplifting praise songs.",
            streamUrl = "https://www.hopetv.co.tz/maombi"
        )
    )
}
