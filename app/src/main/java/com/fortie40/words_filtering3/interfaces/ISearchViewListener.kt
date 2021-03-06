package com.fortie40.words_filtering3.interfaces

import android.animation.Animator
import android.os.Build
import android.view.View
import android.view.ViewAnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.core.view.isVisible
import com.fortie40.words_filtering3.helperclasses.HelperFunctions

interface ISearchViewListener {
    fun onOpenSearchView(
        inputText: EditText, viewToReveal: View, startView: View, width: Float
    ) {
        inputText.setText("")
        viewToReveal.visibility = View.VISIBLE
        searchViewCircularAnimation(viewToReveal, startView, 0f, width)
        inputText.requestFocus()
    }

    fun onCloseSearchView(
        inputText: EditText, viewToReveal: View, startView: View, width: Float
    ) {
        val circularAnim =
            searchViewCircularAnimation(viewToReveal, startView, width, 0f)

        circularAnim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) = Unit
            override fun onAnimationCancel(animation: Animator?) = Unit
            override fun onAnimationStart(animation: Animator?) = Unit
            override fun onAnimationEnd(animation: Animator?) {
                viewToReveal.visibility = View.INVISIBLE
                inputText.setText("")
                circularAnim.removeAllListeners()
            }
        })
    }

    fun onSubmitQuery(actionId: Int, view: EditText): Boolean {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            view.clearFocus()
            HelperFunctions.hideInputMethod(view.context, view)
        }
        return false
    }

    fun onFocusChange(hasFocus: Boolean, string: String, voice: View, close: View) {
        if (hasFocus)
            hideShowVoiceCloseIcon(string, voice, close)
    }

    fun hideShowVoiceCloseIcon(p0: String?, voice: View, close: View) {
        if (p0 == null)
            return

        voice.isVisible = p0.isEmpty()
        close.isVisible = p0.isNotEmpty()
    }

    fun onTextChanged(p0: String) {}

    fun onPromptSpeechInput() {}

    private fun searchViewCircularAnimation(
        viewToReveal: View, startView: View, startRad: Float, endRad: Float
    ): Animator {
        var circularAnim: Animator? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            circularAnim = ViewAnimationUtils.createCircularReveal(
                viewToReveal,
                (startView.right + startView.left) / 2,
                (startView.top + startView.bottom) / 2,
                startRad, endRad
            )
            circularAnim.duration = 300
            circularAnim.start()
        }
        return circularAnim!!
    }
}