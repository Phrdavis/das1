package br.univille.microkernel_kernel.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import br.univille.microkernel_kernel.service.DefaultService;


@RestController
@RequestMapping("/")
public class HomeControllerAPI {

    @Autowired(required=false)
    private Map<String,DefaultService> services;

    @GetMapping
    public ResponseEntity<String> index() {

        String serviceList = null;

        if(services != null){

            serviceList = Arrays.toString(services.keySet().toArray());

        }

        return new ResponseEntity<String>(serviceList,HttpStatus.OK);

    }
    
    
}
