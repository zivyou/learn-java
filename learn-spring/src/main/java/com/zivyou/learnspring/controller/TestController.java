package com.zivyou.learnspring.controller;

import com.zivyou.guardedsuspensiondemo.GuardedObject;
import com.zivyou.learnspring.event.DummyEvent;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestController {
    private final GuardedObject<String, String> guardedObject;
    private final ApplicationContext applicationContext;

    @NoArgsConstructor
    @AllArgsConstructor
    static class Response {
        int code;
        String message;
    }

    @PostMapping("/wakeup-guarded-object/{id}")
    public String wakeupGuardedObject(@PathVariable("id") String id, @RequestParam("value") String value) {
        guardedObject.onChanged(id, value);
        return null;
    }

    @PostMapping("/publish-event")
    public Response publishEvent(@RequestBody String eventContent) {
        applicationContext.publishEvent(new DummyEvent(eventContent));
        return new Response(200, "OK");
    }

}
