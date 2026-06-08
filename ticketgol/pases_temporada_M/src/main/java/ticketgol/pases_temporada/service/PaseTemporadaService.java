package ticketgol.pases_temporada.service;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ticketgol.pases_temporada.model.PaseTemporada;
import ticketgol.pases_temporada.repository.PaseTemporadaRepository;
import ticketgol.pases_temporada.webclient.PaseUsuario;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Transactional
@Service
public class PaseTemporadaService {

    @Autowired
    private PaseTemporadaRepository ptRepository;

    @Autowired
    private PaseUsuario paseUsuario;

    public List<PaseTemporada> findAllPasesTemporada() {
        return ptRepository.findAll();
    }

    public PaseTemporada savePaseTemporada(PaseTemporada pt) {
        Map<String, Object> usuario = paseUsuario.getUsuarioById(pt.getId());
        if (usuario == null || usuario.isEmpty()) {
            throw new RuntimeException("Usuario con id" + pt.getId() + " no existe");
        }
        usuario.get()
    }


}

