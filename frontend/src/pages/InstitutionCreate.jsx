import { useState } from "react";
import { createInstitution } from "../services/api";
import { useNavigate } from "react-router-dom";
import InstitutionForm from "../components/InstitutionForm";

const InstitutionCreate = () => {
	const [formData, setFormData] = useState({ name: "", type: "" });
	const [errors, setErrors] = useState({});
	const navigate = useNavigate();

	const handleSubmit = async (e) => {
		e.preventDefault();
		setErrors({});

		try{
			await createInstitution(formData)
			navigate("/");
		}catch(error){
			if (error.response && error.response.data) {
				setErrors(error.response.data);
			}else{
				setErrors("Erro ao cadastrar instituição:", error);
			}
		}
	};

	return (
		<div>
			<InstitutionForm 
				title={"Cadastrar Instituição"}
				formData={formData}
				setFormData={setFormData} 
				onSubmit={handleSubmit}
				errors={errors}
			/>
		</div>
	);
};

export default InstitutionCreate;
