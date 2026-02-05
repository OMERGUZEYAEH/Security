package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.Entity.Note;
import org.example.Entity.User;
import org.example.repository.NoteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteRepository noteRepository;

    @GetMapping
    public ResponseEntity<List<Note>> getMyNotes(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(noteRepository.findByUser(user));
    }

    @PostMapping
    public ResponseEntity<Note> createNote(@RequestBody Note note, @AuthenticationPrincipal User user) {
        System.out.println(user.toString());
        note.setUser(user);
        return ResponseEntity.ok(noteRepository.save(note));
    }
    @DeleteMapping("/{id}")
    public void deleteNote(@PathVariable Long id) {
        noteRepository.deleteById(id);
    }
}