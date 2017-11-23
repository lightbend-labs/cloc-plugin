package com.lightbend.tools

import org.scalatest.FunSuite

class PluginTests extends FunSuite {
  test("foo") {
    PluginScaffold.compile(new java.io.File("/Users/tisue/euler/src/main/net/tisue/euler/BigRational.scala"))
  }
}
