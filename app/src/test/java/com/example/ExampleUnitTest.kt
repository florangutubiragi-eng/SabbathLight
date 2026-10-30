package com.example

import com.example.data.sample.SampleData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun genesis1_verseNumberingIsContinuous1To31() {
        val genesis = SampleData.sampleBooks.find { it.id == "genesis" }
        assertNotNull("Genesis book must exist", genesis)

        val ch1 = genesis?.sampleChapters?.find { it.chapterNumber == 1 }
        assertNotNull("Genesis chapter 1 must exist", ch1)

        val verses = ch1!!.verses
        assertEquals("Genesis 1 must have 31 continuous verses", 31, verses.size)

        // Verify strictly continuous ascending numbering from 1 to 31
        verses.forEachIndexed { index, verse ->
            val expectedVerseNum = index + 1
            assertEquals("Verse at index $index must have verse number $expectedVerseNum", expectedVerseNum, verse.verseNumber)
            assertEquals("Verse chapter must be 1", 1, verse.chapter)
            assertTrue("Swahili text must not be empty", verse.textSwahili.isNotBlank())
            assertTrue("English text must not be empty", verse.textEnglish.isNotBlank())
        }

        // Verify specific verses
        assertEquals("Hapo mwanzo Mungu aliziumba mbingu na nchi.", verses[0].textSwahili)
        assertTrue("Verse 5 must be evening and morning day 1", verses[4].textSwahili.contains("siku ya kwanza"))
        assertTrue("Verse 6 must not restart, but be about the firmament", verses[5].textSwahili.contains("anga"))
        assertTrue("Verse 31 must be the sixth day completion", verses[30].textSwahili.contains("siku ya sita"))
    }

    @Test
    fun genesis2_isSeparateChapterWithSabbathCreation() {
        val genesis = SampleData.sampleBooks.find { it.id == "genesis" }
        val ch2 = genesis?.sampleChapters?.find { it.chapterNumber == 2 }
        assertNotNull("Genesis chapter 2 must exist", ch2)

        val verses = ch2!!.verses
        assertEquals(4, verses.size)
        assertEquals(1, verses[0].verseNumber)
        assertEquals(2, verses[1].verseNumber)
        assertEquals(3, verses[2].verseNumber)
        assertTrue("Verse 3 must be Sabbath blessing", verses[2].textSwahili.contains("Mungu akaibarikia siku ya saba"))
    }

    @Test
    fun hymnLibrary_containsFourSeparateAdventistCollections() {
        val allHymns = com.example.data.sample.HymnCatalog.hymns
        assertTrue("Hymns collection must be populated", allHymns.size >= 30)

        val nzkHymns = allHymns.filter { it.collection == com.example.data.models.HymnCollection.NYIMBO_ZA_KRISTO }
        val sdahHymns = allHymns.filter { it.collection == com.example.data.models.HymnCollection.SDA_HYMNAL_1985 }
        val otherHymns = allHymns.filter { it.collection == com.example.data.models.HymnCollection.OTHER_ADVENTIST }
        val choirHymns = allHymns.filter { it.collection == com.example.data.models.HymnCollection.CHOIR_SONGS }

        assertTrue("Collection 1: Nyimbo za Kristo must contain hymns", nzkHymns.isNotEmpty())
        assertTrue("Collection 2: SDA Hymnal 1985 must contain hymns", sdahHymns.isNotEmpty())
        assertTrue("Collection 3: Other Adventist collections must contain hymns", otherHymns.isNotEmpty())
        assertTrue("Collection 4: Choir Songs must contain hymns", choirHymns.isNotEmpty())

        // 1. Arrange every collection by its official hymn number in ascending order
        assertAscendingOrder(nzkHymns, "Nyimbo za Kristo")
        assertAscendingOrder(sdahHymns, "SDA Hymnal 1985")
        assertAscendingOrder(otherHymns, "Other Adventist Collections")
        assertAscendingOrder(choirHymns, "Choir Songs")

        // 2. Preserve original hymn numbers and titles; do not mix numbering between hymnbooks
        val nzk1 = nzkHymns.find { it.number == 1 }
        assertNotNull("NZK 1 must exist", nzk1)
        assertEquals("Mungu Ndiye Nguvu Yetu", nzk1?.titleSwahili)
        assertEquals("A Mighty Fortress Is Our God", nzk1?.titleEnglish)

        val sdah506 = sdahHymns.find { it.number == 506 }
        assertNotNull("In SDAH (1985), A Mighty Fortress is #506", sdah506)
        assertEquals("A Mighty Fortress Is Our God", sdah506?.titleEnglish)

        val sdah73 = sdahHymns.find { it.number == 73 }
        assertNotNull("In SDAH (1985), Holy Holy Holy is #73", sdah73)
        assertEquals("Holy, Holy, Holy! Lord God Almighty", sdah73?.titleEnglish)

        val sdah530 = sdahHymns.find { it.number == 530 }
        assertNotNull("In SDAH (1985), It Is Well With My Soul is #530", sdah530)
        assertEquals("It Is Well With My Soul", sdah530?.titleEnglish)

        val choir1 = choirHymns.find { it.number == 1 }
        assertNotNull("Choir #1 must exist", choir1)
        assertEquals("Wakati wa Mavuno Umetimia", choir1?.titleSwahili)

        // 3. No duplicates within each collection
        assertEquals(nzkHymns.size, nzkHymns.map { it.number }.toSet().size)
        assertEquals(sdahHymns.size, sdahHymns.map { it.number }.toSet().size)
        assertEquals(otherHymns.size, otherHymns.map { it.number }.toSet().size)
        assertEquals(choirHymns.size, choirHymns.map { it.number }.toSet().size)

        // 4. Edition name clearly specified and verification notes present
        allHymns.forEach { hymn ->
            assertTrue("Hymn #${hymn.number} must have published edition title", hymn.editionName.isNotBlank())
            assertTrue("Hymn #${hymn.number} must have verification notes", hymn.verificationNotes.isNotBlank())
            assertTrue("Hymn #${hymn.number} must be marked verified", hymn.isVerifiedEntry)
        }
    }

    private fun assertAscendingOrder(hymns: List<com.example.data.models.Hymn>, collectionName: String) {
        for (i in 0 until hymns.size - 1) {
            val current = hymns[i].number
            val next = hymns[i + 1].number
            assertTrue(
                "$collectionName must be in strictly ascending order: #$current must be < #$next",
                current < next
            )
        }
    }

    @Test
    fun hymnAudioUrls_areAllValidSecurePublicDomainStreams() {
        val allHymns = com.example.data.sample.HymnCatalog.hymns
        assertTrue("Hymns catalog should not be empty", allHymns.isNotEmpty())

        allHymns.forEach { hymn ->
            assertTrue(
                "Hymn #${hymn.number} (${hymn.titleSwahili}) must have a non-blank audioUrl",
                hymn.audioUrl.isNotBlank()
            )
            assertTrue(
                "Hymn #${hymn.number} audioUrl must be HTTPS",
                hymn.audioUrl.startsWith("https://")
            )
            assertTrue(
                "Hymn #${hymn.number} audioUrl must originate from verified Wikimedia Commons stream",
                hymn.audioUrl.contains("upload.wikimedia.org")
            )
            assertTrue(
                "Hymn #${hymn.number} audioUrl must be a valid audio container (.ogg)",
                hymn.audioUrl.endsWith(".ogg")
            )
        }
    }
}
