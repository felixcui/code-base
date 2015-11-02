package my.test;

import java.lang.String;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 这里是将新创建好的注解类MyAnnotation标记到AnnotaionTest类上，
 * 并应用了注解类MyAnnotation中定义各种不同类型的的属性
 */
@MyAnnotation(
    color="red",
    value="孤傲苍狼",
    arrayAttr={3,5,6},
    lamp=EumTrafficLamp.GREEN,
    annotationAttr=@MetaAnnotation("gacl")
)
public class MyAnnotationTest {
    @MyAnnotation("将MyAnnotation注解标注到main方法上")
    public static void main(String[] args) {
        /**
         * 这里是检查Annotation类是否有注解，这里需要使用反射才能完成对Annotation类的检查
         */
        if(MyAnnotationTest.class.isAnnotationPresent(MyAnnotation.class)) {
            /**
             * 用反射方式获得注解对应的实例对象后，在通过该对象调用属性对应的方法
             * MyAnnotation是一个类，这个类的实例对象annotation是通过反射得到的，这个实例对象是如何创建的呢？
             * 一旦在某个类上使用了@MyAnnotation，那么这个MyAnnotation类的实例对象annotation就会被创建出来了
             */
            MyAnnotation annotation = (MyAnnotation) MyAnnotationTest.class.getAnnotation(MyAnnotation.class);
            System.out.println(annotation.color());//输出color属性的默认值：red
            System.out.println(annotation.value());//输出value属性的默认值：孤傲苍狼
            System.out.println(annotation.arrayAttr().length);//这里输出的数组属性的长度的结果为：3，数组属性有三个元素，因此数组的长度为3
            System.out.println(annotation.lamp());//这里输出的枚举属性值为：GREEN
            System.out.println(annotation.annotationAttr().value());//这里输出的注解属性值:gacl

            MetaAnnotation ma = annotation.annotationAttr();//annotation是MyAnnotation类的一个实例对象
            System.out.println(ma.value());//输出的结果为：gacl


        }
    }
}

@Retention(RetentionPolicy.RUNTIME)
//Retention注解决定MyAnnotation注解的生命周期
@Target({ElementType.METHOD, ElementType.TYPE})
@interface MyAnnotation {
    String color() default "blue";//为属性指定缺省值

    /**
     * 为注解添加value属性，这个value属性很特殊，如果一个注解中只有一个value属性要设置，
     * 那么在设置注解的属性值时，可以省略属性名和等号不写， 直接写属性值，如@SuppressWarnings("deprecation")，
     * 这里的MyAnnotation注解设置了两个String类型的属性，color和value，
     * 因为color属性指定有缺省值，value属性又是属于特殊的属性，因此使用MyAnnotation注解时
     * 可以这样使用MyAnnotation注解："@MyAnnotation(color="red",value="xdp")"
     * 也可以这样使用："@MyAnnotation("孤傲苍狼")"，这样写就表示MyAnnotation注解只有一个value属性要设置，color属性采用缺省值
     * 当一个注解只有一个value属性要设置时，是可以省略"value="的
     */
    String value();//定义一个名称为value的属性

    //添加一个int类型数组的属性
    int[] arrayAttr() default {1, 2, 4};

    //添加一个枚举类型的属性，并指定枚举属性的缺省值，缺省值只能从枚举类EumTrafficLamp中定义的枚举对象中取出任意一个作为缺省值
    EumTrafficLamp lamp() default EumTrafficLamp.RED;

    //为注解添加一个注解类型的属性,并指定注解属性的缺省值
    MetaAnnotation annotationAttr() default @MetaAnnotation("xdp");
}


enum EumTrafficLamp {
    RED,//红
    YELLOW,//黄
    GREEN//绿
}


/**
 * MetaAnnotation注解类为元注解
 * @author 孤傲苍狼
 *
 */
@interface MetaAnnotation {
    String value();//元注解MetaAnnotation设置有一个唯一的属性value
}
