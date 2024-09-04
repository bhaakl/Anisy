plugins {
    kotlin("jvm")
    id("org.jetbrains.kotlin.plugin.serialization")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    // Javax Inject
    api("javax.inject:javax.inject:1")

    // Kotlin Coroutines
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    //Serialization
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")

    // Paging
    val paging_version = "3.3.2"
    implementation("androidx.paging:paging-common:$paging_version")

    // mapping
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
    // for test
//    testAnnotationProcessor 'org.mapstruct:mapstruct-processor:1.5.5.Final'
}