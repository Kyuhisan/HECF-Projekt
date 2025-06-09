package si.um.feri.__Backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import si.um.feri.__Backend.model.Listing;
import si.um.feri.__Backend.repository.ListingRepository;
import si.um.feri.__Backend.service.ListingService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApplicationTests {

	private ListingRepository listingRepository;
	private ListingService listingService;

	@BeforeEach
	void setUp() {
		listingRepository = mock(ListingRepository.class);
		listingService = new ListingService(listingRepository);
	}

	@Test
	void testGetAllListings() {
		Listing l1 = new Listing();
		l1.setId("1");
		l1.setStatus("Forthcoming");

		Listing l2 = new Listing();
		l2.setId("2");
		l2.setStatus("Open");

		when(listingRepository.findAll()).thenReturn(Arrays.asList(l1, l2));

		List<Listing> listings = listingService.getAllListings();
		assertEquals(2, listings.size());
		verify(listingRepository, times(1)).findAll();
	}

	@Test
	void testGetForthcomingListings() {
		Listing l1 = new Listing();
		l1.setId("1");
		l1.setStatus("Forthcoming");

		Listing l2 = new Listing();
		l2.setId("2");
		l2.setStatus("Open");

		when(listingRepository.findAll()).thenReturn(Arrays.asList(l1, l2));

		List<Listing> forthcoming = listingService.getForthcomingListings();
		assertEquals(1, forthcoming.size());
		assertEquals("Forthcoming", forthcoming.get(0).getStatus());
	}

	@Test
	void testGetOpenListings() {
		Listing l1 = new Listing();
		l1.setId("1");
		l1.setStatus("Open");

		Listing l2 = new Listing();
		l2.setId("2");
		l2.setStatus("Closed");

		when(listingRepository.findAll()).thenReturn(Arrays.asList(l1, l2));

		List<Listing> open = listingService.getOpenListings();
		assertEquals(1, open.size());
		assertEquals("Open", open.get(0).getStatus());
	}

	@Test
	void testGetClosedListings() {
		Listing l1 = new Listing();
		l1.setId("1");
		l1.setStatus("Closed");

		Listing l2 = new Listing();
		l2.setId("2");
		l2.setStatus("Open");

		when(listingRepository.findAll()).thenReturn(Arrays.asList(l1, l2));

		List<Listing> closed = listingService.getClosedListings();
		assertEquals(1, closed.size());
		assertEquals("Closed", closed.get(0).getStatus());
	}
}