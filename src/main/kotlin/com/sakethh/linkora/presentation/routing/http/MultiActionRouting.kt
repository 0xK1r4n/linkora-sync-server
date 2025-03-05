package com.sakethh.linkora.presentation.routing.http

import com.sakethh.linkora.Security
import com.sakethh.linkora.domain.Route
import com.sakethh.linkora.domain.dto.ArchiveMultipleItemsDTO
import com.sakethh.linkora.domain.repository.MultiActionRepo
import com.sakethh.linkora.utils.respondWithResult
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Application.multiActionRouting(multiActionRepo: MultiActionRepo) {
    routing {
        authenticate(Security.BEARER.name) {
            post<ArchiveMultipleItemsDTO>(Route.MultiAction.ARCHIVE_MULTIPLE_ITEMS.name) {
                respondWithResult(multiActionRepo.archiveMultipleItems(it))
            }
        }
    }
}