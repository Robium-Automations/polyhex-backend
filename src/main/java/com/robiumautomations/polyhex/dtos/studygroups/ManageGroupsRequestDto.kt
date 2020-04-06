package com.robiumautomations.polyhex.dtos.studygroups

import com.robiumautomations.polyhex.models.UserId

class ManageGroupsRequestDto(
    // [ManageGroupAction]
    val action: String? = null,
    val userId: UserId? = null
)