//import liveplugin.PluginUtil.registerAction
import groovy.lang.Closure
import liveplugin.*
import liveplugin.PluginUtil.*
import javax.swing.*
import panel.createPanelWithButton

val c: JComponent = createPanelWithButton(currentProjectInFrame()!!)

val cl = object : Closure<JComponent>(this) {
    fun doCall(): JComponent {
        return c
    }
}
registerToolWindow(
        "HelloToolWindow",
        null,
        cl
)
//show("Type ${pluginDisposable::class}, ${pluginDisposable::class.allSuperclasses.map { it.qualifiedName}}")
//registerToolWindow("HelloToolWindow", pluginDisposable) {
//    error("X")
//}
//registerAction(id = "Insert New Line Above", keyStroke = "ctrl alt shift ENTER") { event ->
//    val project = event.project ?: return@registerAction
//    val editor = event.editor ?: return@registerAction
//    executeCommand(project, "X") { document ->
//        val caretModel = editor.caretModel
//        val lineStartOffset = document.getLineStartOffset(caretModel.logicalPosition.line)
//        document.insertString(lineStartOffset, "\n")
//        caretModel.moveToOffset(caretModel.offset + 1)
//    }
//}
// You can run the following statement to remove the tool window
//unregisterToolWindow("helloToolWindow")

// Or you can comment out the registration code and reload the plugin.
// This will cause "pluginDisposable" to be disposed what will unregister the tool window.

//show("Current project: ${project?.name}")
