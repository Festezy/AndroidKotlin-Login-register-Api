package com.ariqandrean.androidregister.Adapter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.ariqandrean.androidregister.MainActivity
import com.ariqandrean.androidregister.R
import com.ariqandrean.androidregister.model.CategoryModel
import kotlinx.android.synthetic.main.item_row.view.*
import retrofit2.http.Url
import java.net.URL

class CategoryAdapter(val context: Context): RecyclerView.Adapter<CategoryAdapter.ViewHolder>(), Filterable {
    var arrayList = ArrayList<CategoryModel>()
    var CategoryListFilter = ArrayList<CategoryModel>()

    fun setData(arrayList: ArrayList<CategoryModel>){
        this.arrayList = arrayList
        this.CategoryListFilter = arrayList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindItem(model: CategoryModel){
            itemView.categoryName.text = "${model.id}, ${model.name}"

            val url = URL("http://ariqa.teknisitik.com/" + model.imagelink)
            val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            itemView.imgCategory.setImageBitmap(bmp)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(arrayList[position])
        holder.itemView.setOnClickListener {
            val model = arrayList[position]

            val categoryId : Int = model.id

            var intent = Intent(context, MainActivity::class.java) // diganti jadi product activity
            intent.putExtra("categoryId", categoryId)
            context.startActivities(arrayOf(intent))
        }
    }

    override fun getFilter(): Filter {
        return object  : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResult = FilterResults()
                if (constraint == null || constraint.length < 0){
                    filterResult.count = CategoryListFilter.size
                    filterResult.values = CategoryListFilter
                } else {
                    var searchChr = constraint.toString()
                    val categorySearch = ArrayList<CategoryModel>()
                    for (item in CategoryListFilter)
                        if (item.name.toLowerCase().contains(searchChr)){
                            categorySearch.add(item)
                    }
                    filterResult.count = categorySearch.size
                    filterResult.values = categorySearch
                }
                return filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                arrayList = results!!.values as ArrayList<CategoryModel>
                notifyDataSetChanged()
            }

        }
    }
}