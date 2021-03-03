package petros.efthymiou.groovy.playlist

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistService(
        private val playlistAPI: PlaylistAPI
)  {

    suspend fun fetchPlaylists() : Flow<Result<List<Playlist>>> {

        playlistAPI.fetchAllPlaylists()

         return flow{  }
}

}
