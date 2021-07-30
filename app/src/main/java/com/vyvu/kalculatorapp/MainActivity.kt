package com.vyvu.kalculatorapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.vyvu.kalculatorapp.databinding.ActivityMainBinding
import kotlin.reflect.full.memberProperties

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val calc = Calculator()
        binding.run {
            this.update(calc)
            this::class.memberProperties.forEach { m ->
                if(m.name.startsWith(BUTTON_VARIABLE_PREFIX)){
                    val button=m.getter.call(this) as MaterialButton
                    button.setOnClickListener {
                        this.update(calc.run {
                            when (button.text) {
                                getString(R.string.text_ce) -> refresh()
                                getString(R.string.text_solve) ->
                                    try {
                                        calc()
                                    } catch (e: Exception) {
                                        Toast.makeText(
                                            applicationContext,
                                            R.string.text_bad_expression,
                                            Toast.LENGTH_LONG
                                        ).show()
                                        refresh()
                                    }
                                getString(R.string.text_delete) -> del()
                                in DOUBLE_TYPE_SET -> if (infix.isEmpty()) infix += button.text.toString() else infix[infix.size - 1] += button.text.toString()
                                else -> infix += arrayOf(button.text.toString(), EMPTY_STRING)
                            }
                            this
                        })
                    }
                }
            }
        }
    }

    private fun ActivityMainBinding.update(calc: Calculator) {
        this.textExpression.text = calc.toString()
        this.textResult.text = getString(R.string.text_output, calc.result.toString())
    }

    companion object {
        const val BUTTON_VARIABLE_PREFIX = "button"
        const val DOUBLE_TYPE_SET = "0123456789."
        const val EMPTY_STRING = ""
    }
}
