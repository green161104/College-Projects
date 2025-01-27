package ipp.estg.mobile.ui.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ipp.estg.mobile.ui.components.images.ProfileImage

@Composable
fun ProfileInformation(
    user: ipp.estg.mobile.data.retrofit.models.user.UserResponse
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        // Profile Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileImage(
                path = user.userAvatar,
                contentDescription = user.username,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(28.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = user.username,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(2f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Profile Details
        ProfileDetailRow(label = "Contact", value = user.contact)
        ProfileDetailRow(label = "Location", value = user.location)
        ProfileDetailRow(label = "Care Experience", value = user.careExperience)
        ProfileDetailRow(label = "Water Availability", value = user.waterAvailability)
        ProfileDetailRow(label = "Luminosity Availability", value = user.luminosityAvailability)
    }
}

@Composable
private fun ProfileDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
    }
}