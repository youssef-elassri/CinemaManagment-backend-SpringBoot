package cinemaproject.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import cinemaproject.dao.*;
import cinemaproject.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CinemaInitServiceImp implements ICinemaInitService {

	@Autowired
	private VilleRepository villeRepository;

	@Autowired
	private CinemaRepository cinemaRepository;

	@Autowired
	private SalleRepository salleRepository;

	@Autowired
	private PlaceRepository placeRepository;

	@Autowired
	private SeanceRepository seanceRepository;

	@Autowired
	private FilmRepository filmRepository;

	@Autowired
	private ProjectionRepository projectionRepository;

	@Autowired
	private CategorieRepository categorieRepository;

	@Autowired
	private TicketRepository ticketRepository;

	@Override
	public void initVilles() {
		// TODO Auto-generated method stub
		Stream.of("Casablanca", "Marrakech", "Rabat", "Tanger").forEach(villeName->{
			Ville ville = new Ville();
			ville.setName(villeName);
			villeRepository.save(ville);
		});

	}

	@Override
	public void initCinemas() {
		// TODO Auto-generated method stub
		villeRepository.findAll().forEach(v->{
			Stream.of("MegaRama", "IMAX", "FOUNOUN", "CHAHRAZAD", "DAOULIZ")
			.forEach(nameCinema->{
				Cinema cinema = new Cinema();
				cinema.setName(nameCinema);
				cinema.setNombreSalles(3+(int)(Math.random()*7));
				cinema.setVille(v);
				cinemaRepository.save(cinema);

			});
		});

	}

	@Override
	public void initSalles() {
		cinemaRepository.findAll().forEach(cinema->{
			for(int i=0; i<cinema.getNombreSalles();i++) {
				Salle salle = new Salle();
				salle.setName("Salle " + i+1 );
				salle.setCinema(cinema);
				salle.setNombrePlaces(20+(int)(Math.random()*30));
				salleRepository.save(salle);
			}
		});
	}

	@Override
	public void initSeances() {
		DateFormat dateFormat=new SimpleDateFormat("HH:mm");
		Stream.of("12:00","15:00","17:30","19:00","21:30")
				.forEach(s->{
					Seance seance=new Seance();
					try {
						seance.setHeureDebut(dateFormat.parse(s));
						seanceRepository.save(seance);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
	}

	@Override
	public void initPlaces() {
		salleRepository.findAll()
				.forEach(salle->{
					for(int i=0;i<salle.getNombrePlaces();i++) {
						Place place=new Place();
						place.setNumero(i+1);
						place.setSalle(salle);
						place.setAltitude(Math.random());
						place.setLatitude(Math.random());
						place.setLongitude(Math.random());
						placeRepository.save(place);
					}
				});
	}

	@Override
	public void initCategories() {
		Stream.of("Actions","Fiction","Drama","Policier","Comedie")
				.forEach(cat->{
					Categorie categorie=new Categorie();
					categorie.setName(cat);
					categorieRepository.save(categorie);
				});

	}

	@Override
	public void initFilms() {
		double[]durees=new double[] {1,1.5,2,2.5,3};
		List<Categorie>categories=categorieRepository.findAll();
		Stream.of("Black Panther","Aladdin","Avengers End Game","Bad Boys 3","Frozen2","Home Alone","The Terminal","HobbsandShaw","Dora")
				.forEach(titreFilm->{
					Film film=new Film();
					film.setTitre(titreFilm);
					film.setDuree(durees[new Random().nextInt(durees.length)]);
					film.setPhoto(titreFilm.replaceAll(" ","")+".jpg");
					film.setCategorie(categories.get(new Random().nextInt(categories.size())));
					film.setDateSortie(new Date());
					filmRepository.save(film);
				});

	}

	@Override
	public void initProjections() {
		double[]prices=new double[] {30,50,60,70,90,100};
		List<Film> films=filmRepository.findAll();
		villeRepository.findAll()
				.forEach(ville->{
					ville.getCinemas().forEach(cinema->{
						cinema.getSalles().forEach(salle->{
							int index=new Random().nextInt(films.size());


							Film film=films.get(index);
							seanceRepository.findAll()
									.forEach(seance->{
										Projection projection=new Projection();
										projection.setDateProjection(new Date());
										projection.setFilm(film);
										projection.setPrix(prices[new Random().nextInt(prices.length)]);
										projection.setSalle(salle);
										projection.setSeance(seance);
										projectionRepository.save(projection);
									});

						});
					});
				});

	}

	@Override
	public void initTickets() {
		projectionRepository.findAll()
				.forEach(p->{
					p.getSalle().getPlaces().forEach(place->{
						Ticket ticket=new Ticket();
						ticket.setPlace(place);
						ticket.setPrix(p.getPrix());
						ticket.setProjection(p);
						ticket.setReserver(false);
						ticketRepository.save(ticket);

					});
				});

	}




}
