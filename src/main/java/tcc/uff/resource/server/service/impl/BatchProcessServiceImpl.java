package tcc.uff.resource.server.service.impl;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tcc.uff.resource.server.service.BatchProcessService;

@Slf4j
@Service
@NoArgsConstructor
public class BatchProcessServiceImpl implements BatchProcessService {

    @Async
    @Override
    public void test(String email) throws InterruptedException {
        Thread.sleep(5000);
        log.info("Teste de batch process finalizado para o email: {}", email);
    }

}
