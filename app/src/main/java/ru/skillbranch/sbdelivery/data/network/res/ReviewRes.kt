package ru.skillbranch.sbdelivery.data.network.res

import java.io.Serializable

//data class ReviewRes(val name: String, val date:Long, val rating:Int, val message:String): Serializable
data class ReviewRes(val name: String, val date:Long, val rating:Int, val message:String): Serializable

data class ReviewFullRes(
    val dishId: String,
    val author: String,
    val date: String,
    val order: Int,
    val rating: Int,
    val text: String?,
    val active: Boolean,
    val createdAt: Long,
    val updatedAt: Long
): Serializable