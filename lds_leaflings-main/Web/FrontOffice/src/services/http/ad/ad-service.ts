import { Ad } from "../../../models/Ad";
import { getFullImageUrl } from "../plant/plant-service";
import MyAPI from "../tokenInterceptor";


/**
 * Fetches all advertisements.
 * 
 * This function makes a request to get all advertisements from the server and maps each advertisement
 * to include the full URL of the ad file.
 * 
 * @returns {Promise<any[]>} - A promise that resolves to an array of ads, with each ad including the full URL for the ad file.
 */
export async function getAds() {
    const response = await MyAPI.get(`/Ad`);
    const ads = response.data.data; // Assuming the "data" contains the list of ads

    if(!ads) {
        return undefined
    }

    const transformedAds = ads.map((ad: Ad) => ({
        ...ad,
        AdFile: getFullImageUrl(ad.adFile),
    }));


    return transformedAds;
}

export async function getRandomAd() {
    const response = await MyAPI.get(`/Ad/random`);
    const ad = response.data.data;


    if(!ad) {
        return undefined
    }

    const transformedAd = {
        ...ad,
        AdFile: getFullImageUrl(ad.adFile),
    }

    return transformedAd;

}
