package com.example.user.tracker.Recycler

class UserListView{
    var fullName: String
    var username: String
    var signedIn: String
    var signedOut: String

    constructor(fullName: String, username: String, signedIn: String, signedOut: String) {
        this.fullName = fullName
        this.username = username
        this.signedIn = signedIn
        this.signedOut = signedOut
    }
}