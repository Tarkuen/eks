package com.example.eksamen.controller;

import com.example.eksamen.Repository.RestordreRepository;
import com.example.eksamen.model.Restordre;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@Controller
public class RestordreController {

    private RestordreRepository repository = RestordreRepository.getInstance();

    @GetMapping("/")
    public String index(Model model){

        List<Restordre> restOrdreRepository = repository.readAllRestordrer();

        if(restOrdreRepository != null && restOrdreRepository.size() > 0){
            model.addAttribute("allRestordrer", restOrdreRepository);
        }
        return "HomePage";
    }

    @GetMapping("/restordre/ekspeder")
    public String eksp(@RequestParam("restordre_nummer") String restordrenummer, Model model ){
        Restordre r = repository.read(restordrenummer);
        model.addAttribute("restordre", r);
        return "Update";
    }

    @PostMapping("/restordre/ekspeder")
    public String eksp( @ModelAttribute @Valid Restordre restordre,  @RequestParam("restordre_nummer") Integer restordrenummer){
        restordre.setRestordre_nummer(restordrenummer);
        repository.update(restordre);
        return "redirect:/";
    }


    @GetMapping("/restordre/ret")
    public String ret(@RequestParam("restordre_nummer") String restordrenummer, Model model){
        Restordre r = repository.read(restordrenummer);
        model.addAttribute("restordre",r);
        return "UpdateAll";
    }

    @PostMapping("/restordre/ret")
    public String ret(@Valid @ModelAttribute Restordre restordre, @RequestParam(value = "restordre_nummer") Integer restordrenummer ){

        restordre.setRestordre_nummer(restordrenummer);
        repository.update(restordre);
        return "redirect:/";
    }


    @GetMapping("restordre/create")
    public String crea(Model model){
        Restordre r = new Restordre();
        model.addAttribute("restordre", r);
        return "Create";
    }

    @PostMapping("restordre/create")
    public String crea(@ModelAttribute("restordre") Restordre restordre){
        repository.create(restordre);
        return "redirect:/";
    }










}
