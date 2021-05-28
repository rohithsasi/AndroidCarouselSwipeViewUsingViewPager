package com.mywagr.carouselviewpager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2



class MainActivity : AppCompatActivity() {

    lateinit var  carouselViewPager: ViewPager2
    lateinit var  carouselAdapter: CarouselAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        carouselViewPager= findViewById(R.id.view_pager)
        carouselAdapter = CarouselAdapter(carouselViewPager, arrayListOf(R.drawable.ic_baseline_account_balance_24,R.drawable.ic_baseline_account_balance_wallet_24,R.drawable.ic_baseline_airplane_ticket_24,R.drawable.ic_baseline_auto_awesome_mosaic_24),this)
        carouselViewPager.adapter = carouselAdapter

        carouselViewPager.clipToPadding = false
        carouselViewPager.clipChildren = false
        carouselViewPager.offscreenPageLimit = 3
        carouselViewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        val compositePageTransformer = CompositePageTransformer().apply {

              addTransformer( MarginPageTransformer(40))
//              addTransformer(object : ViewPager2.PageTransformer{
//                  override fun transformPage(page: View, position: Float) {
//
//                      val r = 1- Math.abs(position)
//                      page.scaleY = .85f +r *.15f
//
//                  }
//
//              })

            addTransformer(DepthPageTransformer())



        }
        carouselViewPager.setPageTransformer(compositePageTransformer)

    }
}

private const val MIN_SCALE_DEPTH = 0.75f

@RequiresApi(21)
class DepthPageTransformer : ViewPager2.PageTransformer {

    override fun transformPage(view: View, position: Float) {
        view.apply {
            val pageWidth = width
            when {
                position < -1 -> { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    alpha = 0f
                }
                position <= 0 -> { // [-1,0]
                    // Use the default slide transition when moving to the left page
                    alpha = 1f
                    translationX = 0f
                    translationZ = 0f
                    scaleX = 1f
                    scaleY = 1f
                }
                position <= 1 -> { // (0,1]
                    // Fade the page out.
                    alpha = 1 - position

                    // Counteract the default slide transition
                    translationX = pageWidth * -position
                    // Move it behind the left page
                    translationZ = -1f

                    // Scale the page down (between MIN_SCALE and 1)
                    val scaleFactor = (MIN_SCALE_DEPTH + (1 - MIN_SCALE_DEPTH) * (1 - Math.abs(position)))
                    scaleX = scaleFactor
                    scaleY = scaleFactor
                }
                else -> { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    alpha = 0f
                }
            }
        }
    }
}

private const val MIN_SCALE_ZOOM = 0.85f
private const val MIN_ALPHA_ZOOM = 0.5f
class ZoomTransformer: ViewPager2.PageTransformer{

    //Zoom in Zoom out
    override fun transformPage(view: View, position: Float) {
        view.apply {
            val pageWidth = width
            val pageHeight = height
            when {
                position < -1 -> { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    alpha = 0f
                }
                position <= 1 -> { // [-1,1]
                    // Modify the default slide transition to shrink the page as well
                    val scaleFactor = Math.max(MIN_SCALE_ZOOM, 1 - Math.abs(position))
                    val vertMargin = pageHeight * (1 - scaleFactor) / 2
                    val horzMargin = pageWidth * (1 - scaleFactor) / 2
                    translationX = if (position < 0) {
                        horzMargin - vertMargin / 2
                    } else {
                        horzMargin + vertMargin / 2
                    }

                    // Scale the page down (between MIN_SCALE and 1)
                    scaleX = scaleFactor
                    scaleY = scaleFactor

                    // Fade the page relative to its size.
                    alpha = (MIN_ALPHA_ZOOM +
                            (((scaleFactor - MIN_SCALE_ZOOM) / (1 - MIN_SCALE_ZOOM)) * (1 - MIN_ALPHA_ZOOM)))
                }
                else -> { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    alpha = 0f
                }
            }
        }
    }

//                  override fun transformPage(page: View, position: Float) {
//
//                      val r = 1- Math.abs(position)
//                      page.scaleY = .85f +r *.15f
//
//                  }

}