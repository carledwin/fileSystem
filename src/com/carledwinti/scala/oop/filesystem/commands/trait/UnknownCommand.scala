package com.carledwinti.scala.oop.filesystem.commands.`trait`
import com.carledwinti.scala.oop.filesystem.State

class UnknownCommand extends Command {

  override def apply(state: State): State = state.setMessage("Command not found")
}
