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

	const handleSubmit = async (e, formData) => {
		e.preventDefault();
		try {
			const payload = {
				id: formData.id,
				name: formData.name,
				typeInstitution: {
					id: formData.typeInstitution.id,
					name: formData.typeInstitution.name
				},
			};
			console.log(formData);
			console.log("payload: ", payload)
			await updateInstitution(payload);
			navigate("/institution");
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
