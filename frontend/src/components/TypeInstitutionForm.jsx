import { useNavigate } from "react-router-dom";
import "../styles/InstitutionForm.css";

const TypeInstitutionForm = ({ title, formData, setFormData, onSubmit, errors }) => {
	const navigate = useNavigate();

	return (
		<div className="container">
			<h1 className="title">{title}</h1>

			<form onSubmit={onSubmit} className="form">
				<div className="input-container">
					<label htmlFor="name" className="input-label">Tipo</label>
					<input type="text" id="name" className="input-field"
						placeholder="Digite o tipo"
						value={formData.name}
						onChange={(e) => setFormData({ ...formData, name: e.target.value })}
						required
					/>
					{errors?.name && <p style={{ color: "red" }}>{errors.name}</p>} {/* Exibe erro do nome */}
				</div>
				{errors?.message && <p style={{ color: "red" }}>{errors.message}</p>} {/* Exibe erro geral */}
				<div className="button-container">
					<button type="button" className="back-button" onClick={() => navigate("/typeinstitution")}>
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

export default TypeInstitutionForm;