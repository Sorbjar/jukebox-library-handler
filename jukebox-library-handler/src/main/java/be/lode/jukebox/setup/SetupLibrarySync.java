package be.lode.jukebox.setup;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import be.lode.general.repository.Repository;
import be.lode.jukebox.business.model.Song;
import be.lode.jukebox.business.repo.SongRepository;
import be.lode.jukebox.library.Library;

public class SetupLibrarySync {

	public static void main(String[] args) {
		try {
			run(args[0]);
		} catch (ArrayIndexOutOfBoundsException ex) {
			run(null);
		}
		System.exit(1);
	}

	public static void run(String path) {
		Library lib = new Library();
		if (path != null)
			lib = new Library(path);
		lib.resyncLibrary();
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("jukebox-business");
		Repository<Song> sRepo = new SongRepository(emf);
		for (Song s : lib.getSongs()) {
			sRepo.save(s);
		}
	}
}
