package petros.efthymiou.groovy.playlist

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import petros.efthymiou.groovy.utils.BaseUntTest
import java.lang.RuntimeException

class PlaylistRepositoryShould : BaseUntTest() {

    private val exception: Throwable = RuntimeException("something went wrong")
    private val service : PlaylistService = mock()
    private val repository = PlaylistRepository(service)
    private val playlists : List<Playlist> = mock<List<Playlist>>()

    @Test
    fun getPlaylistsFromService() = runBlockingTest {
        repository.getPlaylists()
        verify(service, times(1)).fetchPlaylists()

    }

    @Test
    fun emitPlaylistsFromService()= runBlockingTest{
        mockSuccessfulCase()
        assertEquals(playlists, repository.getPlaylists().first().getOrNull())
    }

    @Test
    fun propagateErrors() = runBlockingTest {
        mockFailureCase()
        assertEquals(exception, repository.getPlaylists().first().exceptionOrNull())
    }

    private suspend fun mockFailureCase() {
        whenever(service.fetchPlaylists()).thenReturn(
                flow {
                    emit(Result.failure<List<Playlist>>(exception))
                }
        )
    }

    private suspend fun mockSuccessfulCase(){
        whenever(service.fetchPlaylists()).thenReturn(
                flow {
                    emit(Result.success(playlists))
                }
        )

    }
}