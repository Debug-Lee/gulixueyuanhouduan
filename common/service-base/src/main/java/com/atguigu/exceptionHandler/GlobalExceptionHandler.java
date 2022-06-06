package com.atguigu.exceptionHandler;
import com.atguigu.commonutils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理类
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody// 为了返回json数据
    public R error(Exception e){
        e.printStackTrace();
        return R.error().message("执行了全局异常处理...");
    }

    //特定异常处理
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody// 为了返回json数据
    public R error1(ArithmeticException e){
        e.printStackTrace();
        return R.error().message("执行了ArithmeticException处理...");
    }

    //自定义异常处理
    @ExceptionHandler(GuliException.class)
    @ResponseBody// 为了返回json数据
    public R error2(GuliException e){
        log.error(e.getMessage());
        e.printStackTrace();
        return R.error().code(e.getCode()).message(e.getMsg());
    }

}
