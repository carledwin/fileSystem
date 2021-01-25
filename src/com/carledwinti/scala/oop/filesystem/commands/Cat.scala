package com.carledwinti.scala.oop.filesystem.commands

import com.carledwinti.scala.oop.filesystem.State
import com.carledwinti.scala.oop.filesystem.commands.`trait`.Command

class Cat(filename: String) extends Command{
  override def apply(state: State): State = {
    val wd = state.wd
    val dirEntry = wd.findEntry(filename)
    if(dirEntry == null || !dirEntry.isFile)
      state.setMessage(filename + ": no such file")
      else
      state.setMessage(dirEntry.asFile.contents)

  }
}
