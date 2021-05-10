package com.vahitkeskin.kotlinwhatsappclone.model

class MessageModel {
    var uId: String? = null
    var message: String? = null
    var timestamp: Long? = 0
    var messageId: String? = null

    constructor(
        uId: String?,
        message: String?,
        timestamp: Long?,
        messageId: String?
    ) {
        this.uId = uId
        this.message = message
        this.timestamp = timestamp
        this.messageId = messageId
    }

    constructor(
        uId: String?,
        message: String?
    ) {
        this.uId = uId
        this.message = message
    }

    constructor() {}
}