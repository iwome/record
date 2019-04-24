package com.bangbangqiu.learnPlugin

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import com.bangbangqiu.learnPlugin.inject.MyInjects
import org.apache.commons.codec.digest.DigestUtils
import org.gradle.api.Project

class LearnTransform extends Transform {

    private Project mProject

    LearnTransform(Project p) {
        this.mProject = p
        System.out.println("----------------进入transform了 构造--------------")
    }

    @Override
    String getName() {
        System.out.println("----------------进入transform了 getName--------------")
        return "LearnTransform"
    }

    //设置输入类型，针对class文件处理
    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        System.out.println("----------------进入transform了 getInputTypes--------------")
        return TransformManager.CONTENT_CLASS
    }

    //设置输入范围，针对整个项目
    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        System.out.println("----------------进入transform了 getScopes--------------")
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return true
    }

    //修改字节码的逻辑
    //inputs中是传过来的输入流，其中有两种格式，一种是jar包格式一种是目录格式。
    //outputProvider 获取到输出目录，最后将修改的文件复制到输出目录，这一步必须做不然编译会报错
    //一个transform的output是下一个transform的input
    @Override
    void transform(Context context, Collection<TransformInput> inputs, Collection<TransformInput> referencedInputs, TransformOutputProvider outputProvider, boolean isIncremental) throws IOException, TransformException, InterruptedException {
        System.out.println("----------------************进入transform了 transform************--------------")
        inputs.each { TransformInput input ->
            //遍历文件夹
            System.out.println("input.directoryInputs: ${input.directoryInputs.size()}")
            input.directoryInputs.each { DirectoryInput directoryInput ->
                //注入代码
                System.out.println("path: ${directoryInput.file.absolutePath}")
                MyInjects.inject(directoryInput.file.absolutePath, mProject)

                //获取output目录
                def dest = outputProvider.getContentLocation(directoryInput.name,
                        directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
                //将input的目录复制到output指定目录
                System.out.println("dest path: ${dest.absolutePath}")
                FileUtils.copyDirectory(directoryInput.file, dest)
            }
            //遍历jar文件 对jar不操作，但是要输出到out路径
            System.out.println("input.jarInputs: ${input.jarInputs.size()}")
            input.jarInputs.each { JarInput jarInput ->
                //重命名输出文件（同目录copyFile会冲突）
                def jarName = jarInput.name
                println("jar = " + jarInput.file.getAbsolutePath())
                def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
                if (jarName.endsWith(".jar")) {
                    jarName = jarName.substring(0, jarName.length() - 4)
                }
                def dest = outputProvider.getContentLocation(jarName + md5Name, jarInput.contentTypes, jarInput.scopes, Format.JAR)
                System.out.println("dest path: ${dest.absolutePath}")
                FileUtils.copyFile(jarInput.file, dest)
            }
        }
        System.out.println("--------------结束transform了----------------")
    }
}