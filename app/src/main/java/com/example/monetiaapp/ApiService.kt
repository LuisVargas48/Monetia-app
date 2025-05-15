package com.example.monetiaapp

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

// Interfaz de Retrofit
interface ApiService {
    @POST("login") // Asume que el endpoint para el login es '/login'
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>
}
