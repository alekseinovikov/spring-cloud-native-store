package me.alekseinovikov.store.goodsservice.domain

sealed class GoodsEvent(val id: Identifier, val version: Version) {

    class Created(id: Identifier, version: Version, val name: String, val description: String) : GoodsEvent(id, version)

    class NameChanged(id: Identifier, version: Version, val name: String) : GoodsEvent(id, version)

    class DescriptionChanged(id: Identifier, version: Version, val description: String) : GoodsEvent(id, version)

    class Deleted(id: Identifier, version: Version) : GoodsEvent(id, version)

}