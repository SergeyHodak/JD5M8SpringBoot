package com.goit5.JD5M8SpringBoot.feature.time.admin;

import com.goit5.JD5M8SpringBoot.feature.time.pojo.CurrentTimeRequest;
import com.goit5.JD5M8SpringBoot.feature.time.pojo.CurrentTimeResponse;
import com.goit5.JD5M8SpringBoot.feature.time.service.CurrentTimeService;
import com.goit5.JD5M8SpringBoot.feature.time.InvalidTimeZoneException;
import com.goit5.JD5M8SpringBoot.mvc.LocalizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/current-time") //якщо в усьму класі є загальна частинка посилання то її можна винисти сюда
@Controller
@RequiredArgsConstructor
public class CurrentTimeController {
    private final CurrentTimeService currentTimeService;
    private final LocalizeService localizeService;

    //@RequestMapping( //типу універсальний спосіб задати декілька параметрів мепінга
    //        method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE},
    //        value = {"/current-time", "/another"}
    //)
    //@GetMapping("/current-time")
    @GetMapping("/get") //якщо в усьму класі є загальна частинка посилання, то тут вказуємо ту індивідуальну частинку після загальної
    public ModelAndView getCurrentTime(
            @RequestParam(required = false, name = "timezone", defaultValue = "UTC") String timezone) {
        //інжектимо сюди той параметер який повинен прийти в запиті (цей параметер таким чином повинен бути обовязково в запиті).
        // (required = false) вимикає обов'язковість параметру записуючи його як нуль якщо його немає
        //Приклад параметру до запіту: "key":"timezone","value":"Europe/Kiev"
        ModelAndView result = new ModelAndView("current-time");
        result.addObject("time", currentTimeService.getCurrentTime(timezone));
        return result;
        //Адреса звернення: localhost:8080/current-time/get
    }

    @ResponseBody
    @GetMapping("/getAsString")
    public String getCurrentTimeAsString(@RequestParam(required = false, name = "tz", defaultValue = "UTC") String timezone) {
        return currentTimeService.getCurrentTime(timezone);
        //Адреса звернення: localhost:8080/current-time/getAsString
    }

    @ResponseBody
    @GetMapping("/getAsObject")
    public CurrentTimeResponse getCurrentTimeAsObject(@RequestParam(required = false, name = "tz", defaultValue = "UTC") String timezone) {
        try {
            return CurrentTimeResponse.success(currentTimeService.getCurrentTime(timezone));
        } catch (InvalidTimeZoneException ex) {
            ex.printStackTrace();

            return CurrentTimeResponse.failed(CurrentTimeResponse.Error.invalidTimezone);
        }
        //Адреса звернення: localhost:8080/current-time/getAsObject
        //http://localhost:8080/current-time/getAsObject?tz=UTC
    }

    @PostMapping("/post-x-form-url-encoded")
    public ModelAndView postCurrentTimeFormUrlEncoded(String tz) {
        ModelAndView result = new ModelAndView("current-time");
        result.addObject("time", currentTimeService.getCurrentTime(tz));
        return result;
    }

    @PostMapping("/post-json")
    public ModelAndView getCurrentTimeJson(@RequestBody CurrentTimeRequest request,
                                           @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {
        //Приклад тіла запиту:
        // {
        //    "timezone" : "GMT+3",
        //    "format" : "yyyy.MM.dd HH:mm:ss"
        // }
        ModelAndView result = new ModelAndView("current-time");

        result.addObject("time", currentTimeService.getCurrentTime(
                request.getTimezone(), request.getFormat()
        ));

        result.addObject("currentTimeLabel", localizeService.getCurrentTimeLabel(acceptLanguage));

        return result;
    }

    @GetMapping("/{timezone}/{format}") //шлях за допомогою змінних
    public ModelAndView getPathVariableTime(
        @PathVariable("timezone") String timezone,
        @PathVariable("format") String format
    ) {
        //Приклад адресу запиту: http://localhost:8080/current-time/GMT+3/yyyy.MM.dd HH:mm:ss
        ModelAndView result = new ModelAndView("current-time");

        result.addObject("time", currentTimeService.getCurrentTime(timezone, format));

        result.addObject("currentTimeLabel", localizeService.getCurrentTimeLabel("uk"));

        return result;
    }
}