package ipp.estg.mobile.ui.components.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    title: String? = null,
    scrollBehavior: TopAppBarScrollBehavior,
    onBackClick: () -> Unit,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = {
            if (!title.isNullOrEmpty()) {
                Text(text = title)
            }
        },
        navigationIcon = {
            // Only show the back arrow if it's not the root activity
            IconButton(onClick = {
                onBackClick()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Go Back"
                )
            }
        },
        // Optional actions can be passed as a composable
        actions = actions,
        scrollBehavior = scrollBehavior, // menu disappears when scrolled
    )
}