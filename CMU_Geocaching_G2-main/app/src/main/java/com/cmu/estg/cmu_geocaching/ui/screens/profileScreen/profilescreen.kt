    package com.cmu.estg.cmu_geocaching.ui.screens.profileScreen

    import android.app.Application
    import android.content.Context
    import android.content.res.Configuration
    import android.graphics.BitmapFactory
    import android.net.Uri
    import android.widget.Toast
    import androidx.activity.compose.rememberLauncherForActivityResult
    import androidx.activity.result.contract.ActivityResultContracts
    import androidx.compose.foundation.Image
    import androidx.compose.foundation.background
    import androidx.compose.foundation.border
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.PaddingValues
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.offset
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.layout.size
    import androidx.compose.foundation.rememberScrollState
    import androidx.compose.foundation.shape.CircleShape
    import androidx.compose.foundation.verticalScroll
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.CheckCircle
    import androidx.compose.material.icons.filled.Create
    import androidx.compose.material3.AlertDialog
    import androidx.compose.material3.Button
    import androidx.compose.material3.FloatingActionButton
    import androidx.compose.material3.Icon
    import androidx.compose.material3.IconButton
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.Scaffold
    import androidx.compose.material3.Snackbar
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.LaunchedEffect
    import androidx.compose.runtime.collectAsState
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.runtime.setValue
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.draw.clip
    import androidx.compose.ui.graphics.Brush
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.graphics.asImageBitmap
    import androidx.compose.ui.layout.ContentScale
    import androidx.compose.ui.platform.LocalConfiguration
    import androidx.compose.ui.platform.LocalContext
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.res.stringResource
    import androidx.compose.ui.tooling.preview.Preview
    import androidx.compose.ui.unit.dp
    import androidx.lifecycle.viewmodel.compose.viewModel
    import androidx.navigation.NavController
    import androidx.navigation.compose.rememberNavController
    import com.cmu.estg.cmu_geocaching.R
    import com.cmu.estg.cmu_geocaching.data.local.GeocachingDatabase
    import com.cmu.estg.cmu_geocaching.data.local.entities.User
    import com.cmu.estg.cmu_geocaching.data.local.repository.UserRepository
    import com.cmu.estg.cmu_geocaching.ui.screens.utilcomposables.InputField
    import com.cmu.estg.cmu_geocaching.ui.screens.utilcomposables.ProfileField
    import com.cmu.estg.cmu_geocaching.ui.screens.utilcomposables.dropdownfielduseful
    import com.cmu.estg.cmu_geocaching.ui.screens.utilcomposables.sharedNavBar
    import com.cmu.estg.cmu_geocaching.viewModel.HomePageViewModel
    import com.cmu.estg.cmu_geocaching.viewModel.ProfilePageViewModel
    import com.cmu.estg.cmu_geocaching.viewModel.ProfilePageViewModelFactory
    import com.google.firebase.auth.FirebaseAuth
    import com.google.firebase.firestore.FirebaseFirestore
    import java.io.File


    @Preview
    @Composable
    fun userProfilePreview() {
        val navController = rememberNavController();
        val homePageViewModel = viewModel<HomePageViewModel>()

        UserProfileScreen(navController)
    }

    @Composable
    fun UserProfileScreen(navController: NavController) {
        val context = LocalContext.current
        val application = context.applicationContext as Application

        val userDao = GeocachingDatabase.getDatabase(context).userDao()
        val firebaseAuth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()
        val userRepository = UserRepository(firebaseAuth, firestore, userDao)

        val profilePageViewModelFactory = ProfilePageViewModelFactory(application, userRepository)
        val profilePageViewModel: ProfilePageViewModel =
            viewModel(factory = profilePageViewModelFactory)

        val currentUser by profilePageViewModel.currentUser.collectAsState()
        val errorMessage by profilePageViewModel.errorMessage.collectAsState() // Collect error message

        var showEditDialog by remember { mutableStateOf(false) }

        LaunchedEffect(errorMessage) {
            errorMessage?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show() // Display Toast
                // Reset the error message after showing the toast
                profilePageViewModel.clearErrorMessage()
            }
        }

        Scaffold(
            bottomBar = {
                sharedNavBar(navController)
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showEditDialog = true }
                ) {
                    Icon(imageVector = Icons.Default.Create, contentDescription = "Edit Profile")
                }
            }
        ) { paddingValues ->
            userProfileScreenBody(paddingValues, profilePageViewModel)

            if (showEditDialog) {
                EditProfileDialog(
                    currentUser = currentUser,
                    onDismiss = { showEditDialog = false },
                    onSave = { name, pronouns ->
                        profilePageViewModel.updateUserProfile(name, pronouns)
                        showEditDialog = false
                    }
                )
            }
        }
    }

    @Composable
    fun EditProfileDialog(
        currentUser: User?,
        onDismiss: () -> Unit,
        onSave: (String?, String?) -> Unit
    ) {
        var name by remember { mutableStateOf(currentUser?.name ?: "") }
        var pronouns by remember { mutableStateOf(currentUser?.pronouns ?: "") }

        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(stringResource(R.string.edit_profile), color = MaterialTheme.colorScheme.onBackground) },
            text = {
                Column {
                    InputField(
                        label = stringResource(R.string.name),
                        placeholder = stringResource(R.string.name_input),
                        value = name,
                        onValueChange = { name = it }
                    )
                    dropdownfielduseful(
                        label = stringResource(R.string.pronouns),
                        currentValue = pronouns,
                        options = listOf("he/him", "she/her", "they/them"),
                        onOptionSelected = { pronouns = it }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onSave(
                            name.takeIf { it != currentUser?.name },
                            pronouns.takeIf { it != currentUser?.pronouns },
                        )
                    }
                ) {
                    Text(stringResource(R.string.save))
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text(stringResource(R.string.cancel))
                }
            },
            containerColor = MaterialTheme.colorScheme.background
        )
    }


    @Composable
    fun userProfileScreenBody(
        paddingValues: PaddingValues,
        profilePageViewModel: ProfilePageViewModel
    ) {
        val configuration = LocalConfiguration.current
        val isLandscape = remember { configuration.orientation == Configuration.ORIENTATION_LANDSCAPE }
        val scrollState = rememberScrollState()
        val context = LocalContext.current
        val currentUser by profilePageViewModel.currentUser.collectAsState()


        // Image picker launcher
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let { selectedUri ->
                // Save the image locally
                val imagePath = saveImageLocally(context, selectedUri)
                profilePageViewModel.updateProfilePicture(imagePath)
            }
        }

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(if (isLandscape) scrollState else rememberScrollState())
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(MaterialTheme.colorScheme.tertiary, MaterialTheme.colorScheme.background)
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Profile Image and Edit Button Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                val profileImage = remember(currentUser?.profilePicture) {
                    currentUser?.profilePicture?.let { profilePath ->
                        if (profilePath.isNotBlank()) {
                            BitmapFactory.decodeFile(profilePath)?.asImageBitmap()
                        } else null
                    }
                }

                Box(
                    contentAlignment = Alignment.BottomEnd, // Aligns the button to the bottom-end of the profile picture
                    modifier = Modifier.size(100.dp) // Defines the overall size of the profile picture container
                ) {
                    if (profileImage != null) {
                        // Display the profile image
                        Image(
                            bitmap = profileImage,
                            contentDescription = stringResource(R.string.profile_picture),
                            modifier = Modifier
                                .size(100.dp) // Adjusts the image size
                                .clip(CircleShape) // Ensures the image has a circular shape
                                .border(2.dp, Color.White, CircleShape), // Adds a border
                            contentScale = ContentScale.Crop // Crops the image to fill the container
                        )
                    } else {
                        // Fallback to default profile image
                        Image(
                            painter = painterResource(id = R.drawable.icons8_user_default_64), // Default image resource
                            contentDescription = "Default Profile Picture",
                            modifier = Modifier
                                .size(100.dp) // Adjusts the fallback image size
                                .clip(CircleShape) // Ensures the fallback image is circular
                                .border(2.dp, Color.White, CircleShape), // Adds a border
                            contentScale = ContentScale.Crop // Crops the fallback image
                        )
                    }

                    IconButton(
                        onClick = { launcher.launch("image/*") },
                        modifier = Modifier
                            .size(36.dp) // Smaller size for the button
                            .offset(x = 8.dp, y = 8.dp) // Fine-tune the overlap position
                            .background(Color.White.copy(alpha = 0.8f), shape = CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Create,
                            contentDescription = "Change Profile Picture",
                            tint = Color.Black
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.height(16.dp))

            // User Name
            currentUser?.name?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Section Title
            Text(
                text = stringResource(R.string.your_information),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Information Fields
            ProfileField(label = stringResource(R.string.name), value = currentUser?.name)
            ProfileField(label = stringResource(R.string.email), value = currentUser?.email)
            ProfileField(label = stringResource(R.string.pronouns), value = currentUser?.pronouns)

        }
    }

    fun saveImageLocally(context: Context, uri: Uri): String {
        val filename = "profile_${System.currentTimeMillis()}.jpg"
        val outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE)

        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            inputStream.copyTo(outputStream)
        }

        return File(context.filesDir, filename).absolutePath
    }
