package ufro.redsocial.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum Carrera {

    AGRONOMIA("Agronomía"),
    BIOTECNOLOGIA("Biotecnología"),
    INGENIERIA_EN_RECURSOS_NATURALES("Ingeniería en Recursos Naturales"),
    MEDICINA_VETERINARIA("Medicina Veterinaria"),
    CONTADOR_PUBLICO_Y_AUDITOR("Contador Público y Auditor"),
    DERECHO("Derecho"),
    INGENIERIA_COMERCIAL("Ingeniería Comercial"),
    BACHILLERATO_EN_CIENCIAS_SOCIALES("Bachillerato en Ciencias Sociales"),
    PEDAGOGIA_EN_CASTELLANO_Y_COMUNICACION("Pedagogía en Castellano y Comunicación"),
    PEDAGOGIA_EN_CIENCIAS("Pedagogía en Ciencias"),
    PEDAGOGIA_EN_EDUCACION_DIFERENCIAL("Pedagogía en Educación Diferencial"),
    PEDAGOGIA_EN_EDUCACION_FISICA_DEPORTES_Y_RECREACION("Pedagogía en Educación Física, Deportes y Recreación"),
    PEDAGOGIA_EN_HISTORIA_GEOGRAFIA_Y_EDUCACION_CIVICA("Pedagogía en Historia, Geografía y Educación Cívica"),
    PEDAGOGIA_EN_INGLES("Pedagogía en Inglés"),
    PEDAGOGIA_EN_MATEMATICA("Pedagogía en Matemática"),
    PERIODISMO("Periodismo"),
    PSICOLOGIA("Psicología"),
    SOCIOLOGIA("Sociología"),
    TRABAJO_SOCIAL("Trabajo Social"),
    BIOQUIMICA("Bioquímica"),
    INGENIERIA_CIVIL("Ingeniería Civil"),
    INGENIERIA_CIVIL_AMBIENTAL("Ingeniería Civil Ambiental"),
    INGENIERIA_CIVIL_EN_BIOTECNOLOGIA("Ingeniería Civil en Biotecnología"),
    INGENIERIA_CIVIL_ELECTRICA("Ingeniería Civil Eléctrica"),
    INGENIERIA_CIVIL_ELECTRONICA("Ingeniería Civil Electrónica"),
    INGENIERIA_CIVIL_FISICA("Ingeniería Civil Física"),
    INGENIERIA_CIVIL_INFORMATICA("Ingeniería Civil Informática"),
    INGENIERIA_CIVIL_INDUSTRIAL_MENCION_BIOPROCESOS("Ingeniería Civil Industrial mención Bioprocesos"),
    INGENIERIA_CIVIL_INDUSTRIAL_MENCION_INFORMATICA("Ingeniería Civil Industrial mención Informática"),
    INGENIERIA_CIVIL_INDUSTRIAL_MENCION_MECANICA("Ingeniería Civil Industrial mención Mecánica"),
    INGENIERIA_CIVIL_MATEMATICA("Ingeniería Civil Matemática"),
    INGENIERIA_CIVIL_MECANICA("Ingeniería Civil Mecánica"),
    INGENIERIA_CIVIL_QUIMICA("Ingeniería Civil Química"),
    INGENIERIA_CIVIL_TELEMATICA("Ingeniería Civil Telemática"),
    INGENIERIA_EN_CONSTRUCCION("Ingeniería en Construcción"),
    INGENIERIA_INFORMATICA("Ingeniería Informática"),
    PLAN_COMUN_INGENIERIA_CIVIL("Plan Común Ingeniería Civil"),
    MEDICINA("Medicina"),
    ENFERMERIA("Enfermería"),
    KINESIOLOGIA("Kinesiología"),
    NUTRICION_Y_DIETETICA("Nutrición y Dietética"),
    OBSTETRICIA_Y_PUERICULTURA("Obstetricia y Puericultura"),
    QUIMICA_Y_FARMACIA("Química y Farmacia"),
    TECNOLOGIA_MEDICA("Tecnología Médica"),
    TERAPIA_OCUPACIONAL("Terapia Ocupacional"),
    FONOAUDIOLOGIA("Fonoaudiología"),
    ODONTOLOGIA("Odontología"),
    TECNICO_SUPERIOR_EN_TURISMO("Técnico Superior en Turismo"),
    TECNICO_UNIVERSITARIO_EN_ENFERMERIA("Técnico Universitario en Enfermería"),
    TECNICO_GUIA_TURISMO_AVENTURA("Técnico Guía Turismo Aventura");

    private final String nombreLegible;

    Carrera(String nombreLegible) {
        this.nombreLegible = nombreLegible;
    }

    public String getNombreLegible() {
        return nombreLegible;
    }

    public static List<Carrera> listar() {
        return Arrays.stream(values())
                .sorted((a, b) -> a.nombreLegible.compareToIgnoreCase(b.nombreLegible))
                .toList();
    }
    
    public static Optional<Carrera> desde(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            return Optional.empty();
        }
        return Arrays.stream(values())
                .filter(c -> c.name().equalsIgnoreCase(nombre.trim()))
                .findFirst();
    }
}
