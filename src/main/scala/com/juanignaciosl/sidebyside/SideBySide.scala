package com.juanignaciosl.sidebyside

import scala.scalajs.js.Any
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import org.scalajs.jquery.JQuery
import org.scalajs.jquery.JQueryEventObject
import org.scalajs.dom.raw.KeyboardEvent
import scala.scalajs.js
import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.raw.Element
import org.scalajs.dom.raw.Node
import org.scalajs.dom.raw.Event
import org.scalajs.jquery.jQuery

object SideBySide extends JSApp with CaretLocator{
  def main(): Unit = {
  }

  @JSExport
  def sideBySide(original: JQuery, editorContainer: JQuery): Unit = {
    val highlight = jQuery(document.createElement("pre"))
    val editor = jQuery(document.createElement("pre"))
    editor.attr("contenteditable", "true")
    
    val originalContent = original.text()
    highlight.text(originalContent)
    editor.text(originalContent)
    
    def highlightText = () => { updateCursorPosition(originalContent, highlight, editor) }
    jQuery(editor).keyup(highlightText)
    jQuery(editor).on("mouseup", highlightText)

    editorContainer.append(highlight)
    editorContainer.append(editor)
  }
  
  //INFO: passing `editor` shouldn't be necessary, because `event.srcElement` should be it, but it won't work.
  // In runtime `srcElement` is `undefined`. It can be retrieved through `originalEvent` but current jQuery facade
  // doesn't implement it.
  private def updateCursorPosition(originalContent: String, original: JQuery, editor: JQuery): Unit = {
    val position = getCaretCharacterOffsetWithin(editor.get(0))
    val content = originalContent.splitAt(position)
    original.html("<span class=\"sbs-viewed\">" + content._1 + "</span>" + content._2)
  }

}

/**
 * Original JS source: 
 * - http://stackoverflow.com/questions/4811822/get-a-ranges-start-and-end-offsets-relative-to-its-parent-container/4812022#4812022
 * - http://jsfiddle.net/TjXEG/900/
 */
trait CaretLocator {
  def getCaretCharacterOffsetWithin(node: Node): Int = {
    val selection = document.getSelection()
    val range = selection.getRangeAt(0)
    val preCaretRange = range.cloneRange()
    preCaretRange.selectNodeContents(node)
    preCaretRange.setEnd(range.endContainer, range.endOffset)
    preCaretRange.toString().length()
  }

}