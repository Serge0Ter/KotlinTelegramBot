package org.example

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

private const val BASE_URL = "https://api.telegram.org/bot"

fun main(args: Array<String>) {
    val botToken = args[0]
    var updateId = 0
    println(getMe(botToken))
    val updateIdRegex = ".*\"update_id\":\\s*(\\d+)".toRegex()
    val textRegex = "\"text\":\"(.+?)\"".toRegex()
    while (true) {
        Thread.sleep(2000)
        val updates = getUpdates(botToken, updateId)
        println(updates)
        val updateIdParsing = findGroup(updates, updateIdRegex)
        if (updateIdParsing == null) continue
        updateId = updateIdParsing.toInt() + 1
        val textParsing = findGroup(updates, textRegex)
        if (textParsing == null) continue
        println("updateIdParsing:  $updateIdParsing")
        println("textParsing:  $textParsing")
    }
}

fun findGroup(updates: String, regex: Regex): String? =
    regex.find(updates)?.groups[1]?.value

fun getUpdates(botToken: String, updateId: Int): String {
    val urlGetUpdates = "$BASE_URL$botToken/getUpdates?offset=$updateId"
    val client: HttpClient = HttpClient.newBuilder().build()
    val request: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlGetUpdates)).build()
    val response: HttpResponse<String> = client.send(request, HttpResponse.BodyHandlers.ofString())
    return response.body()
}

fun getMe(botToken: String): String {
    val urlGetMe = "$BASE_URL$botToken/getMe"
    val client: HttpClient = HttpClient.newBuilder().build()
    val request: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlGetMe)).build()
    val response: HttpResponse<String> = client.send(request, HttpResponse.BodyHandlers.ofString())
    return response.body()
}