package dev.logickoder.newshub.app.domain.model

import com.google.common.truth.Truth.assertThat
import dev.logickoder.newshub.app.domain.AppJson
import kotlinx.collections.immutable.persistentListOf
import org.junit.Test
import java.time.LocalDate

class ArticleTypeTest {

    @Test
    fun `Headline type has correct default category`() {
        // When
        val headline = ArticleType.Headline()

        // Then
        assertThat(headline.category).isEqualTo(ArticleCategory.General)
    }

    @Test
    fun `Headline type can be created with custom category`() {
        // When
        val headline = ArticleType.Headline(category = ArticleCategory.Technology)

        // Then
        assertThat(headline.category).isEqualTo(ArticleCategory.Technology)
    }

    @Test
    fun `Everything type has correct default values`() {
        // When
        val everything = ArticleType.Everything()

        // Then
        assertThat(everything.from).isNull()
        assertThat(everything.to).isNull()
        assertThat(everything.sortBy).isNull()
        assertThat(everything.domains).containsExactly(
            "bbc.co.uk",
            "techcrunch.com",
            "engadget.com"
        )
    }

    @Test
    fun `Everything type can be created with custom values`() {
        // Given
        val fromDate = LocalDate.of(2023, 10, 1)
        val toDate = LocalDate.of(2023, 10, 31)
        val sortBy = ArticleSortBy.Popularity
        val domains = persistentListOf("cnn.com", "reuters.com")

        // When
        val everything = ArticleType.Everything(
            from = fromDate,
            to = toDate,
            sortBy = sortBy,
            domains = domains
        )

        // Then
        assertThat(everything.from).isEqualTo(fromDate)
        assertThat(everything.to).isEqualTo(toDate)
        assertThat(everything.sortBy).isEqualTo(sortBy)
        assertThat(everything.domains).containsExactly("cnn.com", "reuters.com")
    }

    @Test
    fun `Everything type with null dates`() {
        // When
        val everything = ArticleType.Everything(
            from = null,
            to = null,
            sortBy = ArticleSortBy.Relevancy,
            domains = persistentListOf()
        )

        // Then
        assertThat(everything.from).isNull()
        assertThat(everything.to).isNull()
        assertThat(everything.sortBy).isEqualTo(ArticleSortBy.Relevancy)
        assertThat(everything.domains).isEmpty()
    }

    @Test
    fun `Headline type serializes and deserializes correctly`() {
        // Given
        val original = ArticleType.Headline(category = ArticleCategory.Sports)

        // When
        val json = AppJson.encodeToString(original)
        val deserialized = AppJson.decodeFromString<ArticleType.Headline>(json)

        // Then
        assertThat(deserialized).isEqualTo(original)
        assertThat(deserialized.category).isEqualTo(ArticleCategory.Sports)
    }

    @Test
    fun `Everything type serializes and deserializes correctly`() {
        // Given
        val original = ArticleType.Everything(
            from = LocalDate.of(2023, 11, 1),
            to = LocalDate.of(2023, 11, 30),
            sortBy = ArticleSortBy.PublishedAt,
            domains = persistentListOf("techcrunch.com", "wired.com")
        )

        // When
        val json = AppJson.encodeToString(original)
        val deserialized = AppJson.decodeFromString<ArticleType.Everything>(json)

        // Then
        assertThat(deserialized).isEqualTo(original)
        assertThat(deserialized.from).isEqualTo(LocalDate.of(2023, 11, 1))
        assertThat(deserialized.to).isEqualTo(LocalDate.of(2023, 11, 30))
        assertThat(deserialized.sortBy).isEqualTo(ArticleSortBy.PublishedAt)
        assertThat(deserialized.domains).containsExactly("techcrunch.com", "wired.com")
    }

    @Test
    fun `Everything type with null values serializes correctly`() {
        // Given
        val original = ArticleType.Everything(
            from = null,
            to = null,
            sortBy = null,
            domains = persistentListOf()
        )

        // When
        val json = AppJson.encodeToString(original)
        val deserialized = AppJson.decodeFromString<ArticleType.Everything>(json)

        // Then
        assertThat(deserialized).isEqualTo(original)
        assertThat(deserialized.from).isNull()
        assertThat(deserialized.to).isNull()
        assertThat(deserialized.sortBy).isNull()
        assertThat(deserialized.domains).isEmpty()
    }

    @Test
    fun `ArticleType sealed interface polymorphic serialization works`() {
        // Given
        val headlineType: ArticleType = ArticleType.Headline(ArticleCategory.Business)
        val everythingType: ArticleType = ArticleType.Everything(
            sortBy = ArticleSortBy.Popularity,
            domains = persistentListOf("bbc.co.uk")
        )

        // When
        val headlineJson = AppJson.encodeToString(headlineType)
        val everythingJson = AppJson.encodeToString(everythingType)

        val deserializedHeadline = AppJson.decodeFromString<ArticleType>(headlineJson)
        val deserializedEverything = AppJson.decodeFromString<ArticleType>(everythingJson)

        // Then
        assertThat(deserializedHeadline).isInstanceOf(ArticleType.Headline::class.java)
        assertThat(deserializedEverything).isInstanceOf(ArticleType.Everything::class.java)

        val headline = deserializedHeadline as ArticleType.Headline
        val everything = deserializedEverything as ArticleType.Everything

        assertThat(headline.category).isEqualTo(ArticleCategory.Business)
        assertThat(everything.sortBy).isEqualTo(ArticleSortBy.Popularity)
        assertThat(everything.domains).containsExactly("bbc.co.uk")
    }

    @Test
    fun `Everything type equality works correctly`() {
        // Given
        val date1 = LocalDate.of(2023, 10, 1)
        val date2 = LocalDate.of(2023, 10, 1)
        val domains = persistentListOf("test.com")

        val everything1 = ArticleType.Everything(
            from = date1,
            to = date2,
            sortBy = ArticleSortBy.Relevancy,
            domains = domains
        )

        val everything2 = ArticleType.Everything(
            from = date1,
            to = date2,
            sortBy = ArticleSortBy.Relevancy,
            domains = domains
        )

        // Then
        assertThat(everything1).isEqualTo(everything2)
        assertThat(everything1.hashCode()).isEqualTo(everything2.hashCode())
    }

    @Test
    fun `Headline type equality works correctly`() {
        // Given
        val headline1 = ArticleType.Headline(ArticleCategory.Science)
        val headline2 = ArticleType.Headline(ArticleCategory.Science)

        // Then
        assertThat(headline1).isEqualTo(headline2)
        assertThat(headline1.hashCode()).isEqualTo(headline2.hashCode())
    }
}