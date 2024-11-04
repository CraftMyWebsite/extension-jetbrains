package fr.craftmywebsite.actions.dialogs.themes.creation

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import fr.craftmywebsite.extensions.toPhpArray
import fr.craftmywebsite.icons.ExtensionIcons
import fr.craftmywebsite.utils.Directory
import fr.craftmywebsite.utils.Files
import fr.craftmywebsite.utils.Templates
import fr.craftmywebsite.utils.Themes

class ThemeAction : AnAction() {
    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun actionPerformed(event: AnActionEvent) {
        //Show theme dialog
        event.project?.let { ThemeDialogWrapper(it) }?.show()
    }

    override fun update(event: AnActionEvent) {
        val virtualFile = event.getData(com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE)
        event.presentation.isEnabledAndVisible =
            virtualFile != null && virtualFile.isDirectory && virtualFile.name == "Themes"
        event.presentation.icon = ExtensionIcons.action
    }

    companion object {
        fun generateTheme(model: ThemeCreationModel, project: Project) {
            val rootThemeDirectory = Themes.findThemeRootDirectory(project) ?: return;

            val themeDirectory = Directory.createDirectory(rootThemeDirectory, model.name) ?: return

            //Create theme init files
            createThemeInitFiles(themeDirectory, model)
        }

        private fun createThemeInitFiles(
            themeDirectory: PsiDirectory,
            model: ThemeCreationModel,
        ) {
            //Theme.php file
            val templateTheme = Templates.getTemplateFile("/templates/themes/files/theme.php.template")
                .replace("\${themeName}", model.name)
                .replace("\${themeAuthor}", model.author)
                .replace("\${themeVersion}", model.version)
                .replace("\${themeCompatiblesPackages}", model.compatiblePackages.toPhpArray())
                .replace("\${themeRequiredPackages}", model.requiredPackages.toPhpArray())

            Files.createFile(themeDirectory, "Theme.php", templateTheme)

            //router.php file
            Templates.createTemplateFile(
                "/templates/themes/files/router.php.template",
                themeDirectory,
            )

            //Create config directory
            val configDir = Directory.createDirectory(themeDirectory, "Config") ?: return

            //Create config file
            val templateConfig = Templates.getTemplateFile(
                "/templates/themes/files/config/config.php.template",
            )
            Files.createFile(configDir, "config.php", templateConfig)

            //Create config settings file
            val templateConfigSettings = Templates.getTemplateFile(
                "/templates/themes/files/config/config.settings.php.template",
            )
            Files.createFile(configDir, "config.settings.php", templateConfigSettings)

            //Create assets directory
            val assetsDir = Directory.createDirectory(themeDirectory, "Assets") ?: return
            val assetsCssDir = Directory.createDirectory(assetsDir, "Css") ?: return

            val templateCss = Templates.getTemplateFile(
                "/templates/themes/files/assets/css/bootstrap.css.template",
            )
            Files.createFile(assetsCssDir, "bootstrap.css", templateCss)

            //Create views directory
            val viewsDir = Directory.createDirectory(themeDirectory, "Views") ?: return

            //Create core files
            createCoreFiles(viewsDir, model)
        }

        private fun createCoreFiles(viewsDir: PsiDirectory, model: ThemeCreationModel) {
            val coreDir = Directory.createDirectory(viewsDir, "Core") ?: return

            //Create alerts files
            createCoreAlertsFiles(coreDir)

            //Create Core files
            listOf("cgu", "cgv", "home", "maintenance").forEach { file ->
                Templates.createTemplateFile(
                    "/templates/themes/files/views/core/$file.view.php.template",
                    coreDir,
                )
            }

            //Create errors files
            createErrorsFiles(viewsDir)

            //Create router demo view file
            val coreRouterViewDir = Directory.createDirectory(coreDir, "Router") ?: return
            Templates.createTemplateFile(
                "/templates/themes/files/views/core/router/demo.view.php.template",
                coreRouterViewDir,
                "demo.view.php"
            )

            //Create includes files
            createIncludesFiles(viewsDir, model.name)

            //Create template file
            Templates.createTemplateFile(
                "/templates/themes/files/views/template.php.template",
                viewsDir,
            )
        }

        private fun createCoreAlertsFiles(viewsDir: PsiDirectory) {
            val alertDir = Directory.createDirectory(viewsDir, "Alerts") ?: return

            listOf("error", "info", "success", "warning").forEach { type ->
                Templates.createTemplateFile(
                    "/templates/themes/files/views/core/alerts/$type.view.php.template",
                    alertDir,
                )
            }
        }

        private fun createErrorsFiles(viewsDir: PsiDirectory) {
            val errorsDir = Directory.createDirectory(viewsDir, "Errors") ?: return

            //Create errors files
            listOf("default", "404").forEach { type ->
                Templates.createTemplateFile(
                    "/templates/themes/files/views/errors/$type.view.php.template",
                    errorsDir,
                )
            }
        }

        private fun createIncludesFiles(viewsDir: PsiDirectory, themeName: String) {
            val includesDir = Directory.createDirectory(viewsDir, "Includes") ?: return

            //Create includes files
            listOf("footer", "header").forEach { type ->
                Templates.createTemplateFile(
                    "/templates/themes/files/views/includes/$type.inc.php.template",
                    includesDir,
                )
            }

            val templateHead = Templates.getTemplateFile(
                "/templates/themes/files/views/includes/head.inc.php.template",
            )
                .replace("\${themeName}", themeName)

            Files.createFile(includesDir, "head.inc.php", templateHead)
        }
    }
}