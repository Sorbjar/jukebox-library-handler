package be.lode.jukebox.library;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import be.lode.jukebox.business.exceptions.IncorrectMetadataException;
import be.lode.jukebox.business.model.Song;

public class Library {
	private String path;
	private Set<Song> songs;

	public Library() {
		super();
		//TODO 100 issue with persising songs metadata
		// TODO 700 path in configuration file
		//this.path = "C:\\Users\\lod\\Dropbox\\JukeboxSongs\\"; //surface
		this.path = "D:\\Users\\Lode\\Dropbox\\JukeboxSongs\\";
		this.songs = new HashSet<Song>();
	}

	public void addSong(String path) {
		try {
			Song song = parse(path);
			if (!songs.contains(song)) {
				songs.add(song);
			}
		} catch (IOException | SAXException | TikaException
				| IncorrectMetadataException e) {
			// TODO 800 handle exception
			// ignore exception, not useful for end user
		}
	}

	public String getPath() {
		return path;
	}

	public Set<Song> getSongs() {
		return songs;
	}

	public void resyncLibrary() {
		this.songs = new HashSet<Song>();
		File dir = new File(path);
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				addSong(child.getPath());
			}
		}
	}

	public void setPath(String path) {
		this.path = path;
	}

	private Song parse(String path) throws IOException, SAXException,
			TikaException, IncorrectMetadataException {
		BodyContentHandler handler = new BodyContentHandler();
		Metadata metadata = new Metadata();
		FileInputStream inputstream;
		ParseContext pcontext = new ParseContext();

		inputstream = new FileInputStream(new File(path));

		Mp3Parser mp3Parser = new Mp3Parser();

		mp3Parser.parse(inputstream, handler, metadata, pcontext);

		String[] metadataNames = metadata.names();
		String title = "";
		String artist = "";
		for (String name : metadataNames) {
			// TODO 800 map names of metadata
			if (name.equalsIgnoreCase("xmpDM:artist"))
				artist = metadata.get(name);
			if (name.equalsIgnoreCase("title"))
				title = metadata.get(name);
		}
		if (artist.length() == 0)
			throw new IncorrectMetadataException(
					"Artist information is missing from the metadata");
		if (title.length() == 0)
			throw new IncorrectMetadataException(
					"Title information is missing from the metadata");
		Song song = new Song(artist, title, path);
		for (String name : metadataNames) {
			// TODO 800 map names of metadata
			song.getMetadataProperties().put(name, metadata.get(name));
		}
		return song;

	}
}
