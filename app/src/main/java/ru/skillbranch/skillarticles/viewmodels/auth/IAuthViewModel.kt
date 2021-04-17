package ru.skillbranch.skillarticles.viewmodels.auth

interface IAuthViewModel {
    /**
     * обработка авторизации пользователя
     */
    fun handleLogin(login: String, password: String, destination: Int?)
}