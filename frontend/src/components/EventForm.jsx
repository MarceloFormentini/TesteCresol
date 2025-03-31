import { useNavigate } from "react-router-dom";
import "../styles/EventForm.css";

const EventForm = ({ title, formData, setFormData, onSubmit, errors, institutionId }) => {
	const navigate = useNavigate();

	return (
		<div className="container-form">
			<h1 className="title">{title}</h1>
			<form onSubmit={onSubmit} className="form">
				<div className="input-container">
					<label htmlFor="name" className="input-label">Nome do Evento</label>
					<input type="text" id="name" className="input-field"
						placeholder="Digite o nome do evento"
						value={formData.name}
						onChange={(e) => setFormData({ ...formData, name: e.target.value })}
						required
					/>
					{errors?.name && <p style={{ color: "red" }}>{errors.name}</p>}
				</div>
				<div className="input-container">
					<label htmlFor="description" className="input-label">Descrição do Evento</label>
					<input type="text" id="description" className="input-field"
						placeholder="Digite a descrição do evento"
						value={formData.description}
						onChange={(e) => setFormData({ ...formData, description: e.target.value })}
						required
					/>
					{errors?.description && <p style={{ color: "red" }}>{errors.description}</p>}
				</div>
				<div className="input-container">
					<label htmlFor="startDate" className="input-label">Data/Hora Início do Evento</label>
					<input
						type="datetime-local"
						id="startDate"
						className="input-field"
						value={formData.startDate}
						onChange={(e) => setFormData({ ...formData, startDate: e.target.value })}
						required
					/>
					{errors?.startDate && <p style={{ color: "red" }}>{errors.startDate}</p>}
				</div>

				<div className="input-container">
					<label htmlFor="endDate" className="input-label">Data/Hora Fim do Evento</label>
					<input
						type="datetime-local"
						id="endDate"
						className="input-field"
						value={formData.endDate}
						onChange={(e) => setFormData({ ...formData, endDate: e.target.value })}
						required
					/>
					{errors?.endDate && <p style={{ color: "red" }}>{errors.endDate}</p>}
				</div>
				<div className="button-container">
					<button type="button" className="back-button" onClick={() => navigate(`/institution/${institutionId}/event`)}>
						Voltar
					</button>
					<button type="submit" className="save-button">
						Salvar
					</button>
				</div>
			</form>
		</div>
	);
};

export default EventForm;
