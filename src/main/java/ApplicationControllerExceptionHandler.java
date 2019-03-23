import com.google.common.collect.Maps;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by gefufeng on 16/7/18.
 */
@ControllerAdvice
public class ApplicationControllerExceptionHandler {

    @ExceptionHandler(value = Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Map<String, Object> handlerError(HttpServletRequest req, Exception e) {

        Map<String, Object> map = Maps.newHashMap();
        map.put("tip", "此错误说明调用接口失败，失败原因见msg，如果msg为空，联系后台");
        map.put("msg", e.getMessage());
        map.put("path", req.getRequestURI());
        map.put("params", req.getParameterMap());
        map.put("status", "0");
        return map;
    }
}