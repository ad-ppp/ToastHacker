package com.example.toasthacker.toast

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.support.annotation.StringRes
import android.view.View
import android.widget.Toast

class ToastCompat(context: Context, val toast: Toast) : @JvmOverloads Toast(context) {
    companion object {
        /**
         * Make a standard toast that just contains a text view.
         *
         * @param context The context to use.  Usually your [android.app.Application]
         * or [android.app.Activity] object.
         * @param text The text to show.  Can be formatted text.
         * @param duration How long to display the message.  Either [.LENGTH_SHORT] or
         * [.LENGTH_LONG]
         */
        fun makeText(context: Context, text: CharSequence, duration: Int): ToastCompat {
            // We cannot pass the SafeToastContext to Toast.makeText() because
            // the View will unwrap the base context and we are in vain.
            @SuppressLint("ShowToast")
            val toast = Toast.makeText(context, text, duration)
            setContextCompat(toast.view, SafeToastContext(context, toast))
            return ToastCompat(context, toast)
        }

        /**
         * Make a standard toast that just contains a text view with the text from a resource.
         *
         * @param context The context to use.  Usually your [android.app.Application]
         * or [android.app.Activity] object.
         * @param resId The resource id of the string resource to use.  Can be formatted text.
         * @param duration How long to display the message.  Either [.LENGTH_SHORT] or
         * [.LENGTH_LONG]
         * @throws Resources.NotFoundException if the resource can't be found.
         */
        @Throws(Resources.NotFoundException::class)
        fun makeText(context: Context, @StringRes resId: Int, duration: Int): ToastCompat {
            return makeText(context, context.resources.getText(resId), duration)
        }

        private fun setContextCompat(view: View, context: Context) {
            if (Build.VERSION.SDK_INT == 25) {
                try {
                    val field = View::class.java.getDeclaredField("mContext")
                    field.isAccessible = true
                    field.set(view, context)
                } catch (throwable: Throwable) {
                    throwable.printStackTrace()
                }

            }
        }
    }

    fun setBadTokenListener(listener: BadTokenListener): ToastCompat {
        val context = view.context
        if (context is SafeToastContext) {
            context.setBadTokenListener(listener)
        }
        return this
    }

    override fun show() {
        ToastHacker.tryToHack(this)
        toast.show()
    }

    override fun setDuration(duration: Int) {
        toast.duration = duration
    }

    override fun setGravity(gravity: Int, xOffset: Int, yOffset: Int) {
        toast.setGravity(gravity, xOffset, yOffset)
    }

    override fun setMargin(horizontalMargin: Float, verticalMargin: Float) {
        toast.setMargin(horizontalMargin, verticalMargin)
    }

    override fun setText(resId: Int) {
        toast.setText(resId)
    }

    override fun setText(s: CharSequence) {
        toast.setText(s)
    }

    override fun setView(view: View) {
        toast.view = view
        setContextCompat(view, SafeToastContext(view.context, this))
    }

    override fun getHorizontalMargin(): Float {
        return toast.horizontalMargin
    }

    override fun getVerticalMargin(): Float {
        return toast.verticalMargin
    }

    override fun getDuration(): Int {
        return toast.duration
    }

    override fun getGravity(): Int {
        return toast.gravity
    }

    override fun getXOffset(): Int {
        return toast.xOffset
    }

    override fun getYOffset(): Int {
        return toast.yOffset
    }

    override fun getView(): View {
        return toast.view
    }

    fun getBaseToast(): Toast {
        return toast
    }
}