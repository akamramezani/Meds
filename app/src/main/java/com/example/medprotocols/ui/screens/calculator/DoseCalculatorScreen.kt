package com.example.medprotocols.ui.screens.calculator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.medprotocols.R

// Deliberately generic: a weight x dose-per-kg x frequency calculator with no
// drug/dose data baked in. That keeps it correct regardless of what content
// you load, and avoids shipping unverified dosing figures as if they were
// a vetted reference.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoseCalculatorScreen() {
    var weight by remember { mutableStateOf("") }
    var dosePerKg by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf("1") }

    val weightVal = weight.toDoubleOrNull()
    val doseVal = dosePerKg.toDoubleOrNull()
    val freqVal = frequency.toIntOrNull()
    val perDose = if (weightVal != null && doseVal != null) weightVal * doseVal else null
    val dailyTotal = if (perDose != null && freqVal != null) perDose * freqVal else null

    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text(stringResource(R.string.dose_calculator), style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        Text(
            stringResource(R.string.calculator_disclaimer),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.Medium
        )
        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = weight, onValueChange = { weight = it },
            label = { Text(stringResource(R.string.weight_kg)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = dosePerKg, onValueChange = { dosePerKg = it },
            label = { Text(stringResource(R.string.dose_per_kg)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = frequency, onValueChange = { frequency = it },
            label = { Text(stringResource(R.string.frequency_per_day)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(24.dp))

        if (perDose != null) {
            Text("${stringResource(R.string.calculated_dose)}: ${"%.1f".format(perDose)} mg", style = MaterialTheme.typography.titleMedium)
        }
        if (dailyTotal != null) {
            Spacer(Modifier.height(4.dp))
            Text("${stringResource(R.string.daily_total)}: ${"%.1f".format(dailyTotal)} mg", style = MaterialTheme.typography.titleMedium)
        }
    }
}
