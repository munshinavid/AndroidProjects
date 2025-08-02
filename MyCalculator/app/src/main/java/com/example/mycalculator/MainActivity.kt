package com.example.mycalculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {

    private var tvInput:TextView?=null

    var lastNumeric=false
    var lastDot=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        tvInput=findViewById(R.id.txtView)
    }

    fun onDigit(view: View) {
        val button = view as TextView
        val value = button.text  // Now you can get the text on the button (like "1", "2", "+", etc.)
        tvInput?.append(value)
        lastDot=false
        lastNumeric=true
    }

    fun onClear(view: View)
    {
        tvInput?.text=""
    }

    fun onDot(view: View)
    {
        if(lastNumeric && !lastDot)
        {
            tvInput?.append(".")
            lastDot=true
            lastNumeric=false
        }

    }

    fun onOperator(view: View)
    {
        var button=view as Button
        var operator= button.text.toString()
        var currentInput=tvInput?.text.toString()

        if(currentInput.isEmpty())
        {
            if (operator=="-")
            {
                tvInput?.append(operator)
            }
            //otherwise return
            return
        }

        var lastChar=currentInput.last()
        if(lastChar == '+' || lastChar=='-'
            || lastChar=='*'
            || lastChar=='/')
        {
            return
            //prevent
        }

        tvInput?.append(operator)

    }
    fun onOperator1(view: View) {
        val button = view as Button
        val operator = button.text.toString()
        val input = tvInput?.text.toString()

        // Allow minus at the start
        if (input.isEmpty()) {
            if (operator == "-") {
                tvInput?.append(operator)
            }
            return
        }

        // Count operators manually (excluding first char if it's '-')
        var operatorCount = 0
        var startIndex = 0

        if (input[0] == '-') {
            startIndex = 1  // skip first minus
        }

        for (i in startIndex until input.length) {
            val ch = input[i]
            if (ch == '+' || ch == '-' || ch == '×' || ch == '÷') {
                operatorCount++
            }
        }

        // If already has one operator, don't allow more
        if (operatorCount >= 1) {
            return
        } else {
            tvInput?.append(operator)
        }
    }

    fun evaluateExpression(input: String): Double {
        //val expression = input.replace("×", "*").replace("÷", "/")
        return ExpressionBuilder(input).build().evaluate()
    }
    fun onEqual1(view: View) {
        val input = tvInput?.text.toString()
        try {
            val result = evaluateExpression(input)
            tvInput?.text = result.toString()
        } catch (e: Exception) {
            tvInput?.text = "Error"
        }
    }

    fun onEqual(view: View) {
        val input = tvInput?.text.toString()

        if (input.isEmpty()) return

        var operatorIndex = -1
        var operator: Char? = null

        // Start from index 1 if input starts with minus (to avoid treating it as an operator)
        val startIndex = if (input[0] == '-') 1 else 0

        // Find the first operator in the string (after optional leading minus)
        for (i in startIndex until input.length) {
            val ch = input[i]
            if (ch == '+' || ch == '-' || ch == '×' || ch == '÷') {
                operatorIndex = i
                operator = ch
                break
            }
        }

        if (operatorIndex == -1) return  // no operator found

        // Split operands
        val first = input.substring(0, operatorIndex).toDoubleOrNull()
        val second = input.substring(operatorIndex + 1).toDoubleOrNull()

        if (first == null || second == null) {
            tvInput?.text = "Error"
            return
        }

        // Perform the calculation
        val result = when (operator) {
            '+' -> first + second
            '-' -> first - second
            '*' -> first * second
            '/' -> if (second != 0.0) first / second else {
                tvInput?.text = "Div/0"
                return
            }
            else -> {
                tvInput?.text = "Error"
                return
            }
        }

        tvInput?.text = result.toString()
    }




}