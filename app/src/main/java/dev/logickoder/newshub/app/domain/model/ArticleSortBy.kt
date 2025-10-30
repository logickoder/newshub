package dev.logickoder.newshub.app.domain.model

enum class ArticleSortBy {
    Relevancy,
    Popularity,
    PublishedAt;

    override fun toString(): String = when (this) {
        Relevancy -> "relevancy"
        Popularity -> "popularity"
        PublishedAt -> "publishedAt"
    }
}