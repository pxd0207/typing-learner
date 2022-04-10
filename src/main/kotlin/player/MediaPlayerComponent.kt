package player

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import com.sun.jna.NativeLibrary
import state.getResourcesFile
import uk.co.caprica.vlcj.binding.RuntimeUtil
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery
import uk.co.caprica.vlcj.player.base.MediaPlayer
import uk.co.caprica.vlcj.player.component.CallbackMediaPlayerComponent
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent
import java.awt.Component
import java.util.*


val LocalMediaPlayerComponent = staticCompositionLocalOf<Component> {
    error("LocalMediaPlayerComponent isn't provided")
}

@Composable
fun rememberMediaPlayerComponent():Component = remember {

    val os = System.getProperty("os.name", "generic").lowercase(Locale.ENGLISH)
    if (os.indexOf("windows") >= 0) {
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), getResourcesFile("VLC")?.absolutePath ?: "地址错误")
    }else{
       NativeDiscovery().discover()
    }

    if (isMacOS()) {
        CallbackMediaPlayerComponent()
    } else {
         EmbeddedMediaPlayerComponent()
    }
}

fun isMacOS(): Boolean {
    val os = System.getProperty("os.name", "generic").lowercase(Locale.ENGLISH)
    return os.indexOf("mac") >= 0 || os.indexOf("darwin") >= 0
}
fun isWindows(): Boolean {
    val os = System.getProperty("os.name", "generic").lowercase(Locale.ENGLISH)
    return os.indexOf("windows") >= 0
}

fun Component.mediaPlayer(): MediaPlayer {
    return when (this) {
        is CallbackMediaPlayerComponent -> mediaPlayer()
        is EmbeddedMediaPlayerComponent -> mediaPlayer()
        else -> throw IllegalArgumentException("You can only call mediaPlayer() on vlcj player component")
    }
}