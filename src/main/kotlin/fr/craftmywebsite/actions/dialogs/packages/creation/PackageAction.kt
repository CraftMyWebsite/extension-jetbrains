package fr.craftmywebsite.actions.dialogs.packages.creation

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import fr.craftmywebsite.actions.dialogs.packages.files.admin_view.AdminView
import fr.craftmywebsite.actions.dialogs.packages.files.controller.Controller
import fr.craftmywebsite.actions.dialogs.packages.files.entity.Entity
import fr.craftmywebsite.actions.dialogs.packages.files.event.Event
import fr.craftmywebsite.actions.dialogs.packages.files.exception.Exception
import fr.craftmywebsite.actions.dialogs.packages.files.interfaces.Interface
import fr.craftmywebsite.actions.dialogs.packages.files.publics.Public
import fr.craftmywebsite.actions.dialogs.packages.files.type.Type
import fr.craftmywebsite.icons.ExtensionIcons
import fr.craftmywebsite.utils.Directory
import fr.craftmywebsite.utils.Files
import fr.craftmywebsite.utils.Packages
import fr.craftmywebsite.utils.Templates

class PackageAction : AnAction() {
    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun actionPerformed(event: AnActionEvent) {
        //Show package dialog
        event.project?.let { PackageDialogWrapper(it) }?.show()
    }

    override fun update(event: AnActionEvent) {
        val virtualFile = event.getData(com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE)
        event.presentation.isEnabledAndVisible =
            virtualFile != null && virtualFile.isDirectory && virtualFile.name == "Package"
        event.presentation.icon = ExtensionIcons.action
    }

    companion object {
        fun generatePackage(model: PackageCreationModel, project: Project) {

            //Create package directory
            val rootPackageDirectory = Packages.findPackageRootDirectory(project) ?: return;

            val packageDirectory = Directory.createDirectory(rootPackageDirectory, model.name) ?: return

            //Create package init files
            createPackageInitFiles(packageDirectory, model)

            //Create package elements
            generateElements(packageDirectory, model)
        }

        private fun createPackageInitFiles(
            packageDirectory: PsiDirectory,
            model: PackageCreationModel
        ) {
            // Package.php file
            val templatePackage = Templates.getTemplateFile("/templates/package/files/package.php.template")
                .replace("\${packageName}", model.name)
                .replace("\${packageNameLower}", model.name.lowercase())
                .replace("\${packageVersion}", model.version)
                .replace("\${packageAuthor}", model.author)

            Files.createFile(packageDirectory, "Package.php", templatePackage)

            //Generate lang files
            val langDirectory = Directory.createDirectory(packageDirectory, "Lang") ?: return

            val templateLang = Templates.getTemplateFile("/templates/package/files/lang.php.template")
                .replace("\${packageName}", model.name)
                .replace("\${packageAuthor}", model.author)

            Files.createFile(langDirectory, "en.php", templateLang)
            Files.createFile(langDirectory, "fr.php", templateLang)

            // Folder /Init/ files
            val initDirectory = Directory.createDirectory(packageDirectory, "Init") ?: return

            //init.sql file
            val templateInitSql = Templates.getTemplateFile("/templates/package/files/init/init.sql.template")
                .replace("\${packageNameLower}", model.name.lowercase())

            Files.createFile(initDirectory, "init.sql", templateInitSql)

            //uninstall.sql file
            val templateUninstallSql = Templates.getTemplateFile("/templates/package/files/init/uninstall.sql.template")
                .replace("\${packageNameLower}", model.name.lowercase())

            Files.createFile(initDirectory, "uninstall.sql", templateUninstallSql)

            //Permissions.php file
            val templatePermissions =
                Templates.getTemplateFile("/templates/package/files/init/permissions.php.template")
                    .replace("\${packageName}", model.name)
                    .replace("\${packageNameLower}", model.name.lowercase())

            Files.createFile(initDirectory, "Permissions.php", templatePermissions)
        }

        private fun generateElements(
            packageDirectory: PsiDirectory,
            model: PackageCreationModel
        ) {
            val packageName = model.name
            val packageNameLower = model.name.lowercase()
            val components = model.components

            listOf(
                "Views" to components.views,
                "Controllers" to components.controllers,
                "Entities" to components.entities,
                "Events" to components.events,
                "Exceptions" to components.exceptions,
                "Interfaces" to components.interfaces,
                "Models" to components.models,
                "Type" to components.type,
                "Public" to components.public
            ).forEach { (folderName, shouldCreate) ->
                if (shouldCreate) {
                    val folder = Directory.createDirectory(packageDirectory, folderName) ?: return
                    when (folderName) {
                        "Views" -> AdminView.generate(folder, "main")
                        "Controllers" -> Controller.generate(folder, packageName)
                        "Entities" -> Entity.generate(folder, packageName)
                        "Events" -> Event.generate(folder, packageName)
                        "Exceptions" -> Exception.generate(folder, packageName)
                        "Interfaces" -> Interface.generate(folder, packageName)
                        "Models" -> Interface.generate(folder, packageName)
                        "Type" -> Type.generate(folder, packageName)
                        "Public" -> Public.generate(folder, "main")
                    }
                }
            }
        }
    }
}