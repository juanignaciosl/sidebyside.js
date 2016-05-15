package com.juanignaciosl.sidebyside

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import org.scalajs.dom.document
import org.scalajs.dom.raw.Node
import org.scalajs.jquery.jQuery
import org.scalajs.jquery.JQuery

object SideBySide extends JSApp with CaretLocator {
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
    val range = document.getSelection().getRangeAt(0)
    val previous = originalContent.splitAt(range.startOffset)
    val next = previous._2.splitAt(range.endOffset - range.startOffset)
    original.html("<span class=\"sbs-viewed\">" + previous._1 + "</span>" + "<span class=\"sbs-selected\">" + next._1 + "</span>" + next._2)
  }

}

/**
 * Original JS source:
 * - http://stackoverflow.com/questions/4811822/get-a-ranges-start-and-end-offsets-relative-to-its-parent-container/4812022#4812022
 * - http://jsfiddle.net/TjXEG/900/
 *
 * This is not actually needed for plain text.
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