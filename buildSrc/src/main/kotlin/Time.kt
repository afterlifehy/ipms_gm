import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by huy  on 2022/8/4.
 */
object Time {

    fun getDate():String{
        return SimpleDateFormat("yyyy-MM-dd").format(Date())
    }
}