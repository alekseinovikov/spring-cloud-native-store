package me.alekseinovikov.store.goodsservice

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.annotation.AliasFor
import org.springframework.test.context.ActiveProfiles

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@SpringBootTest
@ActiveProfiles
annotation class IntegrationTest(
    @get:AliasFor(
        annotation = ActiveProfiles::class,
        attribute = "profiles"
    ) val activeProfiles: Array<String> = ["test"]
)