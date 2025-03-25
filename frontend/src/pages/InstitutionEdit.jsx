import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getInstitutionById, updateInstitution } from "../services/api";
import InstitutionForm from "../components/InstitutionForm";

const InstitutionEdit = () => {
	const { id } = useParams();
	const [formData, setFormData] = useState({ id:"", name: "", type: "" });
	const [errorMessage, setErrorMessage] = useState("");
	const navigate = useNavigate();

	useEffect(() => {
		getInstitutionById(id).then((response) => setFormData(response.data));
	}, [id]);

	const handleSubmit = async (e) => {
		e.preventDefault();
		try {
			await updateInstitution(formData);
			navigate("/");
		} catch (error) {
			if(error.response && error.response.data) {
				setErrorMessage(error.response.data);
			} else {
				setErrorMessage("Erro ao atualizar a instituição.");
			}
		}
	};

	return (
		<div>
			<InstitutionForm
				title={"Editar Instituição"}
				formData={formData}
				setFormData={setFormData}
				onSubmit={handleSubmit}
				errors={errorMessage}
			/>
		</div>
	);
};

export default InstitutionEdit;
