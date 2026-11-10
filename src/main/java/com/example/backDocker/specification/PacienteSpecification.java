package com.example.backDocker.specification;

import com.example.backDocker.dto.PacienteFilter;
import com.example.backDocker.model.Paciente;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import java.util.Locale;

public final class PacienteSpecification {
    private PacienteSpecification() {}

    public static Specification<Paciente> conFiltros(PacienteFilter filter) {
        Specification<Paciente> specification = (root, query, builder) -> builder.conjunction();
        if (StringUtils.hasText(filter.getNombre())) {
            String nombre = contiene(filter.getNombre());
            specification = specification.and((root, query, builder) ->
                    builder.like(builder.lower(root.get("nombre")), nombre));
        }
        if (StringUtils.hasText(filter.getApellido())) {
            String apellido = contiene(filter.getApellido());
            specification = specification.and((root, query, builder) ->
                    builder.like(builder.lower(root.get("apellido")), apellido));
        }
        if (StringUtils.hasText(filter.getDpi())) {
            String dpi = contiene(filter.getDpi());
            specification = specification.and((root, query, builder) ->
                    builder.like(builder.lower(root.get("dpi")), dpi));
        }
        if (filter.getActivo() != null) {
            specification = specification.and((root, query, builder) ->
                    builder.equal(root.get("activo"), filter.getActivo()));
        }
        return specification;
    }

    private static String contiene(String value) {
        return "%" + value.trim().toLowerCase(Locale.ROOT) + "%";
    }
}
