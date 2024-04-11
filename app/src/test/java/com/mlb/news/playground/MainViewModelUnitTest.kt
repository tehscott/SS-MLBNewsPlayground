package com.mlb.news.playground

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.mlb.news.playground.di.NewsPlaygroundContainerInterface
import com.mlb.news.playground.newsfeed.Article
import com.mlb.news.playground.newsfeed.KtorNewsLocalDataSource
import com.mlb.news.playground.newsfeed.KtorNewsRemoteDataSource
import com.mlb.news.playground.newsfeed.NewsFeedRepository
import com.mlb.news.playground.views.MainViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.RETURNS_DEEP_STUBS
import org.mockito.Mockito.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner


@OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class SearchViewModelTests {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainViewModel

    private val mainDispatcher = StandardTestDispatcher()
    private val ioDispatcher = StandardTestDispatcher()
    private lateinit var mockNewsLiveData: MutableLiveData<Result<List<Article>>>
    private lateinit var mockRepository: NewsFeedRepository
    private val mockCapabilities = mock(NetworkCapabilities::class.java)
    private val mockManager = mock(ConnectivityManager::class.java)
    private val mockRemoteDataSource = mock(KtorNewsRemoteDataSource::class.java, RETURNS_DEEP_STUBS)
    private val mockLocalDataSource = mock(KtorNewsLocalDataSource::class.java, RETURNS_DEEP_STUBS)
    private val mockNewsPlaygroundContainer = mock(NewsPlaygroundContainerInterface::class.java)

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(mainDispatcher)

        `when`(mockManager.getNetworkCapabilities(any(Network::class.java))).thenReturn(
            mockCapabilities
        )

        val mockContext = mock(Context::class.java, RETURNS_DEEP_STUBS)
        `when`(mockContext.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(mockManager)

        val mockLifecycleOwner = mock(LifecycleOwner::class.java, RETURNS_DEEP_STUBS)

        mockNewsLiveData = MutableLiveData<Result<List<Article>>>()

        mockRepository = mock(NewsFeedRepository::class.java)
        `when`(mockRepository.newsLiveData).thenReturn(mockNewsLiveData)

        `when`(mockNewsPlaygroundContainer.provideNewsRemoteDataSource(mockContext)).thenReturn(mockRemoteDataSource)
        `when`(mockNewsPlaygroundContainer.provideNewsLocalDataSource(mockContext)).thenReturn(mockLocalDataSource)

        viewModel = MainViewModel(
            context = mockContext,
            lifecycleOwner = mockLifecycleOwner,
            newsFeedRepository = mockRepository,
        )
    }

    @After
    fun close() {
        Dispatchers.shutdown()
    }

    @Test
    fun test_RemoteNewsFetchSuccess() {
        `when`(mockCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))
            .thenReturn(true)

        `when`(mockRemoteDataSource.fetchNews()).then {
            mockRemoteDataSource.newsListLiveData.postValue(Result.success(listOf()))
        }

        viewModel.newsFeedRepository

        mainDispatcher.scheduler.advanceUntilIdle()
        ioDispatcher.scheduler.advanceUntilIdle()

        assert(mockNewsLiveData.value?.isSuccess == true)
    }
}