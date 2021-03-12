package petros.efthymiou.groovy.playlist

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

import org.junit.Assert.*
import petros.efthymiou.groovy.utils.BaseUntTest
import petros.efthymiou.groovy.utils.captureValues
import petros.efthymiou.groovy.utils.getValueForTest
import java.lang.RuntimeException


class
PlaylistViewModelShould : BaseUntTest() {

    private val repository : PlaylistRepository = mock()
    private val playlists = mock<List<Playlist>>()
    private val expected = Result.success(playlists)
    private val exception = RuntimeException("Something went wrong.")


    @Test
    fun getPlaylistsFromRepository() = runBlockingTest{

        val viewModel = mockSuccessfulResult()
        viewModel.playlists.getValueForTest()
        verify(repository, times(1)).getPlaylists()
    }

    @Test
    fun emitPlaylistsFromRepository() = runBlockingTest{
        val viewModel = mockSuccessfulResult()
        assertEquals(expected, viewModel.playlists.getValueForTest())
    }

    @Test
    fun emitErrorWhenReceiveError(){
        val viewModel = mockErrorCase()

        assertEquals(exception, viewModel.playlists.getValueForTest()!!.exceptionOrNull())
    }

    @Test
    fun displayLoaderWhenLoading() = runBlockingTest{
       val viewModel = mockSuccessfulResult()

       viewModel.loader.captureValues{
           viewModel.playlists.getValueForTest()
           assertEquals(true, values[0])
       }
    }

    @Test
    fun hideLoaderWhenLoadingIsFinished() = runBlockingTest{
        val viewModel = mockSuccessfulResult()

        viewModel.loader.captureValues{
            viewModel.playlists.getValueForTest()
            assertEquals(false, values.last())
        }
    }

    @Test
    fun hideLoaderWhenError() = runBlockingTest{
        val viewModel = mockErrorCase()

        viewModel.loader.captureValues{
            viewModel.playlists.getValueForTest()
            assertEquals(false, values.last())
        }
    }
    private fun mockSuccessfulResult(): PlaylistViewModel {
        runBlocking {
            whenever(repository.getPlaylists()).thenReturn(
                    flow {
                        emit(expected)
                    }
            )
        }
        return PlaylistViewModel(repository)
    }

    private fun mockErrorCase(): PlaylistViewModel {
        runBlocking {
            whenever(repository.getPlaylists()).thenReturn(
                    flow {
                        emit(Result.failure<List<Playlist>>(exception))
                    }
            )
        }

        return PlaylistViewModel(repository)
    }
}