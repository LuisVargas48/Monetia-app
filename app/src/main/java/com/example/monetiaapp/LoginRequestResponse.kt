package com.example.monetiaapp

// Esta clase representará los datos que enviamos al Web Service.
data class LoginRequest(val username: String, val password: String)

// Esta clase representará la respuesta que recibimos del Web Service.
data class LoginResponse(val success: Boolean, val message: String)
