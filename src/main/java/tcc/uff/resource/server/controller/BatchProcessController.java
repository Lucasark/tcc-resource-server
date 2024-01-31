package tcc.uff.resource.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tcc.uff.resource.server.service.BatchProcessService;

@Slf4j
@RestController
@RequestMapping("/batch-process")
@RequiredArgsConstructor
public class BatchProcessController {

    private final BatchProcessService batchProcessService;

    @GetMapping("/{string}")
    public void test(@PathVariable String string) throws InterruptedException {
        batchProcessService.test(string);
    }

}
