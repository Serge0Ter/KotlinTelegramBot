package org.example

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

private const val BASE_URL = "https://api.telegram.org/bot"
private const val UPDATE_ID_KEY = "\"update_id\":"

fun main(args: Array<String>) {
    val botToken = args[0]
    var updateId = 0
    println(getMe(botToken))
    val updateIdRegex = "\"update_id\":\\s*(\\d+)".toRegex()
    val textRegex = """"text"\s*:\s*"((?:\\.|[^"\\])*)"""".toRegex()
    while (true) {
        Thread.sleep(2000)
        val updates = getUpdates(botToken, updateId)
        println(updates)
        val updateIdParsing = findGroup(updates, updateIdRegex)
        if (updateIdParsing == null) continue
        while (true) {
            val nextId = updates.indexOf(UPDATE_ID_KEY, updateIdParsing.toInt().plus(1))
            if (nextId == -1) break
            updateId = nextId
        }
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