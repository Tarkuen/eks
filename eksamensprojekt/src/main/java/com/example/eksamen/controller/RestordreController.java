package com.example.eksamen.controller;

import com.example.eksamen.DataClean;
import com.example.eksamen.Repository.RestordreRepository;
import com.example.eksamen.model.Restordre;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
public class RestordreController {

    private RestordreRepository repository = RestordreRepository.getInstance();
    private DataClean dc = DataClean.getInstance();


    @PostMapping("/")
    public String index(Model model) {
        List<Restordre> restordreList = repository.readAllRestordrer();

        dc.filer();

        if (restordreList != null && restordreList.size() > 0) {
            model.addAttribute("allRestordrer", restordreList);
        }
        return "HomePage";
    }
    @GetMapping("/")
    public String index(@RequestParam(value = "search", required = false) String search, Model model) {

        List<Restordre> restordreList = repository.readAllRestordrer();

        if (restordreList != null && restordreList.size() > 0) {
            model.addAttribute("allRestordrer", restordreList);
        }
        return "HomePage";
    }


    @GetMapping("/restordre/ekspeder")
    public String eksp(@RequestParam("restordre_nummer") String restordrenummer, Model model) {
        Restordre r = repository.read(restordrenummer);
        model.addAttribute("restordre", r);
        return "Update";
    }

    @PostMapping("/search")
    public String search(@RequestParam(value = "search", required = false) String search, Model model) {
        List<Restordre> restordreList = repository.search(search);
        model.addAttribute("allRestordrer", restordreList);
        return "HomePage";
    }

    @PostMapping("/restordre/ekspeder")
    public String eksp(@ModelAttribute @Valid Restordre restordre, @RequestParam("restordre_nummer") Integer restordrenummer) {
        restordre.setRestordre_nummer(restordrenummer);
        restordre.setAktiv(0);
        repository.update(restordre);

        return "redirect:/";
    }


    @GetMapping("/restordre/ret")
    public String ret(@RequestParam("restordre_nummer") String restordrenummer, Model model) {
        Restordre r = repository.read(restordrenummer);
        model.addAttribute("restordre", r);
        return "UpdateAll";
    }

    @PostMapping("/restordre/ret")
    public String ret(@Valid @ModelAttribute Restordre restordre, @RequestParam(value = "restordre_nummer") Integer restordrenummer) {

        restordre.setRestordre_nummer(restordrenummer);
        repository.update(restordre);
        return "redirect:/";
    }


    @GetMapping("restordre/create")
    public String crea(Model model) {
        Restordre r = new Restordre();
        model.addAttribute("restordre", r);
        return "Create";
    }

    @PostMapping("restordre/create")
    public String crea(@ModelAttribute("restordre") Restordre restordre) {
        repository.create(restordre);
        return "redirect:/";
    }

    @GetMapping("restordre/delete")
    public String delete(@RequestParam("restordre_nummer") String restordre_nummer) {
        repository.delete(restordre_nummer);
        return "redirect:/";
    }


}
