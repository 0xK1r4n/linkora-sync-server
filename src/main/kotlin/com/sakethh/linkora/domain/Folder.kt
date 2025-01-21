package com.sakethh.linkora.domain

import kotlinx.serialization.Serializable

@Serializable
data class Folder(
    val id: Long,
    val name: String,
    val note: String,
    val parentFolderId: Long?,
    val isArchived: Boolean
)
