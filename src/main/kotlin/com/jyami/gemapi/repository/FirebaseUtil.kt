package com.jyami.gemapi.repository

import com.fasterxml.jackson.module.kotlin.convertValue
import com.jyami.gemapi.utils.MapperUtil

object FirebaseUtil {
    fun performBlockOperation(operation: () -> Unit): Boolean {
        return try {
            operation()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun Any.toMap(): Map<String, Any> {
        return MapperUtil.MAPPER.convertValue<Map<String, Any>>(this)
    }
}
