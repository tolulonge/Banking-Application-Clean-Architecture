package org.drulabs.bankbuddy.local.mapper

interface Mapper<T, E> {

    fun from(e: E): T

    fun to(t: T): E

}