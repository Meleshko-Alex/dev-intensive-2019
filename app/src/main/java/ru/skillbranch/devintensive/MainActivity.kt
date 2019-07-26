package ru.skillbranch.devintensive

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.extensions.hideKeyboard
import ru.skillbranch.devintensive.models.Bender

class MainActivity : AppCompatActivity(), View.OnClickListener, TextView.OnEditorActionListener {

    private lateinit var benderImage: ImageView
    private lateinit var textTxt: TextView
    private lateinit var messageEt: EditText
    private lateinit var sendBtn: ImageView
    private lateinit var benderObj: Bender

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        benderImage = iv_bender
        textTxt = tv_text
        messageEt = et_message
        sendBtn = iv_send

        messageEt.imeOptions = EditorInfo.IME_ACTION_DONE
        messageEt.setOnEditorActionListener(this)

        val status = savedInstanceState?.getString("STATUS") ?: Bender.Status.NORMAL.name
        val question = savedInstanceState?.getString("QUESTION") ?: Bender.Question.NAME.name
        benderObj = Bender(Bender.Status.valueOf(status), Bender.Question.valueOf(question))

        val (r, g, b) = benderObj.status.color
        benderImage.setColorFilter(Color.rgb(r, g, b), PorterDuff.Mode.MULTIPLY)

        textTxt.text = benderObj.askQuestion()
        sendBtn.setOnClickListener(this)
        benderImage.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.iv_send && messageEt.text.toString().isNotEmpty()) {
            checkAnswer()
            hideKeyboard()
        }

        if (v?.id == R.id.iv_bender) {
        }
    }

    override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
        if (p1 == EditorInfo.IME_ACTION_DONE) {
            if (messageEt.text.toString().isNotEmpty()) {
                checkAnswer()
            }
        }
        return false
    }

    private fun checkAnswer() {
        if (benderObj.isValidAnswer(messageEt.text.toString())) {
            val (phrase, color) = benderObj.listenAnswer(messageEt.text.toString().toLowerCase())
            messageEt.setText("")
            val (r, g, b) = color
            benderImage.setColorFilter(Color.rgb(r, g, b), PorterDuff.Mode.MULTIPLY)
            textTxt.text = phrase
        } else {
            val errorText = when {
                benderObj.question == (Bender.Question.NAME) -> "Имя должно начинаться с заглавной буквы"
                benderObj.question == (Bender.Question.PROFESSION) -> "Профессия должна начинаться со строчной буквы"
                benderObj.question == (Bender.Question.MATERIAL) -> "Материал не должен содержать цифр"
                benderObj.question == (Bender.Question.BDAY) -> "Год моего рождения должен содержать только цифры"
                benderObj.question == (Bender.Question.SERIAL) -> "Серийный номер содержит только цифры, и их 7"
                else -> ""
            }
            textTxt.text = "$errorText\n${benderObj.question.question}"
            messageEt.setText("")
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putString("STATUS", benderObj.status.name)
        outState?.putString("QUESTION", benderObj.question.name)
    }
}
