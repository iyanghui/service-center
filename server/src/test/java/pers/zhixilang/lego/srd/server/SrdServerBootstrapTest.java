package pers.zhixilang.lego.srd.server;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pers.zhixilang.lego.srd.server.annotation.EnableSrdServer;

import static org.junit.Assert.*;

/**
 * @author zhixilang
 * @version 1.0.0
 * date 2022-03-16 17:42
 */
@SpringBootTest
@SpringBootApplication
@RunWith(SpringRunner.class)
@EnableSrdServer
public class SrdServerBootstrapTest {

    @Test
    public void run() {
        for (;;) {}
    }
}
