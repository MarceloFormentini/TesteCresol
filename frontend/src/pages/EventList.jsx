import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getEventsByInstitution, deleteEvent } from "../services/api";
import "../styles/EventList.css";

const EventList = () => {
	const { institutionId } = useParams();
	const [events, setEvents] = useState([]);
	const [page, setPage] = useState(0);
	const [totalPages, setTotalPages] = useState(1);
	const navigate = useNavigate();

	useEffect(() => {
		loadEvents();
	}, [page]);

	const loadEvents = () => {
		getEventsByInstitution(institutionId, page)
		.then((response) => {
			setEvents(response.data.content);
			setTotalPages(response.data.totalPages);
		})
		.catch((error) => console.error("Erro ao buscar eventos:", error));
	};

	const handleDelete = async (id) => {
		if (window.confirm("Tem certeza que deseja excluir este evento?")) {
		try {
			await deleteEvent(id);
			loadEvents();
		} catch (error) {
			console.error("Erro ao excluir evento:", error);
		}
		}
	};

	const pageNext = () => {
		if (page + 1 < totalPages) {
			setPage((prevPage) => prevPage + 1);
		}
	};

	const pagePrevious = () => {
		if (page > 0) {
			setPage((prevPage) => prevPage - 1);
		}
	};

	return (
		<div className="container">
			<h1 className="title">Eventos da Instituição</h1>
			<div className="button-container">
				<button className="add-button" onClick={() => navigate(`/institution/${institutionId}/event/create`)}>
					Novo Evento
				</button>
			</div>
			<table className="event-list">
				<thead>
					<tr>
						<th>Nome</th>
						<th>Data Inicial</th>
						<th>Data Final</th>
						<th>Ações</th>
					</tr>
				</thead>
				<tbody>
					{events.map((event) => (
					<tr key={event.id}>
						<td>{event.name}</td>
						<td>{event.startDate}</td>
						<td>{event.endDate}</td>
						<td>
							<button
							onClick={() => navigate(`/institution/${institutionId}/event/edit/${event.id}`)}
							className="action-button edit-button">
								Editar
							</button>
							<button onClick={() => handleDelete(event.id)} className="action-button delete-button">
		 						Excluir
	 						</button>
						</td>
					</tr>
				))}
				</tbody>
			</table>
			<div className="pagination-container">
				<button className="back-button" onClick={() => navigate(-1)}>
					Voltar
				</button>
				<div className="pagination">
					<button disabled={page === 0} onClick={pagePrevious}>
						Anterior
					</button>
					<span>Página {page + 1} de {totalPages}</span>
					<button disabled={page === totalPages - 1} onClick={pageNext}>
						Próxima
					</button>
				</div>
			</div>
		</div>
	);
};

export default EventList;
