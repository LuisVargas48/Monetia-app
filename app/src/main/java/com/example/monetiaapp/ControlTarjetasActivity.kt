package com.example.monetiaapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class ControlTarjetasActivity : AppCompatActivity() {

    private var tarjetaActiva = true
    private var tarjetaBloqueada = false
    private var limiteGasto = 10000.0  // valor inicial simulado

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control_tarjetas)

        val toggleCardButton = findViewById<Button>(R.id.toggleCardButton1)
        val blockCardButton = findViewById<Button>(R.id.blockCardButton1)
        val cardLimitEditText = findViewById<EditText>(R.id.cardLimitEditText1)
        val btnRegresar = findViewById<Button>(R.id.btnRegresarControlTarjetas)

        cardLimitEditText.setText(String.format("%.2f", limiteGasto))

        toggleCardButton.setOnClickListener {
            if (tarjetaBloqueada) {
                Toast.makeText(this, "La tarjeta está bloqueada", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            tarjetaActiva = !tarjetaActiva
            val estado = if (tarjetaActiva) "activada" else "desactivada"
            toggleCardButton.text = if (tarjetaActiva) "Desactivar Tarjeta" else "Activar Tarjeta"
            Toast.makeText(this, "Tarjeta $estado", Toast.LENGTH_SHORT).show()
        }

        blockCardButton.setOnClickListener {
            if (tarjetaBloqueada) {
                Toast.makeText(this, "La tarjeta ya está bloqueada", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            AlertDialog.Builder(this)
                .setTitle("Bloquear tarjeta")
                .setMessage("¿Estás seguro de bloquear esta tarjeta? Esta acción no se puede deshacer.")
                .setPositiveButton("Sí") { _, _ ->
                    tarjetaBloqueada = true
                    tarjetaActiva = false
                    toggleCardButton.isEnabled = false
                    blockCardButton.isEnabled = false
                    cardLimitEditText.isEnabled = false
                    Toast.makeText(this, "Tarjeta bloqueada permanentemente", Toast.LENGTH_LONG).show()
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        cardLimitEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && !tarjetaBloqueada) {
                val nuevoLimite = cardLimitEditText.text.toString().toDoubleOrNull()
                if (nuevoLimite != null && nuevoLimite > 0) {
                    limiteGasto = nuevoLimite
                    Toast.makeText(this, "Límite actualizado a $$limiteGasto", Toast.LENGTH_SHORT).show()
                } else {
                    cardLimitEditText.setText(String.format("%.2f", limiteGasto))
                    Toast.makeText(this, "Límite inválido", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnRegresar.setOnClickListener {
            finish()
        }
    }
}
