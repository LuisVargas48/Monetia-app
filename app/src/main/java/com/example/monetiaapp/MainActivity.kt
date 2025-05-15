package com.example.monetiaapp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var saldoTextView: TextView
    private lateinit var transaccionesListView: ListView
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        saldoTextView = findViewById(R.id.saldoTextView)
        transaccionesListView = findViewById(R.id.transaccionesListView)

        // Mostrar saldo actual
        saldoTextView.text = "Saldo: $${String.format("%.2f", DatosGlobales.saldo)}"

        // Mostrar historial de transacciones
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, DatosGlobales.transacciones)
        transaccionesListView.adapter = adapter

        // Botón: Transferencias y Pagos
        findViewById<Button>(R.id.btnIrTransferencias).setOnClickListener {
            startActivity(Intent(this, TransferenciasActivity::class.java))
        }

        // Botón: Control de Tarjetas
        findViewById<Button>(R.id.btnControlTarjetas).setOnClickListener {
            startActivity(Intent(this, ControlTarjetasActivity::class.java))
        }

        // Botón: Soporte y Seguridad
        findViewById<Button>(R.id.btnSoporteSeguridad).setOnClickListener {
            startActivity(Intent(this, SoporteSeguridadActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        // Actualizar saldo y transacciones al volver a la pantalla principal
        saldoTextView.text = "Saldo: $${String.format("%.2f", DatosGlobales.saldo)}"
        adapter.notifyDataSetChanged()
    }
}

