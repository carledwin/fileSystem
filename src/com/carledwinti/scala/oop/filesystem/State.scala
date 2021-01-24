package com.carledwinti.scala.oop.filesystem

import com.carledwinti.scala.oop.filesystem.files.Directory

class State(val root: Directory, val wd: Directory, val output: String) {

  def show: Unit = {
    println(output)
    print(State.SHELL_TOKEN)
  }
  def setMessage(message: String): State = State(root, wd, message)

}

object State {
  val SHELL_TOKEN = "/$ "
  //special method
  def apply(root: Directory, wd: Directory, output: String = ""): State = new State(root, wd, output)
}
