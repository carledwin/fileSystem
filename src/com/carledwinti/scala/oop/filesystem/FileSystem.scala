package com.carledwinti.scala.oop.filesystem

import com.carledwinti.scala.oop.filesystem.commands.`trait`.Command
import com.carledwinti.scala.oop.filesystem.files.Directory

import java.util.Scanner

object FileSystem extends App {

  val scanner = new Scanner(System.in)
  val root = Directory.ROOT
  var state = State(root, root)

  while(true){
    state.show
    val input = scanner.nextLine()
    state = Command.from(input).apply(state)
  }

}
