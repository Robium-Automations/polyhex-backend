package com.robiumautomations.polyhex.models.dtos.files

import com.robiumautomations.polyhex.enums.DataType

class CreateFileResponseDto(
    val materialId: String,
    val fileName: String,
    val dataType: DataType,
    val fileSize: Long?,
    val authorId: String,
    val creationDate: Long
)