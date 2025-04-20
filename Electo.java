import java.util.Queue;
import java.util.Stack;
import java.util.LinkedList;

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

        public boolean verificarVotante(Votante votante) {
            return votante.getYaVoto();
        }

        public boolean registrarVoto(Votante votante, int candidatoID) {
            if (verificarVotante(votante)) {
                System.out.println("La persona ya voto.");
                return false;
            }

            Candidato candidato = null;
            for (Candidato c: listaCandidatos) {
                if (c.getID() == candidatoID) {
                    candidato = c;
                }
            }
            
            if (candidato == null) {
                System.out.println("Candidato no encontrado.");
                return false;
            }
            
            Voto voto = new Voto(idCounter, votante.getID(), candidatoID, "");

            candidato.agregarVoto(voto);
            historialVotos.push(voto);
            votante.marcarComoVotado();
            idCounter++;

            return true;
        }

        public boolean reportarVoto(Candidato candidato, int idVoto) {
            Queue<Voto> votos = candidato.getVotosRecibidos();

            for(Voto v: votos) {
                System.out.println(v.votanteId);
            }
            return true;
        }

        public void obtenerResultados() {
            for (Candidato c : listaCandidatos) {
                System.out.println(c.getNombre() + ": " + c.getVotosRecibidos().size());
            }
        }
    }

    
    public static void main(String[] args) {
        UrnaElectoral urna = new UrnaElectoral();
        
        Candidato piñera = new Candidato(1, "Sebastian Piñera", "Independiente");
        Candidato bachelet = new Candidato(2, "Michelle Bachelet", "Socialista");
        
        urna.listaCandidatos.add(piñera);
        urna.listaCandidatos.add(bachelet);

        Votante votante = new Votante(1, "Gabriel Varas");
        Votante votante2 = new Votante(2, "Allen Mora");
        urna.registrarVoto(votante, 1);
        urna.registrarVoto(votante2, 1);

        urna.reportarVoto(piñera, 2);

        // urna.obtenerResultados();
    }
}