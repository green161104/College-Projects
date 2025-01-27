package ipp.estg.mobile.data.repositories

import ipp.estg.mobile.data.retrofit.LeaflingsApi
import ipp.estg.mobile.data.retrofit.models.ad.AdResponse
import ipp.estg.mobile.utils.Resource

/**
 * Repository for handling operations related to advertisements.
 *
 * @property leaflingsApi An instance of the API service for performing ad-related network operations.
 */
class AdRepository(
    private val leaflingsApi: LeaflingsApi
) : BaseRepository(
) {

    /**
     * Fetches a random advertisement from the Leaflings API.
     *
     * @param callback A lambda function to handle the response, providing a [Resource] with either the
     *        successful [AdResponse] or an error message.
     */
    fun getRandomAd(
        callback: (Resource<AdResponse>) -> Unit
    ) {
        val call = leaflingsApi.getRandomAd()
        handleResponse(call, callback)
    }
}