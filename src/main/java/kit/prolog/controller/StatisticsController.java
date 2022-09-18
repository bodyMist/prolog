package kit.prolog.controller;


import kit.prolog.dto.StatisticsDto;
import kit.prolog.dto.SuccessDto;
import kit.prolog.service.PostService;
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

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticService statisticService;

    @GetMapping("/mystatis/{year}")
    public SuccessDto findStatisticByUserId(
            @RequestHeader(value = "X-AUTH-TOKEN") String accessToken,
            @RequestHeader(required = false) Long memberPk, @PathVariable int year){

        return new SuccessDto(true,statisticService.viewStatisByUserId(memberPk, year));
    }


    @GetMapping("/myboard/statis/{id}")
    public SuccessDto findStatisticByPostId(
            @RequestHeader(value = "X-AUTH-TOKEN") String accessToken,
            @RequestHeader(required = false) Long memberPk, @PathVariable Long id){
        return new SuccessDto(true,statisticService.viewStatisticByPostId(memberPk, id));
    }
}
