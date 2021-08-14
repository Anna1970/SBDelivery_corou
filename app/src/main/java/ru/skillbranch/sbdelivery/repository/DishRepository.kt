package ru.skillbranch.sbdelivery.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import ru.skillbranch.sbdelivery.data.db.dao.CartDao
import ru.skillbranch.sbdelivery.data.db.dao.DishesDao
import ru.skillbranch.sbdelivery.data.db.entity.CartItemPersist
import ru.skillbranch.sbdelivery.data.network.RestService
import ru.skillbranch.sbdelivery.data.network.req.ReviewReq
import ru.skillbranch.sbdelivery.data.network.res.ReviewRes
import ru.skillbranch.sbdelivery.data.toDishContent
import ru.skillbranch.sbdelivery.data.toReviewResItem
import ru.skillbranch.sbdelivery.screens.dish.data.DishContent
import java.util.*
import javax.inject.Inject

interface IDishRepository {
    suspend fun findDish(id: String): DishContent
    suspend fun addToCart(id: String, count: Int)
    suspend fun cartCount(): Int
    suspend fun loadReviews(dishId: String): List<ReviewRes>
    suspend fun sendReview(id: String, rating: Int, review: String): ReviewRes
}

class DishRepository @Inject constructor(
    private val api: RestService,
    private val dishesDao: DishesDao,
    private val cartDao: CartDao,
) : IDishRepository {
    override suspend fun findDish(id: String): DishContent = dishesDao.findDish(id).toDishContent()

    override suspend fun addToCart(id: String, count: Int) {
        cartDao.addItem(CartItemPersist(dishId = id, count = count))
    }//todo

    override suspend fun cartCount(): Int = cartDao.cartCount() ?: 0 //todo

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun loadReviews(dishId: String): List<ReviewRes> {
        //todo
        val response = api.getReviewsFull(dishId,0,100)//api.getReviews(dishId,0,10)
        return  if (response.isSuccessful) response.body()!!.map{it.toReviewResItem()}
                else emptyList()
    }

    override suspend fun sendReview(id: String, rating: Int, review: String): ReviewRes {
        api.sendReview(id,  ReviewReq(rating, review))
        return ReviewRes("Name", Date().time, rating, review)
    }
}