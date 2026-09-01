package com.example.ui.apps.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DesktopThemeType

@Composable
fun CalculatorApp(
    theme: DesktopThemeType
) {
    var displayValue by remember { mutableStateOf("0") }
    var expression by remember { mutableStateOf("") }
    var memoryValue by remember { mutableStateOf(0.0) }
    var isNewNumber by remember { mutableStateOf(true) }

    fun onNumber(num: String) {
        if (isNewNumber || displayValue == "0") {
            displayValue = num
            isNewNumber = false
        } else {
            displayValue += num
        }
    }

    fun onOperator(op: String) {
        expression = "$displayValue $op"
        isNewNumber = true
    }

    fun onEquals() {
        if (expression.isNotEmpty()) {
            val parts = expression.split(" ")
            if (parts.size >= 2) {
                val num1 = parts[0].toDoubleOrNull() ?: 0.0
                val op = parts[1]
                val num2 = displayValue.toDoubleOrNull() ?: 0.0
                val result = when (op) {
                    "+" -> num1 + num2
                    "-" -> num1 - num2
                    "×" -> num1 * num2
                    "÷" -> if (num2 != 0.0) num1 / num2 else Double.NaN
                    else -> num2
                }
                displayValue = if (result.isNaN()) "Error" else {
                    if (result % 1.0 == 0.0) result.toLong().toString() else result.toString()
                }
                expression = ""
                isNewNumber = true
            }
        }
    }

    fun onClear() {
        displayValue = "0"
        expression = ""
        isNewNumber = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (theme.isDark) Color(0xFF202020) else Color(0xFFF3F3F3))
            .padding(12.dp)
    ) {
        // Mode & History
        Text("Standard Calculator", color = theme.textSecondary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        
        // Expression & Display
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(text = expression, color = theme.textSecondary, fontSize = 12.sp, maxLines = 1)
            Text(
                text = displayValue,
                color = theme.textPrimary,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                textAlign = TextAlign.End
            )
        }

        // Memory Ribbon
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("MC", "MR", "M+", "M-", "MS").forEach { memBtn ->
                Text(
                    text = memBtn,
                    color = theme.textSecondary,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Calculator Buttons Grid
        val buttonRows = listOf(
            listOf("%", "CE", "C", "⌫"),
            listOf("1/x", "x²", "√x", "÷"),
            listOf("7", "8", "9", "×"),
            listOf("4", "5", "6", "-"),
            listOf("1", "2", "3", "+"),
            listOf("+/-", "0", ".", "=")
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            buttonRows.forEach { row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    row.forEach { btn ->
                        val isEquals = btn == "="
                        val isOp = btn in listOf("+", "-", "×", "÷", "=")
                        val btnBg = when {
                            isEquals -> theme.primaryAccent
                            isOp -> if (theme.isDark) Color(0xFF2E2E2E) else Color(0xFFE2E8F0)
                            else -> if (theme.isDark) Color(0xFF3B3B3B) else Color(0xFFFFFFFF)
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(6.dp))
                                .background(btnBg)
                                .clickable {
                                    when (btn) {
                                        "C", "CE" -> onClear()
                                        "⌫" -> {
                                            if (displayValue.length > 1) displayValue = displayValue.dropLast(1)
                                            else displayValue = "0"
                                        }
                                        "=", "+", "-", "×", "÷" -> {
                                            if (btn == "=") onEquals() else onOperator(btn)
                                        }
                                        "x²" -> {
                                            val n = displayValue.toDoubleOrNull() ?: 0.0
                                            displayValue = (n * n).toString()
                                        }
                                        "√x" -> {
                                            val n = displayValue.toDoubleOrNull() ?: 0.0
                                            displayValue = Math.sqrt(n).toString()
                                        }
                                        "+/-" -> {
                                            if (displayValue != "0") {
                                                displayValue = if (displayValue.startsWith("-")) displayValue.drop(1) else "-$displayValue"
                                            }
                                        }
                                        "." -> {
                                            if (!displayValue.contains(".")) displayValue += "."
                                        }
                                        else -> onNumber(btn)
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = btn,
                                color = if (isEquals) Color.White else theme.textPrimary,
                                fontSize = 14.sp,
                                fontWeight = if (isEquals || isOp) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}
