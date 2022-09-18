package kit.prolog.controller;


import kit.prolog.dto.StatisticsDto;
import kit.prolog.dto.SuccessDto;
import kit.prolog.service.StatisticService;
import kit.prolog.dto.*;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class StatisticsController {

    StatisticService statisticService;

    @GetMapping("/mystatis/{year}")
    public SuccessDto findStatisticByUserId(@PathVariable("year") long year, @PathVariable Long id){
        StatisticsDto statis = statisticService.viewStatisByUserId(id);

        return new SuccessDto(true,statis);
    }


    @GetMapping("/myboard/statis/{id}")
    public SuccessDto findStatisticByPostId(
            @RequestHeader(value = "X-AUTH-TOKEN") String accessToken,
            @RequestHeader(required = false) Long memberPk, @PathVariable Long id){

        StatisticsDto statis;

        if(memberPk != null)
            statis = statisticService.viewStatisByPostId(memberPk, id);
        else
            statis = statisticService.viewStatisByPostId(null, id);

        return new SuccessDto(true,statis);
    }
}
