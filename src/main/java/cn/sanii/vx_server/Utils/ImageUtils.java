package cn.sanii.vx_server.Utils;

/**
 * @author: shouliang.wang
 * @create: 2020-06-15 20:22
 * @description:
 **/
public class ImageUtils {

    /**
     * 资源前缀
     */
    public static String PREFIX = "/image/";

    public static String urlConver(String name, String fileName, String url) {
        return ImageUtils.PREFIX + name + "/" + fileName + "." +
                url.substring(url.lastIndexOf(".") + 1);
    }
}
