package com.example.eksamen.controller;

import com.example.eksamen.Repository.RestordreRepository;
import com.example.eksamen.model.Restordre;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
    public String eksp(@ModelAttribute("restordre") Restordre restordre){
        repository.updateEkspeditionsDato(restordre);
        return "redirect:/";
    }


    @PostMapping("/restordre/ret")
    public String ret(@RequestParam("restordre_nummer") String restordrenummer, Model model){

        Restordre r = repository.read(restordrenummer);
        model.addAttribute("toEks",r);
        return "UpdateAll";
    }

    @GetMapping("restordre/create")
    public String crea(ModelMap modelMap){
        Restordre r = new Restordre();
        modelMap.addAttribute("toEks", r);
        return "Create";
    }











}
