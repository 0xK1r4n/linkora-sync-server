package com.sakethh.linkora.domain

import kotlinx.serialization.Serializable

@Serializable
data class PanelFolder(
    val id: Long,
    val folderId: Long,
    val panelPosition: Long,
    val folderName: String,
    val connectedPanelId: Long,
)
