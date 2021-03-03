package petros.efthymiou.groovy.playlist

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.mockito.Mockito.mock
import petros.efthymiou.groovy.utils.BaseUntTest

class PlaylistServiceShould : BaseUntTest() {


    private val playlistApi : PlaylistAPI = mock()
    private val service = PlaylistService(playlistApi)


    @Test
    fun fetchPlaylistsFromApi() = runBlockingTest{
        service.fetchPlaylists()
        verify(playlistApi,times(1)).fetchAllPlaylists()
    }

    @Test
    fun convertValuesToFlowResultAndEmitThem() = runBlockingTest {

    }
}