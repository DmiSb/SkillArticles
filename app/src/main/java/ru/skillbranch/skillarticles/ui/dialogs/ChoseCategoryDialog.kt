package ru.skillbranch.skillarticles.ui.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_category_dialog.view.*
import ru.skillbranch.skillarticles.R
import ru.skillbranch.skillarticles.data.local.entities.CategoryData
import ru.skillbranch.skillarticles.viewmodels.articles.ArticlesViewModel

class ChoseCategoryDialog : DialogFragment() {
    private val viewModel: ArticlesViewModel by activityViewModels()
    private val selectedCategories = mutableListOf<String>()
    private val args: ChoseCategoryDialogArgs by navArgs()

    private val categoryAdapter = CategoryAdapter { categoryId: String, isChecked: Boolean ->
        if (isChecked) selectedCategories.add(categoryId)
        else selectedCategories.remove(categoryId)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        selectedCategories.clear()
        selectedCategories.addAll(
            savedInstanceState?.getStringArray("checked") ?: args.selectedCategories
        )
        val categoryItems = args.categories.map { it.toItem(selectedCategories.contains(it.categoryId)) }

        categoryAdapter.submitList(categoryItems)

        val listView = layoutInflater.inflate(R.layout.fragment_choose_category_dialog, null) as RecyclerView
        listView.adapter = categoryAdapter

        return AlertDialog.Builder(requireContext())
            .setView(listView)
            .setTitle("Chose category")
            .setPositiveButton("Apply") { dialog: DialogInterface?, which: Int ->
                viewModel.applyCategories(selectedCategories)
            }
            .setNegativeButton("Reset") { _, _ ->
                viewModel.applyCategories(emptyList())
            }
            .create()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putStringArray("checked", selectedCategories.toTypedArray())
        super.onSaveInstanceState(outState)
    }
}

class CategoryAdapter(
    private val listener: (String, Boolean) -> Unit
) : ListAdapter<CategoryDataItem, CategoryVH>(CategoryDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryVH {
        return CategoryVH(LayoutInflater.from(parent.context).inflate(R.layout.item_category_dialog, parent, false), listener)
    }

    override fun onBindViewHolder(holder: CategoryVH, position: Int) {
        holder.bind(getItem(position))
    }
}

class CategoryVH(
    override val containerView: View,
    val listener: (String, Boolean) -> Unit
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(item: CategoryDataItem) {
        with (containerView) {
            // remove listener
            ch_select.setOnCheckedChangeListener(null)
            // bind data
            ch_select.isChecked = item.isChecked

            Glide.with(containerView.context)
                .load(item.icon)
                .apply(RequestOptions.circleCropTransform())
                .override(containerView.iv_icon.width)
                .into(containerView.iv_icon)

            tv_category.text = item.title
            tv_count.text = "${item.articlesCount}"

            // set listener
            ch_select.setOnCheckedChangeListener { _, checked ->
                listener(
                    item.categoryId,
                    checked
                )
            }
            itemView.setOnClickListener { containerView.ch_select.toggle() }
        }
    }
}


class CategoryDiffCallback : DiffUtil.ItemCallback<CategoryDataItem>() {
    override fun areItemsTheSame(oldItem: CategoryDataItem, newItem: CategoryDataItem): Boolean {
        return oldItem.categoryId == newItem.categoryId
    }

    override fun areContentsTheSame(oldItem: CategoryDataItem, newItem: CategoryDataItem): Boolean {
        return oldItem == newItem
    }
}

data class CategoryDataItem(
    val categoryId: String,
    val icon: String,
    val title: String,
    val articlesCount: Int = 0,
    val isChecked: Boolean = false
)

fun CategoryData.toItem(checked: Boolean = false) =
    CategoryDataItem(categoryId, icon, title, articlesCount, checked)