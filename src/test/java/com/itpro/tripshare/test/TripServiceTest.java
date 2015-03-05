package com.itpro.tripshare.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalTaskQueueTestConfig;
import com.itpro.tripshare.server.DataServiceImpl;
import com.itpro.tripshare.shared.Trip;

import static com.itpro.tripshare.test.ObjService.factory;

public class TripServiceTest {
	public final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalTaskQueueTestConfig(),
			new LocalDatastoreServiceTestConfig());

	@Before
	public void setUp() {
		factory().register(Trip.class);
		helper.setUp();
	}

	@After
	public void tearDown() {
		helper.tearDown();
	}

	@Test
	public void testInsertTask() {
		DataServiceImpl service = new DataServiceImpl();
	}

	private Trip createTrip() {
		Trip trip = new Trip();
		trip.setName("Hà Nội đi Sapa");
		trip.setDescription("Chuyến đi sẽ đi qua Yên Bái, Dừng chân ở Lào Cai ăn sáng và cuối cùng là đến Sapa");
		trip.setOwner(123L);
		return trip;
	}
}
