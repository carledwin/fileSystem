package com.carledwinti.scala.oop.filesystem.commands

import com.carledwinti.scala.oop.filesystem.State
import com.carledwinti.scala.oop.filesystem.commands.`trait`.Command
import com.carledwinti.scala.oop.filesystem.files.Directory

class Mkdir(name: String) extends Command{

  override def apply(state: State): State = {
    val wd= state.wd
    if(wd.hasEntry(name)){
      state.setMessage("Entry " + name + "already exists!")
    }else if (name.contains(Directory.SEPARATOR)){
      state.setMessage(name + " must not contain separators")
    }else if(checkIllegal(name)){
      state.setMessage(name + " : illegal entry name!")
    }else {
      doMkdir(state, name)
    }
  }//??? //Exception in thread "main" scala.NotImplementedError: an implementation is missing

  def checkIllegal(name: String): Boolean ={
    name.contains(".")
  }

  def doMkdir(state: State, str: String): State = {

    def updateStructure(currentDirectory: Directory, path: List[String], newEntry: Directory): Directory = {
      /*
        someDir
        /a
        /b
        (new) /d

        => new someDir
        /a
        /b
        /d

        OR



        (new) /a
          (new) /b (parent /a)
            /c
            /d
            /e
       */

      if(path.isEmpty) currentDirectory.addEntry(newEntry)
      else {
        /*
          /a/b
            /c
            /d
          (new) /e

        currentDirectory = /a
          path = ["b"]
        */
        val oldEntry = currentDirectory.findEntry(path.head).asDirectory
        currentDirectory.replaceEntry(oldEntry.name, updateStructure(oldEntry, path.tail, newEntry))
      }

      /*
        /a/b
          (contents)
          (new entry) /e

        //recursively
        newRoot = updateStructure(root, ["a", "b"], /e) = root.replaceEntry("a", updateStructure(/a, ["b"], /e) = /a.replaceEntry("b", updateStructure(/b, [], /e) = /b.add(/e))
          => path.isEmpty?
          => oldEntry = /a
          root.replaceEntry("a", updateStructure(/a, ["b"], /e) = /a.replaceEntry("b", updateStructure(/b, [], /e) = /b.add(/e))
            => path.isEmpty?
            => oldeEntry = /b
            /a.replaceEntry("b", updateStructure(/b, [], /e) = /b.add(/e))
              => path.isEmpty? => /b.add(/e)

       */
    }

    val wd = state.wd

    //1. all the directory in the full path
    val allDirsInPath = wd.getAllFoldersInPath

    //2. create new directory entry in the wd
    val newDir = Directory.empty(wd.path, name)

    //3. update the whole directory structure starting from the root (the directory structure is IMMUTABLE)
    val newRoot = updateStructure(state.root, allDirsInPath, newDir)

    //4. find new working directory INSTANCE given wd's full path, in the NEW  directory structure
    val newWd = newRoot.findDescendant(allDirsInPath)

    State(newRoot, newWd)
    //??? //Exception in thread "main" scala.NotImplementedError: an implementation is missing
  }
}
