package com.finder.CafeAroundMe.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@RequestMapping("/open-api/map")
@Slf4j
public class CafeAroundMeMapController {

	@GetMapping("/kakao")
	public String kakaoMapPage() {
		log.info("kakao map page");
		return "kakaoMap";
	}

	@GetMapping("/index")
	public String indexPage() {
		log.info("index page");
		return "index";
	}
}
