package me.alekseinovikov.store.goodsservice.domain

data class Version(var version: ULong) {

    constructor(): this(0u)

    fun next(): Version {
        this.version++
        return Version(this.version)
    }

    fun update(version: Version) {
        this.version = version.version
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    operator fun compareTo(other: Version): Int {
        return version.compareTo(other.version)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Version

        if (version != other.version) return false

        return true
    }

}