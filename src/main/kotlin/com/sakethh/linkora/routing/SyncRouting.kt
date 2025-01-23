package com.sakethh.linkora.routing

import com.sakethh.linkora.Security
import com.sakethh.linkora.domain.repository.SyncRepo
import com.sakethh.linkora.domain.routes.SyncRoute
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.syncRouting(syncRepo: SyncRepo) {
    routing {
        authenticate(Security.BEARER.name) {
            get(SyncRoute.GET_TOMBSTONES.name) {
                val timestamp = getTimeStampFromParam() ?: return@get
                try {
                    call.respond(syncRepo.getTombstonesAfter(timestamp))
                } catch (e: Exception) {
                    call.respond(e.message.toString())
                }
            }

            get(SyncRoute.GET_UPDATES.name) {
                val timestamp = getTimeStampFromParam() ?: return@get
                try {
                    call.respond(syncRepo.getUpdatesAfter(timestamp))
                } catch (e: Exception) {
                    call.respond(e.message.toString())
                }
            }
        }
    }
}

private suspend fun RoutingContext.getTimeStampFromParam(): Long? {
    return try {
        this.call.parameters["timestamp"]?.toLong()
            ?: throw IllegalArgumentException("Expected a valid timestamp value, but received null.")
    } catch (e: Exception) {
        call.respond(message = e.message.toString(), status = HttpStatusCode.BadRequest)
        null
    }
}