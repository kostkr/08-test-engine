package uj.wmii.pwj.anns;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MyTestEngine {

    private final String className;

    public static void main(String[] args) {
        printArt();
        if (args.length < 1) {
            System.out.println("Please specify test class name");
            System.exit(-1);
        }
        String className = args[0].trim();
        System.out.printf("Testing class: %s\n", className);
        MyTestEngine engine = new MyTestEngine(className);
        engine.runTests();
    }

    public MyTestEngine(String className) {
        this.className = className;
    }

    public void runTests() {
        final Object unit = getObject(className);
        List<Method> testMethods = getTestMethods(unit);
        int successCount = 0;
        int failCount = 0;
        int errorCount = 0;
        for (Method m: testMethods) {
            switch (launchSingleMethod(m, unit)){
                case SUCCESS -> successCount++;
                case FAIL -> failCount++;
                case ERROR -> errorCount++;
            }
        }
        System.out.printf("Engine launched %d tests.\n", testMethods.size());
        System.out.printf("%d of them passed, %d failed, %d error\n", successCount, failCount, errorCount);
    }

    private TestResult launchSingleMethod(Method m, Object unit) {
        try {
            String[] params = m.getAnnotation(MyTest.class).params();
            String[] outputs = m.getAnnotation(MyTest.class).outputs();

            if (params.length == 0 && outputs.length == 0) {// no params
                m.invoke(unit);
            } else {// with params
                if(outputs.length == 0){// no outputs
                    for (String param: params) {
                        m.invoke(unit, param);
                    }
                }else{// with outputs
                    if( params.length != outputs.length ){// check params outputs length
                        System.out.printf("Error %s: the number of parameters is different from number of outputs\n", m.getName());
                        return TestResult.ERROR;
                    }else if( m.getReturnType() == Void.TYPE){
                        System.out.printf("Error %s: returned type void\n", m.getName());
                        return TestResult.ERROR;
                    }

                    Object result;
                    for (int i = 0; i < params.length; ++i){
                        result = m.invoke(unit, params[i]);
                        result = result.toString();
                        if(!result.equals(outputs[i])){
                            System.out.printf("Tested method %s test failed: param %s, expected %s but %s\n", m.getName(), params[i], outputs[i], result);
                            return TestResult.FAIL;
                        }
                    }
                }
            }
            System.out.println("Tested method: " + m.getName() + " test successful.");
            return TestResult.SUCCESS;
        } catch (ReflectiveOperationException | IllegalArgumentException e) {
           System.out.println("Tested method: " + m.getName() + " test Error:");
           e.printStackTrace();
            return TestResult.ERROR;
        }
    }

    private static List<Method> getTestMethods(Object unit) {
        Method[] methods = unit.getClass().getDeclaredMethods();
        return Arrays.stream(methods).filter(
                m -> m.getAnnotation(MyTest.class) != null).collect(Collectors.toList());
    }

    private static Object getObject(String className) {
        try {
            Class<?> unitClass = Class.forName(className);
            return unitClass.getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            return new Object();
        }
    }

    private static void printArt(){
            System.out.println(
                    """
                     _______  _______  _______  _______      _______  __    _  _______  ___   __    _  _______
                    |       ||       ||       ||       |    |       ||  |  | ||       ||   | |  |  | ||       |
                    |_     _||    ___||  _____||_     _|    |    ___||   |_| ||    ___||   | |   |_| ||    ___|
                      |   |  |   |___ | |_____   |   |      |   |___ |       ||   | __ |   | |       ||   |___
                      |   |  |    ___||_____  |  |   |      |    ___||  _    ||   ||  ||   | |  _    ||    ___|
                      |   |  |   |___  _____| |  |   |      |   |___ | | |   ||   |_| ||   | | | |   ||   |___
                      |___|  |_______||_______|  |___|      |_______||_|  |__||_______||___| |_|  |__||_______|
                      ======================================================================================
                      
                      ======================================================================================
                    """);
    }
}
