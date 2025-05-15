package com.example.monetiaapp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

data class Transferencia(
    val origen: String,
    val destino: String,
    val monto: Double,
    val descripcion: String
)

class TransferenciasActivity : AppCompatActivity() {

    private val transferencias = mutableListOf<Transferencia>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transferencias)

        val originSpinner = findViewById<Spinner>(R.id.originAccountSpinner)
        val destinationSpinner = findViewById<Spinner>(R.id.destinationAccountSpinner)
        val amountEditText = findViewById<EditText>(R.id.amountEditText)
        val descriptionEditText = findViewById<EditText>(R.id.descriptionEditText)
        val transferButton = findViewById<Button>(R.id.transferButton)
        val btnRegresar = findViewById<Button>(R.id.btnRegresarTransferencias)
        val transferHistoryListView = findViewById<ListView>(R.id.transferHistoryListView)

        val cuentas = listOf("1234567890", "9876543210", "4567890123")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, cuentas)
        originSpinner.adapter = adapter
        destinationSpinner.adapter = adapter

        val historialAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, transferencias.map {
            "De: ${it.origen} a: ${it.destino} - $${it.monto} - ${it.descripcion}"
        }.toMutableList())
        transferHistoryListView.adapter = historialAdapter

        transferButton.setOnClickListener {
            val origen = originSpinner.selectedItem.toString()
            val destino = destinationSpinner.selectedItem.toString()
            val montoStr = amountEditText.text.toString()
            val descripcion = descriptionEditText.text.toString()

            if (origen == destino) {
                Toast.makeText(this, "Las cuentas deben ser diferentes", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (montoStr.isBlank()) {
                Toast.makeText(this, "Ingresa un monto", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val monto = montoStr.toDoubleOrNull()
            if (monto == null || monto <= 0) {
                Toast.makeText(this, "Monto inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (DatosGlobales.cuentaSeleccionada == origen && monto > DatosGlobales.saldo) {
                Toast.makeText(this, "Saldo insuficiente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            AlertDialog.Builder(this)
                .setTitle("Confirmar transferencia")
                .setMessage("¿Deseas transferir $$monto de la cuenta $origen a $destino?\nConcepto: $descripcion")
                .setPositiveButton("Confirmar") { _, _ ->
                    val transferencia = Transferencia(origen, destino, monto, descripcion)
                    transferencias.add(transferencia)

                    historialAdapter.add("De: $origen a: $destino - $${String.format("%.2f", monto)} - $descripcion")

                    // Actualizar saldos y registros según cuenta seleccionada
                    when (DatosGlobales.cuentaSeleccionada) {
                        origen -> {
                            DatosGlobales.saldo -= monto
                            DatosGlobales.transacciones.add("Transferencia a $destino - $${String.format("%.2f", monto)}")
                        }
                        destino -> {
                            DatosGlobales.saldo += monto
                            DatosGlobales.transacciones.add("Transferencia de $origen - $${String.format("%.2f", monto)}")
                        }
                    }

                    Toast.makeText(this, "Transferencia realizada", Toast.LENGTH_LONG).show()
                    amountEditText.text.clear()
                    descriptionEditText.text.clear()
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        btnRegresar.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
