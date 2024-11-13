package com.hospital.salud.controller;

import com.hospital.salud.dto.DocumentoDTO;
import com.hospital.salud.entity.Documentos;
import com.hospital.salud.response.ApiResponse;
import com.hospital.salud.service.DocumentosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/documentos")
public class DocumentosController {

    @Autowired
    private DocumentosService documentosService;

    // Obtener todos los documentos
    @GetMapping
    public ResponseEntity<ApiResponse<List<Documentos>>> obtenerDocumentos() {
        List<Documentos> documentos = documentosService.ListarDocumentos();
        return new ResponseEntity<>(
                ApiResponse.<List<Documentos>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Lista de documentos")
                        .data(documentos)
                        .build(),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Documentos>> obtenerDocumentoPorId(@PathVariable Long id) {
        Documentos documento = documentosService.obtenerById(id);
        return new ResponseEntity<>(
                ApiResponse.<Documentos>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Documento encontrado")
                        .data(documento)
                        .build(),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Documentos>> crearDocumento(@RequestBody DocumentoDTO documentoDTO) {
        Documentos documento = documentosService.save(documentoDTO);
        return new ResponseEntity<>(
                ApiResponse.<Documentos>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Documento creado exitosamente")
                        .data(documento)
                        .build(),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Documentos>> actualizarDocumento(
            @PathVariable Long id,
            @RequestBody DocumentoDTO documentoDTO) {
        Documentos documentoActualizado = documentosService.actualizar(id, documentoDTO);
        return new ResponseEntity<>(
                ApiResponse.<Documentos>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Documento actualizado exitosamente")
                        .data(documentoActualizado)
                        .build(),
                HttpStatus.OK
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Documentos>> eliminarDocumento(@PathVariable Long id) {
        Documentos documentoEliminado = documentosService.eliminar(id);
        return new ResponseEntity<>(
                ApiResponse.<Documentos>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Documento eliminado (desactivado) exitosamente")
                        .data(documentoEliminado)
                        .build(),
                HttpStatus.OK
        );
    }
}
