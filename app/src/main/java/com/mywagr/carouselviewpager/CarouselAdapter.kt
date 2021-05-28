package com.mywagr.carouselviewpager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2


class CarouselAdapter(viewPager2: ViewPager2, val carouselItems: ArrayList<Int>,val context: Context): RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        return CarouselViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_image_view,parent,false))
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        //holder.team1.background = ResourcesCompat.getDrawable(context.resources,carouselItems[position],null)

        holder.team1.setImageResource(carouselItems[position])

    }

    override fun getItemCount(): Int {
        return carouselItems.size
    }

    class CarouselViewHolder(view: View): RecyclerView.ViewHolder(view){
        val team1: ImageView = view.findViewById(R.id.card)
    }
}