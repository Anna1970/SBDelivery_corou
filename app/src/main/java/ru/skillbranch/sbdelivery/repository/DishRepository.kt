package ru.skillbranch.sbdelivery.repository

import android.os.Build
import androidx.annotation.RequiresApi
import ru.skillbranch.sbdelivery.data.db.dao.CartDao
import ru.skillbranch.sbdelivery.data.db.dao.DishesDao
import ru.skillbranch.sbdelivery.data.db.entity.CartItemPersist
import ru.skillbranch.sbdelivery.data.network.RestService
import ru.skillbranch.sbdelivery.data.network.req.ReviewReq
import ru.skillbranch.sbdelivery.data.network.res.ReviewRes
import ru.skillbranch.sbdelivery.data.toDishContent
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
        val _count = cartCount()
        if (_count > 0) cartDao.updateItemCount(id,_count + count)
        else cartDao.addItem(CartItemPersist(dishId = id, count = count))
    }//todo

    override suspend fun cartCount(): Int = cartDao.cartCount() ?: 0 //todo

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun loadReviews(dishId: String): List<ReviewRes> {
        //todo
        val reviews = mutableListOf<ReviewRes>()
        var offset = 0
        while (true) {
            val response = api.getReviews(dishId, offset * 10, 10)
            if (response.isSuccessful) {
                offset++
                reviews.addAll(response.body()!!)
            }
            else break
        }

        return reviews
    }

    override suspend fun sendReview(id: String, rating: Int, review: String) =
        api.sendReview(id, ReviewReq(rating, text = review))
}