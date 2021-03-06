package com.brandnew.jtatest;

import com.brandnew.common.util.FileOperateUtil;
import com.brandnew.jtatest.mapper1.CasUserMapper;
import com.brandnew.jtatest.mapper2.PersonMapper;
import com.brandnew.jtatest.service.UserService1;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JtatestApplicationTests {

    @Autowired
    private CasUserMapper casUserMapper;
    @Autowired
    private PersonMapper  personMapper;
    @Autowired
    private UserService1 userService1;

    @Test
    public void contextLoads() {
        casUserMapper.selectAll().forEach(System.out::println);
        personMapper.selectAll().forEach(System.out::println);
    }

    @Test
    public void testService() {
        userService1.addUser1();
    }

    @Test
    public void clear9hjf() {
        FileOperateUtil.deleteSpecDir(new File("E:\\E_workspace"), Arrays.asList("target",".classpath",".project",".setting",".svn",".idea"));
    }
}
