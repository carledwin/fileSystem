package com.carledwinti.scala.oop.filesystem.commands

import com.carledwinti.scala.oop.filesystem.State
import com.carledwinti.scala.oop.filesystem.commands.`trait`.Command
import com.carledwinti.scala.oop.filesystem.files.{DirEntry, Directory}

import scala.annotation.tailrec

class Cd(dir: String) extends Command{
  override def apply(state: State): State = {
    /*
      cd /something/somethingElse/.../
      cd a/b/c/ = relative to the current working directory
      cd ..
      cd a/./.././a/
     */

    //1. find root
    val root = state.root
    val wd = state.wd

    //2. find the absolute path of the directory I want to cd to
    val absolutePath = {
      if(dir.startsWith(Directory.SEPARATOR)) dir
      else if (wd.isRoot) wd.path + dir
      else wd.path + Directory.SEPARATOR + dir
    }
    //3. find the direcotry to cd to, given the path
    val destinationDirectory = doFindEntry(root, absolutePath)


    //4. change the state given the new directory
    if(destinationDirectory == null || !destinationDirectory.isDirectory)
      state.setMessage(dir + ": no such directory")
    else
      State(root, destinationDirectory.asDirectory)
  }

  def doFindEntry(root: Directory, path: String): DirEntry = {

    @tailrec
    def findEntryHelper(currentDirectory: Directory, path: List[String]): DirEntry =
      if(path.isEmpty || path.head.isEmpty) currentDirectory
      else if(path.tail.isEmpty) currentDirectory.findEntry(path.head)
      else {
        val nextDir = currentDirectory.findEntry(path.head)
        if(nextDir == null || !nextDir.isDirectory) null
        else findEntryHelper(nextDir.asDirectory, path.tail)
      }

    @tailrec
    def collapseRelativeTokens(path: List[String], resultAccum: List[String]): List[String] = {

      /*
        /ab/b => ["a","b"]

        path.isEmpty?
         cRT(["b"], result = List :+ "a" = ["a"])
          path.isEmpty?
            cRT([], result = ["a"] :+ "b" = ["a", "b"])
              path.isEmpty?

         /a/.. => ["a",".."]
         path.isEmpty?
         cRT("..", [] :+ "a" =["a"])
          path.isEmty?
            cRT([],[]) = []

         /a/b/.. => ["a","b", ".."]
         path.isEmpty?
          cRT(["b",".."], ["a"])
            ?
            cRT(["..], ["a", "b"])
              ?
                cRT([],["b"])

         /a/b/c/..
         ...
         cRT([".."], ["a", "b","c"])
          ?
            cRT([], ["a", "b"])

       */

      if(path.isEmpty) resultAccum
      else if(".".equals(path.head)) collapseRelativeTokens(path.tail, resultAccum)
      else if("..".equals(path.head)) {
        if(resultAccum.isEmpty) null
        else collapseRelativeTokens(path.tail, resultAccum.init)
      }else collapseRelativeTokens(path.tail, resultAccum :+ path.head)
    }

    //1. tokens
    val tokens: List[String] = path.substring(1).split(Directory.SEPARATOR).toList

    //1.5 eliminate/collapse relative tokens

    /*
    ["a", "."] => ["a"]
    /a/b/./. => ["a","b",".","."] => ["a", "b"]

    /a/../ => ["a", ".."] => []
    /a/b/../ => ["a", "b", ".."] => ["a"]
     */

    val newTokens = collapseRelativeTokens(tokens, List())
    if(newTokens == null) null
    //2. navigate to the correct entry
    else findEntryHelper(root, newTokens)

  }
}
