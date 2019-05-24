package com.wesmarclothing.kotlintools.kotlin

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

/**
 * Description: 资源操作相关
 * Create by dance, at 2018/12/11
 */

fun Context.color(id: Int) = ContextCompat.getColor(this, id)

fun Context.string(id: Int) = resources.getString(id)

fun Context.stringArray(id: Int) = resources.getStringArray(id)

fun Context.drawable(id: Int) = ContextCompat.getDrawable(this, id)

fun Context.dimenPx(id: Int) = resources.getDimensionPixelSize(id)


fun View.color(id: Int) = context.color(id)

fun View.string(id: Int) = context.string(id)

fun View.stringArray(id: Int) = context.stringArray(id)

fun View.drawable(id: Int) = context.drawable(id)

fun View.dimenPx(id: Int) = context.dimenPx(id)


fun Fragment.color(id: Int) = context!!.color(id)

fun Fragment.string(id: Int) = context!!.string(id)

fun Fragment.stringArray(id: Int) = context!!.stringArray(id)

fun Fragment.drawable(id: Int) = context!!.drawable(id)

fun Fragment.dimenPx(id: Int) = context!!.dimenPx(id)

