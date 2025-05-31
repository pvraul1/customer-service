package com.rperezv365.customerportfolio.exception

import java.lang.RuntimeException

class CustomerNotFoundException(id: String) : RuntimeException(
    "Customer [id=$id] is not found"
)
