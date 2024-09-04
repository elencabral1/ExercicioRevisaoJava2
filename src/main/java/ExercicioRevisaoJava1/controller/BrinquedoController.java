package ExercicioRevisaoJava1.controller;

import java.util.ArrayList;
import java.util.List;
import ExercicioRevisaoJava1.model.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ExercicioRevisaoJava1.model.Brinquedo;
import ExercicioRevisaoJava1.repository.BrinquedoRepositorio;

@CrossOrigin(origins = "https://exerciciorevisaojava2.onrender.com")
@Controller
@RequestMapping("/brinquedos")
public class BrinquedoController {
    @Autowired
    BrinquedoRepositorio brinquedoRepositorio;

    @GetMapping
    public String listAllBrinquedos(@RequestParam(required = false) String nome, Model model) {
        List<Brinquedo> brinquedos = new ArrayList<>();
        try {
            if (nome == null) {
                brinquedoRepositorio.findAll().forEach(brinquedos::add);
            } else {
                brinquedoRepositorio.findByNameContaining(nome).forEach(brinquedos::add);
            }

            model.addAttribute("brinquedos", brinquedos);
            return "brinquedos"; // Nome do arquivo HTML alterado para 'brinquedos.html'
        } catch (Exception e) {
            model.addAttribute("error", new ErrorResponse("Erro ao listar brinquedos.", e.getMessage()));
            return "error";
        }
    }

    @GetMapping("/{id}")
    public String getBrinquedoById(@PathVariable("id") long id, Model model) {
        Brinquedo brinquedo = brinquedoRepositorio.findById(id);
        if (brinquedo != null) {
            model.addAttribute("brinquedo", brinquedo);
            return "detail"; // Nome do arquivo HTML para detalhes do brinquedo
        } else {
            model.addAttribute("error", new ErrorResponse("Brinquedo não encontrado.", "ID: " + id));
            return "error"; // Nome do arquivo HTML para exibir erro
        }
    }

    @GetMapping("/add")
    public String showAddBrinquedoForm(Model model) {
        model.addAttribute("brinquedo", new Brinquedo());
        return "add"; // Nome do arquivo HTML para adicionar brinquedos
    }

    @PostMapping("/add")
    public String createBrinquedo(@ModelAttribute Brinquedo brinquedo) {
        try {
            brinquedoRepositorio.save(brinquedo);
            return "redirect:/brinquedos"; // Redireciona para a lista de brinquedos
        } catch (Exception e) {
            return "error";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditBrinquedoForm(@PathVariable("id") long id, Model model) {
        Brinquedo brinquedo = brinquedoRepositorio.findById(id);
        if (brinquedo != null) {
            model.addAttribute("brinquedo", brinquedo);
            return "edit"; // Nome do arquivo HTML para editar brinquedos
        } else {
            model.addAttribute("error", new ErrorResponse("Brinquedo não encontrado.", "ID: " + id));
            return "error";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateBrinquedo(@PathVariable("id") long id, @ModelAttribute Brinquedo brinquedo) {
        Brinquedo _brinquedo = brinquedoRepositorio.findById(id);
        if (_brinquedo != null) {
            _brinquedo.setNome(brinquedo.getNome());
            _brinquedo.setTipo(brinquedo.getTipo());
            _brinquedo.setClassificacao(brinquedo.getClassificacao());
            _brinquedo.setTamanho(brinquedo.getTamanho());
            _brinquedo.setPreco(brinquedo.getPreco());

            brinquedoRepositorio.save(_brinquedo);
            return "redirect:/brinquedos"; // Redireciona para a lista de brinquedos
        } else {
            return "error";
        }
    }


    @PostMapping("/delete")
    public String deleteBrinquedo(@RequestParam("id") long id) {
        try {
            brinquedoRepositorio.deleteById(id);
            return "redirect:/brinquedos"; // Redireciona para a lista de brinquedos
        } catch (Exception e) {
            return "error";
        }
    }


}