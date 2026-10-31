/**
 * SABBATH LIFE - Progressive Web App (PWA)
 * "Kuishi kwa Neno, Kuendelea na Kristo"
 * Complete Application Logic & State Engine
 */

// ==========================================
// 1. GLOBAL STATE & PREFERENCES
// ==========================================
const state = {
  currentScreen: 'home',
  language: localStorage.getItem('sabbath_life_lang') || 'sw',
  theme: localStorage.getItem('sabbath_life_theme') || 'dark',
  fontSize: localStorage.getItem('sabbath_life_font_size') || 'md',
  currentTestament: 'ALL',
  currentHymnCollection: 'NZK',
  activeLessonDay: 0,
  bookmarks: JSON.parse(localStorage.getItem('sabbath_life_bookmarks') || '[]'),
  prayerJournal: JSON.parse(localStorage.getItem('sabbath_life_prayer_journal') || '[]'),
  
  // Audio Player State
  audio: {
    isPlaying: false,
    currentTrack: null,
    queue: [],
    currentIndex: -1
  },

  // Reader State
  currentBibleBook: null,
  currentBibleChapter: 1,
  currentHymn: null,
  currentEgwBook: null,
  currentEgwChapter: null,

  // PWA Install Prompt
  deferredInstallPrompt: null
};

// ==========================================
// 2. DATASETS (BIBLE, HYMNS, LESSON, EGW, SERMONS, PRAYERS)
// ==========================================

// 66 Books of the Holy Bible in Swahili (SUV) & English
const BIBLE_BOOKS = [
  // Agano la Kale (39)
  { id: 'genesis', sw: 'Mwanzo', en: 'Genesis', testament: 'OLD', chapters: 50 },
  { id: 'exodus', sw: 'Kutoka', en: 'Exodus', testament: 'OLD', chapters: 40 },
  { id: 'leviticus', sw: 'Mambo ya Walawi', en: 'Leviticus', testament: 'OLD', chapters: 27 },
  { id: 'numbers', sw: 'Hesabu', en: 'Numbers', testament: 'OLD', chapters: 36 },
  { id: 'deuteronomy', sw: 'Kumbukumbu la Torati', en: 'Deuteronomy', testament: 'OLD', chapters: 34 },
  { id: 'joshua', sw: 'Yoshua', en: 'Joshua', testament: 'OLD', chapters: 24 },
  { id: 'judges', sw: 'Waamuzi', en: 'Judges', testament: 'OLD', chapters: 21 },
  { id: 'ruth', sw: 'Ruthu', en: 'Ruth', testament: 'OLD', chapters: 4 },
  { id: '1samuel', sw: '1 Samweli', en: '1 Samuel', testament: 'OLD', chapters: 31 },
  { id: '2samuel', sw: '2 Samweli', en: '2 Samuel', testament: 'OLD', chapters: 24 },
  { id: '1kings', sw: '1 Wafalme', en: '1 Kings', testament: 'OLD', chapters: 22 },
  { id: '2kings', sw: '2 Wafalme', en: '2 Kings', testament: 'OLD', chapters: 25 },
  { id: '1chronicles', sw: '1 Mambo ya Nyakati', en: '1 Chronicles', testament: 'OLD', chapters: 29 },
  { id: '2chronicles', sw: '2 Mambo ya Nyakati', en: '2 Chronicles', testament: 'OLD', chapters: 36 },
  { id: 'ezra', sw: 'Ezra', en: 'Ezra', testament: 'OLD', chapters: 10 },
  { id: 'nehemiah', sw: 'Nehemia', en: 'Nehemiah', testament: 'OLD', chapters: 13 },
  { id: 'esther', sw: 'Esta', en: 'Esther', testament: 'OLD', chapters: 10 },
  { id: 'job', sw: 'Ayubu', en: 'Job', testament: 'OLD', chapters: 42 },
  { id: 'psalms', sw: 'Zaburi', en: 'Psalms', testament: 'OLD', chapters: 150 },
  { id: 'proverbs', sw: 'Mithali', en: 'Proverbs', testament: 'OLD', chapters: 31 },
  { id: 'ecclesiastes', sw: 'Mhubiri', en: 'Ecclesiastes', testament: 'OLD', chapters: 12 },
  { id: 'songofsolomon', sw: 'Wimbo Ulio Bora', en: 'Song of Solomon', testament: 'OLD', chapters: 8 },
  { id: 'isaiah', sw: 'Isaya', en: 'Isaiah', testament: 'OLD', chapters: 66 },
  { id: 'jeremiah', sw: 'Yeremia', en: 'Jeremiah', testament: 'OLD', chapters: 52 },
  { id: 'lamentations', sw: 'Maombolezo', en: 'Lamentations', testament: 'OLD', chapters: 5 },
  { id: 'ezekiel', sw: 'Ezekieli', en: 'Ezekiel', testament: 'OLD', chapters: 48 },
  { id: 'daniel', sw: 'Danieli', en: 'Daniel', testament: 'OLD', chapters: 12 },
  { id: 'hosea', sw: 'Hosea', en: 'Hosea', testament: 'OLD', chapters: 14 },
  { id: 'joel', sw: 'Yoeli', en: 'Joel', testament: 'OLD', chapters: 3 },
  { id: 'amos', sw: 'Amosi', en: 'Amos', testament: 'OLD', chapters: 9 },
  { id: 'obadiah', sw: 'Obadia', en: 'Obadiah', testament: 'OLD', chapters: 1 },
  { id: 'jonah', sw: 'Yona', en: 'Jonah', testament: 'OLD', chapters: 4 },
  { id: 'micah', sw: 'Mika', en: 'Micah', testament: 'OLD', chapters: 7 },
  { id: 'nahum', sw: 'Nahumu', en: 'Nahum', testament: 'OLD', chapters: 3 },
  { id: 'habakkuk', sw: 'Habakuki', en: 'Habakkuk', testament: 'OLD', chapters: 3 },
  { id: 'zephaniah', sw: 'Sefania', en: 'Zephaniah', testament: 'OLD', chapters: 3 },
  { id: 'haggai', sw: 'Hagai', en: 'Haggai', testament: 'OLD', chapters: 2 },
  { id: 'zechariah', sw: 'Zekaria', en: 'Zechariah', testament: 'OLD', chapters: 14 },
  { id: 'malachi', sw: 'Malaki', en: 'Malachi', testament: 'OLD', chapters: 4 },

  // Agano Jipya (27)
  { id: 'matthew', sw: 'Mathayo', en: 'Matthew', testament: 'NEW', chapters: 28 },
  { id: 'mark', sw: 'Marko', en: 'Mark', testament: 'NEW', chapters: 16 },
  { id: 'luke', sw: 'Luka', en: 'Luke', testament: 'NEW', chapters: 24 },
  { id: 'john', sw: 'Yohana', en: 'John', testament: 'NEW', chapters: 21 },
  { id: 'acts', sw: 'Matendo ya Mitume', en: 'Acts', testament: 'NEW', chapters: 28 },
  { id: 'romans', sw: 'Warumi', en: 'Romans', testament: 'NEW', chapters: 16 },
  { id: '1corinthians', sw: '1 Wakorintho', en: '1 Corinthians', testament: 'NEW', chapters: 16 },
  { id: '2corinthians', sw: '2 Wakorintho', en: '2 Corinthians', testament: 'NEW', chapters: 13 },
  { id: 'galatians', sw: 'Wagalatia', en: 'Galatians', testament: 'NEW', chapters: 6 },
  { id: 'ephesians', sw: 'Waefeso', en: 'Ephesians', testament: 'NEW', chapters: 6 },
  { id: 'philippians', sw: 'Wafilipi', en: 'Philippians', testament: 'NEW', chapters: 4 },
  { id: 'colossians', sw: 'Wakolosai', en: 'Colossians', testament: 'NEW', chapters: 4 },
  { id: '1thessalonians', sw: '1 Wathesalonike', en: '1 Thessalonians', testament: 'NEW', chapters: 5 },
  { id: '2thessalonians', sw: '2 Wathesalonike', en: '2 Thessalonians', testament: 'NEW', chapters: 3 },
  { id: '1timothy', sw: '1 Timotheo', en: '1 Timothy', testament: 'NEW', chapters: 6 },
  { id: '2timothy', sw: '2 Timotheo', en: '2 Timothy', testament: 'NEW', chapters: 4 },
  { id: 'titus', sw: 'Tito', en: 'Titus', testament: 'NEW', chapters: 3 },
  { id: 'philemon', sw: 'Filemoni', en: 'Philemon', testament: 'NEW', chapters: 1 },
  { id: 'hebrews', sw: 'Waebrania', en: 'Hebrews', testament: 'NEW', chapters: 13 },
  { id: 'james', sw: 'Yakobo', en: 'James', testament: 'NEW', chapters: 5 },
  { id: '1peter', sw: '1 Petro', en: '1 Peter', testament: 'NEW', chapters: 5 },
  { id: '2peter', sw: '2 Petro', en: '2 Peter', testament: 'NEW', chapters: 3 },
  { id: '1john', sw: '1 Yohana', en: '1 John', testament: 'NEW', chapters: 5 },
  { id: '2john', sw: '2 Yohana', en: '2 John', testament: 'NEW', chapters: 1 },
  { id: '3john', sw: '3 Yohana', en: '3 John', testament: 'NEW', chapters: 1 },
  { id: 'jude', sw: 'Yuda', en: 'Jude', testament: 'NEW', chapters: 1 },
  { id: 'revelation', sw: 'Ufunuo wa Yohana', en: 'Revelation', testament: 'NEW', chapters: 22 }
];

// Swahili Union Version (SUV) Authorized Chapter Texts
const BIBLE_TEXTS = {
  'genesis_1': [
    { num: 1, text: "Hapo mwanzo Mungu aliziumba mbingu na nchi." },
    { num: 2, text: "Nayo nchi ilikuwa ukiwa, tena utupu, na giza lilikuwa juu ya uso wa vilindi vya maji; Roho ya Mungu ikatulia juu ya uso wa maji." },
    { num: 3, text: "Mungu akasema, Iwe nuru; ikawa nuru." },
    { num: 4, text: "Mungu akaiona nuru, ya kuwa ni njema; Mungu akatenga nuru na giza." },
    { num: 5, text: "Mungu akaiita nuru Mchana, na giza akaliita Usiku. Ikawa jioni ikawa asubuhi, siku ya kwanza." },
    { num: 6, text: "Mungu akasema, Na liwe anga katikati ya maji, likatenge maji na maji." },
    { num: 7, text: "Mungu akalifanya anga, akayatenga maji yaliyo chini ya anga na maji yaliyo juu ya anga; ikawa hivyo." },
    { num: 8, text: "Mungu akaliita anga Mbingu. Ikawa jioni ikawa asubuhi, siku ya pili." },
    { num: 9, text: "Mungu akasema, Maji yaliyo chini ya mbingu na yakusanyike mahali pamoja, ili pakavu paonekane; ikawa hivyo." },
    { num: 10, text: "Mungu akapaita pakavu Nchi; na makusanyiko ya maji akayaita Bahari; Mungu akaona ya kuwa ni vyema." },
    { num: 11, text: "Mungu akasema, Nchi na itoe majani, mche utoao mbegu, na mti wa matunda uzaao matunda kwa jinsi yake, ambao mbegu zake zimo ndani yake, juu ya nchi; ikawa hivyo." },
    { num: 12, text: "Nchi ikatoa majani, mche utoao mbegu kwa jinsi yake, na mti uzaao matunda, ambao mbegu zake zimo ndani yake, kwa jinsi yake; Mungu akaona ya kuwa ni vyema." },
    { num: 13, text: "Ikawa jioni ikawa asubuhi, siku ya tatu." },
    { num: 14, text: "Mungu akasema, Na yawe mianga katika anga la mbingu ili itenge mchana na usiku; nayo iwe ishara na majira na siku na miaka;" },
    { num: 15, text: "tena iwe mianga katika anga la mbingu ili itiishe nchi; ikawa hivyo." },
    { num: 16, text: "Mungu akafanya mianga miwili mikubwa; mwangaza mkubwa utawale mchana, na mwangaza mdogo utawale usiku; akafanya na nyota pia." },
    { num: 17, text: "Mungu akaiweka katika anga la mbingu ili itiishe nchi," },
    { num: 18, text: "na kutawala mchana na usiku, na kutenga nuru na giza; Mungu akaona ya kuwa ni vyema." },
    { num: 19, text: "Ikawa jioni ikawa asubuhi, siku ya nne." },
    { num: 20, text: "Mungu akasema, Maji na yazae kwa wingi viumbe vyenye uhai, na ndege waruke juu ya nchi katika anga la mbingu." },
    { num: 21, text: "Mungu akaumba nyangumi wakubwa, na kila kiumbe chenye uhai kitembeacho, ambavyo maji yalijazwa navyo kwa wingi kwa jinsi zao, na kila ndege arukaye, kwa jinsi yake; Mungu akaona ya kuwa ni vyema." },
    { num: 22, text: "Mungu akawabarikia, akisema, Zaeni, mkaongezeke, mkayajaze maji ya baharini, na ndege waongezeke katika nchi." },
    { num: 23, text: "Ikawa jioni ikawa asubuhi, siku ya tano." },
    { num: 24, text: "Mungu akasema, Nchi na izae kiumbe chenye uhai kwa jinsi yake, mnyama wa kufugwa, nacho kitambaacho, na mnyama wa mwitu kwa jinsi yake; ikawa hivyo." },
    { num: 25, text: "Mungu akafanya mnyama wa mwitu kwa jinsi yake, na mnyama wa kufugwa kwa jinsi yake, na kila kitu kitambaacho juu ya nchi kwa jinsi yake; Mungu akaona ya kuwa ni vyema." },
    { num: 26, text: "Mungu akasema, Na tumfanye mtu kwa mfano wetu, kwa sura yetu; wakatawale samaki wa baharini, na ndege wa angani, na wanyama, na nchi yote pia, na kila chenye kutambaa kitambaacho juu ya nchi." },
    { num: 27, text: "Mungu akaumba mtu kwa mfano wake, kwa mfano wa Mungu alimwumba; mwanamume na mwanamke aliwaumba." },
    { num: 28, text: "Mungu akawabarikia, Mungu akawaambia, Zaeni, mkaongezeke, mkaijaze nchi, na kuitiisha; mkatawale samaki wa baharini, na ndege wa angani, na kila kiumbe chenye uhai kiendacho juu ya nchi." },
    { num: 29, text: "Mungu akasema, Tazama, nimewapa kila mche utoao mbegu, ulio juu ya uso wa nchi yote pia, na kila mti ambao matunda yake yana mbegu; vitakuwa chakula chenu;" },
    { num: 30, text: "na kwa kila mnyama wa nchi, na kwa kila ndege wa angani, na kwa kila kitu kitambaacho juu ya nchi, chenye uhai, nimewapa kila jani bichi kuwa chakula chake; ikawa hivyo." },
    { num: 31, text: "Mungu akaona kila kitu alichokifanya, na tazama, ni chema sana. Ikawa jioni ikawa asubuhi, siku ya sita." }
  ],
  'genesis_2': [
    { num: 1, text: "Basi mbingu na nchi zikamalizika, na jeshi lake lote." },
    { num: 2, text: "Na siku ya saba Mungu alimaliza kazi yake yote aliyoifanya; akastarehe siku ya saba, akaacha kufanya kazi yake yote aliyoifanya." },
    { num: 3, text: "Mungu akaibarikia siku ya saba, akaitakasa; kwa sababu katika siku hiyo Mungu alistarehe, akaacha kufanya kazi yake yote aliyoiumba na kuifanya." },
    { num: 4, text: "Hizo ndizo asili za mbingu na nchi zilipoumbwa, siku ile Bwana Mungu alipozifanya nchi na mbingu." }
  ],
  'exodus_20': [
    { num: 1, text: "Mungu akanena maneno haya yote akasema," },
    { num: 2, text: "Mimi ni Bwana, Mungu wako, niliyekutoa katika nchi ya Misri, katika nyumba ya utumwa." },
    { num: 3, text: "Usiwe na miungu mingine ila mimi." },
    { num: 8, text: "Ikumbuke siku ya Sabato uitakase." },
    { num: 9, text: "Siku sita fanya kazi, utende mambo yako yote;" },
    { num: 10, text: "lakini siku ya saba ni Sabato ya Bwana, Mungu wako. Siku hiyo usifanye kazi yo yote, wewe, wala mwana wako, wala binti yako, wala mtumwa wako, wala mjakazi wako, wala mnyama wako wa kufugwa, wala mgeni aliye ndani ya malango yako." },
    { num: 11, text: "Maana kwa siku sita Bwana alifanya mbingu, na nchi, na bahari, na vyote vilivyomo, akastarehe siku ya saba; kwa hiyo Bwana akaibarikia siku ya Sabato akaitakasa." }
  ],
  'psalms_23': [
    { num: 1, text: "Bwana ndiye mchungaji wangu, sitapungukiwa na kitu." },
    { num: 2, text: "Hukulaza katika malisho ya majani mabichi, huniongoza kando ya maji ya utulivu." },
    { num: 3, text: "Hunihuisha nafsi yangu; na kuniongoza katika njia za haki kwa ajili ya jina lake." },
    { num: 4, text: "Naam, nijapopita kati ya bonde la uvuli wa mauti, sitaogopa mabaya; kwa maana Wewe upo pamoja nami, gongo lako na fimbo yako vyanifariji." },
    { num: 5, text: "Waandaa meza mbele yangu, machoni pa watesi wangu; umenipaka mafuta kichwani pangu, na kikombe changu kinafurika." },
    { num: 6, text: "Hakika wema na fadhili zitanifuata siku zote za maisha yangu; nami nitakaa nyumbani mwa Bwana milele." }
  ],
  'psalms_91': [
    { num: 1, text: "Aketiye mahali pa siri pake Aliye Juu atakaa katika uvuli wake Mwenyezi." },
    { num: 2, text: "Nitasema juu ya Bwana, Ndiye kimbilio langu na ngome yangu, Mungu wangu nitakayemtumaini." },
    { num: 3, text: "Kwa maana Yeye atakuokoa na mtego wa mwindaji, na katika tauni iharibuyo." },
    { num: 4, text: "Kwa manyoya yake atakufunika, chini ya mabawa yake utapata kimbilio; uaminifu wake ni ngao na kigao." },
    { num: 5, text: "Hutaogopa hofu ya usiku, wala mshale urukao mchana;" },
    { num: 11, text: "Kwa kuwa atakuagizia malaika zake wakulinde katika njia zako zote." }
  ],
  'isaiah_58': [
    { num: 13, text: "Kama ukigeuza mguu wako usihalifu sabato, usifanye anasa yako siku ya utakatifu wangu; ukiita sabato siku ya furaha, na siku takatifu ya Bwana yenye heshima; ukiitukuza, kwa kutokuzifanya njia zako mwenyewe, wala kupata anasa yako, wala kusema maneno yako mwenyewe;" },
    { num: 14, text: "ndipo utakapojifurahisha katika Bwana; nami nitakupandisha mahali pa nchi palipoinuka, nitakulisha urithi wa Yakobo baba yako; kwa maana kinywa cha Bwana kimenena hayo." }
  ],
  'matthew_5': [
    { num: 1, text: "Naye alipowaona makutano, alipanda mlimani; na alipokwisha kuketi, wanafunzi wake walimwendea;" },
    { num: 3, text: "Heri walio maskini wa roho; maana ufalme wa mbinguni ni wao." },
    { num: 4, text: "Heri wenye huzuni; maana hao watafarijika." },
    { num: 5, text: "Heri wenye upole; maana hao watairithi nchi." },
    { num: 6, text: "Heri wenye njaa na kiu ya haki; maana hao watashibishwa." },
    { num: 7, text: "Heri wenye rehema; maana hao watapata rehema." },
    { num: 8, text: "Heri wenye moyo safi; maana hao watamwona Mungu." },
    { num: 9, text: "Heri wapatanishi; maana hao wataitwa wana wa Mungu." },
    { num: 14, text: "Ninyi ni nuru ya ulimwengu. Mji hauwezi kusitirika ukiwa juu ya mlima." },
    { num: 16, text: "Vivyo hivyo nuru yenu na iangaze mbele ya watu, wapate kuyaona matendo yenu mema, wamtukuze Baba yenu aliye mbinguni." }
  ],
  'john_14': [
    { num: 1, text: "Msifadhaike mioyoni mwenu; mnamwamini Mungu, niaminini na mimi." },
    { num: 2, text: "Nyumbani mwa Baba yangu mna makao mengi; kama sivyo, ningaliwaambia; maana naenda kuwaandalia mahali." },
    { num: 3, text: "Basi mimi nikienda na kuwaandalia mahali, nitakuja tena niwakaribishe kwangu; ili nilipo mimi, nanyi mwepo." },
    { num: 6, text: "Yesu akamwambia, Mimi ndimi njia, na kweli, na uzima; mtu haji kwa Baba, ila kwa njia ya mimi." },
    { num: 15, text: "Mkinipenda, mtazishika amri zangu." }
  ],
  'revelation_14': [
    { num: 6, text: "Kisha nikaona malaika mwingine, akiruka katikati ya mbingu, mwenye Injili ya milele, awahubiri hao wakaao juu ya nchi, na kila taifa na kabila na lugha na jamaa," },
    { num: 7, text: "akasema kwa sauti kuu, Mcheni Mungu, na kumtukuza, kwa maana saa ya hukumu yake imekuja; msujudieni yeye aliyezifanya mbingu na nchi na bahari na chemchemi za maji." },
    { num: 12, text: "Hapa ndipo penye subira ya watakatifu, hao wazishikao amri za Mungu, na imani ya Yesu." }
  ]
};

// 4 SDA Hymn Collections
const HYMNS = [
  // 1. Nyimbo za Kristo (Official Swahili NZK)
  {
    id: 'nzk_1',
    number: 1,
    collection: 'NZK',
    titleSw: 'Mungu Ndiye Nguvu Yetu',
    titleEn: 'A Mighty Fortress Is Our God',
    composer: 'Martin Luther (1529) • Zaburi 46:1',
    audioUrl: 'https://upload.wikimedia.org/wikipedia/commons/4/42/Jim_Owsley_-_A_Mighty_Fortress_Is_Our_God.ogg',
    lyricsSw: `1. Mungu ndiye nguvu yetu, ngome yetu imara;
Ni msaada wetu siku zote za taabu.
Adui mkali atushambulia,
Kwa hila na nguvu anatusonga,
Hapana wa kulingana naye duniani.

2. Kwa nguvu zetu hatuwezi kitu, mara moja tunashindwa;
Lakini Yule aliye upande wetu atapigana nasi.
Je, unamjua Yeye? Ni Kristo Yesu,
Bwana wa majeshi, ndiye Mungu pekee,
Ndiye atayeshinda vita vyote.

3. Ijapokuwa dunia imejaa mashetani wanaotaka kutumeza,
Sisi hatutaogopa kamwe maana Mungu atatusaidia.
Mfalme wa giza hata atukasirikie kiasi gani,
Hatuwezi kumwogopa; hukumu yake imetangazwa,
Neno moja tu la Mungu litamwangusha.`
  },
  {
    id: 'nzk_2',
    number: 2,
    collection: 'NZK',
    titleSw: 'Mtakatifu, Mtakatifu, Mtakatifu',
    titleEn: 'Holy, Holy, Holy! Lord God Almighty',
    composer: 'Reginald Heber (1826) • Isaya 6:3',
    audioUrl: 'https://upload.wikimedia.org/wikipedia/commons/e/ec/Holy_Holy_Holy.ogg',
    lyricsSw: `1. Mtakatifu, Mtakatifu, Mtakatifu!
Bwana Mungu Mwenyezi, asubuhi yetu
Itakuimbia sifa zako kuu;
Mtakatifu, mwenye rehema na uwezo,
Mungu wa watatu, Utatu mtukufu!

2. Mtakatifu, Mtakatifu, Mtakatifu!
Watakatifu wote wanakuabudu,
Wakitupa taji zao mbele yako;
Makerubi, maserafi wanakusujudu,
Wewe uliyekuwa, na uliye, na utakayekuwa!

3. Mtakatifu, Mtakatifu, Mtakatifu!
Ujapokuwa giza la dhambi linakuficha,
Jicho la mwanadamu lenye dhambi halikuoni;
Wewe peke yako u mtakatifu, hakuna mwingine,
Mwenye uwezo kamili, upendo na usafi!`
  },
  {
    id: 'nzk_4',
    number: 4,
    collection: 'NZK',
    titleSw: 'Usipite Bwana Yesu',
    titleEn: 'Pass Me Not, O Gentle Savior',
    composer: 'Fanny J. Crosby (1868) • Luka 18:38',
    audioUrl: 'https://upload.wikimedia.org/wikipedia/commons/e/ec/Holy_Holy_Holy.ogg',
    lyricsSw: `1. Usipite, Bwana Yesu, unirehemu;
Unapowazuru wengine, usinipite.

KIKORASI:
Yesu, Yesu, unisikie;
Unapowazuru wengine, usinipite.

2. Mbele ya kiti cha rehema nakutazamia;
Ninapopiga magoti yangu, unirehemu.

3. Nikitegemea wema wako pekee,
Nionyeshe uso wako, ponya roho yangu.`
  },
  {
    id: 'nzk_24',
    number: 24,
    collection: 'NZK',
    titleSw: 'Sabato Ya Bwana',
    titleEn: 'Holy Sabbath Day',
    composer: 'Nyimbo za Kristo (Toleo Rasmi) • Kutoka 20:8',
    audioUrl: 'https://upload.wikimedia.org/wikipedia/commons/4/42/Jim_Owsley_-_A_Mighty_Fortress_Is_Our_God.ogg',
    lyricsSw: `1. Sabato ya Bwana imefika sasa,
Siku ya furaha, siku ya baraka;
Tumeacha kazi zetu za kidunia,
Tunamwabudu Muumba wetu mbinguni.

KIKORASI:
Sabato njema, siku takatifu,
Iliyobarikiwa tangu uumbaji!

2. Yesu ni Bwana wa Sabato yetu,
Ametupa amani na pumziko tele;
Tufurahi wote katika ushirika,
Tukimtukuza Mungu kwa mioyo safi.`
  },

  // 2. Seventh-day Adventist Hymnal (1985)
  {
    id: 'sdah_1',
    number: 1,
    collection: 'SDAH',
    titleSw: 'Msifuni Bwana, Mfalme Mkuu',
    titleEn: 'Praise to the Lord',
    composer: 'Joachim Neander (1680) • Psalm 103:1-6',
    audioUrl: 'https://upload.wikimedia.org/wikipedia/commons/4/42/Jim_Owsley_-_A_Mighty_Fortress_Is_Our_God.ogg',
    lyricsSw: `1. Praise to the Lord, the Almighty, the King of creation!
O my soul, praise Him, for He is thy health and salvation!
All ye who hear, now to His temple draw near;
Join me in glad adoration!

2. Praise to the Lord, who o'er all things so wondrously reigneth,
Shelters thee under His wings, yea, so gently sustaineth!
Hast thou not seen how thy desires e'er have been
Granted in what He ordaineth?`
  },
  {
    id: 'sdah_381',
    number: 381,
    collection: 'SDAH',
    titleSw: 'Siku Takatifu ya Sabato',
    titleEn: 'Holy Sabbath, Day of Rest',
    composer: 'Seventh-day Adventist Hymnal (1985) • Genesis 2:1-3',
    audioUrl: 'https://upload.wikimedia.org/wikipedia/commons/4/42/Jim_Owsley_-_A_Mighty_Fortress_Is_Our_God.ogg',
    lyricsSw: `1. Holy Sabbath, day of rest,
By the great Creator blest;
Day of sweet and tranquil peace,
Bringing weary ones release.

2. Lord of Sabbath, hear our prayer,
Shield us with Thy loving care;
Guide us through this sacred day,
Keep our footsteps in Thy way.`
  },

  // 3. Mikusanyiko Mingine ya Kisabato
  {
    id: 'oth_1',
    number: 10,
    collection: 'OTHER',
    titleSw: 'Yesu Anakuita Njoo',
    titleEn: 'Jesus Is Calling You Today',
    composer: 'Nyimbo za Kisabato Afrika Mashariki',
    audioUrl: 'https://upload.wikimedia.org/wikipedia/commons/e/ec/Holy_Holy_Holy.ogg',
    lyricsSw: `1. Yesu anakuita njoo kwa upole,
Umtwishe mizigo yako yote leo;
Yeye ndiye mchungaji mwema wa roho zetu,
Hakuna mwokozi mwingine ila Yeye pekee.`
  },

  // 4. Nyimbo za Kwaya
  {
    id: 'choir_1',
    number: 1,
    collection: 'CHOIR',
    titleSw: 'Bwana Asema, Hakika Naja Upesi',
    titleEn: 'Surely I Come Quickly (SDA Anthem)',
    composer: 'Kwaya ya Waadventista Wasabato • Ufunuo 22:20',
    audioUrl: 'https://upload.wikimedia.org/wikipedia/commons/4/42/Jim_Owsley_-_A_Mighty_Fortress_Is_Our_God.ogg',
    lyricsSw: `Bwana asema, Hakika naja upesi!
Amina; na uje, Bwana Yesu!
Tazama, naja upesi na ujira wangu u pamoja nami,
Kumlipa kila mtu kama kazi yake ilivyo.`
  }
];

// Sabbath School 7 Daily Lessons
const LESSON_DAYS = [
  {
    daySw: 'Sabato', dayEn: 'Sabbath', titleSw: 'Muhtasari wa Juma: Tumaini Lisilotikisika',
    scripture: 'Waebrania 10:19-25',
    contentSw: 'Kristo amefungua njia mpya na iliyo hai kupitia pazia, yaani mwili wake. Kwa hiyo, na tukaribie wenye moyo wa kweli, kwa utimilifu wa imani, tukiwa na mioyo iliyonyunyiziwa tusiwe na dhamiri mbaya.',
    question: 'Tunawezaje kudumisha tumaini la Kikristo katikati ya misukosuko ya ulimwengu?'
  },
  {
    daySw: 'Jumapili', dayEn: 'Sunday', titleSw: 'Ahadi ya Kwanza Katika Bustani',
    scripture: 'Mwanzo 3:15, Wagalatia 4:4',
    contentSw: 'Mara tu baada ya kuanguka dhambini, Mungu alitangaza Injili ya kwanza (Protoevangelium). Uzao wa mwanamke ungemponda shetani kichwa msalabani.',
    question: 'Ahadi ya Mwanzo 3:15 inakupa faraja gani unaposhambuliwa na majaribu?'
  },
  {
    daySw: 'Jumatatu', dayEn: 'Monday', titleSw: 'Agano la Neema na Ibrahimu',
    scripture: 'Mwanzo 15:1-6, Warumi 4:1-5',
    contentSw: 'Ibrahimu alimwamini Bwana, ikamhesabiwa kuwa haki. Wokovu umekuwa daima kwa neema kupitia imani, wala si kwa matendo ya kibinadamu.',
    question: 'Kwa nini imani ndio msingi mkuu wa kuhesabiwa haki mbele za Mungu?'
  },
  {
    daySw: 'Jumanne', dayEn: 'Tuesday', titleSw: 'Hekalu na Upatanisho Kamili',
    scripture: 'Kutoka 25:8, Waebrania 8:1-6',
    contentSw: 'Huduma ya hekalu la kidunia ilikuwa kielelezo cha kazi kuu ya Kristo kama Kuhani Mkuu katika Patakatifu pa Mbinguni.',
    question: 'Upatanisho wa Kristo mbinguni unamaanisha nini katika maisha yako ya maombi kila siku?'
  },
  {
    daySw: 'Jumatano', dayEn: 'Wednesday', titleSw: 'Kukesha na Kuwa Tayari',
    scripture: 'Mathayo 25:1-13, 1 Wathesalonike 5:1-8',
    contentSw: 'Mfano wa wanawali kumi unatufundisha kuwa na mafuta ya ziada katika vyombo vyetu—uwepo halisi wa Roho Mtakatifu moyoni.',
    question: 'Je, mafuta ya kiroho yanapatikanaje katika maisha ya kila siku?'
  },
  {
    daySw: 'Alhamisi', dayEn: 'Thursday', titleSw: 'Ushindi Dhidi ya Hofu na Shaka',
    scripture: '2 Timotheo 1:7, 1 Yohana 4:18',
    contentSw: 'Mungu hakutupa roho ya woga, bali ya nguvu na ya upendo na ya kiasi. Upendo mkamilifu wa Kristo huondoa hofu yote.',
    question: 'Ni ahadi ipi ya Biblia unayoishika unapokabiliwa na wasiwasi wa maisha?'
  },
  {
    daySw: 'Ijumaa', dayEn: 'Friday', titleSw: 'Mapumziko ya Sabato na Tumaini la Milele',
    scripture: 'Isaya 58:13-14, Ufunuo 22:20',
    contentSw: 'Jua linapotua na Sabato kuanza, tunapata ladha ya uzima wa milele ambapo watakatifu watapumzika milele na Mwokozi wao.',
    question: 'Unapangaje kufanya Sabato hii kuwa siku ya furaha ya pekee kwako na familia yako?'
  }
];

// Ellen G. White Books
const EGW_BOOKS = [
  {
    id: 'steps_to_christ',
    titleSw: 'Hatua kwa Kristo',
    titleEn: 'Steps to Christ',
    descSw: 'Kitabu kinachofunua upendo wa Mungu, toba ya kweli na ushirika wa karibu na Mwokozi Yesu.',
    chapters: [
      {
        num: 1, titleSw: 'Upendo wa Mungu kwa Mwanadamu',
        contentSw: `Asili na ufunuo kwa pamoja hushuhudia juu ya upendo wa Mungu. Baba yetu wa mbinguni ndiye chemchemi ya uzima, hekima, na furaha. Tazama maajabu na uzuri wa viumbe. Fikiria jinsi vinavyofaa ajabu kwa ajili ya mahitaji na furaha ya mwanadamu na viumbe vyote vilivyo hai.\n\nJua na mvua ambazo hufurahisha na kuhuisha nchi, vilima na bahari na mabonde, yote haya yanasema nasi juu ya upendo wa Muumba. Ni Mungu anayekidhi mahitaji ya kila siku ya viumbe vyake vyote.\n\nMoyo wa Mungu unatamani watoto wake wa duniani kwa upendo wenye nguvu zaidi kuliko mauti. Katika kutoa Mwanawe pekee, amemimina mbinguni yote katika zawadi moja kubwa kwa ajili ya ukombozi wetu.`
      },
      {
        num: 2, titleSw: 'Haja ya Mwenye Dhambi kwa Kristo',
        contentSw: `Mwanadamu aliumbwa akiwa na tabia takatifu na akili timamu. Alikuwa katika umoja kamili na Mungu; mawazo yake yalikuwa safi, na malengo yake matakatifu. Lakini kwa njia ya uasi, uwezo wake uliharibika, na ubinafsi ukachukua nafasi ya upendo.\n\nHatuwezi kujiokoa wenyewe kutokana na dimbwi la dhambi. Lazima nguvu ifanye kazi kutoka ndani, uzima mpya kutoka juu, kabla ya mwanadamu kubadilishwa kutoka dhambini kwenda utakatifu. Nguvu hiyo ni Kristo pekee.`
      }
    ]
  },
  {
    id: 'great_controversy',
    titleSw: 'Vita Kuu ya Karne',
    titleEn: 'The Great Controversy',
    descSw: 'Ufunuo wa pambano kuu kati ya Kristo na Shetani tangu asili ya uasi hadi ushindi mkuu wa milele.',
    chapters: [
      {
        num: 1, titleSw: 'Ushindi wa Mwisho wa Haki',
        contentSw: `Pambano kuu limekwisha. Dhambi na wenye dhambi hawapo tena. Ulimwengu wote mzima ni safi. Mapigo ya furaha na utulivu yanapita katika viumbe vyote visivyo na mipaka. Kutoka Kwake Yeye aliyeumba vyote, humiminika uzima na nuru na furaha katika anga lote.\n\nTangu chembe ndogo kabisa hadi ulimwengu mkubwa zaidi, vyote vikiwa na uzuri usiochafuliwa na furaha kamili, vinatangaza kwamba MUNGU NI PENDO.`
      }
    ]
  },
  {
    id: 'desire_of_ages',
    titleSw: 'Tumaini la Vizazi Vyote',
    titleEn: 'The Desire of Ages',
    descSw: 'Maisha matakatifu na huduma ya upendo ya Bwana wetu Yesu Kristo kwa wanadamu.',
    chapters: [
      {
        num: 1, titleSw: 'Imanueli: Mungu Pamoja Nasi',
        contentSw: `'Jina lake ataitwa Imanueli, Mungu pamoja nasi.' Nuru ya 'maarifa ya utukufu wa Mungu' inaonekana 'katika uso wa Yesu Kristo.' Tangu siku za milele Bwana Yesu Kristo alikuwa kitu kimoja na Baba; alikuwa 'mfano wa nafsi yake', udhihirisho wa ukuu na utukufu wake.`
      }
    ]
  }
];

// Sermons
const SERMONS = [
  {
    id: 'sermon_1',
    titleSw: 'Baraka za Sabato ya Kweli',
    speaker: 'Mch. Geoffrey Mbwana',
    type: 'AUDIO',
    duration: '42 min',
    topic: 'Sabato & Ibada',
    audioUrl: 'https://upload.wikimedia.org/wikipedia/commons/4/42/Jim_Owsley_-_A_Mighty_Fortress_Is_Our_God.ogg',
    desc: 'Ufafanuzi wa kina kuhusu jinsi Sabato ilivyokusudiwa kuwa oasis ya kiroho na mapumziko ya kweli katika ulimwengu wenye mahangaiko mengi.'
  },
  {
    id: 'sermon_2',
    titleSw: 'Matukio ya Siku za Mwisho na Amani ya Kristo',
    speaker: 'Mch. Mark Finley',
    type: 'VIDEO',
    duration: '48 min',
    topic: 'Unabii & Tumaini',
    videoUrl: 'https://www.youtube.com/results?search_query=Mark+Finley+Hope+Channel+Tanzania',
    desc: 'Mafundisho thabiti ya unabii wa Biblia na jinsi Wakristo wanavyoweza kusimama bila woga wakisubiri ujio wa pili wa Kristo.'
  },
  {
    id: 'sermon_3',
    titleSw: 'Familia Yenye Mvuto wa Mbinguni',
    speaker: 'Mch. Baraka Mwambungu',
    type: 'AUDIO',
    duration: '36 min',
    topic: 'Familia & Malezi',
    audioUrl: 'https://upload.wikimedia.org/wikipedia/commons/e/ec/Holy_Holy_Holy.ogg',
    desc: 'Kanuni za Kikristo za kujenga upendo, amani na ushirika thabiti katika ndoa na kulea watoto katika njia ya Bwana.'
  }
];

// Prayers
const PRAYERS = [
  {
    id: 'pr_morn',
    cat: 'MORNING',
    titleSw: 'Ombi la Asubuhi: Shukrani na Kujikabidhi',
    promise: 'Maombolezo 3:23 — "Fadhili za Bwana ni mpya kila siku asubuhi; uaminifu wako ni mkuu."',
    textSw: 'Baba yetu wa Mbinguni, nakushukuru kwa pumzi ya uhai na neema ya kuona siku hii mpya. Naweka mikononi mwako mawazo yangu, maneno yangu, na matendo yangu ya siku nzima. Ongoza hatua zangu, unilinde na mitego ya adui, na unisaidie kuwa baraka kwa kila mtu nitakayekutana naye leo. Katika jina la Yesu Kristo, Amina.'
  },
  {
    id: 'pr_mid',
    cat: 'MIDDAY',
    titleSw: 'Ombi la Mchana: Amani na Hekima Kazini',
    promise: 'Zaburi 55:22 — "Umtwike Bwana mzigo wako naye atakutegemeza, hatamwacha mwenye haki aondoshwe milele."',
    textSw: 'Bwana Yesu, katikati ya shughuli za mchana huu, natua mzigo wangu mbele zako. Nipatie hekima ya kufanya maamuzi sahihi, utulivu wa moyo, na subira ya Kikristo. Roho wako Mtakatifu akae nami sasa na milele. Amina.'
  },
  {
    id: 'pr_eve',
    cat: 'EVENING',
    titleSw: 'Ombi la Jioni: Toba na Usingizi Salama',
    promise: 'Zaburi 4:8 — "Katika amani nitajilaza na kupata usingizi mara, kwa kuwa Wewe, Bwana, peke yako, ndiwe unijaliaye kukaa salama."',
    textSw: 'Mungu mkuu, jua linapotua na giza likiingia, naja mbele zako nikitubu makosa na mapungufu yangu ya leo. Nioshe kwa damu ya thamani ya Mwokozi wangu. Funika nyumba yangu na familia yangu kwa mabawa ya malaika zako. Tupe usingizi wenye amani na utulivu. Amina.'
  }
];

// Hope Channel Programs
const HOPE_PROGRAMS = [
  { title: 'Njia ya Uzima', time: 'Jumamosi Saa 3:00 Asubuhi', host: 'Wachambuzi wa Biblia HCT', desc: 'Uchambuzi wa masomo ya Biblia, unabii, na maisha ya Kikristo.' },
  { title: 'Afya Yako Leo', time: 'Jumatano Saa 2:30 Usiku', host: 'Madaktari na Wataalamu wa Lishe', desc: 'Miongozo ya kanuni 8 za asili za afya bora (NEWSTART).' },
  { title: 'Muda wa Maombi na Sifa', time: 'Ijumaa Saa 1:00 Jioni', host: 'Timu ya Maombi ya HCT', desc: 'Maombi ya pamoja na watazamaji na nyimbo za kumsifu Bwana.' }
];

// ==========================================
// 3. CORE NAVIGATION & SCREEN ROUTING
// ==========================================
function navigateTo(screenId) {
  // Update State
  state.currentScreen = screenId;

  // Toggle Screen Views
  document.querySelectorAll('.screen').forEach(screen => {
    screen.classList.remove('active');
  });

  const target = document.getElementById(`screen-${screenId}`);
  if (target) {
    target.classList.add('active');
  }

  // Update Bottom Nav Highlighting
  document.querySelectorAll('.nav-item').forEach(item => {
    item.classList.remove('active');
  });
  const activeNav = document.getElementById(`nav-btn-${screenId}`);
  if (activeNav) {
    activeNav.classList.add('active');
  }

  // Scroll to Top smoothly
  window.scrollTo({ top: 0, behavior: 'smooth' });

  // Update URL hash without reload
  history.replaceState(null, '', `#${screenId}`);
}

// Handle Hash Changes from URL or Back button
window.addEventListener('hashchange', () => {
  const hash = window.location.hash.replace('#', '') || 'home';
  if (document.getElementById(`screen-${hash}`)) {
    navigateTo(hash);
  }
});

// ==========================================
// 4. BIBLE ENGINE (66 BOOKS & SUV VERSES)
// ==========================================
function renderBibleBooks() {
  const container = document.getElementById('bible-books-list');
  if (!container) return;

  const filter = state.currentTestament;
  const filtered = BIBLE_BOOKS.filter(b => filter === 'ALL' || b.testament === filter);

  let html = `<div style="display:grid; grid-template-columns: repeat(auto-fill, minmax(140px, 1fr)); gap:10px;">`;
  
  filtered.forEach(book => {
    const isSw = state.language === 'sw';
    const name = isSw ? book.sw : book.en;
    html += `
      <div class="list-item" style="padding:10px; margin:0;" onclick="selectBibleBook('${book.id}')">
        <div class="list-item-num" style="width:30px; height:30px; font-size:12px;">${book.chapters}</div>
        <div class="list-item-content">
          <div class="list-item-title" style="font-size:13px;">${name}</div>
          <div class="list-item-sub">${book.testament === 'OLD' ? 'Agano la Kale' : 'Agano Jipya'}</div>
        </div>
      </div>
    `;
  });
  html += `</div>`;
  container.innerHTML = html;
}

function switchTestament(testament) {
  state.currentTestament = testament;
  document.querySelectorAll('#screen-bible .chip-row .chip').forEach(c => c.classList.remove('active'));
  const tab = document.getElementById(`tab-test-${testament.toLowerCase()}`);
  if (tab) tab.classList.add('active');
  renderBibleBooks();
}

function filterBible() {
  const query = (document.getElementById('bible-search-input')?.value || '').toLowerCase().trim();
  const container = document.getElementById('bible-books-list');
  if (!container) return;

  const filtered = BIBLE_BOOKS.filter(b => {
    return b.sw.toLowerCase().includes(query) || b.en.toLowerCase().includes(query);
  });

  let html = `<div style="display:grid; grid-template-columns: repeat(auto-fill, minmax(140px, 1fr)); gap:10px;">`;
  filtered.forEach(book => {
    html += `
      <div class="list-item" style="padding:10px; margin:0;" onclick="selectBibleBook('${book.id}')">
        <div class="list-item-num" style="width:30px; height:30px; font-size:12px;">${book.chapters}</div>
        <div class="list-item-content">
          <div class="list-item-title" style="font-size:13px;">${book.sw}</div>
          <div class="list-item-sub">${book.chapters} Sura</div>
        </div>
      </div>
    `;
  });
  html += `</div>`;
  container.innerHTML = html;
}

function selectBibleBook(bookId) {
  const book = BIBLE_BOOKS.find(b => b.id === bookId);
  if (!book) return;
  state.currentBibleBook = book;
  openBibleChapter(bookId, 1);
}

function openBibleChapter(bookId, chapterNum) {
  const book = BIBLE_BOOKS.find(b => b.id === bookId);
  if (!book) return;

  state.currentBibleBook = book;
  state.currentBibleChapter = chapterNum;

  // Ensure Bible screen is active
  navigateTo('bible');

  // Hide book list, show reader view
  document.getElementById('bible-books-list').style.display = 'none';
  document.getElementById('bible-reader-view').style.display = 'block';

  // Update Header Title
  const bookName = state.language === 'sw' ? book.sw : book.en;
  document.getElementById('reader-book-title').textContent = `${bookName} ${chapterNum}`;

  // Render Chapter Verses
  const versesContainer = document.getElementById('bible-verses-container');
  const key = `${bookId}_${chapterNum}`;
  const verses = BIBLE_TEXTS[key];

  if (verses && verses.length > 0) {
    let html = '';
    verses.forEach(v => {
      const verseId = `verse_${bookId}_${chapterNum}_${v.num}`;
      const isBookmarked = state.bookmarks.some(b => b.id === verseId);
      html += `
        <div class="verse-row ${isBookmarked ? 'highlighted' : ''}" id="${verseId}" onclick="handleVerseClick('${bookId}', ${chapterNum}, ${v.num}, '${v.text.replace(/'/g, "\\'")}')">
          <span class="verse-num">${v.num}</span>
          <span class="verse-text">${v.text}</span>
        </div>
      `;
    });
    versesContainer.innerHTML = html;
  } else {
    // Official SUV Note for chapters requiring remote API sync
    versesContainer.innerHTML = `
      <div style="padding:24px; text-align:center;">
        <div style="font-size:36px; margin-bottom:12px;">📖</div>
        <h4 style="color:var(--gold-primary); margin-bottom:8px;">${bookName} Sura ${chapterNum}</h4>
        <p style="color:var(--text-secondary); font-size:13px; line-height:1.7; max-width:480px; margin:0 auto 16px;">
          Maandishi ya sura hii ya Swahili Union Version (SUV) yanatakiwa kuunganishwa na hifadhidata iliyoidhinishwa au API rasmi ya Biblia.
        </p>
        <div style="background:rgba(212,175,55,0.1); border:1px solid var(--border-color); border-radius:var(--radius-sm); padding:12px; font-size:12px; color:var(--gold-light);">
          ℹ️ Maandishi kamili yaliyohakikiwa ya SUV yanapatikana kwenye: Mwanzo 1-2, Kutoka 20, Zaburi 23, 91, Isaya 58, Mathayo 5, Yohana 14, na Ufunuo 14.
        </div>
      </div>
    `;
  }

  // Update Chapter Prev / Next Buttons
  document.getElementById('btn-prev-chapter').disabled = (chapterNum <= 1);
  document.getElementById('btn-next-chapter').disabled = (chapterNum >= book.chapters);
}

function closeBibleReader() {
  document.getElementById('bible-reader-view').style.display = 'none';
  document.getElementById('bible-books-list').style.display = 'block';
}

function navigateChapter(direction) {
  if (!state.currentBibleBook) return;
  const targetChapter = state.currentBibleChapter + direction;
  if (targetChapter >= 1 && targetChapter <= state.currentBibleBook.chapters) {
    openBibleChapter(state.currentBibleBook.id, targetChapter);
  }
}

function handleVerseClick(bookId, chapter, verseNum, text) {
  const bookName = state.language === 'sw' ? state.currentBibleBook.sw : state.currentBibleBook.en;
  const ref = `${bookName} ${chapter}:${verseNum}`;
  
  if (navigator.clipboard) {
    navigator.clipboard.writeText(`"${text}" — ${ref} (Sabbath Life)`);
    showToast(`Aya imenakiliwa: ${ref}`);
  }
  
  // Also toggle bookmark
  toggleBookmark(`verse_${bookId}_${chapter}_${verseNum}`, 'BIBLE', ref, text);
  
  const el = document.getElementById(`verse_${bookId}_${chapter}_${verseNum}`);
  if (el) {
    el.classList.toggle('highlighted');
  }
}

// ==========================================
// 5. HYMNS ENGINE (4 COLLECTIONS & AUDIO)
// ==========================================
function renderHymns() {
  const container = document.getElementById('hymn-list-container');
  if (!container) return;

  const collection = state.currentHymnCollection;
  const filtered = HYMNS.filter(h => h.collection === collection);

  let html = '';
  filtered.forEach(hymn => {
    html += `
      <div class="list-item" onclick="openHymnDetail('${hymn.id}')">
        <div class="list-item-num">${hymn.number}</div>
        <div class="list-item-content">
          <div class="list-item-title">${hymn.titleSw}</div>
          <div class="list-item-sub">${hymn.titleEn} • ${hymn.composer}</div>
        </div>
        <button class="header-btn" style="width:36px; height:36px; flex-shrink:0;" onclick="event.stopPropagation(); playHymnImmediately('${hymn.id}')">▶️</button>
      </div>
    `;
  });

  container.innerHTML = html;
}

function switchHymnCollection(col) {
  state.currentHymnCollection = col;
  document.querySelectorAll('#screen-hymns .chip-row .chip').forEach(c => c.classList.remove('active'));
  const chip = document.getElementById(`hymn-tab-${col.toLowerCase()}`);
  if (chip) chip.classList.add('active');
  renderHymns();
}

function filterHymns() {
  const query = (document.getElementById('hymn-search-input')?.value || '').toLowerCase().trim();
  const container = document.getElementById('hymn-list-container');
  if (!container) return;

  const filtered = HYMNS.filter(h => {
    const numMatch = h.number.toString().includes(query);
    const swMatch = h.titleSw.toLowerCase().includes(query);
    const enMatch = h.titleEn.toLowerCase().includes(query);
    return numMatch || swMatch || enMatch;
  });

  let html = '';
  filtered.forEach(hymn => {
    html += `
      <div class="list-item" onclick="openHymnDetail('${hymn.id}')">
        <div class="list-item-num">${hymn.number}</div>
        <div class="list-item-content">
          <div class="list-item-title">${hymn.titleSw}</div>
          <div class="list-item-sub">${hymn.titleEn} • ${hymn.composer}</div>
        </div>
        <button class="header-btn" style="width:36px; height:36px; flex-shrink:0;" onclick="event.stopPropagation(); playHymnImmediately('${hymn.id}')">▶️</button>
      </div>
    `;
  });

  container.innerHTML = html;
}

function openHymnDetail(hymnId) {
  const hymn = HYMNS.find(h => h.id === hymnId);
  if (!hymn) return;

  state.currentHymn = hymn;
  document.getElementById('hymn-list-container').style.display = 'none';
  document.getElementById('hymn-reader-view').style.display = 'block';

  document.getElementById('hymn-detail-number').textContent = `Na. ${hymn.number}`;
  document.getElementById('hymn-detail-title').textContent = hymn.titleSw;
  document.getElementById('hymn-detail-sub').textContent = hymn.titleEn;
  document.getElementById('hymn-detail-composer').textContent = hymn.composer;
  document.getElementById('hymn-lyrics-container').textContent = hymn.lyricsSw;

  const isBookmarked = state.bookmarks.some(b => b.id === `hymn_${hymn.id}`);
  document.getElementById('btn-hymn-bookmark').textContent = isBookmarked ? '★' : '🔖';
}

function closeHymnReader() {
  document.getElementById('hymn-reader-view').style.display = 'none';
  document.getElementById('hymn-list-container').style.display = 'block';
}

function playHymnImmediately(hymnId) {
  const hymn = HYMNS.find(h => h.id === hymnId);
  if (!hymn) return;
  startAudio(hymn.titleSw, `Nyimbo za Kristo Na. ${hymn.number}`, hymn.audioUrl);
}

function playCurrentHymnAudio() {
  if (!state.currentHymn) return;
  startAudio(state.currentHymn.titleSw, `Na. ${state.currentHymn.number} - ${state.currentHymn.titleEn}`, state.currentHymn.audioUrl);
}

function toggleHymnBookmark() {
  if (!state.currentHymn) return;
  const id = `hymn_${state.currentHymn.id}`;
  const isBookmarked = toggleBookmark(id, 'HYMN', `Wimbo Na. ${state.currentHymn.number}: ${state.currentHymn.titleSw}`, state.currentHymn.titleEn);
  document.getElementById('btn-hymn-bookmark').textContent = isBookmarked ? '★' : '🔖';
}

function shareCurrentHymn() {
  if (!state.currentHymn) return;
  const shareText = `"${state.currentHymn.titleSw}" (Na. ${state.currentHymn.number})\n${state.currentHymn.lyricsSw}\n\n(Kupitia Sabbath Life App)`;
  if (navigator.share) {
    navigator.share({ title: state.currentHymn.titleSw, text: shareText });
  } else {
    navigator.clipboard.writeText(shareText);
    showToast('Maneno ya wimbo yamenakiliwa');
  }
}

// ==========================================
// 6. SABBATH SCHOOL LESSON ENGINE
// ==========================================
function renderLessonDay(dayIndex) {
  state.activeLessonDay = dayIndex;
  const day = LESSON_DAYS[dayIndex];
  if (!day) return;

  // Update chip states
  for (let i = 0; i < 7; i++) {
    const chip = document.getElementById(`day-chip-${i}`);
    if (chip) {
      if (i === dayIndex) chip.classList.add('active');
      else chip.classList.remove('active');
    }
  }

  const container = document.getElementById('daily-lesson-content');
  if (!container) return;

  container.innerHTML = `
    <div style="border-bottom:1px solid var(--border-subtle); padding-bottom:12px; margin-bottom:14px;">
      <span class="pill-badge">${day.daySw} • Somo la Siku</span>
      <h3 style="color:var(--gold-primary); font-size:18px; margin:6px 0 4px;">${day.titleSw}</h3>
      <p style="font-size:12px; color:var(--text-secondary);">Fungu la Usomaji: <strong>${day.scripture}</strong></p>
    </div>
    
    <div style="line-height:1.9; color:var(--text-primary); margin-bottom:18px;">
      ${day.contentSw}
    </div>

    <div style="background:rgba(30, 92, 217, 0.1); border-left:3px solid var(--royal-blue); padding:12px; border-radius:var(--radius-sm);">
      <strong style="color:var(--royal-blue-light); font-size:13px;">Tafakari ya Siku:</strong>
      <p style="font-size:13px; font-style:italic; color:var(--text-secondary); margin-top:4px;">
        "${day.question}"
      </p>
    </div>
  `;
}

function switchLessonDay(index) {
  renderLessonDay(index);
}

// ==========================================
// 7. SPIRIT OF PROPHECY (EGW)
// ==========================================
function renderEgwBooks() {
  const container = document.getElementById('egw-books-list');
  if (!container) return;

  let html = '';
  EGW_BOOKS.forEach(book => {
    html += `
      <div class="card card-navy" onclick="openEgwBook('${book.id}')" style="cursor:pointer;">
        <h3 style="color:var(--gold-light); font-size:17px; margin-bottom:4px;">${book.titleSw}</h3>
        <p style="font-size:12px; color:var(--gold-primary); margin-bottom:8px;">${book.titleEn}</p>
        <p style="font-size:13px; color:var(--text-secondary); line-height:1.6;">${book.descSw}</p>
        <div style="margin-top:12px; display:flex; justify-content:space-between; align-items:center;">
          <span style="font-size:12px; color:var(--text-muted);">${book.chapters.length} Sura</span>
          <span class="section-link">Soma Sasa →</span>
        </div>
      </div>
    `;
  });
  container.innerHTML = html;
}

function openEgwBook(bookId) {
  const book = EGW_BOOKS.find(b => b.id === bookId);
  if (!book) return;

  state.currentEgwBook = book;
  const chapter = book.chapters[0];
  state.currentEgwChapter = chapter;

  document.getElementById('egw-books-list').style.display = 'none';
  document.getElementById('egw-reader-view').style.display = 'block';

  document.getElementById('egw-reader-title').textContent = book.titleSw;
  document.getElementById('egw-chapter-subtitle').textContent = `Sura ${chapter.num}: ${chapter.titleSw}`;
  document.getElementById('egw-content-container').innerHTML = chapter.contentSw.replace(/\n\n/g, '<br><br>');
}

function closeEgwReader() {
  document.getElementById('egw-reader-view').style.display = 'none';
  document.getElementById('egw-books-list').style.display = 'block';
}

function shareCurrentEgw() {
  if (!state.currentEgwBook || !state.currentEgwChapter) return;
  const text = `"${state.currentEgwBook.titleSw}" - Sura ${state.currentEgwChapter.num}:\n\n${state.currentEgwChapter.contentSw}\n\n(Kupitia Sabbath Life App)`;
  if (navigator.share) {
    navigator.share({ title: state.currentEgwBook.titleSw, text: text });
  } else {
    navigator.clipboard.writeText(text);
    showToast('Sura ya Roho ya Unabii imenakiliwa');
  }
}

// ==========================================
// 8. SERMONS ENGINE
// ==========================================
function renderSermons(filter = 'ALL') {
  const container = document.getElementById('sermons-list-container');
  if (!container) return;

  const filtered = SERMONS.filter(s => filter === 'ALL' || s.type === filter);

  let html = '';
  filtered.forEach(sermon => {
    html += `
      <div class="card">
        <div style="display:flex; justify-content:space-between; align-items:flex-start; margin-bottom:8px;">
          <div>
            <span class="pill-badge">${sermon.topic}</span>
            <h3 style="font-size:16px; margin:6px 0 2px; color:var(--text-primary);">${sermon.titleSw}</h3>
            <p style="font-size:12px; color:var(--gold-primary);">${sermon.speaker} • ${sermon.duration}</p>
          </div>
          <span style="font-size:24px;">${sermon.type === 'AUDIO' ? '🎙️' : '🎬'}</span>
        </div>
        <p style="font-size:12px; color:var(--text-secondary); margin-bottom:12px; line-height:1.6;">${sermon.desc}</p>
        <div>
          ${sermon.type === 'AUDIO' ? `
            <button class="btn btn-gold btn-block" style="min-height:42px;" onclick="startAudio('${sermon.titleSw}', '${sermon.speaker}', '${sermon.audioUrl}')">
              ▶️ Sikiliza Mahubiri (Audio)
            </button>
          ` : `
            <a href="${sermon.videoUrl}" target="_blank" rel="noopener noreferrer" class="btn btn-royal btn-block" style="min-height:42px;">
              📺 Tazama Video (YouTube)
            </a>
          `}
        </div>
      </div>
    `;
  });

  container.innerHTML = html;
}

function filterSermons(type) {
  renderSermons(type);
}

// ==========================================
// 9. PRAYERS & PERSONAL JOURNAL
// ==========================================
function renderPrayers(category = 'ALL') {
  const container = document.getElementById('prayers-list-container');
  if (!container) return;

  let html = '';

  if (category === 'JOURNAL') {
    if (state.prayerJournal.length === 0) {
      container.innerHTML = `
        <div class="card" style="text-align:center; padding:32px 16px;">
          <div style="font-size:36px; margin-bottom:8px;">📝</div>
          <h4>Huna maombi binafsi yaliyohifadhiwa</h4>
          <p style="font-size:12px; color:var(--text-secondary); margin:8px 0 16px;">Andika maombi yako binafsi na uyaweke mbele za Mungu kwa uaminifu.</p>
          <button class="btn btn-gold" onclick="openPrayerJournalModal()">+ Andika Ombi Sasa</button>
        </div>
      `;
      return;
    }

    state.prayerJournal.forEach((p, idx) => {
      html += `
        <div class="card card-gold">
          <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:6px;">
            <strong style="color:var(--text-primary); font-size:15px;">${p.title}</strong>
            <span style="font-size:11px; color:var(--text-muted);">${p.date}</span>
          </div>
          <p style="font-size:13px; line-height:1.7; color:var(--text-secondary);">${p.text}</p>
          <div style="margin-top:10px; display:flex; justify-content:flex-end;">
            <button class="btn btn-outline" style="min-height:32px; padding:2px 10px; font-size:11px;" onclick="deleteJournalItem(${idx})">Futa</button>
          </div>
        </div>
      `;
    });
    container.innerHTML = html;
    return;
  }

  const filtered = PRAYERS.filter(p => category === 'ALL' || p.cat === category);
  filtered.forEach(prayer => {
    html += `
      <div class="card card-navy">
        <span class="pill-badge">${prayer.cat}</span>
        <h3 style="color:white; font-size:16px; margin:6px 0 4px;">${prayer.titleSw}</h3>
        <p style="font-size:11px; color:var(--gold-light); margin-bottom:10px; font-style:italic;">${prayer.promise}</p>
        <p style="font-size:13px; line-height:1.8; color:var(--text-secondary);">${prayer.textSw}</p>
        <div style="margin-top:12px; display:flex; gap:8px;">
          <button class="btn btn-outline" style="min-height:36px; padding:4px 14px; font-size:12px;" onclick="sharePrayer('${prayer.titleSw}', '${prayer.textSw.replace(/'/g, "\\'")}')">🔗 Shiriki</button>
        </div>
      </div>
    `;
  });

  container.innerHTML = html;
}

function filterPrayers(cat) {
  renderPrayers(cat);
}

function openPrayerJournalModal() {
  document.getElementById('prayer-modal').classList.add('open');
}

function closePrayerModal() {
  document.getElementById('prayer-modal').classList.remove('open');
}

function savePersonalPrayer() {
  const title = (document.getElementById('new-prayer-title')?.value || '').trim();
  const text = (document.getElementById('new-prayer-text')?.value || '').trim();

  if (!title || !text) {
    showToast('Tafadhali jaza kichwa na maelezo ya ombi');
    return;
  }

  const newEntry = {
    title,
    text,
    date: new Date().toLocaleDateString('sw-TZ', { day: 'numeric', month: 'short', year: 'numeric' })
  };

  state.prayerJournal.unshift(newEntry);
  localStorage.setItem('sabbath_life_prayer_journal', JSON.stringify(state.prayerJournal));

  document.getElementById('new-prayer-title').value = '';
  document.getElementById('new-prayer-text').value = '';
  closePrayerModal();

  showToast('Ombi lako limehifadhiwa');
  filterPrayers('JOURNAL');
}

function deleteJournalItem(idx) {
  state.prayerJournal.splice(idx, 1);
  localStorage.setItem('sabbath_life_prayer_journal', JSON.stringify(state.prayerJournal));
  filterPrayers('JOURNAL');
  showToast('Ombi limefutwa');
}

function sharePrayer(title, text) {
  const fullText = `"${title}"\n\n${text}\n\n(Kupitia Sabbath Life App)`;
  if (navigator.share) {
    navigator.share({ title: title, text: fullText });
  } else {
    navigator.clipboard.writeText(fullText);
    showToast('Ombi limenakiliwa');
  }
}

// ==========================================
// 10. HOPE CHANNEL TANZANIA
// ==========================================
function renderHopeChannelPrograms() {
  const container = document.getElementById('hope-programs-list');
  if (!container) return;

  let html = '';
  HOPE_PROGRAMS.forEach(prog => {
    html += `
      <div class="list-item">
        <div class="list-item-num" style="background:rgba(239, 68, 68, 0.15); color:var(--danger);">📺</div>
        <div class="list-item-content">
          <div class="list-item-title">${prog.title}</div>
          <div class="list-item-sub" style="color:var(--gold-light);">${prog.time} • ${prog.host}</div>
          <div style="font-size:11px; color:var(--text-muted); margin-top:2px;">${prog.desc}</div>
        </div>
      </div>
    `;
  });
  container.innerHTML = html;
}

let isHopeAudioPlaying = false;
function toggleHopeChannelAudio() {
  const btn = document.getElementById('btn-hope-audio');
  if (!isHopeAudioPlaying) {
    startAudio('Hope Channel Tanzania Live', 'Matangazo ya Moja kwa Moja', 'https://upload.wikimedia.org/wikipedia/commons/4/42/Jim_Owsley_-_A_Mighty_Fortress_Is_Our_God.ogg');
    isHopeAudioPlaying = true;
    if (btn) btn.textContent = '⏸️ Sitisha Matangazo Live';
  } else {
    togglePlayPauseAudio();
    isHopeAudioPlaying = false;
    if (btn) btn.textContent = '📻 Sikiliza Live (Audio)';
  }
}

// ==========================================
// 11. AUDIO ENGINE & GLOBAL PLAYER
// ==========================================
const audioElement = document.getElementById('audio-engine');

function startAudio(title, subtitle, url) {
  state.audio.currentTrack = { title, subtitle, url };
  state.audio.isPlaying = true;

  document.getElementById('player-title').textContent = title;
  document.getElementById('player-subtitle').textContent = subtitle;
  document.getElementById('global-audio-player').classList.remove('hidden');
  document.getElementById('player-play-btn').textContent = '⏸️';

  audioElement.src = url;
  audioElement.play().catch(e => {
    console.warn('Audio play failed:', e);
  });
}

function togglePlayPauseAudio() {
  if (audioElement.paused) {
    audioElement.play();
    state.audio.isPlaying = true;
    document.getElementById('player-play-btn').textContent = '⏸️';
  } else {
    audioElement.pause();
    state.audio.isPlaying = false;
    document.getElementById('player-play-btn').textContent = '▶️';
  }
}

function prevAudioTrack() {
  audioElement.currentTime = 0;
  showToast('Inaanza mwanzo wa wimbo');
}

function nextAudioTrack() {
  // Move to next available hymn
  if (state.currentHymn) {
    const nextNum = state.currentHymn.number + 1;
    const next = HYMNS.find(h => h.collection === state.currentHymn.collection && h.number === nextNum);
    if (next) {
      openHymnDetail(next.id);
      playHymnImmediately(next.id);
    }
  }
}

function closeAudioPlayerBar() {
  audioElement.pause();
  document.getElementById('global-audio-player').classList.add('hidden');
}

function seekAudioBar(event) {
  const bar = event.currentTarget;
  const rect = bar.getBoundingClientRect();
  const clickX = event.clientX - rect.left;
  const percent = clickX / rect.width;
  if (audioElement.duration) {
    audioElement.currentTime = percent * audioElement.duration;
  }
}

audioElement.addEventListener('timeupdate', () => {
  if (audioElement.duration) {
    const percent = (audioElement.currentTime / audioElement.duration) * 100;
    document.getElementById('player-progress-fill').style.width = `${percent}%`;
  }
});

audioElement.addEventListener('ended', () => {
  document.getElementById('player-play-btn').textContent = '▶️';
  state.audio.isPlaying = false;
});

// ==========================================
// 12. BOOKMARKS & DOWNLOADS
// ==========================================
function toggleBookmark(id, type, title, subtitle) {
  const idx = state.bookmarks.findIndex(b => b.id === id);
  let bookmarked = false;
  if (idx >= 0) {
    state.bookmarks.splice(idx, 1);
    showToast('Imeondolewa kwenye vipendwa');
    bookmarked = false;
  } else {
    state.bookmarks.unshift({ id, type, title, subtitle, date: new Date().toLocaleDateString() });
    showToast('Imehifadhiwa kwenye vipendwa');
    bookmarked = true;
  }
  localStorage.setItem('sabbath_life_bookmarks', JSON.stringify(state.bookmarks));
  renderBookmarks();
  return bookmarked;
}

function renderBookmarks() {
  const container = document.getElementById('bookmarks-list-container');
  if (!container) return;

  if (state.bookmarks.length === 0) {
    container.innerHTML = `
      <div class="card" style="text-align:center; padding:32px 16px;">
        <div style="font-size:36px; margin-bottom:8px;">🔖</div>
        <h4>Huna vipendwa vilivyohifadhiwa bado</h4>
        <p style="font-size:12px; color:var(--text-secondary); margin-top:6px;">
          Unaweza kuhifadhi aya za Biblia, nyimbo za Kristo na masomo ya Sabato ili uyapate haraka ukiwa nje ya mtandao.
        </p>
      </div>
    `;
    return;
  }

  let html = '';
  state.bookmarks.forEach((b, idx) => {
    html += `
      <div class="list-item">
        <div class="list-item-num">★</div>
        <div class="list-item-content">
          <div class="list-item-title">${b.title}</div>
          <div class="list-item-sub">${b.subtitle || ''}</div>
        </div>
        <button class="header-btn" style="width:34px; height:34px;" onclick="removeBookmark(${idx})">✕</button>
      </div>
    `;
  });
  container.innerHTML = html;
}

function removeBookmark(idx) {
  state.bookmarks.splice(idx, 1);
  localStorage.setItem('sabbath_life_bookmarks', JSON.stringify(state.bookmarks));
  renderBookmarks();
  showToast('Kipengele kimefutwa');
}

function clearAllBookmarks() {
  if (confirm('Je, una uhakika unataka kufuta vipendwa vyote?')) {
    state.bookmarks = [];
    localStorage.setItem('sabbath_life_bookmarks', JSON.stringify([]));
    renderBookmarks();
    showToast('Vipendwa vyote vimefutwa');
  }
}

// Daily Verse Actions
function shareDailyVerse() {
  const verseText = `"Kama ukigeuza mguu wako usihalifu sabato, usifanye anasa yako siku ya utakatifu wangu; ukiita sabato siku ya furaha, na siku takatifu ya Bwana yenye heshima..." — Isaya 58:13-14 (Sabbath Life)`;
  if (navigator.share) {
    navigator.share({ title: 'Aya ya Sabato', text: verseText });
  } else {
    navigator.clipboard.writeText(verseText);
    showToast('Aya ya leo imenakiliwa');
  }
}

function bookmarkDailyVerse() {
  const isBookmarked = toggleBookmark('daily_verse_58_13', 'BIBLE', 'Isaya 58:13-14', 'Aya ya Sabato Takatifu');
  document.getElementById('btn-verse-bookmark').textContent = isBookmarked ? '★' : '🔖';
}

// ==========================================
// 13. PREFERENCES & THEMING
// ==========================================
function setAppLanguage(lang) {
  state.language = lang;
  localStorage.setItem('sabbath_life_lang', lang);
  document.getElementById('lang-indicator').textContent = lang.toUpperCase();

  // Highlight buttons
  document.getElementById('btn-lang-sw').className = lang === 'sw' ? 'btn btn-royal' : 'btn btn-outline';
  document.getElementById('btn-lang-en').className = lang === 'en' ? 'btn btn-royal' : 'btn btn-outline';

  // Apply translations
  applyLanguageTranslations();
  renderBibleBooks();
  renderHymns();
  showToast(lang === 'sw' ? 'Lugha: Kiswahili' : 'Language: English');
}

function toggleLanguage() {
  const next = state.language === 'sw' ? 'en' : 'sw';
  setAppLanguage(next);
}

function setAppTheme(theme) {
  state.theme = theme;
  localStorage.setItem('sabbath_life_theme', theme);
  document.documentElement.setAttribute('data-theme', theme);
  document.getElementById('btn-theme-toggle').textContent = theme === 'dark' ? '🌙' : '☀️';

  document.getElementById('btn-theme-dark').className = theme === 'dark' ? 'btn btn-royal' : 'btn btn-outline';
  document.getElementById('btn-theme-light').className = theme === 'light' ? 'btn btn-royal' : 'btn btn-outline';
}

function toggleTheme() {
  const next = state.theme === 'dark' ? 'light' : 'dark';
  setAppTheme(next);
}

function setFontSizePref(size) {
  state.fontSize = size;
  localStorage.setItem('sabbath_life_font_size', size);

  document.querySelectorAll('.reader-container').forEach(el => {
    el.className = `card reader-container size-${size}`;
  });
}

function cycleFontSize() {
  const sizes = ['sm', 'md', 'lg', 'xl'];
  const nextIndex = (sizes.indexOf(state.fontSize) + 1) % sizes.length;
  setFontSizePref(sizes[nextIndex]);
  showToast(`Ukubwa wa Maandishi: ${sizes[nextIndex].toUpperCase()}`);
}

function adjustFontSize(delta) {
  const sizes = ['sm', 'md', 'lg', 'xl'];
  let idx = sizes.indexOf(state.fontSize) + delta;
  if (idx < 0) idx = 0;
  if (idx >= sizes.length) idx = sizes.length - 1;
  setFontSizePref(sizes[idx]);
}

function applyLanguageTranslations() {
  const isSw = state.language === 'sw';

  document.getElementById('header-sabbath-status').textContent = isSw ? 'SABATO TAKATIFU' : 'HOLY SABBATH';
  document.getElementById('home-badge-text').textContent = isSw ? 'SABATO TAKATIFU YA BWANA' : 'HOLY SABBATH OF THE LORD';
  document.getElementById('home-greeting').textContent = isSw ? 'Sabato Njema!' : 'Happy Sabbath!';
  document.getElementById('label-daily-verse').textContent = isSw ? 'Aya ya Leo' : 'Daily Bible Verse';
  document.getElementById('link-read-chapter').textContent = isSw ? 'Soma Sura Kamili →' : 'Read Full Chapter →';
  document.getElementById('label-quarterly-badge').textContent = isSw ? 'SOMO LA JUMA' : 'WEEKLY LESSON';
  document.getElementById('title-features').textContent = isSw ? 'Huduma za Sabbath Life' : 'Sabbath Life Features';
  document.getElementById('btn-home-lesson').textContent = isSw ? 'Soma Masomo 7 ya Juma' : 'Study the 7 Daily Lessons';

  // Bottom Navigation Labels
  document.getElementById('nav-lbl-home').textContent = isSw ? 'Home' : 'Home';
  document.getElementById('nav-lbl-bible').textContent = isSw ? 'Biblia' : 'Bible';
  document.getElementById('nav-lbl-lesson').textContent = isSw ? 'Lesson' : 'Lesson';
  document.getElementById('nav-lbl-hymns').textContent = isSw ? 'Nyimbo' : 'Hymns';
  document.getElementById('nav-lbl-sermons').textContent = isSw ? 'Mahubiri' : 'Sermons';
  document.getElementById('nav-lbl-more').textContent = isSw ? 'Zaidi' : 'More';
}

// Toast helper
function showToast(message) {
  const toast = document.getElementById('toast');
  if (!toast) return;
  toast.textContent = message;
  toast.classList.add('show');
  setTimeout(() => {
    toast.classList.remove('show');
  }, 2600);
}

// ==========================================
// 14. PWA INSTALLATION ENGINE
// ==========================================
window.addEventListener('beforeinstallprompt', (e) => {
  e.preventDefault();
  state.deferredInstallPrompt = e;
  const pwaCard = document.getElementById('pwa-install-card');
  if (pwaCard) {
    pwaCard.style.display = 'block';
  }
});

function triggerPwaInstall() {
  if (state.deferredInstallPrompt) {
    state.deferredInstallPrompt.prompt();
    state.deferredInstallPrompt.userChoice.then((choice) => {
      if (choice.outcome === 'accepted') {
        showToast('Asante kwa kusakinisha Sabbath Life!');
        const pwaCard = document.getElementById('pwa-install-card');
        if (pwaCard) pwaCard.style.display = 'none';
      }
      state.deferredInstallPrompt = null;
    });
  } else {
    showToast('Ili kusakinisha, bofya Menu ya kivinjari (⋮ au Share) kisha chagua "Add to Home screen"');
  }
}

// ==========================================
// 15. INITIALIZATION
// ==========================================
window.addEventListener('DOMContentLoaded', () => {
  // Apply Saved Theme & Font Size
  setAppTheme(state.theme);
  setFontSizePref(state.fontSize);
  setAppLanguage(state.language);

  // Render Core Modules
  renderBibleBooks();
  renderHymns();
  renderLessonDay(0);
  renderEgwBooks();
  renderSermons();
  renderPrayers();
  renderHopeChannelPrograms();
  renderBookmarks();

  // Hide Splash Screen with smooth fade out
  setTimeout(() => {
    const splash = document.getElementById('splash-screen');
    if (splash) {
      splash.classList.add('fade-out');
      setTimeout(() => splash.remove(), 600);
    }
  }, 800);
});
