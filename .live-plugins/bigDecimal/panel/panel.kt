package panel

import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.ui.components.JBTextArea
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.Align
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.builder.text
import liveplugin.PluginUtil.show
import org.jetbrains.annotations.ApiStatus
import java.math.BigDecimal
import java.util.*
import javax.swing.JPanel

@ApiStatus.Internal
internal data class Model(
        var text: String = "Some text"
)

fun onClick(tInput: JBTextField, output: JBTextArea, panel: DialogPanel, project: Project) {
    runCatching {
        val input = BigDecimal(tInput.text)

        val typesStats = FileTypeManager.getInstance().registeredFileTypes.mapNotNull { fileType ->
            val scope = GlobalSearchScope.projectScope(project)
            val fileCount = FileTypeIndex.getFiles(fileType, scope).size
            if (fileCount == 0) {
                null
            } else {
                "${fileType.defaultExtension}: ${fileCount}"
            }
        }
        val numbers = run {
            val types = FileTypeManager.getInstance()
                    .registeredFileTypes
                    .filter { !it.isBinary }

            types.flatMap { type ->
                FileTypeIndex
                        .getFiles(type, GlobalSearchScope.projectScope(project))
                        .flatMap {
                            sequence<BigDecimal> {
                                val sc = Scanner(it.inputStream)
                                while (sc.hasNextBigDecimal()) {
                                    yield(sc.nextBigDecimal())
                                }
                                sc.close()
                            }
                        }

            }
        }
        show("Searching: ${numbers.joinToString("\n") { it.toString() }}")

        val flow = factorize(
                listOf(input),
                numbers
        )
        output.text = flow.joinToString("\n") { it.toString() }
//        liveplugin.runBackgroundTask(
//                taskTitle = "Background task",
//                canBeCancelledInUI = true,
//                task = { indicator: ProgressIndicator ->
//                    val coroutineScope = CoroutineScope(Job() + Dispatchers.IO)
//                    flow
//
//                            .onEach {
//                                output.text += it + "\n"
//                                indicator.text = it
//                            }
//                            .launchIn(coroutineScope)
//                }
//        )


//                    val selectedText = event.getData(CommonDataKeys.EDITOR)?.selectionModel?.selectedText
//                    val inactiveSelected = event.getData(CommonDataKeys.EDITOR_EVEN_IF_INACTIVE)?.selectionModel?.selectedText
//                    val selectedElement = event.getData(CommonDataKeys.NAVIGATABLE).toString()
//            output.text = """Searching: ${input}
//                        in ${numbers}
//                        result: ${result}
//                        types: ${typesStats}""".trimMargin()
        ""
    }.onFailure { exc ->
        output.text = "Invalid input ${tInput.text}:${exc.message}:${exc.stackTraceToString()}"
    }

}

fun createPanelWithButton(project: Project): JPanel {
    val model = Model()
    lateinit var panel: DialogPanel
    lateinit var tf: JBTextField
    lateinit var ta: JBTextArea
    panel = panel {
        row("Number") {
            tf = textField()
                    .align(Align.FILL)
                    .text("1.2")
                    .onChanged {
                        panel.apply()
                    }
                    .component
            button("Search") {
                onClick(tf, ta, panel, project)
                panel.apply()
            }
        }
        row("Result") {
            ta = textArea()
                    .align(Align.FILL)
                    .component
        }
    }
//    panel.registerValidators(myDisposable)
    return panel
//    val selfReproducingButton = JButton("Hello")
//    selfReproducingButton.addActionListener(object: AbstractAction() {
//        open override fun actionPerformed(x: ActionEvent){
//            createPanelWithButton(panel)
//        }
//    })
//    panel.add(selfReproducingButton)
//    panel.revalidate()
//    return panel
}
