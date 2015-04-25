package be.lode.jukebox.library;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import be.lode.setup.ResetDBSetupTestData;

public class LibraryTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ResetDBSetupTestData.run();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		ResetDBSetupTestData.run();
	}

	private String libPath;

	@Before
	public void setUp() throws Exception {
		//libPath = "C:\\Users\\lod\\Dropbox\\JukeboxSongs\\"; //surface
		libPath = "D:\\Users\\Lode\\Dropbox\\JukeboxSongs\\"; //desktop
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAddSong() {
		Library lib = new Library();
		String testPath = libPath
				+ "U_Can_Unlearn_Guitar_-_18_-_The_fireflies.mp3";
		assertNotNull("Library songcollection not null", lib.getSongs());
		assertEquals("Library is empty", 0, lib.getSongs().size());
		lib.addSong(testPath);
		assertTrue("Library is not empty", lib.getSongs().size() > 0);

		// test file not found
		Library lib2 = new Library();
		testPath = "C:\\NOTFOUND\\test1.mp3";
		assertNotNull("Library songcollection not null", lib2.getSongs());
		assertEquals("Library is empty", 0, lib2.getSongs().size());
		lib2.addSong(testPath);
		assertEquals("Library is still empty", 0, lib2.getSongs().size());
	}

	@Test
	public void testResyncLibrary() {
		Library lib = new Library();
		lib.setPath(libPath);
		assertEquals(libPath, lib.getPath());
		assertNotNull("Library songcollection not null", lib.getSongs());
		assertEquals("Library is empty", 0, lib.getSongs().size());
		lib.resyncLibrary();
		assertTrue("Library is not empty", lib.getSongs().size() > 0);

		// incorrect metadata
		Library lib2 = new Library();
		String testPath = libPath
				+ "The_Agrarians_-_02_-_Sincerity_It_Moves_with_Me.mp3";
		assertNotNull("Library songcollection not null", lib2.getSongs());
		assertEquals("Library is empty", 0, lib2.getSongs().size());
		lib2.addSong(testPath);
		assertEquals("Library is size 1", 1, lib2.getSongs().size());
		lib.resyncLibrary();
		assertTrue("Library is not empty", lib.getSongs().size() > 0);
		assertTrue("Library is bigger than 1", lib.getSongs().size() > 1);
	}

}
