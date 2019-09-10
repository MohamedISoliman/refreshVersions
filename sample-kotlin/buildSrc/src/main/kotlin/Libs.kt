import kotlin.String

/**
 * Generated by https://github.com/jmfayard/buildSrcVersions
 *
 * Update this file with
 *   `$ ./gradlew buildSrcVersions`
 */
object Libs {
  /**
   * https://github.com/google/guava
   */
  const val guava: String = "com.google.guava:guava:" + Versions.guava

  /**
   * https://github.com/google/guice
   */
  const val guice: String = "com.google.inject:guice:" + Versions.guice

  const val org_jetbrains_kotlin_jvm_gradle_plugin: String =
      "org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:" +
      Versions.org_jetbrains_kotlin_jvm_gradle_plugin

  /**
   * https://kotlinlang.org/
   */
  const val kotlin_scripting_compiler_embeddable: String =
      "org.jetbrains.kotlin:kotlin-scripting-compiler-embeddable:" +
      Versions.kotlin_scripting_compiler_embeddable
}
