package be.lode.jukebox.setup;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import be.lode.general.repository.Repository;
import be.lode.jukebox.business.model.Song;
import be.lode.jukebox.business.repo.SongRepository;
import be.lode.jukebox.library.Library;
import be.lode.setup.ResetDBSetupTestData;

public class ResetDBSetupTestDataSetupLibrarySync {

	public static void main(String[] args) {
		ResetDBSetupTestData.run();
		run();
	}

	public static void run() {
		Library lib = new Library();
		lib.resyncLibrary();
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("jukebox-business");
		Repository<Song> sRepo = new SongRepository(emf);
		for (Song s : lib.getSongs()) {
			sRepo.save(s);
		}
	}
}
