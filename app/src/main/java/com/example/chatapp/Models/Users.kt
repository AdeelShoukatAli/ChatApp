package com.example.chatapp.Models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Users(var userId:String = "",
                 var userName:String = "",
                 var userEmail:String = "",
                 var userImageeUrl:String= "") : Parcelable {
}