package kit.prolog.controller;


import kit.prolog.domain.User;
import kit.prolog.dto.StatisticsDto;
import kit.prolog.dto.SuccessDto;
import kit.prolog.service.JwtService;
import kit.prolog.service.PostService;
import kit.prolog.service.StatisticService;
import kit.prolog.dto.*;
import kit.prolog.service.UserService;
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
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class StatisticsController {

    private final StatisticService statisticService;
    private final UserService userService;
    private final JwtService jwtService;
    private final static String SERVER_ERROR = "Unexpected Server Error";

    @GetMapping("/mystatis/{year}")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public SuccessDto findStatisticByUserId(
            @RequestHeader(value = "X-AUTH-TOKEN") String accessToken,
            @PathVariable Long year){
        SuccessDto response;
        try{
            Long memberPk = validateUser(accessToken);
            StatisticsDto statisticsDto = statisticService.viewStatisByUserId(memberPk, year);
            response = new SuccessDto(true, statisticsDto);
        }catch (IllegalArgumentException | NullPointerException exception) {
            response = new SuccessDto(false, exception.getMessage());
        }catch (Exception e){
            response = new SuccessDto(false, SERVER_ERROR);
        }
        return response;
    }


    @GetMapping("/myboard/statis/{id}")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public SuccessDto findStatisticByPostId(
            @RequestHeader(value = "X-AUTH-TOKEN") String accessToken,
            @PathVariable Long id){
        SuccessDto response;
        try{
            Long memberPk = validateUser(accessToken);
            StatisticsDto statisticsDto = statisticService.viewStatisticByPostId(memberPk, id);
            response = new SuccessDto(true, statisticsDto);
        }catch (IllegalArgumentException | NullPointerException exception) {
            response = new SuccessDto(false, exception.getMessage());
        }catch (Exception e){
            response = new SuccessDto(false, SERVER_ERROR);
        }
        return response;
    }
    private Long validateUser(String accessToken) throws NullPointerException, IllegalArgumentException{
        String memberPk = jwtService.validateToken(accessToken) ? jwtService.getUserPk(accessToken) : null;
        if (memberPk == null) throw new NullPointerException("No User Data");
        User user = userService.readUser(Long.valueOf(memberPk));
        if (Long.parseLong(memberPk) != user.getId()) throw new IllegalArgumentException("No Permissions");
        return Long.parseLong(memberPk);
    }
}
