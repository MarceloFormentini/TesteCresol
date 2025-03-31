import { useEffect, useState } from "react";
import { data, useNavigate, useParams } from "react-router-dom";
import { getEventById, updateEvent } from "../services/api";
import EventForm from "../components/EventForm";

const EventEdit = () => {
	const { institutionId, id } = useParams();
	const [formData, setFormData] = useState({ id, name: "", description: "", startDate: "", endDate: "", institutionId });
	const [errors, setErrors] = useState({});
	const navigate = useNavigate();

	useEffect(() => {
		const fetchEvent = async () => {
			try{
				const response = await getEventById(institutionId, id)
				const event = response.data

				const formattedData = {
					...event,
					startDate: formatDateForInput(event.startDate),
					endDate: formatDateForInput(event.endDate),
				};

				setFormData(formattedData);

			} catch (error) {
				console.error("Erro ao buscar evento por id:", error);
			}
		};
		fetchEvent();
	}, [id, institutionId]);

	const formatDateForInput = (dateString) => {
		if (!dateString) return "";
		const [datePart, timePart] = dateString.split(" ");
		const [day, month, year] = datePart.split("/");
		return `${year}-${month}-${day}T${timePart.slice(0, 5)}`;
	};

	const handleSubmit = async (e) => {
		e.preventDefault();
		setErrors({});

		try {
			// envio dos dados para a API
			const formattedData = {
				...formData,
				startDate: formData.startDate.replace(" ", "T") + ":00", // Converte para o formato esperado pela API
				endDate: formData.endDate.replace(" ", "T") + ":00",
			};

			await updateEvent(institutionId, formattedData);
			navigate(`/institution/${institutionId}/event`);

		} catch (error) {
			if (error.response && error.response.status === 400) {
				setErrors(error.response.data);
			} else {
				console.error("Erro inesperado ao atualizar evento:", error);
			}
		}
	};

	return (
		<div>
			<EventForm
				title={"Editar Evento"}
				formData={formData}
				setFormData={setFormData}
				onSubmit={handleSubmit}
				errors={errors}
				institutionId={institutionId}
			/>
		</div>
	);
};

export default EventEdit;
