package com.carledwinti.scala.oop.filesystem.commands

import com.carledwinti.scala.oop.filesystem.State
import com.carledwinti.scala.oop.filesystem.commands.`trait`.Command

class Pwd extends Command{

  override def apply(state: State): State =
    state.setMessage(state.wd.path)
}
