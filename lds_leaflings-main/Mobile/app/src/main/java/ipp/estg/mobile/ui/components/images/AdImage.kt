package ipp.estg.mobile.ui.components.images

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ipp.estg.mobile.R
import ipp.estg.mobile.ui.components.utils.OnlineImage

@Composable
fun AdImage(
    path: String,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    OnlineImage(
        path = path,
        errorImage = R.drawable.default_ad_image,
        contentDescription = contentDescription,
        modifier = modifier
    )
}