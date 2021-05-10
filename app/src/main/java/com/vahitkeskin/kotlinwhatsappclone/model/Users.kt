package com.vahitkeskin.kotlinwhatsappclone.model

class Users {
    var profilepic: String? = null
    var userName: String? = null
    var mail: String? = null
    var password: String? = null
    var userId: String? = ""
    var lastMessage: String? = null
    var status: String? = null

    constructor(
        profilePic: String?,
        userName: String?,
        mail: String?,
        password: String?,
        userId: String?,
        lastMessage: String?,
        status: String?
    ) {
        this.profilepic = profilePic
        this.userName = userName
        this.mail = mail
        this.password = password
        this.userId = userId
        this.lastMessage = lastMessage
        this.status = status
    }

    constructor() {}
    constructor(userName: String?, mail: String?, password: String?) {
        this.userName = userName
        this.mail = mail
        this.password = password
    }
}