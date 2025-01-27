package com.cmu.estg.cmu_geocaching.ui.screens.cacheActivity

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cmu.estg.cmu_geocaching.R
import com.cmu.estg.cmu_geocaching.data.local.entities.Cache
import com.cmu.estg.cmu_geocaching.ui.screens.utilcomposables.sharedNavBar
import com.cmu.estg.cmu_geocaching.viewModel.SharedCacheViewModel


@Preview
@Composable
fun CacheInformationPreview() {
    val navController = rememberNavController()
    val cache = Cache(
        cacheId = "1",
        createdBy = "1",
        difficulty = "Easy",
        latitude = "76.3827",
        longitude = "77.03921",
        rating = "1",
        image = "",
        description = "cache"
    )
    val sharedCacheViewModel: SharedCacheViewModel = viewModel()
    sharedCacheViewModel.setSelectedCache(cache)

    CacheInformationScreen(navController, sharedCacheViewModel)
}

@Composable
fun CacheInformationScreen(
    navController: NavController,
    sharedCacheViewModel: SharedCacheViewModel
) {
    Scaffold(
        bottomBar = {
            sharedNavBar(navController)
        }
    ) { innerPadding ->
        CacheInformationDetailScreen(
            Modifier.padding(innerPadding),
            navController,
            sharedCacheViewModel
        )
    }
}

@Composable
fun CacheInformationDetailScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    sharedCacheViewModel: SharedCacheViewModel
) {

    val cache = sharedCacheViewModel.selectedCache.collectAsState().value

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        //map view
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f)
                .background(color = Color.Gray)
        ) {
            //image of cache
            Image(
                painter = painterResource(id = R.drawable._60_f_836375491_2pdjkeq0i12nzljf1csig8tbmfdegxvf),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
        //cache info view
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(1f)
                .padding(10.dp)
        ) {
            //find cache title
            Text(
                text = stringResource(R.string.cache_information),
                fontSize = 32.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .padding(vertical = 10.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                InformationCard(stringResource(R.string.latitude), cache!!.latitude)
                Spacer(Modifier.padding(10.dp))
                InformationCard(stringResource(R.string.longitude), cache.longitude)
                Spacer(Modifier.padding(10.dp))
                InformationCard(stringResource(R.string.difficulty), cache.difficulty)
                Spacer(Modifier.padding(10.dp))
                InformationCard("Description", cache.description)
            }


            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 20.dp)
            ) {
                //lets go button
                Button(
                    onClick = { navController.navigate("lookforcache") },
                    colors = ButtonDefaults.buttonColors(Color(0xFF4A6456)),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .wrapContentSize()
                        .height(48.dp)
                        .border(3.dp, Color(0xFFB195DC), RoundedCornerShape(24.dp))
                        .shadow(2.dp, shape = RoundedCornerShape(24.dp))
                        .align(Alignment.BottomCenter)
                ) {
                    Text(
                        text = stringResource(R.string.lets_go),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun InformationCard(valueName: String, value: String) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFFFFFBEB),
        border = BorderStroke(1.dp, Color.DarkGray),
        shadowElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = valueName,
                fontSize = 16.sp,
                color = Color.DarkGray,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 14.sp,
                color = Color.DarkGray,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.fillMaxWidth(),
                softWrap = true,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}



