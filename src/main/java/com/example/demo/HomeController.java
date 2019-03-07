package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Controller
public class HomeController {
    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    Book_AuthorRepository book_authorRepository;

    @RequestMapping("/booklist")
    public String getBookList(Model model) {
        model.addAttribute("books",bookRepository.findAll());

        Iterable<Book> books = bookRepository.findAll();
        Iterator<Book> bookIterator = books.iterator();

        while(bookIterator.hasNext()){
            System.out.println("authors set: " + bookIterator.next().getAuthors());
        }

        return "booklist";
    }
    @RequestMapping("/authorlist")
    public String getAuthorList(Model model) {
        model.addAttribute("authors",authorRepository.findAll());
        return "authorlist";
    }
    @GetMapping("/add")
    public String bookForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("authors",authorRepository.findAll());
        return "bookform";
    }
    @GetMapping("/addauthor")
    public String authorForm(Model model) {
        model.addAttribute("author", new Author());
        model.addAttribute("books",bookRepository.findAll());
        return "authorform";
    }
    @PostMapping("/process")
    public String processForm(@ModelAttribute("book") @Valid Book book, BindingResult result, @RequestParam("authors") String authorid) {
        if (result.hasErrors()){
            return "bookform";
        }
        if (book.getIsInStock().equalsIgnoreCase("no")){
            book.setAmount(0);
        }

        Author author = authorRepository.findById(new Long(authorid)).get();
        Book_Author bk = new Book_Author(book, author);
        book_authorRepository.save(bk);
        Set<Book_Author> authorSet = new HashSet<>();
        authorSet.add(bk);
        book.setAuthors(authorSet);
        bookRepository.save(book);
        return "redirect:/booklist";
    }
    @PostMapping("/processauthor")
    public String processAuthorForm(@ModelAttribute("author") @Valid Author author, BindingResult result) {
        if (result.hasErrors()){
            return "authorform";
        }
        authorRepository.save(author);
        return "redirect:/authorlist";
    }
    @RequestMapping("/detailbook/{id}")
    public String detailsOfBook(@PathVariable("id") long id, Model model){
        model.addAttribute("book",bookRepository.findById(id).get());
        return "detailbook";
    }
    @RequestMapping("/updatebook/{id}")
    public String updateBookList (@PathVariable("id") long id, Model model){
        model.addAttribute("book",bookRepository.findById(id).get());
        model.addAttribute("authors",authorRepository.findAll());
        return "bookform";
    }
    @RequestMapping("/deletebook/{id}")
    public String deleteBookList (@PathVariable("id") long id, Model model){
        bookRepository.deleteById(id);
        return "redirect:/booklist";
    }
    @RequestMapping("/detailauthor/{id}")
    public String detailsOfAuthor(@PathVariable("id") long id, Model model){
        model.addAttribute("author",authorRepository.findById(id).get());
        return "detailauthor";
    }
    @RequestMapping("/updateauthor/{id}")
    public String updateAuthorList (@PathVariable("id") long id, Model model){
        model.addAttribute("author",authorRepository.findById(id).get());
        model.addAttribute("books",bookRepository.findAll());
        return "authorform";
    }
    @RequestMapping("/deleteauthor/{id}")
    public String deleteAuthorList (@PathVariable("id") long id, Model model){
        authorRepository.deleteById(id);
        return "redirect:/authorlist";
    }
}
