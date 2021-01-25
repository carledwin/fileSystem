package com.carledwinti.scala.oop.filesystem.commands

import com.carledwinti.scala.oop.filesystem.State
import com.carledwinti.scala.oop.filesystem.commands.`trait`.Command

import scala.annotation.tailrec

class Echo(args: Array[String]) extends Command{
  override def apply(state: State): State = {
    /*
      if no args, state
      else if just one arg, print to console
      ele if multiple args {
      operator = nex to last argument
      if > to a file (may create a file if not there)
      if >>
      append to a file
      else
      just echo everything to console
     */



    if(args.isEmpty) state
    else if(args.length ==1) state.setMessage(args(0))
    else{
      val operator = args(args.length - 2)
      val filename = args(args.length -1)
      val contents = createContent(args, args.length - 2)

      if(">>".equals(operator))
        doEcho(state, contents, filename, append = true)
      else if(">".equals(operator))
        doEcho(state, contents, filename, append = false)
      else
        state.setMessage(createContent(args, args.length))
    }
  }

  def doEcho(state: State, contents: String, filename: String, append: Boolean) = {
    ???
  }

  //topIndex NON INCLUSIVE!
  def createContent(args: Array[String], topIndex: Int): String = {
    @tailrec
    def createContentHelper(currentIndex: Int, accumulator: String): String = {
      if(currentIndex >= topIndex) accumulator
      else createContentHelper(currentIndex + 1, accumulator + " " + args(currentIndex))
    }

    createContentHelper(0, "")
  }
}