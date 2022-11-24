package me.xiaox.donotdo.config

import me.xiaox.donotdo.PL
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.scheduler.BukkitTask
import java.io.IOException
import java.util.logging.Level

abstract class AbstractConfig(protected val fileName: String) {
    protected val file by lazy { PL.dataFolder.resolve(fileName) }
    protected lateinit var config: YamlConfiguration

    protected val lock = Object()
    @Volatile
    protected var saveTask: BukkitTask? = null

    open fun load() {
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

    fun save() {
        file.parentFile.mkdirs()
        try {
            config.save(file)
        } catch (e: Exception) {
            PL.logger.log(Level.WARNING, "保存配置文件`${fileName}`加载时出现异常", e)
        }
    }

    fun scheduleSave() {
        synchronized(lock) {
            if (saveTask != null) return
            saveTask = Bukkit.getScheduler().runTaskLaterAsynchronously(PL, Runnable {
                save()
                saveTask = null
            }, 10 * 60 * 1000)
        }
    }

    fun saveImmediately() {
        synchronized(lock) {
            saveTask?.cancel()
            saveTask = null
        }
        save()
    }

    operator fun get(path: String) = config.getString(path)
    fun getBoolean(path: String) = config.getBoolean(path)
    fun getStringList(path: String) = config.getStringList(path)
}