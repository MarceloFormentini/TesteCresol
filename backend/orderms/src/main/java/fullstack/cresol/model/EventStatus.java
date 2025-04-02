package fullstack.cresol.model;

import java.time.LocalDateTime;

public enum EventStatus {

	ATIVO(1),
	EM_ANDAMENTO(2),
	FINALIZADO(3),
	CANCELADO(4);

	private final int code;

	EventStatus(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
	
	public static int fromCode(LocalDateTime startDate, LocalDateTime endDate) {
		LocalDateTime now = LocalDateTime.now();
		
		if(now.isBefore(startDate)) {
			return ATIVO.code; 				// Evento não começou
        } else if (now.isAfter(endDate)) {
            return FINALIZADO.code; 		// Evento já terminou
        } else {
            return EM_ANDAMENTO.code; 		// Evento em andamento
        }
    }

	public static int eventCancel() {
		return CANCELADO.code;
	}
}
