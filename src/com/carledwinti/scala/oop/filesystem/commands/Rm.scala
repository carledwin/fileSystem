package com.carledwinti.scala.oop.filesystem.commands

import com.carledwinti.scala.oop.filesystem.State
import com.carledwinti.scala.oop.filesystem.commands.`trait`.Command
import com.carledwinti.scala.oop.filesystem.files.Directory

import scala.annotation.tailrec

class Rm(name: String) extends Command{

  override def apply(state: State): State = {
    //1. get working dir
    val wd = state.wd

    //2. get absolute path
    val absolutePath= {
      if(name.startsWith(Directory.SEPARATOR)) name
      else if(wd.isRoot) wd.path + name
      else wd.path + Directory.SEPARATOR + name
    }

    //3. do some checks
    if(Directory.ROOT_PATH.equals(absolutePath))
      state.setMessage("Nuclear war not supported yet!")
    else
      doRum(state, absolutePath)

  }

  def doRum(state: State, path: String): State = {

    //TODO remember to implement findDescendant(String)
    /*
      /a => ["a"]
        ?
        new root without the folder a

      /a/b => ["a", "b"]
        ?
          newDirectory = /a
          rmHelper(/a, ["b"])
            ?
            new /a
         root.replace("a", new /a) = new root

    */
    def rmHelper(currentDirectory: Directory, path: List[String]): Directory = {
      if (path.isEmpty) currentDirectory
      else if (path.tail.isEmpty) currentDirectory.removeEntry(path.head)
      else {
        val nextDirectory = currentDirectory.findEntry(path.head)
        if (!nextDirectory.isDirectory)
          currentDirectory
        else {
          val newNextDirectory = rmHelper(nextDirectory.asDirectory, path.tail)
          if (newNextDirectory == newNextDirectory)
            currentDirectory
          else currentDirectory.replaceEntry(path.head, newNextDirectory)
        }
      }
    }

    //4. find the entry to remve
    //5. update structure like we do for mkdir

    val tokens = path.substring(1).split(Directory.SEPARATOR).toList
    val newRoot:Directory = rmHelper(state.root, tokens)

    if(newRoot == state.root)
      state.setMessage(path + ": no such file or directory")
    else
      State(newRoot, newRoot.findDescendant(state.wd.path.substring(1)))
  }
}
