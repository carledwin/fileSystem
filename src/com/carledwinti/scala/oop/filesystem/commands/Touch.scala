package com.carledwinti.scala.oop.filesystem.commands
import com.carledwinti.scala.oop.filesystem.State
import com.carledwinti.scala.oop.filesystem.files.{DirEntry, File}

class Touch(name: String) extends CreateEntry(name) {

  override def createSpecificEntry(state: State): DirEntry =
    File.empty(state.wd.path, name)
}
