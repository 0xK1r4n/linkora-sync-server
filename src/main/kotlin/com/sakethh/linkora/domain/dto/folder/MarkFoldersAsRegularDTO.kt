package com.sakethh.linkora.domain.dto.folder

import com.sakethh.linkora.domain.dto.Correlation
import kotlinx.serialization.Serializable

@Serializable
data class MarkItemsRegularDTO(
    val foldersIds: List<Long>,
    val linkIds: List<Long>,
    val eventTimestamp: Long,
    val correlation: Correlation
)