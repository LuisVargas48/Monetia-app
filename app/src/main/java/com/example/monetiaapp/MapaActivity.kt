package com.example.monetiaapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapaActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Sucursales ficticias en Guadalajara
        val sucursales = listOf(
            LatLng(20.6736, -103.344),  // Centro
            LatLng(20.6597, -103.3496), // Chapultepec
            LatLng(20.6667, -103.3918), // La Minerva
            LatLng(20.6843, -103.3496), // Zona Expo
            LatLng(20.6710, -103.3877)  // Andares
        )

        sucursales.forEachIndexed { index, location ->
            map.addMarker(
                MarkerOptions()
                    .position(location)
                    .title("Sucursal ${index + 1}")
            )
        }

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sucursales[0], 12f))
    }
}
