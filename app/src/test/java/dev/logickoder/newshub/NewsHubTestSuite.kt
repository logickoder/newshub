package dev.logickoder.newshub

import dev.logickoder.newshub.app.data.mapper.ArticleMapperTest
import dev.logickoder.newshub.app.data.mapper.ErrorMapperTest
import dev.logickoder.newshub.app.data.remote.interceptor.ApiKeyInterceptorTest
import dev.logickoder.newshub.app.data.repository.NewsRepositoryImplTest
import dev.logickoder.newshub.app.domain.model.ArticleTypeTest
import dev.logickoder.newshub.feed.NewsFeedViewModelTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    NewsFeedViewModelTest::class,
    NewsRepositoryImplTest::class,
    ArticleMapperTest::class,
    ErrorMapperTest::class,
    ApiKeyInterceptorTest::class,
    ArticleTypeTest::class
)
class NewsHubTestSuite