package me.alekseinovikov.store.goodsservice.domain

data class Version(val version: ULong) {

    constructor(): this(0u)

    fun next(): Version {
        return Version(version + 1u)
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    operator fun compareTo(other: Version): Int {
        return version.compareTo(other.version)
    }

}