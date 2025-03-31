import { useState } from "react";
import { createTypeInstitution } from "../services/api";
import { useNavigate } from "react-router-dom";
import TypeInstitutionForm from "../components/TypeInstitutionForm";

const TypeInstitutionCreate = () => {
	const [formData, setFormData] = useState({ name: ""});
	const [errors, setErrors] = useState({});
	const navigate = useNavigate();

	const handleSubmit = async (e) => {
		e.preventDefault();
		setErrors({});

		try{
			await createTypeInstitution(formData)
			navigate("/typeinstitution");
		}catch(error){
			if (error.response && error.response.data) {
				setErrors(error.response.data);
			}else{
				setErrors("Erro ao cadastrar tipo:", error);
			}
		}
	};

	return (
		<div>
			<TypeInstitutionForm
				title={"Cadastrar Tipo"}
				formData={formData}
				setFormData={setFormData} 
				onSubmit={handleSubmit}
				errors={errors}
			/>
		</div>
	);
};

export default TypeInstitutionCreate;
