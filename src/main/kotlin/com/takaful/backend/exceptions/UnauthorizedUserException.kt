package com.takaful.backend.exceptions


class UnauthorizedUserException constructor(override val message: String?) : Exception()