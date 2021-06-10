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
		Stream.of("Marrakech", "Agadir", "Rabat", "Casablanca").forEach(nameVille -> {
			Ville ville = new Ville();
			ville.setName(nameVille);
			villeRepository.save(ville);
		});
	}

	@Override
	public void initCinemas() {
		villeRepository.findAll().forEach(v -> {
			Stream.of("MegaRama", "Imax", "Founoun", "Chahrazad", "Daouliz").forEach(name -> {
				Cinema cinema = new Cinema();
				cinema.setName(name);
				cinema.setNombreSalles((int) (3 + Math.random() * 7));
				cinema.setVille(v);
				cinemaRepository.save(cinema);
			});
		});
	}

	@Override
	public void initSalles() {
		cinemaRepository.findAll().forEach(cinema -> {
			for (int i = 0; i < cinema.getNombreSalles(); i++) {
				Salle salle = new Salle();
				salle.setName("Salle " + (i + 1));
				salle.setCinema(cinema);
				salle.setNombrePlace((int) (15 + ((Math.random() * 20))));
				salleRepository.save(salle);
			}
		});
	}

	@Override
	public void initPlaces() {
		salleRepository.findAll().forEach(salle -> {
			for (int i = 0; i < salle.getNombrePlace(); i++) {
				Place place = new Place();
				place.setNumero((i + 1));
				place.setSalle(salle);
				placeRepository.save(place);
			}
		});
	}

	@Override
	public void initSeances() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm");
		Stream.of("12:00", "15:00", "17:00", "19:00", "21:00").forEach(s -> {
			Seance seance = new Seance();
			try {
				seance.setHeureDebut(dateFormat.parse(s));
				seanceRepository.save(seance);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	public void initGategories() {
		Stream.of("Comedy", "Action", "Fiction", "Drama", "Horror").forEach(c -> {
			Categorie categorie = new Categorie();
			categorie.setName(c);
			categorieRepository.save(categorie);
		});
	}

	@Override
	public void initFilms() {
		double[] durees = new double[] { 1.5, 2, 2.5, 3 };
		List<Categorie> categories = categorieRepository.findAll();
		Stream.of("12 years a slave", "tenet", "wrath of a man", "the avengers").forEach(mov -> {
			Film film = new Film();
			film.setTitre(mov);
			film.setDuree(durees[new Random().nextInt(durees.length)]);
			film.setPhoto(mov.toLowerCase().replaceAll(" ", ""));
			film.setCategorie(categories.get(new Random().nextInt(categories.size())));
			filmRepository.save(film);
		});
	}

	@Override
	public void initProjections() {
		double[] prices = new double[] { 30, 50, 70, 90, 100 };
		List<Film> films = filmRepository.findAll();
		villeRepository.findAll().forEach(ville -> {
			ville.getCinemas().forEach(cinema -> {
				cinema.getSalles().forEach(salle -> {
					int index= new Random().nextInt(films.size());
                    Film film = films.get(index);
                    seanceRepository.findAll().forEach(seance -> {
                        Projection projection = new Projection();
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
		projectionRepository.findAll().forEach(projection -> {
			projection.getSalle().getPlaces().forEach(place -> {
				Ticket ticket = new Ticket();
				ticket.setPlace(place);
				ticket.setPrix(projection.getPrix());
				ticket.setProjection(projection);
				ticket.setReserve(false);
				ticketRepository.save(ticket);
			});
		});
	}
}
