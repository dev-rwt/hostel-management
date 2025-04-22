package com.example.hostelmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.hostelmanagement.entity.Notice;
import com.example.hostelmanagement.repository.NoticeRepository;

@Controller
@RequestMapping("/notices")
public class NoticeController {

    @Autowired
    private NoticeRepository noticeRepository;

    // Display all notices
    @GetMapping
    public String getAllNotices(Model model) {
        List<Notice> notices = noticeRepository.findAllByOrderByDateDesc();
        model.addAttribute("notices", notices);
        return "notices_list";
    }

    // Show form to create a new notice
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("notice", new Notice());
        return "notices_create";
    }

    // Handle the submission of a new notice
    @PostMapping("/save")
    public String saveNotice(@ModelAttribute Notice notice, RedirectAttributes redirectAttributes) {
        noticeRepository.save(notice);
        redirectAttributes.addFlashAttribute("successMessage", "Notice has been added successfully");
        return "redirect:/notices";
    }

    // Show a specific notice details
    @GetMapping("/{id}")
    public String viewNotice(@PathVariable Long id, Model model) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid notice Id:" + id));
        model.addAttribute("notice", notice);
        return "notices_view";
    }

    // Show form to edit an existing notice
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid notice Id:" + id));
        model.addAttribute("notice", notice);
        return "notices_edit";
    }

    // Handle the deletion of a notice
    @GetMapping("/delete/{id}")
    public String deleteNotice(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid notice Id:" + id));
        noticeRepository.delete(notice);
        redirectAttributes.addFlashAttribute("successMessage", "Notice has been deleted successfully");
        return "redirect:/notices";
    }

    // Get recent notices for dashboard
    @GetMapping("/recent")
    public String getRecentNotices(Model model) {
        List<Notice> recentNotices = noticeRepository.findTop5ByOrderByDateDesc();
        model.addAttribute("recentNotices", recentNotices);
        return "fragments/recent-notices :: recentNoticesList";
    }
}

