package com.example.king_bob_nae.util

import android.os.Looper
import androidx.annotation.CheckResult
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.databinding.ViewDataBinding
import com.example.king_bob_nae.R
import com.example.king_bob_nae.util.Extensions.Companion.CERTIFICATION_ERROR
import com.example.king_bob_nae.util.Extensions.Companion.EMAIL_ERROR
import com.example.king_bob_nae.util.Extensions.Companion.NICK_ERROR
import com.example.king_bob_nae.util.Extensions.Companion.PASSWD_ERROR
import com.example.king_bob_nae.util.Extensions.Companion.SIGN_IN_ERROR
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

class Extensions {
    companion object {
        const val SIGN_IN_ERROR = 0
        const val EMAIL_ERROR = 1
        const val PASSWD_ERROR = 2
        const val NICK_ERROR = 3
        const val CERTIFICATION_ERROR = 4
    }
}

fun TextInputLayout.hideIcon() {
    isEndIconVisible = false
}

fun checkMainThread() {
    check(Looper.myLooper() == Looper.getMainLooper()) {
        "Expected to be called on the main thread but was " + Thread.currentThread().name
    }
}

@ExperimentalCoroutinesApi
@CheckResult
fun TextInputLayout.textChanges(): Flow<CharSequence?> {
    return callbackFlow {
        checkMainThread()
        val listener = editText?.doOnTextChanged { text, _, _, _ -> trySend(text) }
        awaitClose { editText?.removeTextChangedListener(listener) }
    }.onStart { emit(editText?.text) }
}

fun TextInputLayout.isValid(isValid: (String) -> Boolean) {
    textChanges().debounce(400)
        .onEach {
            error = null
            isEndIconVisible = isValid(it.toString())
            setEndIconDrawable(R.drawable.ic_correct_20)
        }
        .launchIn(CoroutineScope(Dispatchers.Main))
}

fun TextInputLayout.setButtonEnable(textInputLayout: TextInputLayout, button: AppCompatButton) {
    textChanges().debounce(400)
        .onEach {
            button.isEnabled(isEndIconVisible && textInputLayout.isEndIconVisible)
        }
        .launchIn(CoroutineScope(Dispatchers.Main))
}

fun TextInputLayout.setButtonEnable(isValid: (String) -> Boolean, button: AppCompatButton) {
    textChanges().debounce(400)
        .onEach {
            button.isEnabled(isValid(it.toString()))
        }
        .launchIn(CoroutineScope(Dispatchers.Main))
}

fun TextInputLayout.setError(errorType: Int) {
    error = when (errorType) {
        SIGN_IN_ERROR -> context.getString(R.string.sign_up_input_nick_error)
        EMAIL_ERROR -> context.getString(R.string.input_email_error)
        PASSWD_ERROR -> context.getString(R.string.sign_up_input_passwd_error)
        NICK_ERROR -> context.getString(R.string.sign_up_input_nick_error)
        CERTIFICATION_ERROR -> context.getString(R.string.certification_error)
        else -> return
    }
    setEndIconDrawable(R.drawable.ic_error_20)
}

fun AppCompatButton.isEnabled(condition: Boolean) {
    isEnabled = condition
    setTextColor(
        ContextCompat.getColor(context, if (condition) R.color.white else R.color.brown_gray_300)
    )
    setBackgroundResource(if (condition) R.drawable.radius_orange else R.drawable.radius_gray)

}

fun ViewDataBinding.initTextInputLayout(
    textInputLayout: TextInputLayout,
    checkFunction: (String) -> Boolean,
    button: AppCompatButton
) {
    textInputLayout.apply {
        hideIcon()
        isValid {
            checkFunction(it)
        }
        setButtonEnable(this, button)
    }
}

fun ViewDataBinding.initTextInputLayout(
    firstTextInputLayout: TextInputLayout,
    secondTextInputLayout: TextInputLayout,
    firstCheckFunction: (String) -> Boolean,
    secondCheckFunction: (String) -> Boolean,
    button: AppCompatButton
) {
    firstTextInputLayout.apply {
        hideIcon()
        isValid {
            firstCheckFunction(it)
        }
        setButtonEnable(secondTextInputLayout, button)
    }
    secondTextInputLayout.apply {
        hideIcon()
        isValid {
            secondCheckFunction(it)
        }
        setButtonEnable(firstTextInputLayout, button)
    }
}