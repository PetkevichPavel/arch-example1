package com.arch.app.common.utils

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.Guideline
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.arch.app.assets.R
import com.google.android.material.checkbox.MaterialCheckBox


/**
 * Sets the visibility of the view.
 * @param isVisible True if set [View.VISIBLE], false to set [altVisibility].
 * @param altVisibility The alternative visibility if [isVisible] is false. Default is [View.GONE]
 */
@BindingAdapter(value = ["isVisible", "altVisibility"])
fun View.makeVisible(isVisible: Boolean = true, altVisibility: Int = View.GONE) {
    visibility = if (isVisible) View.VISIBLE else altVisibility
}

/**
 * ImageView extension function set image drawable via [drawableRes].
 * @param drawableRes - drawable resource reference.
 */
@BindingAdapter("android:src")
fun ImageView.setImageDrawable(@DrawableRes drawableRes: Int?) {
    setImageDrawable(drawableRes?.run { ContextCompat.getDrawable(context, this@run) })
}

@BindingAdapter(value = ["constraintPercent"])
fun setConstraintPercent(view: Guideline, percent: Float) {
    view.setGuidelinePercent(percent)
}

/**
 * TextView extension function set text via [stringRes] or optional [textValue].
 * @param stringRes String resource that is used when [textValue] is not provided.
 * @param textValue String value to use.
 */
@BindingAdapter(value = ["android:text", "textValue"], requireAll = false)
fun TextView.setTextResource(@StringRes stringRes: Int? = null, textValue: Any? = null) {
    safe { textValue?.let { text = it.toString() } ?: stringRes?.let { setText(it) } }
}

/**
 * Set image to the view.
 * @param url - is a dynamic string url of the final image.
 * @param defUrl - is a default string url of the final image.
 * @param placeHolder - is a place holder when image loading.
 * @param placeHolderError - is a place holder on error, if image didn't loaded.
 * @param finishProcess - is the lambda which is invoke after any result success/failed.
 * @param showProgressBar - whether to show loading progress bar or not.
 */
@SuppressLint("CheckResult")
@BindingAdapter(
    value = [
        "glideUrl", "glideDefUrl", "glidePlaceHolder",
        "glidePlaceHolderError", "glideProgress", "glideCacheSupport", "glideFinishProcess"
    ],
    requireAll = false
)
fun ImageView.setImageToView(
    url: String?,
    @Nullable defUrl: String? = null,
    @Nullable placeHolder: Int? = null,
    @Nullable placeHolderError: Int? = null,
    showProgressBar: Boolean = false,
    supportCache: Boolean = false,
    @Nullable finishProcess: (() -> Unit)? = null
) {
    if (url == null) return
    val strategy = DiskCacheStrategy.NONE.takeIf { supportCache.not() } ?: DiskCacheStrategy.ALL
    Glide.with(context)
        .load(url)
        .apply(
            RequestOptions()
                .diskCacheStrategy(strategy)
                .skipMemoryCache(supportCache.not())
                .error(placeHolderError ?: R.drawable.place_holder)
                .priority(Priority.HIGH)
                .applyIf(showProgressBar) {
                    CircularProgressDrawable(context).apply {
                        setStyle(CircularProgressDrawable.LARGE)
                        start()
                    }.also { placeholder(it) }
                }
                .apply {
                    placeHolder?.let { placeholder(it) }
                }
        )
        .listener(
            object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    finishProcess?.invoke()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    finishProcess?.invoke()
                    return false
                }
            }
        )
        .apply {
            defUrl?.let { error(Glide.with(context).load(defUrl)) }
        }
        .dontTransform()
        .into(this)
}


/**
 * TextView extension for setting text with spannable or not.
 * @param text - text.
 * @param spannableText - spannable string optional.
 */
@BindingAdapter(value = ["text", "spannableText"], requireAll = false)
fun TextView.setText(text: String, spannableText: SpannableString? = null) {
    spannableText?.let {
        setText(it, TextView.BufferType.SPANNABLE)
        movementMethod = LinkMovementMethod.getInstance()
        setOnLongClickListener { true }
    } ?: setText(text)
}

/**
 * TextView extension for setting text with spannable or not.
 * @param text - text.
 * @param spannableText - spannable string optional.
 */
@BindingAdapter(value = ["text", "spannableText"], requireAll = false)
fun MaterialCheckBox.setText(text: String, spannableText: SpannableString? = null) {
    spannableText?.let {
        setText(it, TextView.BufferType.SPANNABLE)
        movementMethod = LinkMovementMethod.getInstance()
        setOnLongClickListener { true }
    } ?: setText(text)
}

/**
 * View Extension function setting bg color with [colorId].
 * @param colorId - color id as Int.
 */
@BindingAdapter(value = ["colorId"])
fun View.setBgColor(colorId: Int) {
    setBackgroundColor(context.getColor(colorId))
}
