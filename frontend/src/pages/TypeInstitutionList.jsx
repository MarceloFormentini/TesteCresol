import { useEffect, useState } from "react";
import { getTypeInstitution, deleteTypeInstitution } from "../services/api";
import { useNavigate } from "react-router-dom";
import "../styles/EventList.css";

const TypeInstitutionList = () => {
	const [typeInstitution, setTypeInstitution] = useState([]);
	const [page, setPage] = useState(0);
	const [totalPages, setTotalPages] = useState(1);
	const [errorLoad, setErrorLoad] = useState("");
	const navigate = useNavigate();

	useEffect(() => {
		loadTypeInstitution();
	}, [page]); // atualiza a lista de tipos toda vez que a página muda

	const loadTypeInstitution = () => {
		getTypeInstitution(page)
			.then((response) => {
				setTypeInstitution(response.data); // content -> contém os dados da página atual
				setTotalPages(response.data.totalPages); // totalPages -> contém o total de páginas
				setErrorLoad("");
			})
			.catch((error) => {
				setErrorLoad("Erro ao carregar a lista de tipos. Tente novamente mais tarde.");
			});
	}

	const handleTypeDelete = async (id) => {
		if (window.confirm("Tem certeza que deseja excluir este tipo?")) {
			try {
				await deleteTypeInstitution(id);
				loadTypeInstitution();
			} catch (error) {
				setErrorLoad("Erro ao excluir o tipo. Verifique se não esta vinculado a uma instituição.");
			}
		}
	};

	const pageNext = () => {
		if (page + 1 < totalPages) {
			setPage((prevPage) => prevPage + 1);
		}
	};

	const pagePrevious = () => {
		if (page > 0) {
			setPage((prevPage) => prevPage - 1);
		}
	};

	return (
		<div className="container">
			<h1 className="title">Tipos</h1>
			<div className="button-container">
				<button className="add-button" onClick={() => navigate("/typeinstitution/create")}>
					Novo Tipo
				</button>
			</div>
			<table className="institution-list">
				<thead>
					<tr>
						<th className="table-header">Nome</th>
						<th className="table-header">Ações</th>
					</tr>
				</thead>
				<tbody>
					{typeInstitution.map((inst) => (
						<tr key={inst.id} className="table-row">
							<td className="table-cell">{inst.name}</td>
							<td className="table-cell action-buttons">
								<button onClick={() => navigate(`/typeinstitution/edit/${inst.id}`)} className="action-button edit-button">
									Editar
								</button>
								<button onClick={() => handleTypeDelete(inst.id)} className="action-button delete-button">
									Excluir
								</button>
							</td>
						</tr>
					))}
				</tbody>
			</table>
			{errorLoad && <p style={{ color: "red", marginBottom: "16px" }}>{errorLoad}</p>}
			<div className="pagination-container">
				<button className="back-button" onClick={() => navigate("/institution")}>
					Voltar
				</button>
				<div className="pagination">
					<button onClick={pagePrevious} disabled={page === 0}>
						Anterior
					</button>
					<span> Página {page + 1} de {totalPages} </span>
					<button onClick={pageNext} disabled={page === totalPages - 1}>
						Próxima
					</button>
				</div>
			</div>
		</div>
	);
};

export default TypeInstitutionList;