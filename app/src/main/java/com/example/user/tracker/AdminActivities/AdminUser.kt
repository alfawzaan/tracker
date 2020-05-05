package com.example.user.tracker.AdminActivities

class AdminUser {
    var email: String
    var password: String
    var username: String
    var firstname: String
    var lastname: String

    constructor(email: String, password: String, username: String, firstname: String, lastname: String) {
        this.email = email
        this.password = password
        this.username = username
        this.firstname = firstname
        this.lastname = lastname
    }

    /*companion object {
        lateinit var email: String
        lateinit var password: String
    }*/
}