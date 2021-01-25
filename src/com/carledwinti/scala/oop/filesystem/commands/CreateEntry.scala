package com.carledwinti.scala.oop.filesystem.commands

import com.carledwinti.scala.oop.filesystem.State
import com.carledwinti.scala.oop.filesystem.commands.`trait`.Command
import com.carledwinti.scala.oop.filesystem.files.{DirEntry, Directory}

abstract class CreateEntry(name: String) extends Command{

  override def apply(state: State): State = {
    val wd= state.wd
    if(wd.hasEntry(name)){
      state.setMessage("Entry " + name + "already exists!")
    }else if (name.contains(Directory.SEPARATOR)){
      state.setMessage(name + " must not contain separators")
    }else if(checkIllegal(name)){
      state.setMessage(name + " : illegal entry name!")
    }else {
      doCreate(state, name)
    }
  }//??? //Exception in thread "main" scala.NotImplementedError: an implementation is missing

  def checkIllegal(name: String): Boolean ={
    name.contains(".")
  }

  def doCreate(state: State, str: String): State = {

    def updateStructure(currentDirectory: Directory, path: List[String], newEntry: DirEntry): Directory = {

      if(path.isEmpty) currentDirectory.addEntry(newEntry)
      else {

        val oldEntry = currentDirectory.findEntry(path.head).asDirectory
        currentDirectory.replaceEntry(oldEntry.name, updateStructure(oldEntry, path.tail, newEntry))
      }
    }

    val wd = state.wd

    //1. all the directory in the full path
    val allDirsInPath = wd.getAllFoldersInPath

    //2. create new directory entry in the wd
    //val newDir = Directory.empty(wd.path, name)
    //TODO implement this
    val newEntry: DirEntry = createSpecificEntry(state)

    //3. update the whole directory structure starting from the root (the directory structure is IMMUTABLE)
    val newRoot = updateStructure(state.root, allDirsInPath, newEntry)

    //4. find new working directory INSTANCE given wd's full path, in the NEW  directory structure
    val newWd = newRoot.findDescendant(allDirsInPath)

    State(newRoot, newWd)
    //??? //Exception in thread "main" scala.NotImplementedError: an implementation is missing
  }

  def createSpecificEntry(state: State): DirEntry
}
