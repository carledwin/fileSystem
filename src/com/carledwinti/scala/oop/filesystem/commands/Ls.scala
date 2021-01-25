package com.carledwinti.scala.oop.filesystem.commands

import com.carledwinti.scala.oop.filesystem.State
import com.carledwinti.scala.oop.filesystem.commands.`trait`.Command
import com.carledwinti.scala.oop.filesystem.files.DirEntry

class Ls extends Command{

  override def apply(state: State): State = {
    val contents = state.wd.contents
    val niceOutput = createNiceOutput(contents)
    state.setMessage(niceOutput)
  }

  def createNiceOutput(contents: List[DirEntry]): String = {
    if(contents.isEmpty) ""
    else {
      val entry = contents.head
      entry.name + "[" + entry.getType + "] \n" + createNiceOutput(contents.tail)
    }
  }
}
