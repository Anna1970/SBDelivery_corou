package ru.skillbranch.sbdelivery.screens.dish.logic

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import ru.skillbranch.sbdelivery.repository.DishRepository
import ru.skillbranch.sbdelivery.screens.root.logic.Eff
import ru.skillbranch.sbdelivery.screens.root.logic.IEffectHandler
import ru.skillbranch.sbdelivery.screens.root.logic.Msg
import javax.inject.Inject

class DishEffHandler @Inject constructor(
    private val repository: DishRepository,
    private val notifyChannel: Channel<Eff.Notification>,
    private val dispatcher: CoroutineDispatcher  = Dispatchers.Default
) :
    IEffectHandler<DishFeature.Eff, Msg> {

    private var localJob: Job? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun handle(effect: DishFeature.Eff, commit: (Msg) -> Unit) {

        if (localJob == null) localJob = Job()
        withContext(localJob!! + dispatcher) {
            when (effect) {
                is DishFeature.Eff.AddToCart -> {//todo()
                    repository.addToCart(effect.id, effect.count)
                    commit(Msg.UpdateCartCount(repository.cartCount()))
                    notifyChannel.send(
                        Eff.Notification.Text("В корзину добавлено ${effect.count} товаров")
                    )
                }
                is DishFeature.Eff.LoadDish -> {
                    val dish = repository.findDish(effect.dishId)
                    commit(DishFeature.Msg.ShowDish(dish).toMsg())
                }
                is DishFeature.Eff.LoadReviews -> {
                    Log.e("SBD_Eff.LoadReviews","Load ${effect.dishId}")
                    try {
                        val reviews = repository.loadReviews(effect.dishId)
                        Log.e("SBD_Eff.LoadReviews","Load $reviews")
                        commit(DishFeature.Msg.ShowReviews(reviews).toMsg())
                    } catch (t: Throwable) {
                        notifyChannel.send(Eff.Notification.Error(t.message ?: "something error"))
                    }
                }
                is DishFeature.Eff.SendReview -> {
                    val review = repository.sendReview(effect.id, effect.rating, effect.review)
                    val newReviews = repository.loadReviews(effect.id) + review
                    commit(DishFeature.Msg.ShowReviews(newReviews).toMsg())
                    notifyChannel.send(Eff.Notification.Text("Отзыв успешно отправлен"))
                }//todo()
                is DishFeature.Eff.Terminate -> {
                    localJob?.cancel("Terminate coroutine scope")
                    localJob = null
                }
            }
        }

    }

    private fun DishFeature.Msg.toMsg(): Msg = Msg.Dish(this)
}



