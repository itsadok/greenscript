package controllers.greenscript;

import java.util.Map;

import play.modules.greenscript.GreenScriptPlugin;
import play.mvc.Controller;
import play.mvc.Http;

public class Service extends Controller {
    
    public static void getInMemoryCache(String key) {
        String content = GreenScriptPlugin.getInstance().getInMemoryFileContent(key);
        notFoundIfNull(content);
        final long l = System.currentTimeMillis();
        final String etag = "\"" + l + "-" + key.hashCode() + "\"";
        response.cacheFor(etag, "100d", l);
        
        Map<String, Http.Header> headers = request.headers;
        if (headers.containsKey("if-none-match") && headers.containsKey("if-modified-since")) {
            response.status = Http.StatusCode.NOT_MODIFIED;
            return;
        }
 
        if (key.endsWith(".js")) {
            response.setContentTypeIfNotSet("text/javascript");
        } else if (key.endsWith(".css")) {
            response.setContentTypeIfNotSet("text/css");
        }
        
        renderText(content);
    }

}
