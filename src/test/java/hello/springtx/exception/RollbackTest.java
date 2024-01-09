package hello.springtx.exception;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class RollbackTest {

    @Autowired RollbackService rollbackService;

    @Test
    void runtimeException(){
        Assertions.assertThatThrownBy(() ->rollbackService.runtimeException())
                .isInstanceOf(RuntimeException.class);
    }
    @Test
    void checkedException(){
        Assertions.assertThatThrownBy(() ->rollbackService.checkException())
                .isInstanceOf(MyException.class);
    }
    @Test
    void rollbackFor(){
        Assertions.assertThatThrownBy(() ->rollbackService.rollbackFor())
                .isInstanceOf(MyException.class);
    }
    @TestConfiguration
    static class RollbackTestConfig{
        @Bean
        RollbackService rollbackService(){
            return new RollbackService();
        }
    }

    @Slf4j
    static class RollbackService{
        //런타인 예외 발생 : 롤백
        @Transactional
        public void runtimeException(){
            log.info("call runtimeException");
            throw new RuntimeException();
        }
        //체크 예외 발생 : 커밋
        @Transactional
        public void checkException() throws MyException {
            log.info("call checkException");
            throw new MyException();
        }
        //체크 예외 rollbackFor 지정 : 롤백
        @Transactional(rollbackFor = MyException.class)
        public void rollbackFor() throws MyException {
            log.info("call rollbackFor");
            throw new MyException();
        }

    }

    static class MyException extends Exception{

    }
}
