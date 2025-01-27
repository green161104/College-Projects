package com.cmu.estg.cmu_geocaching.ui.screens.cacheActivity


import android.app.Application
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cmu.estg.cmu_geocaching.R
import com.cmu.estg.cmu_geocaching.data.local.GeocachingDatabase
import com.cmu.estg.cmu_geocaching.data.local.entities.relations.UserCacheCrossRef
import com.cmu.estg.cmu_geocaching.data.local.repository.UserCacheCrossRefRepository
import com.cmu.estg.cmu_geocaching.data.local.repository.UserRepository
import com.cmu.estg.cmu_geocaching.ui.screens.utilcomposables.sharedNavBar
import com.cmu.estg.cmu_geocaching.viewModel.CacheHistoryViewModel
import com.cmu.estg.cmu_geocaching.viewModel.CacheHistoryViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale

@Preview
@Composable
fun cacheHistoryPreview() {
    val navController = rememberNavController();
    CacheHistoryScreen(navController)
}


@Composable
fun CacheHistoryScreen(navController: NavController) {
    Scaffold(
        bottomBar = {
            sharedNavBar(navController)
        }
    ) { innerPadding ->
        YourFindsScreen(Modifier.padding(innerPadding))
    }
}

@Composable
fun YourFindsScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val userCacheDao = GeocachingDatabase.getDatabase(context).userCacheCrossRefDao()
    val userCacheRepository = UserCacheCrossRefRepository(userCacheDao)
    val userDao = GeocachingDatabase.getDatabase(context).userDao()
    val firebaseAuth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val userRepository = UserRepository(firebaseAuth, firestore, userDao)
    val userHistoryViewModelFactory =
        CacheHistoryViewModelFactory(application, userCacheRepository, userRepository)
    val cacheHistoryViewModel: CacheHistoryViewModel = viewModel(factory = userHistoryViewModelFactory)
    val userHistory by cacheHistoryViewModel.userHistory.observeAsState()
    val error by cacheHistoryViewModel.errorMessage.collectAsState(initial = null)


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        //title
        Text(
            text = stringResource(R.string.your_finds_english),
            fontSize = 32.sp,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(vertical = 22.dp)
        )
        LazyColumn {
            userHistory?.let { historyList ->
                if (historyList.isNotEmpty()) {
                    items(historyList) { historyItem ->
                        FoundItemCard(
                            imageid = R.drawable._60_f_836375491_2pdjkeq0i12nzljf1csig8tbmfdegxvf,
                            cacheCrossRef = historyItem
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                } else {
                    item {
                        error?.let { Text(it) }
                    }
                }
            } ?: item {
                error?.let { Text(it) }
            }
        }

    }
}

@Composable
fun FoundItemCard(imageid: Int, cacheCrossRef: UserCacheCrossRef) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(10.dp))
            .border(BorderStroke(1.dp, Color.DarkGray), shape = RoundedCornerShape(10.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        //icon of find
        Image(
            painter = painterResource(id = imageid),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(64.dp)
                .padding(8.dp)
                .clip(CircleShape)
        )

        //time of find
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())

            Text(
                text = dateFormat.format(cacheCrossRef.dateFound),
                fontSize = 20.sp
            )

        }
        //how many points
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .background(
                    Color(0xFF65558F),
                    shape = RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp)
                )
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = "+ " + cacheCrossRef.pointsEarned + " " + stringResource(R.string.points_earned),
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
    }
}

