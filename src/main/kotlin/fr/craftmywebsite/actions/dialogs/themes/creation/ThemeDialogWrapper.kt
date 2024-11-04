package fr.craftmywebsite.actions.dialogs.themes.creation

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.Messages
import com.intellij.util.ui.JBUI
import fr.craftmywebsite.utils.Themes
import org.jetbrains.annotations.Nullable
import java.awt.CardLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.event.ActionEvent
import javax.swing.*

class ThemeDialogWrapper(private val project: Project) : DialogWrapper(true) {

    private val cardLayout = CardLayout()
    private val dialogPanel = JPanel(cardLayout)
    private var currentStep = 1
    private val totalSteps = 3

    // Step 1 components
    private val themeNameInput = JTextField(20)
    private val authorInput = JTextField(20)
    private val versionInput = JTextField(20)

    // Step 2 components
    private val compatiblePackagesFields = mutableListOf<JTextField>()
    private val requiredPackagesFields = mutableListOf<JTextField>()

    // Step 3 component (summary)
    private val summaryLabel = JLabel()

    // Actions
    private val previousAction = object : DialogWrapperAction("Previous") {
        override fun doAction(e: ActionEvent?) {
            if (currentStep > 1) {
                currentStep--
                updateStep()
            }
        }
    }

    private val nextAction = object : DialogWrapperAction("Next") {
        override fun doAction(e: ActionEvent?) {
            if (currentStep < totalSteps) {
                currentStep++
                updateStep()
            } else {
                getValues()
                close(OK_EXIT_CODE)
            }
        }
    }

    init {
        updateTitle()
        init()
        updateButtons()

        addFieldListeners()
        validateStepOneFields()
    }

    private fun addFieldListeners() {
        val listener = object : javax.swing.event.DocumentListener {
            override fun insertUpdate(e: javax.swing.event.DocumentEvent?) = validateStepOneFields()
            override fun removeUpdate(e: javax.swing.event.DocumentEvent?) = validateStepOneFields()
            override fun changedUpdate(e: javax.swing.event.DocumentEvent?) = validateStepOneFields()
        }

        themeNameInput.document.addDocumentListener(listener)
        authorInput.document.addDocumentListener(listener)
        versionInput.document.addDocumentListener(listener)
    }

    private fun isThemeNameExist(): Boolean {
        val themeName = themeNameInput.text
        val isExist = Themes.isThemeNameExist(themeName, project)
        if (isExist) {
            nextAction.isEnabled = false

            Messages.showErrorDialog(
                project,
                "Theme name $themeName already exists",
                "Error"
            )
        }

        return isExist
    }

    private fun validateStepOneFields() {
        val isStepOneValid =
            themeNameInput.text.isNotBlank() && authorInput.text.isNotBlank() && versionInput.text.isNotBlank()

        if (themeNameInput.text.isNotBlank() && isThemeNameExist()) {
            nextAction.isEnabled = false
            return
        }

        nextAction.isEnabled = if (currentStep == 1) isStepOneValid else true
    }

    @Nullable
    override fun createCenterPanel(): JComponent {
        setupSteps()
        return dialogPanel
    }

    private fun updateTitle() {
        title = "CMW - New Theme ($currentStep/$totalSteps)"
    }

    private fun setupSteps() {
        dialogPanel.add(createFirstStepPanel(), "Step1")
        dialogPanel.add(createSecondStepPanel(), "Step2")
        dialogPanel.add(createThirdStepPanel(), "Step3")

        // Show default step
        cardLayout.show(dialogPanel, "Step1")
    }

    private fun createFirstStepPanel(): JPanel {
        val panel = JPanel(GridBagLayout())
        val constraints = GridBagConstraints().apply {
            insets = JBUI.insets(5)
            anchor = GridBagConstraints.WEST
            fill = GridBagConstraints.HORIZONTAL
        }

        constraints.gridx = 0
        constraints.gridy = 0
        panel.add(JLabel("Theme Name:"), constraints)

        constraints.gridx = 1
        panel.add(themeNameInput, constraints)

        constraints.gridx = 0
        constraints.gridy = 1
        panel.add(JLabel("Author:"), constraints)

        constraints.gridx = 1
        panel.add(authorInput, constraints)

        constraints.gridx = 0
        constraints.gridy = 2
        panel.add(JLabel("Version:"), constraints)

        constraints.gridx = 1
        versionInput.text = "1.0.0"
        panel.add(versionInput, constraints)

        return panel
    }

    private fun createSecondStepPanel(): JPanel {
        val panel = JPanel(GridBagLayout())
        val constraints = GridBagConstraints().apply {
            insets = JBUI.insets(5)
            anchor = GridBagConstraints.WEST
            fill = GridBagConstraints.HORIZONTAL
            weightx = 1.0
        }

        // Container for compatible packages
        val compatiblePackagesPanel = JPanel()
        compatiblePackagesPanel.layout = BoxLayout(compatiblePackagesPanel, BoxLayout.Y_AXIS)

        val addCompatibleButton = JButton("Add Compatible Package").apply {
            addActionListener {
                val textField = JTextField(20).apply {
                    alignmentX = JPanel.LEFT_ALIGNMENT
                }
                compatiblePackagesPanel.add(textField)
                compatiblePackagesFields.add(textField)
                compatiblePackagesPanel.revalidate()
                compatiblePackagesPanel.repaint()
            }
        }

        // Container for required packages
        val requiredPackagesPanel = JPanel()
        requiredPackagesPanel.layout = BoxLayout(requiredPackagesPanel, BoxLayout.Y_AXIS)

        val addRequiredButton = JButton("Add Required Package").apply {
            addActionListener {
                val textField = JTextField(20).apply {
                    alignmentX = JPanel.LEFT_ALIGNMENT
                }
                requiredPackagesPanel.add(textField)
                requiredPackagesFields.add(textField)
                requiredPackagesPanel.revalidate()
                requiredPackagesPanel.repaint()
            }
        }

        // Layout for the second step panel
        constraints.gridx = 0
        constraints.gridy = 0
        panel.add(JLabel("Compatible Packages:"), constraints)
        constraints.gridy++
        panel.add(compatiblePackagesPanel, constraints)
        constraints.gridy++
        panel.add(addCompatibleButton, constraints)

        constraints.gridy++
        panel.add(JLabel("Required Packages:"), constraints)
        constraints.gridy++
        panel.add(requiredPackagesPanel, constraints)
        constraints.gridy++
        panel.add(addRequiredButton, constraints)

        return panel
    }


    private fun createThirdStepPanel(): JPanel {
        val panel = JPanel()
        panel.add(summaryLabel)
        return panel
    }

    override fun createActions(): Array<Action> {
        val cancelAction = cancelAction
        return arrayOf(previousAction, nextAction, cancelAction)
    }

    private fun updateStep() {
        cardLayout.show(dialogPanel, "Step$currentStep")
        updateButtons()
        updateTitle()

        // Update summary when on the last step
        if (currentStep == totalSteps) {
            updateSummary()
        }
    }

    private fun updateButtons() {
        previousAction.isEnabled = currentStep > 1
        nextAction.putValue(Action.NAME, if (currentStep == totalSteps) "Create" else "Next")
        validateStepOneFields()
    }

    private fun updateSummary() {
        val compatiblePackages = compatiblePackagesFields.map { it.text }.filter { it.isNotBlank() }
        val requiredPackages = requiredPackagesFields.map { it.text }.filter { it.isNotBlank() }

        val compatibleListHtml = compatiblePackages.joinToString("") { "<li>$it</li>" }
        val requiredListHtml = requiredPackages.joinToString("") { "<li>$it</li>" }

        val summaryText = """
        <html>
        <b>Package Name:</b> ${themeNameInput.text}<br>
        <b>Author:</b> ${authorInput.text}<br>
        <b>Version:</b> ${versionInput.text}<br><br>
        <b>Compatible Packages:</b>
        <ul>$compatibleListHtml</ul>
        <b>Required Packages:</b>
        <ul>$requiredListHtml</ul>
        </html>
    """.trimIndent()

        summaryLabel.text = summaryText
    }

    private fun getValues() {
        val themeName = themeNameInput.text
        val author = authorInput.text
        val version = versionInput.text
        val compatiblePackages = compatiblePackagesFields.map { it.text }.filter { it.isNotBlank() }
        val requiredPackages = requiredPackagesFields.map { it.text }.filter { it.isNotBlank() }

        val model = ThemeCreationModel(
            themeName,
            author,
            version,
            compatiblePackages,
            requiredPackages
        )

        ThemeAction.generateTheme(model, project)
    }

}