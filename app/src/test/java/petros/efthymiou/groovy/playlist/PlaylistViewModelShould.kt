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
import petros.efthymiou.groovy.utils.getValueForTest
import java.lang.RuntimeException


class PlaylistViewModelShould : BaseUntTest() {

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
        runBlocking {
            whenever(repository.getPlaylists()).thenReturn(
                    flow {
                        emit(Result.failure<List<Playlist>>(exception))
                    }
            )
        }

        val viewModel = PlaylistViewModel(repository)

        assertEquals(exception, viewModel.playlists.getValueForTest()!!.exceptionOrNull())
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
}