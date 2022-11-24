package me.xiaox.donotdo.config

import me.xiaox.donotdo.PL
import org.bukkit.configuration.file.YamlConfiguration
import java.io.IOException
import java.util.logging.Level

object GameData : AbstractConfig("data.yml") {
    override fun load() {
        if (file.isDirectory) file.deleteRecursively()
        if (!file.exists()) {
            file.parentFile.mkdirs()
            PL.getResource(fileName)!!.use { input ->
                file.outputStream().use { out ->
                    input.copyTo(out)
                }
            }
            PL.logger.info("保存默认`$fileName`")
        }
        val cfg = YamlConfiguration()
        try {
            cfg.load(file)
            config = cfg
        } catch (e: IOException) {
            PL.logger.log(Level.WARNING, "加载配置文件`${fileName}`yaml语法错误, 请检查配置文件", e)
        } catch (e: Exception) {
            PL.logger.log(Level.WARNING, "加载配置文件`${fileName}`加载时出现异常", e)
        }
    }
}