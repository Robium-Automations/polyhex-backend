package com.robiumautomations.polyhex.dtos.studygroups

import com.robiumautomations.polyhex.models.UserId

class ManageGroupsRequestDto(
    val action: String? = null,
    val userId: UserId? = null
)