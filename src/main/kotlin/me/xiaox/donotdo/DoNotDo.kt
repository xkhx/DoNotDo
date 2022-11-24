package me.xiaox.donotdo

import me.xiaox.donotdo.config.DndConfig
import me.xiaox.donotdo.config.GameConfig
import me.xiaox.donotdo.config.Lang
import org.bukkit.plugin.java.JavaPlugin
class DoNotDo : JavaPlugin() {

    companion object {
        @JvmStatic
        lateinit var INSTANCE: DoNotDo
            private set
    }

    override fun onEnable() {
        INSTANCE = this
        GameConfig.load()
        DndConfig.load()
        Lang.load()
        logger.info("插件启用完成")
    }
}

val PL: JavaPlugin
    get() = DoNotDo.INSTANCE