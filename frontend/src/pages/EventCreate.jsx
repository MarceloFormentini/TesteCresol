import { useState } from "react";
import { createEvent } from "../services/api";
import { useNavigate, useParams } from "react-router-dom";
import EventForm from "../components/EventForm";

const EventCreate = () => {
	const { institutionId } = useParams();
	const [formData, setFormData] = useState({ name: "", description:"", startDate: "", endDate: ""});
	const [errors, setErrors] = useState({});
	const navigate = useNavigate();

	// Função para formatar a data antes de enviar
	const formatDate = (date) => {
		if (!date) {
			console.error("Data inválida recebida:", date);
			return "";
		}
		return new Date(date).toISOString().slice(0, 19); // Converte para yyyy-MM-ddTHH:mm:ss
	};

	const handleSubmit = async (e) => {
		e.preventDefault();
		setErrors({});
		console.error(formData);

		// Formata as datas antes de enviar
		const eventFormatted = {
			...formData,
			startDate: formatDate(formData.startDate),
			endDate: formatDate(formData.endDate),
		};

		try {
			console.error(eventFormatted);
			await createEvent(institutionId, eventFormatted);
			navigate(`/institution/${institutionId}/event`);
		} catch (error) {
			console.error(error.response);
			if (error.response && error.response.data) {
				setErrors(error.response.data);
			}
		}
	};

	return (
		<div>
			<EventForm
				title={"Cadastrar Evento"}
				formData={formData}
				setFormData={setFormData}
				onSubmit={handleSubmit}
				errors={errors}
				institutionId={institutionId}
			/>
		</div>
	);
};

export default EventCreate;