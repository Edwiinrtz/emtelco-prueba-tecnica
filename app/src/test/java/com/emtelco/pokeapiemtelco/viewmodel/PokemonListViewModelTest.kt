package com.emtelco.pokeapiemtelco.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.emtelco.pokeapiemtelco.core.ConnectionHelper
import com.emtelco.pokeapiemtelco.core.NotificationsHelper
import com.emtelco.pokeapiemtelco.data.dba.PokemonCartEntity
import com.emtelco.pokeapiemtelco.data.dba.PokemonDao
import com.emtelco.pokeapiemtelco.data.dba.PokemonItemEntity
import com.emtelco.pokeapiemtelco.data.model.Pokemon
import com.emtelco.pokeapiemtelco.data.model.PokemonApiResponse
import com.emtelco.pokeapiemtelco.data.network.PokemonApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var mockApi: PokemonApiClient

    @Mock
    private lateinit var mockPokemonDao: PokemonDao
    @Mock
    private lateinit var mockConnectionHelper: ConnectionHelper
    @Mock
    private lateinit var mockNotificationsHelper: NotificationsHelper

    private lateinit var viewModel: PokemonListViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init loads pokemon list from API when internet is available`() = runTest(testDispatcher) {
        // Arrange
        val fakePokemonList = listOf(Pokemon("bulbasaur", "url1"), Pokemon("ivysaur", "url2"))
        val fakeApiResponse = PokemonApiResponse(results = fakePokemonList)
        whenever(mockConnectionHelper.isNetworkAvailable()).thenReturn(true)
        whenever(mockApi.getPokemonList()).thenReturn(fakeApiResponse)

        viewModel = PokemonListViewModel(mockApi, mockPokemonDao, mockConnectionHelper, mockNotificationsHelper)

        // Act - init block will be called here
        testDispatcher.scheduler.advanceUntilIdle() // Ensure all coroutines launched in init complete

        // Assert
        val pokemonList = viewModel.pokemonList.first() // Get the first emitted value
        assertEquals(fakePokemonList, pokemonList)
        assertTrue(viewModel.internetStatus.first())
        verify(mockApi).getPokemonList()
        verify(mockPokemonDao).insertList(fakePokemonList.map { PokemonItemEntity(name = it.name, url = it.url) })
    }

    @Test
    fun `init loads pokemon list from DAO when internet is unavailable and DAO has data`() = runTest(testDispatcher) {
        // Arrange
        val fakeDaoPokemonEntities = listOf(PokemonItemEntity("charmander", "url3"), PokemonItemEntity("charmeleon", "url4"))
        val expectedPokemonList = fakeDaoPokemonEntities.map { Pokemon(name = it.name, url = it.url) }
        whenever(mockConnectionHelper.isNetworkAvailable()).thenReturn(false)
        whenever(mockPokemonDao.getAllList()).thenAnswer { fakeDaoPokemonEntities }

        viewModel = PokemonListViewModel(mockApi, mockPokemonDao, mockConnectionHelper, mockNotificationsHelper)
        // Act
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val pokemonList = viewModel.pokemonList
        assertEquals(expectedPokemonList.first(), pokemonList.value.first())
        assertEquals(false, viewModel.internetStatus.value)
        verify(mockPokemonDao).getAllList()
    }

    @Test
    fun `init shows empty list and notification when internet is unavailable and DAO is empty`() = runTest(testDispatcher) {
        // Arrange
        whenever(mockConnectionHelper.isNetworkAvailable()).thenReturn(false)
        whenever(mockPokemonDao.getAllList()).thenReturn(emptyList())

        // Re-initialize the viewModel after setting up mocks
        viewModel = PokemonListViewModel(mockApi, mockPokemonDao, mockConnectionHelper, mockNotificationsHelper)

        // Act
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val pokemonList = viewModel.pokemonList.first()
        assertTrue(pokemonList.isEmpty())
        assertEquals(false, viewModel.internetStatus.first())
        verify(mockPokemonDao).getAllList()
        verify(mockNotificationsHelper).sendNotification("Database", "No tienes internet y la base de datos esta vacia. No tenemos nada que mostrar.")
    }

    @Test
    fun `getPokemonList fetches from API when internet becomes available`() = runTest(testDispatcher) {
        // Arrange: Initial state - no internet, empty DAO
        whenever(mockConnectionHelper.isNetworkAvailable()).thenReturn(false)
        whenever(mockPokemonDao.getAllList()).thenReturn(emptyList())

        // Re-initialize the viewModel after setting up mocks
        viewModel = PokemonListViewModel(mockApi, mockPokemonDao, mockConnectionHelper, mockNotificationsHelper)
        testDispatcher.scheduler.advanceUntilIdle()

        // Act: Simulate internet becoming available and calling getPokemonList
        val fakePokemonListApi = listOf(Pokemon("squirtle", "url5"))
        val fakeApiResponse = PokemonApiResponse(results = fakePokemonListApi)
        whenever(mockConnectionHelper.isNetworkAvailable()).thenReturn(true) // Internet is now ON
        whenever(mockApi.getPokemonList()).thenReturn(fakeApiResponse)

        viewModel.checkInternetStatus() // This will update internetStatus
        viewModel.getPokemonList()      // Manually call to fetch
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val pokemonList = viewModel.pokemonList.first()
        assertEquals(fakePokemonListApi, pokemonList)
        assertTrue(viewModel.internetStatus.value)
        verify(mockApi).getPokemonList()
        verify(mockPokemonDao).insertList(fakePokemonListApi.map { PokemonItemEntity(name = it.name, url = it.url) })
    }

    @Test
    fun `addPokemonToCart calls DAO insert`() = runTest(testDispatcher) {
        // Arrange
        whenever(mockConnectionHelper.isNetworkAvailable()).thenReturn(true) // Assume internet for init
        whenever(mockApi.getPokemonList()).thenReturn(PokemonApiResponse(results=emptyList())) // Mock API for init

        // Re-initialize the viewModel after setting up mocks
        viewModel = PokemonListViewModel(mockApi, mockPokemonDao, mockConnectionHelper, mockNotificationsHelper)
        testDispatcher.scheduler.advanceUntilIdle()

        val pokemonId = "25"
        val name = "Pikachu"

        // Act
        viewModel.addPokemonToCart(pokemonId, name)
        testDispatcher.scheduler.advanceUntilIdle() // Ensure the launch block completes

        // Assert
        val expectedEntity = PokemonCartEntity(name = name, id = pokemonId, price = 10.0, cant = 1)
        verify(mockPokemonDao).insert(expectedEntity)
    }
}