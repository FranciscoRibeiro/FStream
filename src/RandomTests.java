import sun.reflect.ConstantPool;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

public class RandomTests {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
        Function<Integer,Integer> f = x -> x + 1;

        Method getConstantPool = Class.class.getDeclaredMethod("getConstantPool");
        getConstantPool.setAccessible(true);
        ConstantPool constantPool = (ConstantPool) getConstantPool.invoke(f.getClass());
        //String[] methodRefInfo = constantPool.getMemberRefInfoAt(constantPool.getSize() - 2);
        for(int i = 0; i < constantPool.getSize(); i++) {
            System.out.println(constantPool.getClass());
        }

        /*int argumentIndex = 0;
        String argumentType = jdk.internal.org.objectweb.asm.Type.getArgumentTypes(methodRefInfo[2])[argumentIndex].getClassName();
        Class<?> type = (Class<?>) Class.forName(argumentType);*/
    }
}
