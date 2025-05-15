package com.example.monetiaapp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.app.NotificationCompat

class SoporteSeguridadActivity : AppCompatActivity() {

    private val CHANNEL_ID = "monetia_channel"
    private val CHANNEL_NAME = "Monetia Notifications"

    private fun solicitarPermisoNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1001)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        solicitarPermisoNotificaciones()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_soporte_seguridad)

        val supportEditText = findViewById<EditText>(R.id.supportEditText)
        val sendSupportButton = findViewById<Button>(R.id.sendSupportButton)
        val blockAccountButton = findViewById<Button>(R.id.blockAccountButton)
        val btnRegresar = findViewById<Button>(R.id.btnRegresar)

        sendSupportButton.setOnClickListener {
            val message = supportEditText.text.toString()
            if (message.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "message/rfc822"
                    putExtra(Intent.EXTRA_EMAIL, arrayOf("soporte@monetia.com"))
                    putExtra(Intent.EXTRA_SUBJECT, "Solicitud de Apoyo - Monetia App")
                    putExtra(Intent.EXTRA_TEXT, message)
                }

                try {
                    startActivity(Intent.createChooser(intent, "Enviar correo con:"))
                } catch (e: android.content.ActivityNotFoundException) {
                    Toast.makeText(this, "No hay apps de correo instaladas.", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Escribe un mensaje antes de enviar", Toast.LENGTH_SHORT).show()
            }
        }

        blockAccountButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Confirmar bloqueo")
                .setMessage("¿Estás seguro de que quieres bloquear tu cuenta?")
                .setPositiveButton("Sí") { _, _ ->
                    // Muestra el mensaje de cuenta bloqueada
                    Toast.makeText(this, "Cuenta bloqueada", Toast.LENGTH_LONG).show()

                    // Crear y mostrar la notificación
                    showNotification("Cuenta Bloqueada", "Tu cuenta ha sido bloqueada por seguridad.")
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        // Configura el botón de regresar a MainActivity
        btnRegresar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()  // Esto asegura que la actividad actual se cierre
        }

        val btnVerSucursales = findViewById<Button>(R.id.btn_ver_sucursales)
        btnVerSucursales.setOnClickListener {
            val intent = Intent(this, MapaActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showNotification(title: String, message: String) {
        // Crear el canal de notificación (requerido para versiones de Android 8.0 y superiores)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Crear la notificación
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher) // Asegúrate de tener un ícono válido
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true) // Para que se cierre al tocarla
            .build()

        // Mostrar la notificación
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notification)
    }
}
