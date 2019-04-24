package com.bangbangqiu.learnPlugin.inject

import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import org.gradle.api.Project

public class DataCollectionInject {
    //初始化类池
    private final static ClassPool pool = ClassPool.getDefault()

    public static void inject(String path, Project project) {
        //将当前路径加入类池，不然找不到这个类
        pool.appendClassPath(path)
        //project.android.bootClasspath加入android.jar,不然找不到android相关类
        pool.appendClassPath(project.android.bootClasspath[0].toString())
        //引入android.os.Bundle包，因为onCreate方法参数里有Bundle
        pool.importPackage("android.os.Bundle")

        File dir = new File(path)
        if (dir.isDirectory()) {
            //遍历文件夹
            dir.eachFileRecurse { File file ->
                println("filePath = ${file.absolutePath}")
                if (file.getName().contains("Activity.class")) {
                    //获取MainActivity.class
                    String className = getClassName(file.absolutePath)
                    println("className = $className")
                    CtClass ctClass = pool.getCtClass(className)
                    println("ctClass = " + ctClass)
                    insertToLifeCycle(ctClass, "onResume")
                    insertToLifeCycle(ctClass, "onCreate")
                    insertToLifeCycle(ctClass, "onPause")
                    ctClass.writeFile(path)
                    ctClass.detach()//释放
                }
            }
        }
    }

    static def i = 0

    private static void insertToLifeCycle(CtClass ctClass, String method) {
        //解冻
        if (ctClass.isFrozen())
            ctClass.defrost()
        //获取到OnCreate方法
        CtMethod ctMethod = ctClass.getDeclaredMethod(method)
        println("方法名 = " + ctMethod)
        //在方法开头插入代码
        ctMethod.insertBefore(generateCode("$method $i"))
        i++
    }

    private static String generateCode(code) {
        """ android.widget.Toast.makeText(this,"${code}",android.widget.Toast.LENGTH_SHORT).show();
                                                """
    }

    // ..\MyApplication\app\build\intermediates\transforms\desugar\debug\0\com\example\bangbangqiu\myapplication\MainActivity.class
    private static String getClassName(String path) {
        String s1 = path.substring(path.indexOf("com"), path.indexOf(".class"))
        return s1.replaceAll('\\\\', '.')
    }
}