import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import InstitutionList from "../pages/InstitutionList";
import InstitutionCreate from "../pages/InstitutionCreate";
import InstitutionEdit from "../pages/InstitutionEdit";
import EventList from "../pages/EventList";
import EventCreate from "../pages/EventCreate";
import EventEdit from "../pages/EventEdit";

const AppRoutes = () => (
	<Router>
		<Routes>
			{/* Define a rota raiz da aplicação */}
			<Route path="/" element={<Navigate to="/institution" replace />} />

			{/* Institution */}
			<Route path="/institution" element={<InstitutionList />} />
			<Route path="/institution/create" element={<InstitutionCreate />} />
			<Route path="/institution/edit/:id" element={<InstitutionEdit />} />
			
			{/* Event */}
			<Route path="/institution/:institutionId/event" element={<EventList />} />
			<Route path="/institution/:institutionId/event/create" element={<EventCreate />} />
			<Route path="/institution/:institutionId/event/edit/:id" element={<EventEdit />} />
		</Routes>
	</Router>
);

export default AppRoutes;