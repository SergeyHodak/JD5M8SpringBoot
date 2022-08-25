package com.goit5.JD5M8SpringBoot.feature.time;

import com.goit5.JD5M8SpringBoot.feature.time.pojo.CurrentTimeRequest;
import com.goit5.JD5M8SpringBoot.feature.time.pojo.CurrentTimeResponse;
import com.goit5.JD5M8SpringBoot.feature.time.service.CurrentTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@RequestMapping("/api/v1/time")
@RestController //типу дозволяє повертати відразу ентіті
public class TimeApiController {
    private final CurrentTimeService timeService;

    @PostMapping
    public CurrentTimeResponse get(@RequestBody CurrentTimeRequest request) {
        try {
            String time = timeService.getCurrentTime(
                    request.getTimezone(),
                    request.getFormat()
            );

            return CurrentTimeResponse.success(time);
        } catch (InvalidTimeZoneException e) {
            return CurrentTimeResponse.failed(CurrentTimeResponse.Error.invalidTimezone);
        }
        //Адреса звернення: localhost:8080/api/v1/time
        //Тіло звернення: JSON (Postman)
        //{
        //    "timezone" : "Europe/Kiev",
        //    "format" : "yyyy:MM:dd HH:mm:ss"
        //}
    }

    @GetMapping("/download")
    public ResponseEntity<ByteArrayResource> download(@RequestParam(value = "tz", required = false) String timezone) {
        String currentTime = timeService.getCurrentTime(timezone);
        byte[] bytes = currentTime.getBytes(StandardCharsets.UTF_8);

        ByteArrayResource resource = new ByteArrayResource(bytes);

        String filename = "time-" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + ".json";

        return ResponseEntity.ok()
                // Content-Disposition
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + filename + "\"")
                // Content-Type
                .contentType(MediaType.APPLICATION_JSON) //
                // Context-Length
                .contentLength(bytes.length) //
                .body(resource);

        //Адреса звернення: localhost:8080/api/v1/time/download
    }

    @GetMapping(value = "/download/v2", consumes = {"application/json"}) //контролер буде реагувати якщо в запиті хедер контент тайп буде application/json
    public String download2(@RequestParam(value = "tz", required = false) String timezone,
                                                       HttpServletRequest request,
                                                       HttpServletResponse response) {
        String currentTime = timeService.getCurrentTime(timezone);
        byte[] bytes = currentTime.getBytes(StandardCharsets.UTF_8);

        ByteArrayResource resource = new ByteArrayResource(bytes);

        String filename = "time-" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + ".json";

        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + filename + "\"");
        response.setContentType(MediaType.APPLICATION_JSON.toString());
        response.setContentLength(bytes.length);

        return currentTime;

        //Адреса звернення: localhost:8080/api/v1/time/download/v2
    }

    @GetMapping
    public CurrentTimeResponse get(@RequestParam(name = "timezone", required = false) String tz,
                                   HttpServletResponse response) {
        try {
            response.addHeader("Access-Control-Allow-Origin", "*");
            String time = timeService.getCurrentTime(tz);
            return CurrentTimeResponse.success(time);
        } catch (InvalidTimeZoneException e) {
            return CurrentTimeResponse.failed(CurrentTimeResponse.Error.invalidTimezone);
        }
    }
}