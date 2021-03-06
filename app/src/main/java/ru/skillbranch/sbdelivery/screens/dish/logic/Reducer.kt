package ru.skillbranch.sbdelivery.screens.dish.logic

import ru.skillbranch.sbdelivery.screens.dish.data.DishUiState
import ru.skillbranch.sbdelivery.screens.dish.data.ReviewUiState
import ru.skillbranch.sbdelivery.screens.root.logic.Eff
import ru.skillbranch.sbdelivery.screens.root.logic.RootState
import ru.skillbranch.sbdelivery.screens.root.logic.ScreenState

fun DishFeature.State.selfReduce(msg: DishFeature.Msg) : Pair<DishFeature.State, Set<Eff>> =
    when(msg){
        is DishFeature.Msg.AddToCart -> this to setOf(
            DishFeature.Eff.AddToCart(id = msg.id, count = msg.count)
        ).toEffs()//todo
        is DishFeature.Msg.DecrementCount -> copy(count = this.count.dec()) to emptySet()//todo()
        is DishFeature.Msg.HideReviewDialog -> copy(isReviewDialog = false) to emptySet() //todo()
        is DishFeature.Msg.IncrementCount -> copy(count = this.count.inc()) to emptySet()//todo()
        is DishFeature.Msg.SendReview -> this to setOf(DishFeature.Eff.SendReview(msg.dishId, msg.rating, msg.review)).toEffs()//TODO()
        is DishFeature.Msg.ShowDish -> copy(content = DishUiState.Value(msg.dish)) to emptySet()
        is DishFeature.Msg.ShowReviewDialog -> copy(isReviewDialog = true) to emptySet()//todo()
        is DishFeature.Msg.ShowReviews -> copy(reviews = ReviewUiState.Value(msg.reviews)) to emptySet()
        is DishFeature.Msg.ToggleLike -> copy(isLiked = this.isLiked.not()) to emptySet() //todo()
    }

fun  DishFeature.State.reduce(root: RootState, msg: DishFeature.Msg) : Pair<RootState, Set<Eff>> {
    val (screenState, effs) = selfReduce(msg)
    return root.changeCurrentScreen<ScreenState.Dish> { copy(state = screenState) } to effs
}

private fun Set<DishFeature.Eff>.toEffs(): Set<Eff> = mapTo(HashSet(), Eff::Dish)