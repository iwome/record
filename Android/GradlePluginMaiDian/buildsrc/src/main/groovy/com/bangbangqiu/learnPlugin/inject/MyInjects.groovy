package com.bangbangqiu.learnPlugin.inject

import com.a.lib.DataContent
import com.a.lib.DataName
import com.android.build.gradle.AppExtension
import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import org.gradle.api.Project

public class MyInjects {
    //初始化类池
    private final static ClassPool pool = ClassPool.getDefault()

    public static void inject(String path, Project project) {
        //将当前路径加入类池，不然找不到这个类
        pool.appendClassPath(path)
        //project.android.bootClasspath加入android.jar,不然找不到android相关类
        pool.appendClassPath(project.android.bootClasspath[0].toString())
        System.println("classpath ")
        //引入android.os.Bundle包，因为onCreate方法参数里有Bundle
        pool.importPackage("android.os.Bundle")
        def appId = project.extensions.getByType(AppExtension).getDefaultConfig().applicationId
        def appIdPath = appId.replaceAll(/\./, '\\\\')
        println("appId :${appId} appPath ${appIdPath}")
        File dir = new File(path)
        if (dir.isDirectory()) {
            //遍历文件夹
            dir.eachFileRecurse { File file ->
                println("filePath = ${file.absolutePath}")
                if (file.absolutePath.contains(appIdPath)
                        && file.absolutePath.contains(".class")
                        && !file.absolutePath.contains("R\$")
                        && !file.absolutePath.contains("R.class")
                        && !file.absolutePath.contains("BuildConfig")) {
                    CtClass ctClass = pool.getCtClass(getClassName(file.absolutePath))
                    if (ctClass.isFrozen())
                        ctClass.defrost()
                    def methodArray = ctClass.getMethods()
                    def hasInsert = false
                    methodArray.each {
                        CtMethod ctMethod ->
                            println("ctMethod :${ctMethod.name}")
                            def annotionArray = ctMethod.getAnnotations()
                            if (annotionArray.size() > 0) {
                                def dataName = "", dataContent = ""
                                annotionArray.each {
                                    if (it instanceof DataName) {
                                        dataName = it.value()
                                    }
                                    if (it instanceof DataContent) {
                                        dataContent = it.value()
                                    }
                                }
                                if (!isEmpty(dataName) || !isEmpty(dataContent)) {
                                    hasInsert = true
                                    ctMethod.insertBefore(generateCode(dataName, dataContent))
                                }
                            }
                    }

                    if (hasInsert) {
                        ctClass.writeFile(path)
                        ctClass.detach()
                    }

                }
            }
        }
    }

    private static boolean isEmpty(String s) {
        return s == null || s.size() == 0
    }

    private static String generateCode(name, content) {
        """ android.widget.Toast.makeText(this,"insert name is ${name} insert value is ${content}",android.widget.Toast.LENGTH_SHORT).show();
                                                """
    }

    // ..\MyApplication\app\build\intermediates\transforms\desugar\debug\0\com\example\bangbangqiu\myapplication\MainActivity.class
    private static String getClassName(String path) {
        String s1 = path.substring(path.indexOf("com"), path.indexOf(".class"))
        return s1.replaceAll('\\\\', '.')
    }
}