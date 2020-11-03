package tn.esprit.spring.controller;

import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import tn.esprit.spring.entities.Contrat;
import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.Entreprise;
import tn.esprit.spring.entities.Mission;
import tn.esprit.spring.entities.Role;
import tn.esprit.spring.entities.Timesheet;
import tn.esprit.spring.services.IEmployeService;


@Scope(value = "session")
@Controller(value = "employeController")
@ELBeanName(value = "employeController")
@Join(path = "/", to = "/login.jsf")
public class ControllerEmployeImpl  {

	@Autowired
	IEmployeService employeService;

	private String login; 
	private String password1; 
	private Boolean loggedIn1;

	private Employe authenticatedUser = null; 
	private String prenom1; 
	private String nom1; 
	private String email1;
	private boolean actif1;
	private Role role1;  
	public Role[] getRoles() { return Role.values(); }

	private List<Employe> employes; 

	private Integer employeIdToBeUpdated; 
	
	String loginUrl= "/login.xhtml?faces-redirect=true";


	public String doLogin() {

		String navigateTo = "null";
		authenticatedUser=employeService.authenticate(login, password1);
		if (authenticatedUser != null && authenticatedUser.getRole() == Role.ADMINISTRATEUR) {
			navigateTo = "/pages/admin/welcome.xhtml?faces-redirect=true";
			loggedIn1 = true;
		}		

		else
		{
			
			FacesMessage facesMessage =
					new FacesMessage("Login Failed: Please check your username/password and try again.");
			FacesContext.getCurrentInstance().addMessage("form:btn",facesMessage);
		}
		return navigateTo;	
	}

	public String doLogout()
	{
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
	
	return "/login.xhtml?faces-redirect=true";
	}


	public String addEmploye() {

		if (authenticatedUser==null || !loggedIn1) return loginUrl;

		employeService.addOrUpdateEmploye(new Employe(nom1, prenom1, email1, password1, actif1, role1)); 
		return "null"; 
	}  

	public String removeEmploye(int employeId) {
		String navigateTo = "null";
		if (authenticatedUser==null || !loggedIn1) return loginUrl;

		employeService.deleteEmployeById(employeId);
		return navigateTo; 
	} 

	public String displayEmploye(Employe empl) 
	{
		String navigateTo = "null";
		if (authenticatedUser==null || !loggedIn1) return loginUrl;


		this.setPrenom1(empl.getPrenom());
		this.setNom1(empl.getNom());
		this.setActif1(empl.isActif()); 
		this.setEmail1(empl.getEmail());
		this.setRole1(empl.getRole());
		this.setPassword1(empl.getPassword());
		this.setEmployeIdToBeUpdated(empl.getId());

		return navigateTo; 

	} 

	public String updateEmploye() 
	{ 
		String navigateTo = "null";
		
		if (authenticatedUser==null || !loggedIn1) return loginUrl;

		employeService.addOrUpdateEmploye(new Employe(employeIdToBeUpdated, nom1, prenom1, email1, password1, actif1, role1)); 

		return navigateTo; 

	} 


	// getters and setters 

	public IEmployeService getEmployeService() {
		return employeService;
	}

	public void setEmployeService(IEmployeService employeService) {
		this.employeService = employeService;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword1() {
		return password1;
	}

	public void setPassword1(String password) {
		this.password1 = password;
	}


	public List<Employe> getAllEmployes() {
		return employeService.getAllEmployes();
	}

	public Boolean getLoggedIn1() {
		return loggedIn1;
	}

	public void setLoggedIn1(Boolean loggedIn) {
		this.loggedIn1 = loggedIn;
	}

	public int ajouterEmploye(Employe employe)
	{
		employeService.addOrUpdateEmploye(employe);
		return employe.getId();
	}

	public void mettreAjourEmailByEmployeId(String email, int employeId) {
		employeService.mettreAjourEmailByEmployeId(email, employeId);

	}

	public void affecterEmployeADepartement(int employeId, int depId) {
		employeService.affecterEmployeADepartement(employeId, depId);

	}



	public void desaffecterEmployeDuDepartement(int employeId, int depId)
	{
		employeService.desaffecterEmployeDuDepartement(employeId, depId);
	}

	public int ajouterContrat(Contrat contrat) {
		employeService.ajouterContrat(contrat);
		return contrat.getReference();
	}

	public void affecterContratAEmploye(int contratId, int employeId)
	{
		employeService.affecterContratAEmploye(contratId, employeId);
	}


	public String getEmployePrenomById(int employeId) {
		return employeService.getEmployePrenomById(employeId);
	}

	public void deleteEmployeById(int employeId) {
		employeService.deleteEmployeById(employeId);

	}
	public void deleteContratById(int contratId) {
		employeService.deleteContratById(contratId);
	}

	public int getNombreEmployeJPQL() {

		return employeService.getNombreEmployeJPQL();
	}

	public List<String> getAllEmployeNamesJPQL() {

		return employeService.getAllEmployeNamesJPQL();
	}

	public List<Employe> getAllEmployeByEntreprise(Entreprise entreprise) {
		return employeService.getAllEmployeByEntreprise(entreprise);
	}

	public void mettreAjourEmailByEmployeIdJPQL(String email, int employeId) {	
		employeService.mettreAjourEmailByEmployeIdJPQL(email, employeId);

	}

	public void deleteAllContratJPQL() {
		employeService.deleteAllContratJPQL();

	}

	public float getSalaireByEmployeIdJPQL(int employeId) {
		return employeService.getSalaireByEmployeIdJPQL(employeId);
	}


	public Double getSalaireMoyenByDepartementId(int departementId) {
		return employeService.getSalaireMoyenByDepartementId(departementId);
	}

	public List<Timesheet> getTimesheetsByMissionAndDate(Employe employe, Mission mission, Date dateDebut,
			Date dateFin) {
		return employeService.getTimesheetsByMissionAndDate(employe, mission, dateDebut, dateFin);
	}

	public String getPrenom1() {
		return prenom1;
	}

	public void setPrenom1(String prenom) {
		this.prenom1 = prenom;
	}

	public String getNom1() {
		return nom1;
	}

	public void setNom1(String nom) {
		this.nom1 = nom;
	}

	public String getEmail1() {
		return email1;
	}

	public void setEmail1(String email) {
		this.email1 = email;
	}




	public boolean isActif1() {
		return actif1;
	}

	public void setActif1(boolean actif) {
		this.actif1 = actif;
	}

	public Role getRole1() {
		return role1;
	}

	public void setRole1(Role role) {
		this.role1 = role;
	}

	public List<Employe> getEmployes() {
		employes = employeService.getAllEmployes(); 
		return employes;
	}

	public void setEmployes(List<Employe> employes) {
		this.employes = employes;
	}

	public Integer getEmployeIdToBeUpdated() {
		return employeIdToBeUpdated;
	}

	public void setEmployeIdToBeUpdated(Integer employeIdToBeUpdated) {
		this.employeIdToBeUpdated = employeIdToBeUpdated;
	}

	public Employe getAuthenticatedUser() {
		return authenticatedUser;
	}

	public void setAuthenticatedUser(Employe authenticatedUser) {
		this.authenticatedUser = authenticatedUser;
	}

}
