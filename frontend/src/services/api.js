import axios from "axios";

const API_URL = "http://localhost:9090"; // Ajuste conforme necessário

const api = axios.create({
	baseURL: API_URL,
	headers: { "Content-Type": "application/json" },
});

// TYPEINSTITUTION
export const getTypeInstitution = (page = 0, size = 10, sort = "id") => {
	return api.get(`/typeinstitution?page=${page}&size=${size}&sort=${sort}`);
};
export const getTypeInstitutionById = (id) => api.get(`/typeinstitution/${id}`);
export const createTypeInstitution = (data) => api.post("/typeinstitution", data);
export const updateTypeInstitution = (data) => api.put("/typeinstitution", data);
export const deleteTypeInstitution = (id) => api.delete(`/typeinstitution/${id}`);

// INSTITUTION
export const getInstitution = (page = 0, size = 10, sort = "id") => {
	return api.get(`/institution?page=${page}&size=${size}&sort=${sort}`);
};
export const getInstitutionById = (id) => api.get(`/institution/${id}`);
export const createInstitution = (data) => api.post("/institution", data);
export const updateInstitution = (data) => api.put("/institution", data);
export const deleteInstitution = (id) => api.delete(`/institution/${id}`);

// EVENT
export const getEventsByInstitution = (institutionId, page = 0, size = 10, sort = "startDate,asc") => {
	return api.get(`/event/${institutionId}?page=${page}&size=${size}&sort=${sort}`);
};
export const getEventById = (institutionId, id) => api.get(`/event/${institutionId}/${id}`);
export const createEvent = (institutionId, data) => api.post(`/event/${institutionId}`, data);
export const updateEvent = (institutionId, data) => api.put(`/event/${institutionId}`, data);
export const deleteEvent = (id) => api.delete(`/event/${id}`);

export default api;