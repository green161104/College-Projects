package ipp.estg.mobile.ui.components.images

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ipp.estg.mobile.R
import ipp.estg.mobile.ui.components.utils.OnlineImage

@Composable
fun PlantImage(
    path: String,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    OnlineImage(
        path = path,
        errorImage = R.drawable.default_plant_image,
        contentDescription = contentDescription,
        modifier = modifier
    )
}