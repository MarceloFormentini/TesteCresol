import { useEffect, useState } from "react";
import { useNavigate} from "react-router-dom";
import { getTypeInstitution } from "../services/api"; 
import "../styles/InstitutionForm.css";

const InstitutionForm = ({ title, formData, setFormData, onSubmit, errors }) => {
	const navigate = useNavigate();
	const [typeOptions, setTypeOptions] = useState([]);

	useEffect(() => {
		getTypeInstitution(0) // Supondo que a API suporta paginação
			.then((response) => {
				setTypeOptions(response.data || []); // Armazena os tipos no estado
			})
			.catch((error) => {
				console.error("Erro ao carregar tipos de instituição:", error);
			});
	}, []);

	const handleSubmit = (e) => {
		onSubmit(e, formData);
	};

	return (
		<div className="container">
			<h1 className="title">{title}</h1>

			<form onSubmit={handleSubmit} className="form">
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
					{/* <input type="text"
						placeholder="Digite o tipo da instituição"
						id="type"
						className="input-field"
						value={formData.type}
						onChange={(e) => setFormData({ ...formData, type: e.target.value })}
						required
					/> */}

					<select id="type" className="input-field"
						value={formData.typeInstitution?.id || ""} // Define o valor como o ID do tipo selecionado
						// onChange={(e) => 
						// 	setFormData({ ...formData, typeInstitution: e.target.value })
						// }
						onChange={(e) => {
							const selectedType = typeOptions.find((type) => type.id === Number(e.target.value)); // Busca o objeto completo
							setFormData({ ...formData, typeInstitution: selectedType }); // Atualiza o formData com o objeto completo
						  }}
						required>
						<option value="" disabled>
							Selecione o tipo da instituição
						</option>
						{typeOptions.map((type) => (
							<option key={type.id} value={type.id}>
								{type.name}
							</option>
						))}
					</select>
					{errors?.type && <p style={{ color: "red" }}>{errors.type}</p>} {/* Exibe erro do tipo */}

				</div>
				{errors?.message && <p style={{ color: "red" }}>{errors.message}</p>} {/* Exibe erro geral */}
				<div className="button-container">
					<button type="button" className="back-button" onClick={() => navigate("/institution")}>
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