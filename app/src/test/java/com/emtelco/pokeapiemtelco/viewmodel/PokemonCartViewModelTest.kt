package com.emtelco.pokeapiemtelco.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.emtelco.pokeapiemtelco.core.AuthHelper
import com.emtelco.pokeapiemtelco.core.NotificationsHelper
import com.emtelco.pokeapiemtelco.data.dba.PokemonCartEntity
import com.emtelco.pokeapiemtelco.data.dba.PokemonDao
import com.emtelco.pokeapiemtelco.data.model.PokemonCartItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonCartViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var pokemonDao: PokemonDao
    @Mock
    private lateinit var authHelper: AuthHelper
    @Mock
    private lateinit var notificationsHelper: NotificationsHelper

    private lateinit var viewModel: PokemonCartViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)

        viewModel = PokemonCartViewModel(
            pokemonDao=pokemonDao,
            authHelper=authHelper,
            notificationsHelper=notificationsHelper
        )

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getPokemonCartList when data is available`() = runTest(testDispatcher) {
        val cartEntityList = listOf(PokemonCartEntity(name = "Pikachu", id="25", price = 10.0, cant = 1), PokemonCartEntity(name = "Bulbasaur", id="1", price = 10.0, cant = 5))
        whenever(pokemonDao.getAllCart()).thenReturn(cartEntityList)

        viewModel.getPokemonCartList()
        testDispatcher.scheduler.advanceUntilIdle()


        val expectedList = cartEntityList.map { PokemonCartItem( name = it.name, id =  it.id, cant = it.cant, price = it.price) }
        assert(viewModel.pokemonCartList.value == expectedList)
        assert(viewModel.totalCost.value == 60.0)
    }

    @Test
    fun `getPokemonCartList when data is empty` () = runBlocking {

        whenever(pokemonDao.getAllCart()).thenReturn(emptyList())

        viewModel.getPokemonCartList()

        assert(viewModel.pokemonCartList.value.isEmpty())
        assert(viewModel.totalCost.value == 0.0)
    }

    @Test
    fun `update an existing pokemon in the cart` () = runBlocking {

        val cartEntityList = listOf(PokemonCartEntity(name = "Pikachu", id="25", price = 10.0, cant = 1), PokemonCartEntity(name = "Bulbasaur", id="1", price = 10.0, cant = 5))
        whenever(pokemonDao.getAllCart()).thenReturn(cartEntityList)

        viewModel.getPokemonCartList()

        val updatedPokemon = PokemonCartItem(name = "Pikachu", id = "25", cant = 2, price = 10.0 )
        viewModel.updatePokemonCart(updatedPokemon)

        val expectedPokemon = cartEntityList.filter { it.id == "25" }
        assert(viewModel.pokemonCartList.value.filter{ it.id == "25" } != expectedPokemon)
    }

    @Test
    fun `drop a pokemon from the cart`()=runBlocking {
        val cartEntityList = listOf(
            PokemonCartEntity(name = "Pikachu", id="25", price = 10.0, cant =  1)
        )
        whenever(pokemonDao.getAllCart()).thenAnswer{cartEntityList}

        viewModel = PokemonCartViewModel(pokemonDao, authHelper, notificationsHelper)
        viewModel.getPokemonCartList()

        viewModel.dropPokemon("25")

        assert(viewModel.pokemonCartList.value.isEmpty())
    }
}