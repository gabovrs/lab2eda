import java.util.Queue;
import java.util.Stack;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

public class Electo {

    public static class Voto {
        private int id;
        private int votanteId;
        private int candidatoId;
        private String timestamp;

        public Voto(int id, int votanteId, int candidatoId, String timestamp) {
            this.id = id;
            this.votanteId = votanteId;
            this.candidatoId = candidatoId;
            this.timestamp = timestamp;
        }

        public int getID() {
            return id;
        }

        public int getVotanteID() {
            return votanteId;
        }

        public int getCandidatoID() {
            return candidatoId;
        }

        public String getTimestamp() {
            return timestamp;
        }
    }

    public static class Candidato {
        private int id;
        private String nombre;
        private String partido;
        private Queue<Voto> votosRecibidos;

        public Candidato(int id, String nombre, String partido) {
            this.id = id;
            this.nombre = nombre;
            this.partido = partido;
            this.votosRecibidos = new LinkedList<>();
        }

        public void agregarVoto(Voto voto) {
            this.votosRecibidos.offer(voto);
        }

        public void eliminarVoto(Voto voto) {
            this.votosRecibidos.remove(voto);
        }

        public int getID() {
            return id;
        }

        public String getNombre() {
            return nombre;
        }

        public String getPartido() {
            return partido;
        }

        public Queue<Voto> getVotosRecibidos() {
            return votosRecibidos;
        }
    }

    public static class Votante {
        private int id;
        private String nombre;
        private boolean yaVoto;

        public Votante(int id, String nombre) {
            this.id = id;
            this.nombre = nombre;
            this.yaVoto = false;
        }

        public void marcarComoVotado() {
            this.yaVoto = true;
        }

        public boolean getYaVoto() {
            return this.yaVoto;
        }

        public int getID() {
            return id;
        }

        public String getNombre() {
            return nombre;
        }
    }

    public static class UrnaElectoral {
        private LinkedList<Candidato> listaCandidatos;
        private Stack<Voto> historialVotos;
        private Queue<Voto> votosReportados;
        int idCounter;

        public UrnaElectoral() {
            listaCandidatos = new LinkedList<>();
            historialVotos = new Stack<>();
            votosReportados = new LinkedList<>();
            idCounter = 1;
        }

        public Candidato buscarCandidato(int id) {
            for (Candidato c : listaCandidatos) {
                if (c.getID() == id) {
                    return c;
                }
            }
            return null;
        }

        public boolean verificarVotante(Votante votante) {
            return votante.getYaVoto();
        }

        public boolean registrarVoto(Votante votante, int candidatoID) {
            if (verificarVotante(votante)) {
                System.out.println("El votante " + votante.getNombre() + " ya ha votado.");
                Candidato candidato = buscarCandidato(candidatoID);
                if (candidato != null) {
                    for (Voto v : candidato.getVotosRecibidos()) {
                        if (v.getVotanteID() == votante.getID()) {
                            reportarVoto(candidato, v.getID());
                        }
                    }
                }
                return false;
            }

            Candidato candidato = buscarCandidato(candidatoID);
            
            if (candidato == null) {
                System.out.println("Candidato no encontrado.");
                return false;
            }

            String timestamp = java.time.LocalTime.now().toString();
            timestamp = timestamp.substring(0, 8);

            Voto voto = new Voto(idCounter, votante.getID(), candidatoID, timestamp);

            candidato.agregarVoto(voto);
            historialVotos.push(voto);
            votante.marcarComoVotado();
            idCounter++;

            return true;
        }

        public boolean reportarVoto(Candidato candidato, int idVoto) {
            System.out.println("Reportando voto..." + idVoto);
            Queue<Voto> votos = candidato.getVotosRecibidos();

            for (Voto v : votos) {
                if (v.getID() == idVoto) {
                    votosReportados.offer(v);
                    candidato.eliminarVoto(v);
                    System.out.println("Voto reportado: " + v.getID());
                    return true;
                }
            }
            return false;
        }

        public Map<String, Integer> obtenerResultados() {
            Map<String, Integer> resultados = new HashMap<>();
            for (Candidato c : listaCandidatos) {
                resultados.put(c.getNombre(), c.getVotosRecibidos().size());
            }
            return resultados;
        }

        public void agregarCandidato(Candidato candidato) {
            listaCandidatos.add(candidato);
        }
    }

    public static void main(String[] args) {
        UrnaElectoral urna = new UrnaElectoral();

        // Crear candidatos
        Candidato piñera = new Candidato(1, "Sebastián Piñera", "Chile Vamos");
        Candidato bachelet = new Candidato(2, "Michelle Bachelet", "La Fuerza de la Mayoría");
        Candidato kast = new Candidato(3, "José Antonio Kast", "Partido Republicano");
        Candidato narvaez = new Candidato(4, "Paula Narváez", "Partido Socialista");
        Candidato jadue = new Candidato(5, "Daniel Jadue", "Partido Comunista");
        Candidato provoste = new Candidato(6, "Yasna Provoste", "Partido Demócrata Cristiano");
        Candidato sichel = new Candidato(7, "Joaquín Sichel", "Partido de la Gente");
        Candidato parisi = new Candidato(8, "Franco Parisi", "Partido de la Gente");

        // Agregar candidatos a la urna
        urna.agregarCandidato(piñera);
        urna.agregarCandidato(bachelet);
        urna.agregarCandidato(kast);
        urna.agregarCandidato(narvaez);
        urna.agregarCandidato(jadue);
        urna.agregarCandidato(provoste);
        urna.agregarCandidato(sichel);
        urna.agregarCandidato(parisi);

        // Simulación de votantes
        Votante votante1 = new Votante(1, "Gabriel Varas");
        Votante votante2 = new Votante(2, "Allen Mora");

        // Votante 1 vota por Piñera
        urna.registrarVoto(votante1, piñera.getID());
        // Votante 2 vota por Bachelet
        urna.registrarVoto(votante2, bachelet.getID());

        // Votante 1 intenta votar nuevamente
        urna.registrarVoto(votante1, bachelet.getID());
    }
}