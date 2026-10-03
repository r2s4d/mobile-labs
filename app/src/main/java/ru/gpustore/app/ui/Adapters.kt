package ru.gpustore.app.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.gpustore.app.data.CartLine
import ru.gpustore.app.data.GraphicsCard
import ru.gpustore.app.databinding.ItemCardBinding
import ru.gpustore.app.databinding.ItemCartLineBinding
import ru.gpustore.app.databinding.ItemFavoriteBinding
import ru.gpustore.app.util.brandArt
import ru.gpustore.app.util.formatPrice

/*
 * Адаптеры связывают список данных с RecyclerView.
 * RecyclerView создает ровно столько строк, сколько помещается на экране,
 * и переиспользует их при прокрутке. ListAdapter с DiffUtil сам вычисляет,
 * какие строки изменились, и обновляет только их.
 */

/** Строка каталога: картинка, название, характеристики и цена */
class CardAdapter(
    private val onClick: (GraphicsCard) -> Unit
) : ListAdapter<GraphicsCard, CardAdapter.Holder>(CardDiff) {

    class Holder(val binding: ItemCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
        Holder(ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val card = getItem(position)
        with(holder.binding) {
            image.setImageResource(brandArt(card.brand))
            name.text = card.name
            spec.text = card.shortSpec
            price.text = formatPrice(card.price)
            root.setOnClickListener { onClick(card) }
        }
    }
}

/** Строка избранного: то же, что в каталоге, плюс кнопка удаления из избранного */
class FavoriteAdapter(
    private val onClick: (GraphicsCard) -> Unit,
    private val onRemove: (GraphicsCard) -> Unit
) : ListAdapter<GraphicsCard, FavoriteAdapter.Holder>(CardDiff) {

    class Holder(val binding: ItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
        Holder(ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val card = getItem(position)
        with(holder.binding) {
            image.setImageResource(brandArt(card.brand))
            name.text = card.name
            spec.text = card.shortSpec
            price.text = formatPrice(card.price)
            root.setOnClickListener { onClick(card) }
            removeButton.setOnClickListener { onRemove(card) }
        }
    }
}

/** Строка корзины: название, цена за штуку, количество с кнопками плюс и минус, удаление */
class CartAdapter(
    private val onOpen: (CartLine) -> Unit,
    private val onIncrease: (CartLine) -> Unit,
    private val onDecrease: (CartLine) -> Unit,
    private val onRemove: (CartLine) -> Unit
) : ListAdapter<CartLine, CartAdapter.Holder>(CartDiff) {

    class Holder(val binding: ItemCartLineBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
        Holder(ItemCartLineBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val line = getItem(position)
        with(holder.binding) {
            image.setImageResource(brandArt(line.brand))
            name.text = line.name
            // Показываем стоимость всей строки (цена на количество)
            price.text = formatPrice(line.lineTotal)
            quantity.text = line.quantity.toString()
            root.setOnClickListener { onOpen(line) }
            plusButton.setOnClickListener { onIncrease(line) }
            minusButton.setOnClickListener { onDecrease(line) }
            deleteButton.setOnClickListener { onRemove(line) }
        }
    }
}

/** DiffUtil сравнивает старый и новый списки: один и тот же товар определяется по id */
private object CardDiff : DiffUtil.ItemCallback<GraphicsCard>() {
    override fun areItemsTheSame(a: GraphicsCard, b: GraphicsCard) = a.id == b.id
    override fun areContentsTheSame(a: GraphicsCard, b: GraphicsCard) = a == b
}

private object CartDiff : DiffUtil.ItemCallback<CartLine>() {
    override fun areItemsTheSame(a: CartLine, b: CartLine) = a.cardId == b.cardId
    override fun areContentsTheSame(a: CartLine, b: CartLine) = a == b
}
