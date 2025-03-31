import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getTypeInstitutionById, updateTypeInstitution } from "../services/api";
import TypeInstitutionForm from "../components/TypeInstitutionForm";

const TypeInstitutionEdit = () => {
	const { id } = useParams();
	const [formData, setFormData] = useState({ id:"", name: "" });
	const [errorMessage, setErrorMessage] = useState("");
	const navigate = useNavigate();

	useEffect(() => {
		getTypeInstitutionById(id).then((response) => setFormData(response.data));
	}, [id]);

	const handleSubmit = async (e) => {
		e.preventDefault();
		try {
			await updateTypeInstitution(formData);
			navigate("/typeinstitution");
		} catch (error) {
			if(error.response && error.response.data) {
				setErrorMessage(error.response.data);
			} else {
				setErrorMessage("Erro ao atualizar o tipo.");
			}
		}
	};

	return (
		<div>
			<TypeInstitutionForm
				title={"Editar Tipo"}
				formData={formData}
				setFormData={setFormData}
				onSubmit={handleSubmit}
				errors={errorMessage}
			/>
		</div>
	);
};

export default TypeInstitutionEdit;
