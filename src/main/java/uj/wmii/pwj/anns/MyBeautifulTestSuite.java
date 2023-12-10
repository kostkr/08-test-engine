package uj.wmii.pwj.anns;

public class MyBeautifulTestSuite {

    @MyTest
    public void testSomething() {
        System.out.println("I'm testing something!");
    }

    @MyTest(params = {"a param", "b param", "c param. Long, long C param."})
    public void testWithParam(String param) {
        System.out.printf("I was invoked with parameter: %s\n", param);
    }

    public void notATest() {
        System.out.println("I'm not a test.");
    }

    @MyTest
    public void imFailure() {
        System.out.println("I AM EVIL.");
        throw new NullPointerException();
    }

    @MyTest(params = {"1","2"}, outputs = {"2","3"})
        public int increment(String k){
        int i = Integer.parseInt(k);
        return i+1;
    }

    @MyTest(params = {"1","2"}, outputs = {"4","3"})
    public int incrementFailure(String k){
        int i = Integer.parseInt(k);
        return i+1;
    }

    @MyTest(outputs = "a param")
    public void testFailureWithoutParams(String param){
        System.out.println("test without params" + param);
    }

    @MyTest(params = {"output"}, outputs= {"output"})
    public String correctTest(String output){
        return output;
    }

    @MyTest(params = {"output"}, outputs= {"output"})
    public String incorrectTest(String output){
        return output + "launcher";
    }

    @MyTest(params = {"5"}, outputs= {"6"})
    public int testInteger(String param){
        return Integer.parseInt(param) + 1;
    }

    @MyTest(params = {"5"}, outputs= {"6"})
    public void testError(String param){
    }
}
