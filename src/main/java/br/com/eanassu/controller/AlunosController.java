package br.com.eanassu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.eanassu.dao.DaoAluno;
import br.com.eanassu.pojo.Aluno;

@Controller
@RequestMapping("/alunos")
public class AlunosController {

	private DaoAluno dao;

	@Autowired
	public AlunosController(DaoAluno dao) {
		this.dao = dao;
	}

	@GetMapping
	public String showAlunos() {
		return "alunos";
	}

	@GetMapping("/")
	public String showAlunosBarra() {
		return "alunos";
	}

	@GetMapping("/new")
	public String showInsertForm(Model model) {
		model.addAttribute("aluno", new Aluno());
		return "create-aluno";
	}
	@PostMapping
	public String createAluno(@ModelAttribute Aluno aluno) {
		dao.insert(aluno);
		return "redirect:alunos";
	}
	@GetMapping("/lista")
	public String listarAlunos(Model model) {
		List<Aluno> lista = dao.getLista();
		model.addAttribute("alunos", lista);
		return "lista-alunos";
	}
	@GetMapping("/busca")
	public String buscarAluno() {
		return "busca-aluno";
	}
	@GetMapping("/showUpdate")
	public String showUpdate(@RequestParam int ra, Model model) {
		Aluno aluno = dao.buscarPeloRa(ra);
		model.addAttribute("aluno", aluno);
		return "alterar-aluno";
	}
	@PostMapping("/update")
	public String update(@ModelAttribute Aluno aluno) {
		dao.update(aluno);
		return "alunos";
	}
	@GetMapping("/excluir")
	public String showExcluir() {
		return "excluir-aluno";
	}
	@GetMapping("/delete")
	public String delete(@RequestParam int ra) {
		Aluno aluno = dao.buscarPeloRa(ra);
		dao.delete(aluno);
		return "alunos";
	}
}
