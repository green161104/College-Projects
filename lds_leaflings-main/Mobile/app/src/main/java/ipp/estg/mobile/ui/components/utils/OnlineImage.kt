package ipp.estg.mobile.ui.components.utils


import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import ipp.estg.mobile.BuildConfig

@Composable
fun OnlineImage(
    path: String,
    errorImage: Int,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    if(path.isEmpty())
    {
        Image(
            painter = painterResource(id = errorImage),
            contentDescription = contentDescription,
            modifier = modifier
        )
    }
    else {
        val LEAFLINGS_API_BASE_URL = BuildConfig.LEAFLINGS_API_BASE_URL
        AsyncImage(
            model = "$LEAFLINGS_API_BASE_URL/$path",
            contentDescription = contentDescription,
            modifier = modifier,
        )
    }
}