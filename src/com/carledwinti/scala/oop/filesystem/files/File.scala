package com.carledwinti.scala.oop.filesystem.files

import com.carledwinti.scala.oop.filesystem.exception.FileSystemException

class File (override val parentPath: String, override val name: String, contents: String) extends DirEntry(parentPath , name) {

  override def asDirectory: Directory =
    throw new FileSystemException("A file cannot be converted to a directory!")

  def getType: String = "File"

  def asFile: File = this
}

object File {
  def empty(parentPath: String, name: String): File =
    new File(parentPath, name, "")
}
