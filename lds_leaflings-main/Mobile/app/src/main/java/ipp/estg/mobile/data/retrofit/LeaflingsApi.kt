package ipp.estg.mobile.data.retrofit


import ipp.estg.mobile.data.retrofit.leaflingsEndpoints.AdApi
import ipp.estg.mobile.data.retrofit.leaflingsEndpoints.AuthApi
import ipp.estg.mobile.data.retrofit.leaflingsEndpoints.DiaryApi
import ipp.estg.mobile.data.retrofit.leaflingsEndpoints.LogApi
import ipp.estg.mobile.data.retrofit.leaflingsEndpoints.PlantApi
import ipp.estg.mobile.data.retrofit.leaflingsEndpoints.TaskApi
import ipp.estg.mobile.data.retrofit.leaflingsEndpoints.UserApi
import ipp.estg.mobile.data.retrofit.leaflingsEndpoints.UserPlantApi

interface LeaflingsApi : AuthApi, PlantApi, UserApi, UserPlantApi, TaskApi, DiaryApi, LogApi, AdApi