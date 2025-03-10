package com.example.book_management.controller;

import com.example.book_management.model.Publisher;
import com.example.book_management.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

@Controller
@RequestMapping("/publishers")
public class PublisherController {
    @Autowired
    private PublisherService publisherService;

    // show all publishers
    @GetMapping
    public String listPublishers(Model model) {
        model.addAttribute("publishers", publisherService.getAllPublishersSortedByName());
        return "publishers/listPublishers";
    }

    // add a new publisher
    @GetMapping("/add")
    public String addPublisherForm(Model model) {
        model.addAttribute("publisher", new Publisher());
        return "publishers/addPublisher";
    }

    @PostMapping
    public String savePublisher(@Valid @ModelAttribute Publisher publisher,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "publishers/addPublisher";
        }

        // checking for duplicate publisher
        if (publisherService.existsByName(publisher.getName())) {
            model.addAttribute("errorMessage", "Publisher name already exists.");
            return "publishers/addPublisher"; // Stay on the same page
        }

        // save the publisher if all validations pass
        publisherService.savePublisher(publisher);
        redirectAttributes.addFlashAttribute("successMessage", "Publisher added successfully!");
        return "redirect:/publishers";
    }

    // form to edit a publisher
    @GetMapping("/edit/{id}")
    public String editPublisherForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Publisher publisher = publisherService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid publisher ID: " + id));
        model.addAttribute("publisher", publisher);
        return "publishers/editPublisher";
    }

    // update an existing publisher
    @PostMapping("/update/{id}")
    public String updatePublisher(@PathVariable("id") Long id, @ModelAttribute Publisher publisher, RedirectAttributes redirectAttributes) {
        if (publisherService.existsByNameExcludingId(publisher.getName(), id)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Publisher name already exists.");
            return "redirect:/publishers/edit/" + id;
        }
        publisherService.savePublisher(publisher);
        redirectAttributes.addFlashAttribute("successMessage", "Publisher updated successfully!");
        return "redirect:/publishers";
    }

    @GetMapping("/delete/{id}")
    public String deletePublisher(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            publisherService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Publisher deleted successfully!");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete publisher as it is referenced by books.");
        }
        return "redirect:/publishers";
    }
}
