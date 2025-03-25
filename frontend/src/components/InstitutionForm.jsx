import { useNavigate } from "react-router-dom";
import "../styles/InstitutionForm.css";

const InstitutionForm = ({ title, formData, setFormData, onSubmit, errors }) => {
	const navigate = useNavigate();

	return (
		<div className="container">
			<h1 className="title">{title}</h1>

			<form onSubmit={onSubmit} className="form">
				<div className="input-container">
					<label htmlFor="name" className="input-label">Nome do Instituição</label>
					<input type="text" id="name" className="input-field"
						placeholder="Digite o nome da instituição"
						value={formData.name}
						onChange={(e) => setFormData({ ...formData, name: e.target.value })}
						required
					/>
					{errors?.name && <p style={{ color: "red" }}>{errors.name}</p>} {/* Exibe erro do nome */}
				</div>
				<div className="input-container">
					<label htmlFor="type" className="input-label">Tipo da Instituição</label>
					<input type="text"
						placeholder="Digite o tipo da instituição"
						id="type"
						className="input-field"
						value={formData.type}
						onChange={(e) => setFormData({ ...formData, type: e.target.value })}
						required
					/>
					{errors?.type && <p style={{ color: "red" }}>{errors.type}</p>} {/* Exibe erro do tipo */}
				</div>
				{errors?.message && <p style={{ color: "red" }}>{errors.message}</p>} {/* Exibe erro geral */}
				<div className="button-container">
					<button type="button" className="back-button" onClick={() => navigate(-1)}>
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

export default InstitutionForm;