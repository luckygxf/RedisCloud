package com.gxf.agent.util;

/**
 * Created by 58 on 2017/7/12.
 * 幂等操作
 */
public abstract class IdempotentConfirmer {
    private int retry = 3;

    public IdempotentConfirmer(int retry) {
        this.retry = retry;
    }

    public IdempotentConfirmer() {
    }

    public abstract boolean execute();

    public boolean run(){
        while(retry -- > 0){
            try{
                boolean isOk = execute();
                if(isOk){
                    return true;
                }
                Thread.sleep(1000);
            } catch (Exception e){
                e.printStackTrace();
                try{
                    Thread.sleep(1000);
                } catch (Exception e1){
                    e1.printStackTrace();
                } //catch
                continue;
            } //catch
        } //while

        return false;
    }
}
