package be.lode.jukebox.setup;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import be.lode.general.repository.Repository;
import be.lode.jukebox.business.model.Jukebox;
import be.lode.jukebox.business.model.Song;
import be.lode.jukebox.business.repo.JukeboxRepository;
import be.lode.jukebox.business.repo.SongRepository;
import be.lode.jukebox.library.Library;
import be.lode.setup.ResetDBSetupLiveData;

public class ResetDBSetupLiveDataSetupLibrarySync {

	public static void main(String[] args) {
		ResetDBSetupLiveData.run();
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

		Repository<Jukebox> jRepo = new JukeboxRepository(emf);
		for (Jukebox j : jRepo.getList()) {
			int count = 0;
			for (Song s : sRepo.getList()) {
				if (count < 3) {
					j.getMandatoryPlaylist().addSong(s);
				}
				count++;
			}
			jRepo.save(j);
		}
	}
}
