import { useEffect, useState } from "react";
import { getInstitution, deleteInstitution } from "../services/api";
import { useNavigate } from "react-router-dom";
import "../styles/InstitutionList.css";

const InstitutionList = () => {
	const [institution, setInstitution] = useState([]);
	const [page, setPage] = useState(0);
	const [totalPages, setTotalPages] = useState(1);
	const [errorLoad, setErrorLoad] = useState(false);
	const navigate = useNavigate();

	useEffect(() => {
		loadInstitution();
	}, [page]); // atualiza a lista de instituições toda vez que a página muda

	const loadInstitution = () => {
		getInstitution(page)
			.then((response) => {
				setInstitution(response.data.content); // content -> contém os dados da página atual
				setTotalPages(response.data.totalPages); // totalPages -> contém o total de páginas
				setErrorLoad("");
			})
			.catch((error) => {
				setErrorLoad("Erro ao carregar a lista de instituição. Tente novamente mais tarde.");
			});
	}

	const handleDelete = async (id) => {
		if (window.confirm("Tem certeza que deseja excluir esta instituição?")) {
			try {
				await deleteInstitution(id);
				loadInstitution();
			} catch (error) {
				setErrorLoad("Erro ao excluir a instituição. Verifique se ele está vinculado a algum evento.");
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
			<h1 className="title">Instituições</h1>
			<div className="button-container">
				<button className="add-button" onClick={() => navigate("/typeinstitution")}>
					Tipos
				</button>
				<button className="add-button" onClick={() => navigate("/institution/create")}>
					Nova Instituição
				</button>
			</div>
			<table className="institution-list">
				<thead>
					<tr>
						<th className="table-header">Nome</th>
						<th className="table-header">Tipo</th>
						<th className="table-header">Ações</th>
					</tr>
				</thead>
				<tbody>
					{institution.map((inst) => (
						<tr key={inst.id} className="table-row">
							<td className="table-cell">{inst.name}</td>
							<td className="table-cell">{inst.typeInstitution.name}</td>
							<td className="table-cell action-buttons">
								<button onClick={() => navigate(`/institution/${inst.id}/event`)} className="action-button view-button">
									Eventos
								</button>
								<button onClick={() => navigate(`/institution/edit/${inst.id}`)} className="action-button edit-button">
									Editar
								</button>
								<button onClick={() => handleDelete(inst.id)} className="action-button delete-button">
									Excluir
								</button>
							</td>
						</tr>
					))}
				</tbody>
			</table>
			{errorLoad && <p style={{ color: "red", marginBottom: "16px" }}>{errorLoad}</p>}
			<div className="pagination-container">
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

export default InstitutionList;
