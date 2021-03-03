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
import org.mockito.Mockito.mock
import petros.efthymiou.groovy.utils.BaseUntTest
import java.lang.RuntimeException

class PlaylistServiceShould : BaseUntTest() {

    private lateinit var service : PlaylistService
    private val playlistApi : PlaylistAPI = mock()
    private val playlists : List<Playlist> = mock()


    @Test
    fun fetchPlaylistsFromApi() = runBlockingTest{
        service = PlaylistService(playlistApi)
        service.fetchPlaylists().first()
        verify(playlistApi,times(1)).fetchAllPlaylists()
    }

    @Test
    fun convertValuesToFlowResultAndEmitThem() = runBlockingTest {
    mockSuccessfulCase()
        assertEquals(Result.success(playlists), service.fetchPlaylists().first())
    }


    @Test
    fun emitErrorResultWhenNetworkFails() = runBlockingTest {
        mockFailureCase()
        assertEquals("Something went wrong",
            service.fetchPlaylists().first().exceptionOrNull()?.message)
    }

    private suspend fun mockFailureCase() {
        whenever(playlistApi.fetchAllPlaylists())
            .thenThrow(RuntimeException("Damn backend developers"))

        service = PlaylistService(playlistApi)
    }


    private suspend fun mockSuccessfulCase() {
        whenever(playlistApi.fetchAllPlaylists()).thenReturn(playlists)
        service = PlaylistService(playlistApi)
    }
}