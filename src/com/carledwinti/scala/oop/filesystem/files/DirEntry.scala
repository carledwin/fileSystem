package com.carledwinti.scala.oop.filesystem.files

abstract class DirEntry (val parentPath: String, val name: String){

  def path: String = parentPath + Directory.SEPARATOR + name
  def asDirectory: Directory
}
