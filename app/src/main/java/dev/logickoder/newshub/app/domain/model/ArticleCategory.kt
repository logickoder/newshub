package dev.logickoder.newshub.app.domain.model

enum class ArticleCategory {
    Business,
    Entertainment,
    General,
    Health,
    Science,
    Sports,
    Technology;

    override fun toString() = name.lowercase()
}