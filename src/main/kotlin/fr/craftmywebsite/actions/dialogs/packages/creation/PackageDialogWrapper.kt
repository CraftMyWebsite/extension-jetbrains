package fr.craftmywebsite.actions.dialogs.packages.creation

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.Messages
import com.intellij.util.ui.JBUI
import fr.craftmywebsite.utils.Packages
import org.jetbrains.annotations.Nullable
import java.awt.CardLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.event.ActionEvent
import javax.swing.*

class PackageDialogWrapper(private val project: Project) : DialogWrapper(true) {

    private val cardLayout = CardLayout()
    private val dialogPanel = JPanel(cardLayout)
    private var currentStep = 1
    private val totalSteps = 3

    // Step 1 components
    private val packageNameInput = JTextField(20)
    private val authorInput = JTextField(20)
    private val versionInput = JTextField(20)

    // Step 2 components
    private val controllersCheck = JCheckBox("Controllers", true)
    private val modelsCheck = JCheckBox("Models", true)
    private val entitiesCheck = JCheckBox("Entities", true)
    private val viewsCheck = JCheckBox("Views", true)
    private val publicCheck = JCheckBox("Public", true)
    private val eventsCheck = JCheckBox("Events")
    private val interfacesCheck = JCheckBox("Interfaces")
    private val exceptionsCheck = JCheckBox("Exceptions")
    private val typeCheck = JCheckBox("Type")

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

        packageNameInput.document.addDocumentListener(listener)
        authorInput.document.addDocumentListener(listener)
        versionInput.document.addDocumentListener(listener)
    }

    private fun isPackageNameExist(): Boolean {
        val packageName = packageNameInput.text
        val isExist = Packages.isPackageNameExist(packageName, project)
        if (isExist) {
            nextAction.isEnabled = false

            Messages.showErrorDialog(
                project,
                "Package name $packageName already exists",
                "Error"
            )
        }

        return isExist
    }

    private fun validateStepOneFields() {
        val isStepOneValid =
            packageNameInput.text.isNotBlank() && authorInput.text.isNotBlank() && versionInput.text.isNotBlank()

        if (packageNameInput.text.isNotBlank() && isPackageNameExist()) {
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
        title = "CMW - New Package ($currentStep/$totalSteps)"
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
        panel.add(JLabel("Package Name:"), constraints)

        constraints.gridx = 1
        panel.add(packageNameInput, constraints)

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
        }

        var row = 0
        listOf(
            controllersCheck,
            modelsCheck,
            entitiesCheck,
            viewsCheck,
            publicCheck,
            eventsCheck,
            interfacesCheck,
            exceptionsCheck,
            typeCheck
        ).forEach { checkBox ->
            constraints.gridx = 0
            constraints.gridy = row++
            panel.add(checkBox, constraints)
        }

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

    // Update the summary label with filled data
    private fun updateSummary() {
        val selectedComponents = listOf(
            "Controllers" to controllersCheck.isSelected,
            "Models" to modelsCheck.isSelected,
            "Entities" to entitiesCheck.isSelected,
            "Views" to viewsCheck.isSelected,
            "Public" to publicCheck.isSelected,
            "Events" to eventsCheck.isSelected,
            "Interfaces" to interfacesCheck.isSelected,
            "Exceptions" to exceptionsCheck.isSelected,
            "Type" to typeCheck.isSelected
        ).filter { it.second }
            .joinToString("") { "<li>${it.first}</li>" }

        val summaryText = """
        <html>
        <b>Package Name:</b> ${packageNameInput.text}<br>
        <b>Author:</b> ${authorInput.text}<br>
        <b>Version:</b> ${versionInput.text}<br><br>
        <b>Selected Components:</b>
        <ul>$selectedComponents</ul>
        </html>
    """.trimIndent()

        summaryLabel.text = summaryText
    }

    // Retrieve values from inputs and checkboxes
    private fun getValues() {
        val packageName = packageNameInput.text
        val author = authorInput.text
        val version = versionInput.text

        val selectedComponents = PackageCreationComponentsModel(
            controllers = controllersCheck.isSelected,
            models = modelsCheck.isSelected,
            entities = entitiesCheck.isSelected,
            views = viewsCheck.isSelected,
            public = publicCheck.isSelected,
            events = eventsCheck.isSelected,
            interfaces = interfacesCheck.isSelected,
            exceptions = exceptionsCheck.isSelected,
            type = typeCheck.isSelected
        )

        val data = PackageCreationModel(
            name = packageName,
            author = author,
            version = version,
            components = selectedComponents,
        )

        //Generate package
        PackageAction.generatePackage(data, project)
    }
}
