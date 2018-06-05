package uk.co.xlntech.architectureapp.utils

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide

@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, imageUrl: String) {
    Glide.with(imageView).load(imageUrl).into(imageView)
}