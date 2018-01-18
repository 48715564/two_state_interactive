//package com.cn.common.infrastructure;
//
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.springframework.stereotype.Component;
//
//@Aspect
//@Component
//public class XClarityInterceptor {
//    /**
//     * 拦截器具体实现
//     * @param pjp
//     * @return JsonResult（被拦截方法的执行结果，或需要登录的错误提示。）
//     */
//    @Around("execution(* com.cn.common.utils.XClarityApi(..))")
//    public Object Interceptor(ProceedingJoinPoint pjp){
//        Object result = null;
//        try {
//            result = pjp.proceed();
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
//        return result;
//    }
//
//}