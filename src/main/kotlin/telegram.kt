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
    while (true) {
        Thread.sleep(2000)
        val updates = getUpdates(botToken, updateId)
        println(updates)
        val startUpdateId = updates.indexOf(UPDATE_ID_KEY)
        if (startUpdateId == -1) continue
        val endUpdateId = updates.indexOf(",", startUpdateId)
        if (endUpdateId == -1) continue
        val updateIdString = updates.substring(startUpdateId + UPDATE_ID_KEY.length, endUpdateId)
        updateId = updateIdString.toInt() + 1
    }

}

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