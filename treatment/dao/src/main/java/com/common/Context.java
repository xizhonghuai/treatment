package com.common;

/**
 * @ClassName Context
 * @Description: TODO
 * @Author xizhonghuai
 * @Date 2020/4/9
 * @Version V1.0
 **/
public class Context {

    private static Context instance;

    private Context() {
    }

    public static synchronized Context getInstance() {
        if (instance == null) {
            instance = new Context();
        }
        return instance;
    }

    private ThreadLocal<Object> objThreadLocal = ThreadLocal.withInitial(() -> null);

    public void setObj(Object obj) {
        this.objThreadLocal.set(obj);
    }

    public Object getObj() {

        return this.objThreadLocal.get();
    }

    public void clear() {

          this.objThreadLocal.remove();
    }
}

