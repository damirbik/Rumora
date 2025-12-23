// org.lamdateam.rumora_demo.controller.DebugController.java

package org.lamdateam.rumora_demo.controller;

import org.lamdateam.rumora_demo.entity.Comment;
import org.lamdateam.rumora_demo.repository.ICommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/debug")
public class DebugController {

    @Autowired
    private ICommentRepository commentRepository;

    @GetMapping("/comments")
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }
}