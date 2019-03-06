package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
public class HomeController {
    @Autowired
    BookRepository bookRepository;

    @RequestMapping("/")
    public String getHome(Model model) {
        model.addAttribute("books",bookRepository.findAll());
        return "booklist";
    }
    @GetMapping("/add")
    public String bookForm(Model model) {
        model.addAttribute("book", new Book());
        return "bookform";
    }
    @PostMapping("/process")
    public String processForm(@Valid Book book, BindingResult result) {
        if (result.hasErrors()){
            return "bookform";
        }
        if (book.getIsInStock().equalsIgnoreCase("no")){
            book.setAmount(0);
        }
        bookRepository.save(book);
        return "redirect:/";
    }
    @RequestMapping("/detail/{id}")
    public String detailsOfBook(@PathVariable("id") long id, Model model){
        model.addAttribute("book",bookRepository.findById(id).get());
        return "detail";
    }
    @RequestMapping("/update/{id}")
    public String updateBookList (@PathVariable("id") long id, Model model){
        model.addAttribute("book",bookRepository.findById(id).get());
        return "bookform";
    }
    @RequestMapping("/delete/{id}")
    public String deleteBookList (@PathVariable("id") long id, Model model){
        bookRepository.deleteById(id);
        return "redirect:/";
    }
}
