package pe.edu.utp.zonagamer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.edu.utp.zonagamer.dto.GamerDto;
import pe.edu.utp.zonagamer.dto.LanCenterDto;
import pe.edu.utp.zonagamer.dto.UserDto;
import pe.edu.utp.zonagamer.entity.Gamer;
import pe.edu.utp.zonagamer.entity.LanCenter;
import pe.edu.utp.zonagamer.entity.User;
import pe.edu.utp.zonagamer.repository.GamerRepo;
import pe.edu.utp.zonagamer.repository.LanCenterRepo;
import pe.edu.utp.zonagamer.repository.UserRepo;

import javax.servlet.http.HttpServletRequest;

/**
 * @author UTP
 */
@Controller
@RequestMapping
@SessionAttributes(value = {"dto", "gamerDto", "lanDto", "lan", "gamer"})
public class InicioController {

    @Autowired
    private UserRepo repo;

    @Autowired
    private GamerRepo gamerRepo;

    @Autowired
    private LanCenterRepo lanCenterRepo;

    @GetMapping
    public String inicio(Model model) {
        model.addAttribute("dto", new UserDto());
        model.addAttribute("gamerDto", new GamerDto());
        model.addAttribute("lanDto", new LanCenterDto());
        return "index";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute UserDto dto, HttpServletRequest request, RedirectAttributes attrs) {
        User user = repo.login(dto.getEmail(), dto.getPassword());
        if (user == null) {
            attrs.addFlashAttribute("message", "El usuario o la contraseña son incorrectos.");
            return "redirect:/";
        }

        if (user.getType().equals("G")) {
            request.getSession().setAttribute("gamer", gamerRepo.findByUser(user.getId()));
            return "redirect:/gamer";
        } else {
            request.getSession().setAttribute("lan", lanCenterRepo.findByUser(user.getId()));
            return "redirect:/lancenter";
        }
    }

    @PostMapping("/registergamer")
    @Transactional
    public String registerGamer(@ModelAttribute(name = "gamerDto") GamerDto dto, HttpServletRequest request,
                                RedirectAttributes attrs) {
        if (!dto.getPassword().equals(dto.getConfPassword())) {
            attrs.addFlashAttribute("message", "Las contraseñas no son iguales");
            return "redirect:/";
        }

        User user = repo.findByEmail(dto.getEmail());
        if (user != null) {
            attrs.addFlashAttribute("message", "El correo ya se encuentra registrado");
            return "redirect:/";
        }

        // Crear usuario
        User u = new User();
        u.setEmail(dto.getEmail());
        u.setPassword(dto.getPassword());
        u.setType("G");
        repo.save(u);

        Gamer gamer = new Gamer();
        gamer.setName(dto.getName());
        gamer.setAge(dto.getAge());
        gamer.setLastname(dto.getLastname());
        gamer.setSex(dto.getSex());
        gamer.setUser(u.getId());
        gamerRepo.save(gamer);

        attrs.addFlashAttribute("success", "Se ha registrado correctamente");
        return "redirect:/";
    }

    @PostMapping("/registerlan")
    @Transactional
    public String registerLan(@ModelAttribute(name = "lanDto") LanCenterDto dto, HttpServletRequest request,
                              RedirectAttributes attrs) {
        if (!dto.getPassword().equals(dto.getConfPassword())) {
            attrs.addFlashAttribute("message", "Las contraseñas no son iguales");
            return "redirect:/";
        }

        User user = repo.findByEmail(dto.getEmail());
        if (user != null) {
            attrs.addFlashAttribute("message", "El correo ya se encuentra registrado");
            return "redirect:/";
        }
        // Crear usuario
        User u = new User();
        u.setEmail(dto.getEmail());
        u.setPassword(dto.getPassword());
        u.setType("L");
        repo.save(u);

        LanCenter lanCenter = new LanCenter();
        lanCenter.setAddress(dto.getAddress());
        lanCenter.setDistrict(dto.getDistrict());
        lanCenter.setHorario(dto.getHorario());
        lanCenter.setName(dto.getName());
        lanCenter.setPhone(dto.getPhone());
        lanCenter.setUser(u.getId());
        lanCenterRepo.save(lanCenter);

        attrs.addFlashAttribute("success", "Se ha registrado correctamente");
        return "redirect:/";
    }

    @GetMapping("/gamer")
    public String gamer(HttpServletRequest request) {
        if (request.getSession().getAttribute("gamer") == null) {
            return "redirect:/";
        }
        return "gamer";
    }

    @GetMapping("/lancenter")
    public String lancenter(HttpServletRequest request) {
        if (request.getSession().getAttribute("lan") == null) {
            return "redirect:/";
        }
        return "lancenter";
    }

    @GetMapping("logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/";
    }
}
