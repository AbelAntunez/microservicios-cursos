package com.curso.microservicios.app.cursos.controllers;

import com.curso.microservicios.app.cursos.models.entity.Curso;
import com.curso.microservicios.app.cursos.services.CursoService;
import com.curso.microservicios.commons.alumnos.models.entity.Alumno;
import com.curso.microservicios.commons.controllers.CommonController;
import com.curso.microservicios.commons.examenes.models.entity.Examen;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class CursoController extends CommonController<Curso, CursoService> {

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Curso curso, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) {
            return this.validar(result);
        }
        Optional<Curso> o = this.service.findById(id);
        if (o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Curso cursoDb = o.get();
        cursoDb.setNombre(curso.getNombre());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(cursoDb));
    }

    @PutMapping("/{id}/asignar-alumnos")
    public ResponseEntity<?> asignarAlumnos(@RequestBody List<Alumno> alumnos, @PathVariable Long id) {
        Optional<Curso> o = this.service.findById(id);
        if (o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Curso cursoDb = o.get();
        alumnos.forEach(cursoDb::addAlumno);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(cursoDb));
    }

    @PutMapping("/{id}/eliminar-alumno")
    public ResponseEntity<?> eliminarAlumno(@RequestBody Alumno alumno, @PathVariable Long id) {
        Optional<Curso> o = this.service.findById(id);
        if (o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Curso cursoDb = o.get();
        cursoDb.removeAlumno(alumno);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(cursoDb));
    }

    @GetMapping("/alumno/{id}")
    public ResponseEntity<?> buscarPorAlumnoId(@PathVariable Long id) {
        Curso curso = service.findCursoByAlumnoId(id);
        return ResponseEntity.ok(curso);
    }

    @PutMapping("/{id}/asignar-examenes")
    public ResponseEntity<?> asignarExamenes(@RequestBody List<Examen> examenes, @PathVariable Long id) {
        Optional<Curso> o = this.service.findById(id);
        if (o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Curso cursoDb = o.get();
        examenes.forEach(cursoDb::addExamen);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(cursoDb));
    }

    @PutMapping("/{id}/eliminar-examen")
    public ResponseEntity<?> eliminarExamen(@RequestBody Examen examen, @PathVariable Long id) {
        Optional<Curso> o = this.service.findById(id);
        if (o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Curso cursoDb = o.get();
        cursoDb.removeExamen(examen);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(cursoDb));
    }
}
