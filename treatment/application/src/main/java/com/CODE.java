package com;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class CODE {

    public static void main(String[] args) {

        List list= ManagementFactory.getRuntimeMXBean().getInputArguments();

        list.forEach(new Consumer() {
            @Override
            public void accept(Object o) {
                System.out.println(o);
            }
        });

        String name = ManagementFactory.getRuntimeMXBean().getName();
        System.out.println(name);

        MemoryUsage heapMemoryUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();//堆内存

        System.out.println(heapMemoryUsage);

        MemoryUsage nonHeapMemoryUsage = ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage();//堆外内存
        System.out.println(nonHeapMemoryUsage);

        ThreadInfo[] threadInfos = ManagementFactory.getThreadMXBean().dumpAllThreads(false, false);

        List<ThreadInfo> threadInfoList = Arrays.asList(threadInfos);

        threadInfoList.forEach(new Consumer<ThreadInfo>() {
            @Override
            public void accept(ThreadInfo threadInfo) {
                System.out.println(threadInfo);
            }
        });


    }
}
